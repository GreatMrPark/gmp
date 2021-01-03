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

var todaySumCount = 0;
// 에너지 사용현황 차트를 그리기위한 데이터 임시 저장
var todaySumData = { 
    elec   : { todaySum : 0, yesterDauSum :0},
    gas    : { todaySum : 0, yesterDauSum :0},
    water    : { todaySum : 0, yesterDauSum :0},
    hotw    : { todaySum : 0, yesterDauSum :0},
    heat    : { todaySum : 0, yesterDauSum :0},
    etc    : { todaySum : 0, yesterDauSum :0}
};

//검침 종류
const READING_UNIT = ["elec", "gas", "water", "hotw", "heat", "etc"];
var readingUnitSeq = 0;

var scrollTemp = 0;
var rollingTimer;

// --------------------------------------------------------------------------------
// fnInit
// --------------------------------------------------------------------------------
$(document).ready(function() {
    fnInit();  
});

var fnInit = function() {
    getWeather();
    centerPieLineChart();
}

//--------------------------------------------------------------------------------
//fnSetCurrentDate
//--------------------------------------------------------------------------------
var fnSetCurrentDate = function() {
    setCurrentDate = setInterval(function() {
        var currentDate = new Date();
//        $("#currentDate").html(currentDate.toLocaleString());
    }, 1000);
}

//chart obj destroy
function setChartDestroy(){
for(var i=0; i<chartObjArr.length; i++){
    chartObjArr[i].destroy();
}
chartObjArr = [];
}

function centerPieLineChart(){
    getDashboardChart("elec", "#chartElecBox"); // 전기 사용현황
    getDashboardChart("gas", "#chartGasBox");   // 가스
    getDashboardChart("water", "#chartWaterBox"); //수도
    getDashboardChart("hotw", "#chartHotwBox"); // 온수
    getDashboardChart("heat", "#chartHeatBox"); // 난방
    getDashboardChart("etc", "#chartEtcBox");   // 신재생
}


/**
 * 중앙 시간별 사용량 라인차트
 * @param meterType
 * @param container
 */
function getDashboardChart(meterType, container){
    var data = [];
    for(var i = 0; i < 24; i++) {
        var json = {};
        json.date = i;
        json.todayValue = Math.round(Math.random()*100);
        json.yesterDayValue = Math.round(Math.random()*100);
        data.push(json);
    }

    displayDashboardChart(container, data, meterType);    // 라인차트
    displayDashboardPieChart(meterType, data); // gauge 차트, 에너지 사용현황
}

/**
 * 라인차트
 * @param targetObj
 * @param data
 * @returns {boolean}
 */
function displayDashboardChart(targetObj, data, meterType){

    if(data.length <= 0) return false;

    var keyArr = ["date", "todayValue", "yesterDayValue"];
    var labelArr = ["당일", "전일"];
    var typesArr = ["bar","spline"];
    var colorArr = [getReadingUnit(meterType).color,'#b1a9a6'];
    var chartCycle = "HOUR"; // DAY, WEEK, MONTH, HOUR
    
    // 금일 데이터중 최고 idx 및 값 조회
    var maxIdx = getMaxIndexByArray(data, "todayValue");
    var maxValue = data[maxIdx].todayValue;
    // 최대값으로 변환할 단위 조회
    var chartDisplayUnit = getReadingUnit(meterType, maxValue).displayUnit;

    // 단위 환산
    var dataConverted = [];
    for(var i = 0; i <= data.length -1; i++){
        dataConverted.push({
            date : data[i].date,
            todayValue : convertUnit(data[i].todayValue, meterType, chartDisplayUnit),
            yesterDayValue : convertUnit(data[i].yesterDayValue, meterType, chartDisplayUnit),
        });
    }
    var config = chart.baseConfig(targetObj, dataConverted, keyArr, labelArr, typesArr, colorArr);
    config.axis.y.show = true;
    config.axis.y.min = 0;
    //config.axis.y.tick = { format : function (x){ return x + chartDisplayUnit} };    // y 축 단위 설정
    config.legend.show = true;
    config.legend.position = "right";
    config.axis.y.label = chartDisplayUnit; // 단위 노출

    chart.chartDateStr(config, chartCycle);

    // 당일 차트 색깔 지정.. 현재 시간이면 붉은색.
    var nowHour = new Date().getHours();
    config.data.colors = {
        "당일": function (d){
            if(d.x == nowHour){
                return getReadingUnit(meterType).color;
            } else {
                return  (d.x === parseInt(maxIdx)) ? "#ff0000" : "#a09895";
            }
        },
        "전일": "#e9e1de"
    };

    dashbardChart.lineChart.push(bb.generate(config));
}

