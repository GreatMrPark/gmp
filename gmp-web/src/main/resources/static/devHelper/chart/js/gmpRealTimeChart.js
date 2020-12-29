var realtime = {
    inst: null,
    interval: null,
    start: false,
    init: function() {
        this.generate();
        this.attachEvent();
    },
    stop: function() {
      this.start = false;
    },
    generate: function() {
         chart = this.inst = bb.generate({
            padding: {
              bottom: 10
            },
            data: {
                x: 'x',
                columns: this.getInitData(5),
                type: "area"
            },
            axis: {
                x: {
                    type: 'timeseries',
                    tick: {
                        format: '%Y.%m.%d %H:%M:%S'
                    }
                }
            },
            legend: {
                show: false
            }
        });

        this.start = true;
        this.flow();
    },
    attachEvent: function() {
        document.addEventListener("visibilitychange", () => {
            if (document.visibilityState === "visible") {
                setTimeout(() => {
                    !this.start && this.init();
                }, 1500)
            } else {
                this.stop()
            }
        });
    },
    flow: function() {
        this.start && this.inst.flow({
            columns: [
                ["x", +new Date],
                ["sample", Math.round(Math.random()*1000)]
            ],
            length: 1,
            duration: 500,
            done: this.flow.bind(this)
        });
    },
    getInitData: function(len) {
        const d = +new Date - 1000 * len;
        const data = [
            ["x"], ["sample"]
        ];

        for (let i = 0; i < len; i++) {
            data[0].push(d + (1000 * i));
            data[1].push(null);
        }

        return data;
    }
};