// --------------------------------------------------------------------------------
// 공통변수
// --------------------------------------------------------------------------------
var gDate = new Date();
var gHour = gDate.getHours();
var gChartDataList = [];
var gChartData = {};
//--------------------------------------------------------------------------------
// 챠트 기본 객체
// --------------------------------------------------------------------------------
var keyArr = [];
var labelArr = [];
var typesArr = [];
var colorArr = [];
var chartObjArr = [];
var config;

//--------------------------------------------------------------------------------
//소켓 기본 객체
//--------------------------------------------------------------------------------
const contextPath = "/";
const intervalTimeout = 1000 * 10;   // 업데이트 시간
var timer;

//항목 데이터 정보
var DataDefault01 = [
    {
        "ip": "127.0.0.1",
        "serverName": "GMP API 서버",
        "id" : "server_gmp_api",
        "actuatorUri" : "http://localhost:9999/actuator/",
        "websocketUri" : "http://localhost:9999/websockethandler"
    }
];

//--------------------------------------------------------------------------------
//초기화
//--------------------------------------------------------------------------------
$(document).ready(function() {
    
    // 챠트
    fnSetChat(gChartDataList);
    
    // socket 연결
    socketInit();
    
    // 사용량 업데이트
    setMetricValue();
    
});

//--------------------------------------------------------------------------------
//소켓 연결
//--------------------------------------------------------------------------------
function socketConnect(socketUrl, id) {

  var headers = {};

  var socket = new SockJS(socketUrl);
  var stompClient;

  stompClient = Stomp.over(socket);
  stompClient.reconnect_delay = 5000;
  stompClient.debug = true;
  stompClient.connect(headers,
      function (frame) {
          console.log(frame);
          var subscribe = stompClient.subscribe('/topic/request', function (data) {

              try{
                  var jsonData = JSON.parse(data.body);
                  console.log(jsonData.serverId + " - " + jsonData.uri);
                  var serverBulletObj = "#" + jsonData.serverId + "_bullet";
                  
//                  serverBulletBlink(serverBulletObj);
              } catch (e) {
                  console.error(e);
              }
          });
          
          console.log(subscribe);
      },
      function(e){
          console.log("socket connect error !!" + e);
          setTimeout(function () {
              console.log("socket connect retry !!");
              socketConnect(socketUrl);
          }, 5000);
      }
  );
}

//--------------------------------------------------------------------------------
//실시간 연결 소켓 connect
//--------------------------------------------------------------------------------
function socketInit(){

    DataDefault01.forEach(function (obj, idx) {
        var socketUrl = obj.websocketUri;
        var id  = obj.id;

        if(typeof socketUrl !== "undefined"){
            if(socketUrl !== ""){
                console.log("socket Connect : " + socketUrl + " - " + id);
                socketConnect(socketUrl, id);
            }
        }
    });

}

//--------------------------------------------------------------------------------
//getMetricAction
//--------------------------------------------------------------------------------
function getMetricAction(url, successCallback, errorCallback) {

    jQuery.ajax({
        type : "GET",
        url: url,
        timeout: 3000,
        success: function (response) {
            if (typeof successCallback === "function") successCallback(response);
        },
        error: function (req, status, error) {
            if (typeof errorCallback === "function") errorCallback(req, status, error);
        }
    });
}

