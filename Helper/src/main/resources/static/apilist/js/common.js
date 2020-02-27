/**
 * 공통 변수들
var host = "http://" + $(location).attr('host')+"/gwaapi";
 */
var host = "http://" + $(location).attr('host') + "/gmp/v1.0";
var token = "";
var actionUrl = "";
var projectName = "Dev Helper Of Great Mr. Park";
var projectMenu = "<li>"+projectName+"</li>";
var projectLink = "<li>"+projectName+"</li>";
        
/**
 * 프로젝트 선언
 */
document.title = projectName;
$( document ).ready(function() {
	$("#projectMenu").html(projectMenu);
	$("#projectLink").html(projectLink);
	$("#projectName").text(projectName);
});

/**
 * jQuery import 바로아래에 넣어 주면 됩니다.
 * Cannot read property 'msie' of undefined 에러 나올때 
 * @returns
 */
jQuery.browser = {};
(function () {
 jQuery.browser.msie = false;
 jQuery.browser.version = 0;
 if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
     jQuery.browser.msie = true;
     jQuery.browser.version = RegExp.$1;
 }
})();

/**
 * 브라우저 강제 종료
 * @returns
 */
$(function(){   
    $('#window-btn').click(function(){     
      if ( $.browser.msie ) {
        top.window.opener = top;
        top.window.open('','_parent', '');
        top.window.close();
        open(location, '_self').close();
      } else {
        top.window.open('about:blank','_self').self.close();  // IE에서 묻지 않고 창 닫기
        top.window.opener=self;
        top.self.close(); 
      }
    })
});


/**
 * cs_cmm html 공통 Include 함수
 * >> 반드시 html 앞에 선언하여야됨
 */
(function ($) {
    $.include = function (url) {
        $.ajax({
            url: url,
            async: false,
            success: function (result) {
                document.write(result);
                $("#responseResult").val('');
            }
        });
    };
}(jQuery));

/**
 * From 객체 TO JSON
 */
jQuery.fn.serializeObject = function() { 
    var obj = null; 

    /**
     * 
     */
    var checkboxes = this.find('input[type="checkbox"]');
    var checkedItems = {};
    checkboxes.each(function(index, item) {
        if (item.checked) {
            if (checkedItems[item.name]==undefined) {
                checkedItems[item.name] = [];
            } 
            checkedItems[item.name].push(item.value);
        }
    });
    
    /**
     * 
     */
    try { 

        if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) { 
            var arr = this.serializeArray(); 
            if(arr){ 
                obj = {}; 
                jQuery.each(arr, function() {
                    var key = this.name;
                    var value = this.value;
                    if(checkedItems[key]) {
                        value = checkedItems[key].toString();
                    }
                    obj[key] = value;
                }); 
            } 
        } 
    }catch(e) { 
        alert(e.message); 
    }finally {} 
    return obj; 
};


/**
 * Object To String
 * @param obj
 * @returns
 */
function fnOTS (obj) {
    var str = '';
    if (typeof obj === "object" ) {

        for (var p in obj) {
            if (obj.hasOwnProperty(p)) {
                str += p + ':' + obj[p] + '\n';
            }
        }
        
    } else {
        str += obj;
    }
    return str;
}

/**
 * 페이지 로딩
 * @returns
 */
function pageLoad(url,input) {
    $('#apiInput').html("");
    if(url==="") {
        url = "./blank.html"
    }
    $('#apiContent').load(url);
    if(input!=="Y") {
        $('#apiInput').load("input.html");
    }
}

/**
 * Ajax Post 호출
 * @param url
 * @param params
 * @param success
 * @param fail
 */
function fnSendPostAjax(url, header, params, success, fail) {
    
    $.ajax({
        url: url,
        type: "POST",
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: header,
        dataType: "xml",
        contentType: "application/xml; charset=utf-8",
        data: params,
        success : function(data) {
            if (typeof success !== 'undefined' && $.isFunction(success)) {
                success(data);
            }
        },
        error : function(xhr, status, error) {
            if (typeof fail !== 'undefined' && $.isFunction(fail)) {
                fail(xhr, status, error);
            }
        }
    });
}

