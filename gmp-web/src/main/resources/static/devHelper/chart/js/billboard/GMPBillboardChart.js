// --------------------------------------------------------------------------------
// GMPChart 
// - 챠트 확장
// --------------------------------------------------------------------------------
var GMPBillboardChart = {
    baseConfig : function(targerObj, chartData, keyArr, labelArr, typesArr, colorArr){
        // data 세팅
        var columnsArr = [];
        for(var i=0; i<chartData.length; i++){

            if(i == 0){
                for(var j=0; j<keyArr.length; j++){
                    var obj = [];
                    obj.push(chartData[i] [keyArr[j]]);
                    columnsArr.push(obj);
                }
            }else{
                for(var j=0; j<keyArr.length; j++){
                    columnsArr[j].push(chartData[i][ keyArr[j] ]);
                }
            }
        }

        for(var i=0; i<columnsArr.length; i++){
            if(i == 0){
                columnsArr[i].unshift("x");
            }else{
                columnsArr[i].unshift(labelArr[i-1]);
            }
        }

        //컬러 타입
        var colors = {};
        var types = {};
        for(var i=0; i<labelArr.length; i++){
            colors[labelArr[i]] = colorArr[i];
            types[labelArr[i]] = typesArr[i];
        }

        return {
            data: {
                x: "x",
                columns: columnsArr,
                type: "bar",
                types: types,
                colors: colors,
                labels: false
            },

            zoom: {
                enabled: {
                    type: "drag"
                }
            },

            axis: {
                x: {
                    type: "category",
                    show: true,
                    tick: {
                        centered:true, fit: false, multiline:false
                    }
                },
                y : {
                    show :true,
                    tick: {
                        format: function(x) {
                            return x;
                        }
                    }
                }
            },

            grid: {
                x: {
                    show: false
                },
                y: {
                    show: false
                }
            },

            legend: {
                show : true
            },

            bar :{ padding: 2 },

            bindto: targerObj
        }

    },

    initConfig : function(targerObj, columnsArr){
        return {
            data: {
                x: "x",
                columns: columnsArr,
                type: "bar",
                labels: true,

            },

            axis: {
                x: {
                    type: "category",
                    show: true,
                    tick: {
                        centered:true, fit: false, multiline:false
                    }
                },
                y : {
                   show :true,
                   tick: {
                       format: function(x) {
                           return x;
                       }
                   }
                }

            },

            grid: {
                x: {
                    show: true
                },
                y: {
                    show: true
                }
            },

            legend: {
                show : true
            },

            bar : { padding: 2 },

            bindto: targerObj
        }
    },

    donutConfig : function(columns, targerObj) {
        return {
            size:{
                width:180,
                height:180
            },
            data: {
                columns: columns,
                type: "donut"
            },
            donut: {
                title: ""
            },
            legend : {
                position : "right"
            },
            bindto: targerObj
        }
    },

    gaugeConfig : function (data){

        return {
            size: data.size,
            data: {
                columns: [
                    data.data
                ],
                type: "gauge",
                },
            color: {
                    pattern: data.colors
                },
            gauge: {
                max: data.maxValue,
                label: {
                    format: function (value, ratio) {
                        return unitPoint(data.meterType, value, true) + "\n" + getReadingUnit(data.meterType, value).displayUnit;
                        },
                    extents: function (value, isMax) {
                        // min, max 값 커스터마이징
                        if(isMax){
                            return unitPoint(data.meterType, value, true) + " " + getReadingUnit(data.meterType, value).displayUnit;
                            //return data.maxValue;
                        } else {
                            return "사용량";
                        }
                    }
                },
                width: 20
                },
            bindto: data.targetObj
            };
    },

    pieConfig : function (targetObj, data, meterType, energyType){
        return {
            data: {
                columns: data,
                type: "pie"
            },
            pie: {
                label: {
                    format: function(value, ratio, id) {    return value +" " + getItemUnit(energyType).displayUnit;       }
                }
            },
            bindto: targetObj
        }
    },

    dateAxisSet : function(config, chartCycle){
        if(chartCycle != "DAY"){
            config.axis.x.type = "category";
            delete config.axis.x.tick;
        }

        return config;
    },

    chartDateStr : function(config, chartCycle){
        if(chartCycle == 'DAY'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr.substr(5,10);
                }
            };
        }else if(chartCycle == 'WEEK'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr.substr(5,10);
                }
            };
        }else if(chartCycle == 'MONTH'){

        }else if(chartCycle == 'HOUR'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr.substr(5,13);
                }
            };
        }else if(chartCycle ==='HOUR_BY_FULLSTRING'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr.substr(8);
                }
            };
        }

        return config;
    }
}