//--------------------------------------------------------------------------------
//시스템 사용량 update
//--------------------------------------------------------------------------------
function setMetricValue(){

    gChartData = gChartDataList.slice(-1)[0];
//    console.log(gChartData);
    
    DataDefault01.forEach(function (obj, idx) {
        
        try {

            // cpu 사용량.-----------------------------------------------------------------------------------------
            var cpuActionUrl = obj.actuatorUri +  "metrics/system.cpu.usage";
            
            getMetricAction(cpuActionUrl,
                function(data){
//                    console.log("metrics/system.cpu.usage : ", data);
                    var cpuUsage = data.measurements[0].value;
                        cpuUsage = parseInt(cpuUsage * 100);
                    gChartData.cpu   = cpuUsage;
//                    console.log(gChartData);
                    
                    fnSetChartData(gChartData);
                },
                function (req, status, error){
                    console.error(cpuActionUrl  + " - " + error);
                });

//            // disk 사용량.-----------------------------------------------------------------------------------------
//            var diskActionUrl = obj.actuatorUri +  "health/diskSpace";
//            getMetricAction(diskActionUrl,
//                    function (data) {
//                        console.log("health/diskSpace : ", data);
//                        // 남은 용량
//                        var remainUsage = data.details.free;
//                        // 전체 용량
//                        var totalUsage = data.details.total;
//                        gChartData.disk  = ((remainUsage / totalUsage) * 100).toFixed(0);
//                        fnSetChartData(gChartData);
//                        
//                    }, function (req, status, error) {
//                    });
//
//            // memory 사용량.-----------------------------------------------------------------------------------------
//            var memoryActionUrl = obj.actuatorUri +  "metrics/jvm.memory.used";
//            getMetricAction(memoryActionUrl,
//                    function (data) {
//                    console.log("metrics/jvm.memory.used : ", data);
//                        gChartData.ram   = (data.measurements[0].value / 1024 / 1024).toFixed(0);
//                        fnSetChartData(gChartData);
//
//                        },
//                    function (req, status, error) {
//                    });

        } catch (e) {
            console.error("Monitoring Resource Error !! - ");
        }

    });

    timer = setTimeout(setMetricValue, intervalTimeout);
}

//--------------------------------------------------------------------------------
//챠트 DATA UPDATE
//--------------------------------------------------------------------------------
function fnSetChartData(data) {

    gChartData = data;
    gDate = new Date();
    
    var index = gChartDataList.length - 1;
    var lastCtime = gChartData.ctime;
    var lastHour = lastCtime.split(":")[0];
    
    var currentHours   = "0"+gDate.getHours();
    var currentMinutes = "0"+gDate.getMinutes();
    var currentSeconds = "0"+gDate.getSeconds();    
    var ctimeValue = currentHours.slice(-2) + ":" + currentMinutes.slice(-2) + ":" + currentSeconds.slice(-2);
    var currentHour = ctimeValue.split(":")[0];

    gChartData.ctime = ctimeValue;
    console.log(gChartData);

    if (lastHour == currentHour) {
        gChartDataList[index] = gChartData;
    }
    else {
        gChartDataList.push(gChartData);
    }
//  console.log(gChartDataList);
   
    fnSetChat(gChartDataList);
}
//--------------------------------------------------------------------------------
//챠트 초기화
//--------------------------------------------------------------------------------
function setChartDestroy(){
  for(var i=0; i<chartObjArr.length; i++){
      chartObjArr[i].destroy();
  }
  chartObjArr = [];
}

//--------------------------------------------------------------------------------
//챠트 
//--------------------------------------------------------------------------------
function fnSetChat(data) {
    setChartDestroy();
    
    var targetObj = "#barSocketChart";

    var chartData = data;
    var keyArr = ["ctime","cpu","ram","disk"];
    var labelArr = ["CPU","RAM","DISK"];
    var typesArr = ["line","bar","bar"];
    var colorArr = [];
    var dataColor = [];
    var chartCycle = ""; // DAY, WEEK, MONTH, HOUR
    
    if (fnIsEmpty(chartData) === true) {
        gDate = new Date;
        gHour = gDate.getHours();
        var currentHour = parseInt(gHour, 10);
        for (var i = 0; i <= currentHour; i++) {
            var hour = "0"+i;
            var ctimeValue = hour.slice(-2)+":00:00";
            var cpuValue  = 90 - i;
            var ramValue  = 90 - i;
            var diskValue = 90 - i;
            var obj   = new Object;

            obj.ctime = ctimeValue
            obj.cpu   = cpuValue;
            obj.ram   = ramValue;
            obj.disk  = diskValue;
            
            console.log(obj);
            
            chartData.push(obj);
        }
        gChartDataList = chartData;
    }
    
    var config = GMPBillboardChart.baseConfig(targetObj, chartData, keyArr, labelArr, typesArr, colorArr);

    config.legend.show = true;
    config.legend.position = "bottom";
    config.grid.x.show = false;
    config.grid.y.show = false;
    config.axis.x.show = true;
    config.axis.y = {show:true, label:"%"};
    config.axis.rotated = false;
//    config.bar.width = 25;
//    config.bar.padding = 15;
    config.data.labels = false;
//    config.data.axes = {"미터수" : "y2"};
//    config.axis.y2 = {show : true};

    GMPBillboardChart.chartDateStr(config, chartCycle);
    chartObjArr.push(bb.generate( config ));
}