function fnSendJsonPostAjax(url, header, params, success, fail) {
    
    $.ajax({
        url: url,
        type: "POST",
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: header,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: params,
        success : function(data) {
            if (typeof success !== 'undefined' && $.isFunction(success)) {
                success(data);
            }
        },
        error : function(xhr, status, error) {
            if (typeof fail !== 'undefined' && $.isFunction(fail)) {
                fail(xhr, status, error);
            }
        }
    });
}

/**
 * Ajax Put 호출
 * @param url
 * @param params
 * @param success
 * @param fail
 */
function fnSendPutAjax(url, header, params, success, fail) {

    $.ajax({
        url: url,
        type: "PUT",
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: header,
        dataType: "xml",
        contentType: "application/xml; charset=utf-8",
        data: params,
        success : function(data) {
            if (typeof success !== 'undefined' && $.isFunction(success)) {
                success(data);
            }
        },
        error : function(xhr, status, error) {
            if (typeof fail !== 'undefined' && $.isFunction(fail)) {
                fail(xhr, status, error);
            }
        }
    });
}

/**
 * Ajax Get 호출
 * @param url
 * @param success
 * @param fail
 */
function fnSendGetAjax(url, header, params, success, fail) {
	
    $.ajax({
        url: url,
        type: "GET",
        timeout: 100000,
        cache: false,
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: header,
        dataType: "xml",
        contentType: "application/xml; charset=utf-8",
        data: params,
        success: function (data) {
        	fnSetResponse(data);
        },
        error: function (xhr, status, error) {
            if (typeof fail !== 'undefined' && $.isFunction(fail)) {
                fail(xhr, status, error);
            }
        },
    });
};

