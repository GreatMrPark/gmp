// --------------------------------------------------------------------------------
// 전역변수
// --------------------------------------------------------------------------------
gDate = new Date();

// --------------------------------------------------------------------------------
// fnInit
// --------------------------------------------------------------------------------
$(document).ready(function() {
    fnInit();  
});

var fnInit = function() {
    fnSetCurrentDate();
    chartHourRealTime.init();

}

//--------------------------------------------------------------------------------
//fnSetCurrentDate
//--------------------------------------------------------------------------------
var fnSetCurrentDate = function() {
    setCurrentDate = setInterval(function() {
        var currentDate = new Date();
        $("#currentDate").html(currentDate.toLocaleString());

        $("#summeryHourRealTime #comleted").html(Math.round(Math.random()*100));
        $("#summeryHourRealTime #failed").html(Math.round(Math.random()*100));
        $("#summeryHourRealTime #start").html(Math.round(Math.random()*100));
    
    }, 1000);
}

var chartXAxisTickPositionServer11 = bb.generate({
    data: {
      x: "x",
      columns: [
      ["x", "금일", "전일"],
      ["완료", Math.round(Math.random()*100),Math.round(Math.random()*100)],
      ["실패", Math.round(Math.random()*100),Math.round(Math.random()*100)],
      ["진행", Math.round(Math.random()*100),Math.round(Math.random()*100)]
      ],
      type: "bar", // for ESM specify as: bar()
      groups: [
        [
          "완료",
          "실패",
          "진행"
        ]
      ]
    },
    bar: {
        width: {
            ratio: 0.7
        }
    },
    axis: {
      rotated: true,
      x: {
        type: "category",
        clipPath: false,
        inner: false,
        tick: {
          text: {
            position: {
              x: 35,
              y: -21
            }
          }
        }
      },
      y: {
        show: false
      }
    },
    bindto: "#chartXAxisTickPositionServer11"
  });


var chartHourRealTime = {
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
        chartHourRealTime = this.inst = bb.generate({
            padding: {
              bottom: 10
            },
            data: {
                x: 'x',
                columns: this.getInitData(5),
                type: "spline"
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
            },
            bindto: "#chartHourRealTime"
        });

        this.start = true;
        this.flow();
    },
    attachEvent: function() {
        document.addEventListener("visibilitychange", () => {
            if (document.visibilityState === "visible") {
                setTimeout(() => {
                    !this.start && this.init();
                }, 1000 * 3)
            } else {
                this.stop()
            }
        });
    },
    flow: function() {
        this.start && this.inst.flow({
            columns: [
                ["x", +new Date],
                ["실패", Math.round(Math.random()*100)],
                ["진행", Math.round(Math.random()*100)],
                ["완료", Math.round(Math.random()*100)]
            ],
            length: 1,
            duration: 3000,
            done: this.flow.bind(this)
        });
    },
    getInitData: function(len) {
        const d = +new Date - 3000 * len;
        const data = [
            ["x"], ["실패"],
            ["x"], ["진행"],
            ["x"], ["완료"]
        ];

        for (let i = 0; i < len; i++) {
            data[0].push(d + (100 * i));
            data[1].push(null);
        }

        return data;
    }
};