/**
 * 게이지 차트
 * @param meterType
 * @param data
 */
function displayDashboardPieChart(meterType, data){
    var todaySum = 0;
    var yesterDaySum = 0;

    for (var i = 0; i < data.length; i++) {
        todaySum += data[i].todayValue;
        yesterDaySum += data[i].yesterDayValue;
    }

    todaySumData[meterType].todaySum = todaySum;
    todaySumData[meterType].yesterDauSum = yesterDaySum;

    // 에너지 사용 현황 금일 사용량 출력
//    displayTodaySum(meterType, todaySum);

    // 당월, 전월 게이지 차트
    var baseDate = new Date().toISOString().substring(0, 10);
    var url = "/v1.0/dashboard/getUsageMonth/" + meterType + "/" + baseDate;
    var targetObj = "#chart" + meterType + "piebox";

//    $.fn.Ajax("GET", url, "", function (monthData) {
//        if(monthData.resultCode === "OK") {
var monthData = [];
            var chartData = {};
            var thisMonthVal = Math.round(Math.random()*100);
            //var thisMonthValOrg = thisMonthVal;
            //thisMonthVal = unitPointNumber(meterType,thisMonthVal,false);
            var lastMonthVal = Math.round(Math.random()*100);
            //lastMonthVal = unitPointNumber(meterType,lastMonthVal, false);

            chartData.maxValue = lastMonthVal;
            chartData.data = [ "당월", thisMonthVal];
            //chartData.unitString = getReadingUnit(meterType, thisMonthValOrg).displayUnit;
            chartData.targetObj = targetObj;
            chartData.colors = [getReadingUnit(meterType).color];
            chartData.size = { "width": $(targetObj).width() - 60 , "height": $(targetObj).height() - 60};
            chartData.meterType = meterType;
            var config = chart.gaugeConfig(chartData);
            config.gauge.width = 15;
            dashbardChart.pieChart.push(bb.generate( config ));
//        };
//    });

}


function displayTodaySum(meterType, todaySumValue){

    var targetObj = "#todayUsage_" + meterType;
    var readingUnit = getReadingUnit(meterType, todaySumValue);
    var data = {
        meterType: meterType,
        displayName: readingUnit.displayName,
        displayUnit: readingUnit.displayUnit,
        sumValue: unitPoint(meterType, todaySumValue, true)
    };
    setTemplate($("#todayUsageSumTmpl"), $(targetObj), data, function () {
        todaySumCount +=1;
        if(todaySumCount === 6){
            displayTodaySumChart2("#todayUsageChartBox");
            todaySumCount = 0;
        }
    });
}

