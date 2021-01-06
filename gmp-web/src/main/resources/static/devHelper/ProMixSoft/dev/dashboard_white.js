// --------------------------------------------------------------------------------
// 전역변수
// --------------------------------------------------------------------------------
gDate = new Date();

//chart에 사용되는 obj
var keyArr = [];
var labelArr = [];
var typesArr = [];
var colorArr = [];
var config;
var chartObjArr = [];

var dashbardChart = {
  lineChart : [],
  pieChart : [],
  barChart : [],
  donutChart : []
};

// --------------------------------------------------------------------------------
// fnInit
// --------------------------------------------------------------------------------
$(document).ready(function() {
    fnInit();  
});

var fnInit = function() {
    fnSetCurrentDate();
    chartHourRealTime.init();
    chartServerRealTimeInit();
    fnChartDaily();
    fnChartMonthly();

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

        $("#summeryServerRealTime #server01").html(Math.round(Math.random()*100));
        $("#summeryServerRealTime #server02").html(Math.round(Math.random()*100));
        $("#summeryServerRealTime #server03").html(Math.round(Math.random()*100));
    
    }, 1000);
}

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

var chartServerRealTime = bb.generate({
    data: {
        columns: [
            ["서버1", Math.round(Math.random()*100)],
            ["서버2", Math.round(Math.random()*100)],
            ["서버3", Math.round(Math.random()*100)]
        ],
        type: "bar"
      },
      bar: {
          padding: 25
      },
      axis: {
          x: {
              show: false
          }
        },
      bindto: "#chartServerRealTime"
});

var chartServerRealTimeInit = function() {
    interval = setInterval(function() {
        chartServerRealTime.load({
            columns: [
                ["서버1", Math.round(Math.random()*100)],
                ["서버2", Math.round(Math.random()*100)],
                ["서버3", Math.round(Math.random()*100)]
            ]
        });
    }, 1000 * 3);
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
      type: "bar",
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

//var chartDaily = bb.generate({
//    data: {
//        x: "x",
//        columns: [
//            ["x","9시"],
//            ["완료", [Math.round(Math.random()*100)]],
//            ["실패", [Math.round(Math.random()*100)]],
//            ["진행", [Math.round(Math.random()*100)]]
//        ],
//        type: "bar"
//      },
//      bindto: "#chartDaily"
//});


function fnChartDaily() {
    
//  console.log("setAptDataStatusHourChart : ", data);

    var targetObj = "#chartDaily";
    var keyArr = ["ctime", "completed", "failed", "start"];
    var labelArr = ["완료", "실패", "진행"];
    var typesArr = ["bar", "bar", "line"];
    var colorArr = ["#50C2F3","#FFC000","#FF0000"];
    var chartCycle = ""; // DAY, WEEK, MONTH, HOUR
    
    var chartData = [];

    var toDate = new Date();
    var data = [];
    for (var i = 0; i < 24; i++) {
        toDate.setHours(toDate.getHours()-1);
        
        var hour = toDate.getHours();
        
        var obj = new Object;
        obj.ctime       = hour;
        obj.completed   = Math.round(Math.random()*100);
        obj.failed      = Math.round(Math.random()*100);
        obj.start       = Math.round(Math.random()*100);
        
        data.push(obj);
    }
    
    if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            var obj = new Object;

            obj.ctime       = parseInt(data[i].ctime) + "시";
            obj.completed = data[i].completed;
            obj.failed = data[i].failed;
            obj.start = data[i].start;

            chartData.push(obj);
        }
    }

    var config = chart.baseConfig(targetObj, chartData, keyArr, labelArr, typesArr, colorArr);
    config.legend.show = true;
    config.legend.position = "bottom";
    config.grid.x.show  = true;
    config.grid.y.show  = true;
    config.axis.y.show  = true;
    config.axis.x.show  = true;
    
    chart.chartDateStr(config, "");
    chartObjArr.push(bb.generate( config ));
}

function fnChartMonthly() {
    
//  console.log("setAptDataStatusHourChart : ", data);

    var targetObj = "#chartMonthly";
    var keyArr = ["ctime", "completed", "failed", "start"];
    var labelArr = ["완료", "실패", "진행"];
    var typesArr = ["bar", "bar", "line"];
    var colorArr = ["#50C2F3","#FFC000","#FF0000"];
    var chartCycle = ""; // DAY, WEEK, MONTH, HOUR
    
    var chartData = [];

    var toDate = new Date();
    var data = [];
    for (var i = 0; i < 30; i++) {
        toDate.setDate(toDate.getDate()-1);
        
        var obj = new Object;
        obj.ctime       = shiftDate(toDate, 0, 0, 0, "-");
        obj.completed   = Math.round(Math.random()*100);
        obj.failed      = Math.round(Math.random()*100);
        obj.start       = Math.round(Math.random()*100);
        
        data.push(obj);
    }
    
    if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            var obj = new Object;

            obj.ctime       = data[i].ctime;
            obj.completed = data[i].completed;
            obj.failed = data[i].failed;
            obj.start = data[i].start;

            chartData.push(obj);
        }
    }

    var config = chart.baseConfig(targetObj, chartData, keyArr, labelArr, typesArr, colorArr);
    config.legend.show = true;
    config.legend.position = "bottom";
    config.grid.x.show  = true;
    config.grid.y.show  = true;
    config.axis.y.show  = true;
    config.axis.x.show  = true;
    
    chart.chartDateStr(config, "");
    chartObjArr.push(bb.generate( config ));
}

