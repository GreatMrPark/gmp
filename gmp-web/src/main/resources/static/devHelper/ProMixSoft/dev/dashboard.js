// --------------------------------------------------------------------------------
// 전역변수
// --------------------------------------------------------------------------------
gDate = new Date();

var dashbardChart = {
    lineChart : [],
    pieChart : [],
    barChart : [],
    donutChart : []
};

var todaySumCount = 0;
// 에너지 사용현황 차트를 그리기위한 데이터 임시 저장
var todaySumData = { elec   : { todaySum : 0, yesterDauSum :0},
    gas    : { todaySum : 0, yesterDauSum :0},
    water    : { todaySum : 0, yesterDauSum :0},
    hotw    : { todaySum : 0, yesterDauSum :0},
    heat    : { todaySum : 0, yesterDauSum :0},
    etc    : { todaySum : 0, yesterDauSum :0}
};

//검침 종류
const READING_UNIT = ["성공", "실패", "진행", "로컬", "개발", "검증"];
var readingUnitSeq = 0;

var scrollTemp = 0;
var rollingTimer;

// --------------------------------------------------------------------------------
// fnInit
// --------------------------------------------------------------------------------
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
        $("#currentDate").html(currentDate.toLocaleString());
    }, 1000);
}

function centerPieLineChart(){
    getDashboardChart("성공", "#chartElecBox"); // 전기 사용현황
    getDashboardChart("실패", "#chartGasBox");   // 가스
    getDashboardChart("진행", "#chartWaterBox"); //수도
    getDashboardChart("로컬", "#chartHotwBox"); // 온수
    getDashboardChart("개발", "#chartHeatBox"); // 난방
    getDashboardChart("검증", "#chartEtcBox");   // 신재생
}


/**
 * 중앙 시간별 사용량 라인차트
 * @param meterType
 * @param container
 */
function getDashboardChart(meterType, container){
    displayDashboardChart(container, data.data, meterType);    // 라인차트
    displayDashboardPieChart(meterType, data.data); // gauge 차트, 에너지 사용현황
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
    var chartCycle = ""; // DAY, WEEK, MONTH, HOUR

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
    displayTodaySum(meterType, todaySum);

    // 당월, 전월 게이지 차트
    var baseDate = new Date().toISOString().substring(0, 10);
    var url = "/v1.0/dashboard/getUsageMonth/" + meterType + "/" + baseDate;
    var targetObj = "#chart" + meterType + "piebox";

    $.fn.Ajax("GET", url, "", function (monthData) {
        if(monthData.resultCode === "OK") {

            var chartData = {};
            var thisMonthVal = isEmpty(monthData.data.thisMonth) ? 0 : monthData.data.thisMonth.value;
            //var thisMonthValOrg = thisMonthVal;
            //thisMonthVal = unitPointNumber(meterType,thisMonthVal,false);
            var lastMonthVal = isEmpty(monthData.data.lastMonth) ? 0 : monthData.data.lastMonth.value;
            //lastMonthVal = unitPointNumber(meterType,lastMonthVal, false);

            chartData.maxValue = lastMonthVal;
            chartData.data = [ "당월사용량", thisMonthVal];
            //chartData.unitString = getReadingUnit(meterType, thisMonthValOrg).displayUnit;
            chartData.targetObj = targetObj;
            chartData.colors = [getReadingUnit(meterType).color];
            chartData.size = { "width": $(targetObj).width() - 60 , "height": $(targetObj).height() - 60};
            chartData.meterType = meterType;
            var config = chart.gaugeConfig(chartData);
            config.gauge.width = 15;
            dashbardChart.pieChart.push(bb.generate( config ));
        };
    });

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
            //displayTodaySumChart("#todayUsageChartBox");
            displayTodaySumChart2("#todayUsageChartBox");
            todaySumCount = 0;
        }
    });
}



function displayTodaySumChart2(container) {

    var $targetObj = $(container);

    $targetObj.children().remove();

    var elecValue = (todaySumData.elec.todaySum / todaySumData.elec.yesterDauSum) * 100;
    elecValue = (elecValue > 100)  ? 100 : elecValue;
    elecValue = (elecValue < 0) ? 0 : elecValue;
    var gasValue = (todaySumData.gas.todaySum / todaySumData.gas.yesterDauSum) * 100;
    gasValue = (gasValue > 100) ? 100 : gasValue;
    gasValue = (gasValue < 0) ? 0 : gasValue;
    var waterValue = (todaySumData.water.todaySum / todaySumData.water.yesterDauSum) * 100;
    waterValue = (waterValue > 100) ? 100 : waterValue;
    waterValue = (waterValue < 0) ? 0 : waterValue;
    var hotwValue = (todaySumData.hotw.todaySum / todaySumData.hotw.yesterDauSum) * 100;
    hotwValue = (hotwValue > 100) ? 100 : hotwValue;
    hotwValue = (hotwValue < 0) ? 0 : hotwValue;
    var heatValue = (todaySumData.heat.todaySum / todaySumData.heat.yesterDauSum) * 100;
    heatValue = (heatValue > 100) ? 100 : heatValue;
    heatValue = (heatValue < 0) ? 0 : heatValue;
    var etcValue = (todaySumData.etc.todaySum / todaySumData.etc.yesterDauSum) * 100;
    etcValue = (etcValue > 100) ? 100 : etcValue;
    etcValue = (etcValue < 0) ? 0 : etcValue;
    etcValue = isNaN(etcValue) ? 0 : etcValue;

    var data = [
        {value: elecValue.toFixed(1),   label: "전기",     color: getReadingUnit("elec").color},
        {value: gasValue.toFixed(1), label: "가스", color: getReadingUnit("gas").color},
        {value: waterValue.toFixed(1), label: "수도", color: getReadingUnit("water").color},
        {value: hotwValue.toFixed(1),   label: "온수",     color: getReadingUnit("hotw").color},
        {value: heatValue.toFixed(1),   label: "난방",     color: getReadingUnit("heat").color},
        {value: etcValue.toFixed(1),    label: "신재생",    color: getReadingUnit("etc").color}
    ];

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

/**
 * 날씨
 */
function getWeather() {
// readUsageChartBox
}