function formatXml(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/*)/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function(index, node) {
        var indent = 0;
        if (node.match( /.+<\/\w[^>]*>$/ )) {
            indent = 0;
        } else if (node.match( /^<\/\w/ )) {
            if (pad != 0) {
                pad -= 1;
            }
        } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
            indent = 1;
        } else {
            indent = 0;
        }

        var padding = '';
        for (var i = 0; i < pad; i++) {
            padding += '  ';
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}

/**
 * 결과출력
 * @param res
 * @returns
 */
function fnSetResponse(res) {
	if (jQuery.isXMLDoc( res )) {
		$('#responseResult').val(formatXml((new XMLSerializer()).serializeToString(res)));
	} else {
	    $('#responseResult').val(JSON.stringify(res, null, 4));
	}
}

function fnSetXmlResponse(res) {
    $('#responseResult').val((new XMLSerializer()).serializeToString(res));
}

/**
 * 콘솔출력
 * @param log
 * @returns
 */
function fnSetConsole(log) {
    $("#responseConsole").prepend("\n");
    $("#responseConsole").prepend(fnOTS(log));
}

/**
 * 토큰 생성
 */
function fnGetToken() {

    var token = "";
    
    var contentType = $('#ContentType').val();
    var xAuthToken  = $('#XAuthToken').val();
    var actionType  = $('#apiMehtod').val();
    var actionUrl   = host + "/user/login";
    
    var id = "admin";
    var pw = "1234";

    var header = {
              ContentType: contentType
            , XAuthToken: ""
    }
    var params = {
              username: id
            , password: pw
    }
    
    if(actionType=="POST") {
        params = JSON.stringify(params);
    }
    
    fnSetConsole(params);
    fnSetConsole(header);
    fnSetConsole(actionType);
    fnSetConsole(actionUrl);
    fnSetConsole("============================================================");
    fnSetConsole(""); 
    
    fnSendPostAjax(actionUrl, header, params,
        function (data) {
            if ( data.token !=="" ) {
                token = data.token;
                fnSetToken(token);
            }
            fnSetResponse(data);
        },
        function (xhr, status, error) {
            fnSetResponse(xhr);
        }
    );
}

/**
 * 토큰세팅
 * @param token
 * @returns
 */
function fnSetToken(token) {
    if (token !== "" && token !== undefined ) {
        $("#XAuthToken").val(token);
    }
}

/**
 * API 호출
 *
 * @returns
 */
function doAction() {
    
    var contentType = $('#ContentType').val();
    var xAuthToken  = $('#XAuthToken').val();
    var actionType  = $('#apiMehtod').val();
    var actionUrl   = $('#apiUrl').val();
    var smgwId   	= $('#smgwId').val();
    var meterType   = $('#meterType').val();
    var meterId   	= $('#meterId').val();
    var msCode   	= $('#msCode').val();
    var msId   	    = $('#msId').val();
    var msCodeId   	= msCode+"-"+msId;
    var touTTable  	= $('#touTTable').val();
    var billingType	= $('#billingType').val();
    var header = {
              ContentType: contentType
              , XAuthToken: xAuthToken
              , smgwId: smgwId
              , meterType: meterType
              , meterId: meterId
              , msCode: msCode
              , msCodeId: msCodeId
              , touTTable: touTTable
              , billingType: billingType
    }
    var params;
    
    var input = $("#input").val();
    params = $("#xml").val();

	$("#responseResult").val("");
    if (actionUrl === "") return;
    
    fnSetConsole(params);
    fnSetConsole(header);
    fnSetConsole(actionType);
    fnSetConsole(actionUrl);
    fnSetConsole("============================================================");
    fnSetConsole(""); 
    
//    if(actionType==="POST" || actionType==="PUT" ) {
//        params = JSON.stringify(params);
//    }

    fnSetConsole(params);
    fnSetConsole(header);
    fnSetConsole(actionType);
    fnSetConsole(actionUrl);
    fnSetConsole("============================================================");
    fnSetConsole(""); 
    
    if(actionType==="GET") {

    	fnSendGetAjax(actionUrl, header, params,
            function (data) {
                fnSetResponse(data);
            },
            function (xhr, status, error) {
                fnSetResponse(xhr);
            }
        );
        
    } 
    else if(actionType==="PUT") {
        fnSendPutAjax(actionUrl, header, params,
                function (data) {
                    fnSetResponse(data);
                },
                function (xhr, status, error) {
                    fnSetResponse(xhr);
                });
    }
    else {
        fnSendPostAjax(actionUrl, header, params,
            function (data) {
                fnSetResponse(data);
            },
            function (xhr, status, error) {
                fnSetResponse(xhr);
            }
        );
    }
}


function jsonAction() {
    
    var contentType = $('#ContentType').val();
    var xAuthToken  = $('#XAuthToken').val();
    var actionType  = $('#apiMehtod').val();
    var actionUrl   = $('#apiUrl').val();

    var header = {
              ContentType: contentType
              , XAuthToken: xAuthToken
    }
    var params;
    
    var input = $("#input").val();
    
    
    params = $("#FrmApi").serializeObject();


	$("#responseResult").val("");
    if (actionUrl === "") return;
    
    fnSetConsole(params);
    fnSetConsole(header);
    fnSetConsole(actionType);
    fnSetConsole(actionUrl);
    fnSetConsole("============================================================");
    fnSetConsole(""); 
    
    if(actionType==="POST" || actionType==="PUT" ) {
        params = JSON.stringify(params);
    }

    fnSetConsole(params);
    fnSetConsole(header);
    fnSetConsole(actionType);
    fnSetConsole(actionUrl);
    fnSetConsole("============================================================");
    fnSetConsole(""); 
    
    if(actionType==="GET") {

    	fnSendGetAjax(actionUrl, header, params,
            function (data) {
                fnSetResponse(data);
            },
            function (xhr, status, error) {
                fnSetResponse(xhr);
            }
        );
        
    } 
    else if(actionType==="PUT") {
        fnSendPutAjax(actionUrl, header, params,
                function (data) {
                    fnSetResponse(data);
                },
                function (xhr, status, error) {
                    fnSetResponse(xhr);
                });
    }
    else {
        fnSendJsonPostAjax(actionUrl, header, params,
            function (data) {
                fnSetResponse(data);
            },
            function (xhr, status, error) {
                fnSetResponse(xhr);
            }
        );
    }
}

function setJobCron() {
	var jobCron = ""; // 0/3 * * * * ?
	var jobSecond	= $("#jobSecond").val();
	var jobMinute	= $("#jobMinute").val();
	var jobHour		= $("#jobHour").val();
	
	jobCron = jobSecond + " " + jobMinute + " " + jobHour + " " + "* * ?";
	 $("#jobCron").val(jobCron);
}