function displayTodaySumChart2(container) {

    var $targetObj = $(container);

    $targetObj.children().remove();

    var elecValue = Math.round(Math.random()*100);
    var gasValue = Math.round(Math.random()*100);
    var waterValue = Math.round(Math.random()*100);
    var hotwValue = Math.round(Math.random()*100);
    var heatValue = Math.round(Math.random()*100);
    var etcValue = Math.round(Math.random()*100);

    var data = [
        {value: elecValue.toFixed(0),   label: "성공", color: getReadingUnit("elec").color},
        {value: gasValue.toFixed(0),    label: "실패", color: getReadingUnit("gas").color},
        {value: waterValue.toFixed(0),  label: "진행", color: getReadingUnit("water").color},
        {value: hotwValue.toFixed(0),   label: "로컬", color: getReadingUnit("hotw").color},
        {value: heatValue.toFixed(0),   label: "개발", color: getReadingUnit("heat").color},
        {value: etcValue.toFixed(0),    label: "검증", color: getReadingUnit("etc").color}
    ];
    
    console.log(data);

    var height = $targetObj.height() - 10;
    var width = height;

    var arcSize = (6 * width / 100) - 1;
    var innerRadius = arcSize * 3;

    function render() {

        var svg = d3.select(container).append('svg').attr('width', width).attr('height', width);

        var arcs = data.map(function (obj, i) {
            return d3.svg.arc().innerRadius(i * arcSize + innerRadius).outerRadius((i + 1) * arcSize - (width / 100) + innerRadius);
        });

        var arcsGrey = data.map(function (obj, i) {
            return d3.svg.arc().innerRadius(i * arcSize + (innerRadius + ((arcSize / 2) - 2))).outerRadius((i + 1) * arcSize - ((arcSize / 2)) + (innerRadius));
        });

        var pieData = data.map(function (obj, i) {
            return [
                {value: obj.value * 0.75, arc: arcs[i], object: obj},
                {value: (100 - obj.value) * 0.75, arc: arcsGrey[i], object: obj},
                {value: 100 * 0.25, arc: arcs[i], object: obj}];
        });

        var pie = d3.layout.pie().sort(null).value(function (d) {
            return d.value;
        });

        var g = svg.selectAll('g').data(pieData).enter()
            .append('g')
            .attr('transform', 'translate(' + width / 2 + ',' + width / 2 + ') rotate(180)');
        var gText = svg.selectAll('g.textClass').data([{}]).enter()
            .append('g')
            .classed('textClass', true)
            .attr('transform', 'translate(' + width / 2 + ',' + width / 2 + ') rotate(180)');

        g.selectAll('path').data(function (d) {
            return pie(d);
        }).enter()
            .append('path')
            .attr('id', function (d, i) {
                if (i == 1) {
                    return "Text" + d.data.object.label
                }
            })
            .attr('d', function (d) {
                return d.data.arc(d);
            }).attr('fill', function (d, i) {
            /*return i == 0 ? d.data.object.color : i == 1 ? '#D3D3D3' : 'none';*/
            return i == 0 ? d.data.object.color : i == 1 ? 'none' : 'none';     // 베이스 라인 중앙 색..
        });

        svg.selectAll('g').each(function (d, index) {
            var el = d3.select(this);
            var path = el.selectAll('path').each(function (r, i) {
                if (i === 1) {
                    var centroid = r.data.arc.centroid({
                        startAngle: r.startAngle + 0.05,
                        endAngle: r.startAngle + 0.001 + 0.05
                    });
                    var lableObj = r.data.object;
                    g.append('text')
                        //.attr('font-size', ((5 * width) / 100))
                        .attr('font-size', '10px')
                        .attr('dominant-baseline', 'central')
                        .append("textPath")
                        .attr("textLength", function (d, i) {
                            return 0;
                        })
                        .attr("xlink:href", "#Text" + r.data.object.label)
                        .attr("startOffset", '5')
                        .attr("dy", '-3em')
                        .attr('fill', lableObj.color)
                        .text(lableObj.value + '%');
                }
                if (i === 0) {
                    var centroidText = r.data.arc.centroid({
                        startAngle: r.startAngle,
                        endAngle: r.startAngle
                    });
                    var lableObj = r.data.object;
                    gText.append('text')
                        //.attr('font-size', ((5 * width) / 100))
                        .attr('font-size', '10px')
                        .attr('fill', lableObj.color)
                        .text(lableObj.label)
                        .attr('transform', "translate(" + (centroidText[0] - ((1.5 * width) / 100)) + "," + (centroidText[1] + ") rotate(" + (180) + ")"))
                        .attr('dominant-baseline', 'central');
                }
            });
        });

        // 가운데처리
        /*svg.append("text")
            .attr("font-size", "18px")
            .attr("font-weight", "bold")
            .attr("transform", "translate("+(width/2-27)+", "+(height/2+17)+")")
            .style("fill", "#000000")
            .text(pftValue+"%");*/
    }

    render();
}

