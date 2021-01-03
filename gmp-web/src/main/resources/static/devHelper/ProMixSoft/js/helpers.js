
Handlebars.registerHelper("plus", function(value, option)
{
    return parseInt(value) + parseInt(option);
});

Handlebars.registerHelper("minus", function(value, option)
{
    return parseInt(value) - parseInt(option);
});

Handlebars.registerHelper("division", function(value, option)
{
    return parseInt(value) / parseInt(option);
});


Handlebars.registerHelper("isArray", function(value)
{
    return Handlebars.Utils.isArray(value);
});

Handlebars.registerHelper("greater", function(value, option)
{
    return parseInt(value) > parseInt(option);
});

Handlebars.registerHelper("ifEqual", function(value, value2)
{
    return value === value2;
});

/**
 * Equal if문
 */
Handlebars.registerHelper('isEqual', function(v1, v2, options) {
    if(v1 === v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});

/**
 * not Equal
 */
Handlebars.registerHelper('notEqual', function(v1, v2, options) {
    if(v1 !== v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});


Handlebars.registerHelper("colorClass", function(value, option)
{
    var str = "";
    if (parseInt(value) < 10) {
        switch(parseInt(option)) {
            case 0:
                str = "bg_blue";
                break;
            case 1:
                str = "bg_red";
                break;
            case 2:
                str = "bg_purple";
                break;
            case 3:
                str = "bg_yellow";
                break;
            case 4:
                str = "bg_cyan";
                break;
            case 5:
                str = "bg_blue2";
                break;
            case 6:
                str = "bg_red2";
                break;
            case 7:
                str = "bg_purple2";
                break;
            case 8:
                str = "bg_yellow2";
                break;
            case 9:
                str = "bg_cyan2";
                break;
            default:
                str = "bg_cyan";
        }
    }
    return str;
});

// 파이차트 칼라
Handlebars.registerHelper("colorClassPieChart", function(value)
{
    var str = "";
        switch(parseInt(value)) {
            case 0:
                str = "dot_blue";
                break;
            case 1:
                str = "dot_green";
                break;
            case 2:
                str = "dot_red";
                break;
            case 3:
                str = "dot_yellow";
                break;
            case 4:
                str = "dot_purple";
                break;
            default:
                str = "dot_purple";
        }

    return str;
});

// 콤마 처리
Handlebars.registerHelper("fnCommaNumberFormat", function(value)
{
    return fnCommaNumberFormat(value);
});

// 콤마 처리 - 소수점 제거
Handlebars.registerHelper("fnCommaNumberFormatFloor", function(value)
{
    return fnCommaNumberFormat(Math.floor(value));
});

// 인코딩
Handlebars.registerHelper("fnEncoding", function(value)
{
    return encodeURIComponent(value);
});

// 날짜포맷 (초 -> 포맷)
Handlebars.registerHelper("fnDateFormat", function(value)
{
    let resultStr = '';
    if(value) {
        let sec_num = parseInt(value, 10);
        let hours   = Math.floor(sec_num / 3600);
        let minutes = Math.floor((sec_num - (hours * 3600)) / 60);
        let seconds = sec_num - (hours * 3600) - (minutes * 60);


        if(hours) { resultStr += hours + '시 '; }
        if(minutes) { resultStr += minutes + '분 '; }
        if(seconds) { resultStr += seconds + '초'; }

    }
    return resultStr;
});

// 절대갑 반환, 소수점 버림
Handlebars.registerHelper("MathFloor", function(value)
{
    return Math.floor(value);
});

// 증가
Handlebars.registerHelper("incremented", function(value)
{
    value++;
    return value;
});

// 백분율 변환 (소수점 버림)
Handlebars.registerHelper("percent", function(value)
{
    let result = value * 100;
    result = Math.floor(result);
    return result != 'NAN' ? result : '';
});

// 비교 연산
Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
    switch (operator) {
        case '==':
            return (v1 == v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 != v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
});

Handlebars.registerHelper('fnGetDate', function(v1) {

    return v1.split(" ")[0].replace(/-/g, '.');
});

Handlebars.registerHelper('fnGetTime', function(v1) {

    return v1.split(" ")[1];
});

// 연산
Handlebars.registerHelper('math', function (v1, operator, v2) {
    switch (operator) {
        case '+':
            return v1 + v2;
        case '-':
            return v1 - v2;
        case '/':
            return v1 / v2;
    }
});

// URI 인코딩
Handlebars.registerHelper('encodeURIComponent', function (v1) {
    return encodeURIComponent(v1);
});

Handlebars.registerHelper('fnFloor', function(v1){
    return fnCommaNumberFormat(Math.floor(v1));
});

Handlebars.registerHelper('getPage', function(page, index){
   return (page * 10) + index + 1;
});

Handlebars.registerHelper('breaklines', function(text) {
    text = text.replace(/<br>/g, "\r\n");
    text = Handlebars.Utils.escapeExpression(text);
    text = text.replace(/(\r\n|\n|\r)/gm, '<br>');
    return new Handlebars.SafeString(text);
});

Handlebars.registerHelper('fnGetDateTime', function(v1) {

    return v1.split(" ")[1] + " / " + v1.split(" ")[0].replace(/-/g, '.');
});

Handlebars.registerHelper('chkEventName', function(deviceType){
    var name = "chkType_";
    if(deviceType == "0"){
        name = name + "SMGW";
    }else if(deviceType == "9"){
        name = name + "CLS";
    }else{
        name = name + "METER";
    }

    return name;

});

Handlebars.registerHelper('getTariffName', function(tariff){
    return getTariffName(tariff);

});