function setBarChart() {

    var targetObj = "#todayUsageChartBox";
    var keyArr = ["areaName","usageAvg"];
    var labelArr = ["건수"];
    var typesArr = ["bar"];
    var colorArr = ["#D9D9D9","#FF0000","#0987DB"]; // 기본 : #D9D9D9, 최고 : #FF0000, 최저 : #01B56E, 지역 : #0987DB
    var chartCycle = ""; // DAY, WEEK, MONTH, HOUR
    var chartData = [];
    var array = [];
    var barColor = [];
    var areaIndex = 0;
    var areaParentAvg = Math.round(Math.random()*100);

    var maxValue = 0;
    var minValue = 0;
    var maxIndex = 0;
    var minIndex = 0;
    var chatCount = 0;
    
    var data = [];
    var items = ["성공","실패","진행"];
    for (var i = 0; i < items.length; i++) {
        var obj = new Object;
        obj.areaName = items[i];
        obj.usageAvg = Math.round(Math.random()*100);
        
        data.push(obj);
    }
    
    if (data.length > 0) {
        chatCount = data.length;
        for (var i = 0; i < data.length; i++) {
            var obj = new Object;
            obj.areaName = data[i].areaName;
            obj.usageAvg = data[i].usageAvg;

            chartData.push(obj);
            
            array.push(data[i].usageAvg);

            barColor.push("#D9D9D9");
        }
        var maxValue = array.reduce( function (previous, current) { 
            return previous > current ? previous:current;
        });
        var minValue = array.reduce( function (previous, current) { 
            return previous > current ? current:previous;
        });
        var maxIndex = $.inArray(maxValue,array);
        var minIndex = $.inArray(minValue,array);
        barColor[minIndex] = "#01B56E";
        barColor[maxIndex] = "#FF0000";
        barColor[areaIndex] = "#0987DB";
    }

    var config = chart.baseConfig(targetObj, chartData, keyArr, labelArr, typesArr, colorArr);
    config.legend.show = false;
    config.legend.position = "bottom";
    config.grid.x.show = false;
    config.grid.y.show = false;
    config.axis.y.show = true;
    config.axis.x.show = true;
    config.axis.rotated = true;
//    if (chatCount <= 10) {
//        config.bar.width = 25;
//        config.bar.padding = 15;
//    }
    config.data.labels = true;
    config.data.color = function(color, d) {
        return barColor[d.index];
    }
    // 평균 세로바 삽입..
    config.grid.y.lines = [
        {
            value: (maxValue / 2),
            class: "barchart_center_line",
            text: "평균 : " + areaParentAvg
        }
    ];

    chart.chartDateStr(config, chartCycle);
    chartObjArr.push(bb.generate( config ));
}

/**
 * 날씨
 */
function getWeather() {
    realtime.init();
    setBarChart();
}

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
         realChart = this.inst = bb.generate({
            padding: {
              bottom: 10
            },
            data: {
                x: 'x',
                columns: this.getInitData(5),
                type: "bar"
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
            bindto: "#realChart"
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
                ["로컬", Math.round(Math.random()*100)],
                ["개발", Math.round(Math.random()*100)],
                ["검증", Math.round(Math.random()*100)]
            ],
            length: 1,
            duration: 500,
            done: this.flow.bind(this)
        });
    },
    getInitData: function(len) {
        const d = +new Date - 1000 * len;
        const data = [
            ["x"], ["로컬"],
            ["x"], ["개발"],
            ["x"], ["검증"]
        ];

        for (let i = 0; i < len; i++) {
            data[0].push(d + (100 * i));
            data[1].push(null);
        }

        return data;
    }
};

