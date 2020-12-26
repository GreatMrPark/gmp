var gPageSize = 10;
var gElasticSearchWindowSize = 10000;
var gIntMaxValue = 2147483647;
var gPageSizeAllData = gIntMaxValue;
var gExcelDownloadSize = gIntMaxValue;
var gContextPath = '/';
var gVersionPath = '/v1'
var gServerAddress = window.location.origin;
gServerAddress = (
	! gServerAddress 
	? window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '')
	: gServerAddress
);

var CONTEXT_PATH = "/";
var CONTENT_TYPE = 'application/json;charset=UTF-8';
var API_HEADERS = {};

//2020.12.08 추가
var paramObj = {};
var _params = $(location).attr('search');
_params = _params.replace('?', '');
if (_params == '' || _params == undefined) {
  paramObj = {};
} else {
  var str = decodeURIComponent(_params);
  var chunks = str.split('&'),
      obj = {};
  for (var c = 0; c < chunks.length; c++) {
      var split = chunks[c].split('=', 2);
      obj[split[0]] = split[1];
  }
  paramObj = obj;
}

// --------------------------------------------------------------------------------
// fnIsEmpty
// --------------------------------------------------------------------------------
var fnIsEmpty = function(value) { 
    if ( value == "" ) { value += ''; } // 정수 0인 경우 
    if( value == "" || value == null || value == undefined ||
      ( value != null && typeof value == "object" && !Object.keys(value).length ) ) {
        return true 
    } else {
        return false 
    } 
};

// --------------------------------------------------------------------------------
// fnPad
// --------------------------------------------------------------------------------
var fnPad = function(n, width) {
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
};

//--------------------------------------------------------------------------------
//fnAddComma
//--------------------------------------------------------------------------------
var fnAddComma = function(value){
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g,",");
};

// --------------------------------------------------------------------------------
// fnGetLength
// --------------------------------------------------------------------------------
var fnGetLength = function(text) {
    var charLength = 0;
    var ch1 = "";
    for(var i = 0; i < text.length; i++) {
        ch1 = text.charAt(i);
        if(escape(ch1).length > 4) {		
            charLength += 2;
        } else {
            charLength += 1;
        }
    }
    return charLength;
};

// --------------------------------------------------------------------------------
// fnIsDigit
// --------------------------------------------------------------------------------
var fnIsDigit = function(str) {
	return /^\d+$/.test(str);
};

// --------------------------------------------------------------------------------
// isNumeric
// --------------------------------------------------------------------------------
var fnIsNumeric = function(str) {
	return !isNaN(Number(str + ''));
}

// --------------------------------------------------------------------------------
// fnParamsToObj
// --------------------------------------------------------------------------------
var fnHrefParamsToObject = function() {
	if ( window.location.href.indexOf('?') === -1 ) { return {}; }
	var obj = fnParamsToObj(decodeURIComponent(window.location.href.slice(window.location.href.indexOf('?') + 1)));
	for ( var key in obj ) {
        if ( obj.hasOwnProperty(key) ) {
            obj[key] = obj[key].replace('#', '');
        }
    }
	return obj;
};

// --------------------------------------------------------------------------------
// fnParamsToObj
// --------------------------------------------------------------------------------
var fnParamsToObj = function(params) {
    var obj = [], arr = params.split('&'), ele;
    for( var i = 0; i < arr.length; i++ ) {
        ele = arr[i].split(/=(.+)/);
        obj[ele[0]] = ele[1];
    }
    return obj;
};

// --------------------------------------------------------------------------------
// fnObjToParams
// --------------------------------------------------------------------------------
var fnObjToParams = function(obj) {
    var params = [];
    for ( var key in obj ) {
        if ( obj.hasOwnProperty(key) ) {
            params.push(key + '=' + obj[key]);
        }
    }
    return params.join('&');
};

// --------------------------------------------------------------------------------
// fnJsonToObj
// --------------------------------------------------------------------------------
var fnJsonToObj = function(json) { return JSON.parse(json); };

// --------------------------------------------------------------------------------
// fnObjToJson
// --------------------------------------------------------------------------------
var fnObjToJson = function(obj) { return JSON.stringify(obj); };

// --------------------------------------------------------------------------------
// fnParamsToJson
// --------------------------------------------------------------------------------
var fnParamsToJson = function(params) {
    var obj = fnParamsToObj(params);
    return fnObjToJson(obj);
};

// --------------------------------------------------------------------------------
// fnJsonToParams
// --------------------------------------------------------------------------------
var fnJsonToParams = function(json) {
    var obj = fnJsonToObj(json);
    return fnObjToParams(obj);
};

//--------------------------------------------------------------------------------
// fnReplaceHistoryState
//--------------------------------------------------------------------------------
var fnReplaceHistoryState = function(state, href) {
	var current = history.state;
	history.replaceState(fnIsEmpty(state) === false ? state : null, '', location.href);
	if ( fnIsEmpty(href) === false ) {
		var splitHref = href.split('?');
		if ( splitHref.length > 1 ) {
			href = splitHref[0] + '?';
			var arr = splitHref[1].split('&'), ele;
			for( var i = 0; i < arr.length; i++ ) {
		        ele = arr[i].split(/=(.+)/);
		        href += (i !== 0 ? '&' : '') + ele[0] + '=' + encodeURIComponent(ele[1]);
		    }
		}
		location.href = href;
	}
	return current;
};

// --------------------------------------------------------------------------------
// fnSendAjax
// --------------------------------------------------------------------------------
var fnSendAjax = function(url, params, success, error, complete) {
	console.log(url);
    console.log(params);

//    $("body").prepend('<div id="preloader"></div>');
    $.ajax({
        type: 'POST',
        url: gContextPath + gVersionPath + url,
        contentType: 'application/json',
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: {'X-Requested-With':'XMLHttpRequest'},
        dataType: "JSON",
        data: params,
        success : function(data) {
//            $("#preloader").remove();
            console.log(data);
            fnRecvAjaxSuccess(success, data);
        },
        error : function(xhr, status, thrown) {
//            $("#preloader").remove();
            fnRecvAjaxError(error, xhr, status, thrown, url);
        },
        complete: function(xhr, status) {
//        		fnUpdateSessionTimer();
    		if (typeof complete !== undefined && $.isFunction(complete)) {
    			complete(xhr, status);
    		}
        }
    });
};

// --------------------------------------------------------------------------------
// fnRecvAjaxSuccess
// --------------------------------------------------------------------------------
var fnRecvAjaxSuccess = function(success, data) {
    if (typeof success !== undefined && $.isFunction(success)) {
        success(data);
    }
};

// --------------------------------------------------------------------------------
// fnRecvAjaxError
// --------------------------------------------------------------------------------
var fnRecvAjaxError = function(error, xhr, status, thrown, url) {
    if ( xhr.status == 401 ) {
    	window.location.href = gContextPath + "/html/login.html";
    } else {
        alert("status code: " + xhr.status + "\n" 
            + "message: " + xhr.responseText + "\n"
            + "status: " + status + "\n"
            + "thrown: " + thrown);
        if (typeof error !== undefined && $.isFunction(error)) {
            error(xhr, status, thrown);
        }
    }
};

// --------------------------------------------------------------------------------
// fnMoveToPage
// --------------------------------------------------------------------------------
var fnMoveToPage = function(page, classes) {
    if (typeof cbMoveToPage !== undefined && $.isFunction(cbMoveToPage)) {
        cbMoveToPage(page, classes);
    }
};

// --------------------------------------------------------------------------------
// fnPagination
// --------------------------------------------------------------------------------
var fnDisplayPagination = function(page, size, totalElements, $pagination) {
    var groupSize   = 10;

    // set default value, if page, size is negative
    page  = page * 1 > 0 ? page * 1 : 0;
    size  = size * 1 > 0 ? size * 1 : 10;
    
    // set zero, if totalElements is negative
    if ( totalElements < 0 ) {
        totalElements = 0;
    }
    
    // calculate a number of total pages
    //var totalPages = (totalElements / size) + (totalElements % size == 0 ? 0 : 1);
    var totalPages = Math.ceil(totalElements / size);
    var lastPage = totalPages - 1 > 1 ? totalPages  - 1 : 0;
    
    // adjust page, if page great than totalPagess
    page = page >= totalPages ? (totalPages > 0 ? totalPages - 1 : 0) : page;

    // previous and next page number
    var prevPage = Math.floor(page / groupSize) * groupSize - 1;
    var nextPage = prevPage + groupSize + 1;
    
    // start and end page number
    var startPage =  prevPage + 1;
    var endPage = nextPage - 1;
    
    // adjust the end page number, if it's wrong
    if( endPage >= totalPages ){
        endPage = totalPages - 1;
    }

    // current page group number
	var currPageGroup = Math.ceil((page + 1) / groupSize);
    
    // a number of total page group
    var totalPageGroup = Math.ceil(totalPages / groupSize);

    if ( fnIsEmpty($pagination) ) {
    	$pagination = $('.pagination');
    }
    var classArr = $pagination.attr("class").split(' ');
    if ( classArr.indexOf('pagination') === -1 ) {
        classArr.push('pagination');
    }
    var classes = classArr.join(' ');
    var html =  '<div class="' + classes + '">';
        html +=     '<ul>';
        html +=         '<li class="p_img page-first"><a href="javascript:void(0);"';
    if ( page >= 1 ) {
        html +=         ' onclick="javascript:fnMoveToPage(0, \'' + classes + '\'); return false;"';
    }
        html +=         '></a></li>';
        html +=         '<li class="p_img page-prev"><a href="javascript:void(0);"';
    if ( prevPage + 1 >= groupSize ) { 
        html +=             ' onclick="javascript:fnMoveToPage(' + prevPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '></a></li>';
    for ( var i = startPage; i <= endPage; i++ ) {
        html +=         '<li class="' + (i == page ? 'on' : '') + '" data-page="' + i + '">';
        html +=             '<a href="javascript:void(0);" onclick="javascript:fnMoveToPage(' + i + ', \'' + classes + '\'); return false;">' + (i + 1) + '</a>';
        html +=         '</li>';
    }
    html +=             '<li class="p_img page-next"><a href="javascript:void(0);"';
    if ( currPageGroup < totalPageGroup ) {
        html +=             ' onclick="javascript:fnMoveToPage(' + nextPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '></a></li>';
        html +=         '<li class="p_img page-last"><a href="javascript:void(0);"';
    if ( page < endPage ) {
        html +=             ' onclick="javascript:fnMoveToPage(' + lastPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '></a></li>';
        html +=     '</ul>';
        html += '</div>';
    if ( $pagination.hasClass('pagination') == false ) {
        $('.content_wrap').append(html);
    } else {
        $pagination.replaceWith(html);
    }
    
    $('.pagination ul li a:not([onclick])').closest('li').css('opacity', 0.2);
};


//--------------------------------------------------------------------------------
//fnDisplayPageNav
//--------------------------------------------------------------------------------
var fnDisplayPageNav = function(page, size, totalElements, $pagination) {
    var groupSize   = 10;

    // set default value, if page, size is negative
    page  = page * 1 > 0 ? page * 1 : 0;
    size  = size * 1 > 0 ? size * 1 : 10;
 
    // set zero, if totalElements is negative
    if ( totalElements < 0 ) {
        totalElements = 0;
    }
 
    // calculate a number of total pages
    // var totalPages = (totalElements / size) + (totalElements % size == 0 ? 0 : 1);
    var totalPages = Math.ceil(totalElements / size);
    var lastPage = totalPages - 1 > 1 ? totalPages  - 1 : 0;
 
    // adjust page, if page great than totalPagess
    page = page >= totalPages ? (totalPages > 0 ? totalPages - 1 : 0) : page;

    // previous and next page number
    var prevPage = Math.floor(page / groupSize) * groupSize - 1;
    var nextPage = prevPage + groupSize + 1;
 
    // start and end page number
    var startPage =  prevPage + 1;
    var endPage = nextPage - 1;
 
    // adjust the end page number, if it's wrong
    if( endPage >= totalPages ){
        endPage = totalPages - 1;
    }

    // current page group number
    var currPageGroup = Math.ceil((page + 1) / groupSize);
 
    // a number of total page group
    var totalPageGroup = Math.ceil(totalPages / groupSize);

    if ( fnIsEmpty($pagination) ) {
        $pagination = $('.pageNav');
    }
    var classArr = $pagination.attr("class").split(' ');
    if ( classArr.indexOf('pageNav') === -1 ) {
        classArr.push('pageNav');
    }
    var classes = classArr.join(' ');
    var html =  '<nav class="' + classes + '  mg-top-20">';
        html +=     '<ul class="pagination type-a">';
        html +=         '<li class="first"><a aria-label="first" href="javascript:void(0);"';
    if ( page >= 1 ) {
            html +=         ' onclick="javascript:fnMoveToPage(0, \'' + classes + '\'); return false;"';
    }
        html +=         '><span aria-hidden="true"></span></a></li>';
        html +=         '<li class="prev"><a aria-label="Previous" href="javascript:void(0);"';
    if ( prevPage + 1 >= groupSize ) { 
            html +=             ' onclick="javascript:fnMoveToPage(' + prevPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '><span aria-hidden="true">&lt;</span></a></li>';
    for ( var i = startPage; i <= endPage; i++ ) {
            html +=         '<li class="' + (i == page ? 'active' : '') + '" data-page="' + i + '">';
            html +=             '<a href="javascript:void(0);" onclick="javascript:fnMoveToPage(' + i + ', \'' + classes + '\'); return false;">' + (i + 1) + '</a>';
            html +=         '</li>';
    }
        html +=             '<li class="next"><a  aria-label="next" href="javascript:void(0);"';
    if ( currPageGroup < totalPageGroup ) {
            html +=             ' onclick="javascript:fnMoveToPage(' + nextPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '><span aria-hidden="true">&gt;</span></a></li>';
        html +=         '<li class="last"><a aria-label="last" href="javascript:void(0);"';
    if ( page < endPage ) {
            html +=             ' onclick="javascript:fnMoveToPage(' + lastPage + ', \'' + classes + '\'); return false;"';
    }
        html +=         '><span aria-hidden="true">&gt;&gt;</span></a></li>';
        html +=     '</ul>';
        html += '</nav>';
    if ( $pagination.hasClass('pageNav') == false ) {
            $('.content_wrap').append(html);
    } else {
            $pagination.replaceWith(html);
    }
    $('.pageNav ul li a:not([onclick])').closest('li').css('opacity', 0.2);
};

//--------------------------------------------------------------------------------
//fnRegFocus
//--------------------------------------------------------------------------------
var fnRegFocus = function(added, $tr) {
	if ( added === true ) {
		if ( fnIsEmpty($tr) || $tr.length === 0 ) {
			$('.fixed_table_container table tbody').find('tr input:first').focus();
		} else {
			$tr.next().find('input:first').focus();
		}
	} else {
		if ( $tr.next().length !== 0 ) {
			$tr.next().find('input:first').focus();
		} else {
			$tr.prev().find('input:first').focus();
		}
	}
};

// --------------------------------------------------------------------------------
// removeSpecialCharacters (IE8)
// --------------------------------------------------------------------------------
var fnRemoveSpecialCharacters = function(word, charArr, altArr) {
	if ( fnIsEmpty(word) ) { return ''; }
	var specialChars = fnIsEmpty(charArr) ? '!@#$^&%*()+=-[]\/{}|:<>?,.' : charArr.join(''), tmp = word;
	for ( var i = 0; i < specialChars.length; i++ ) {
		var alt = fnIsEmpty(charArr) === false && fnIsEmpty(altArr) === false && charArr.length === altArr.length ? altArr[i] : '';
		tmp = tmp.replace(new RegExp('\\' + specialChars[i], 'gi'), alt);
	}
	return tmp;
}

// --------------------------------------------------------------------------------
// paintBackgroundYellow
// --------------------------------------------------------------------------------
var fnPaintBackgroundYellow = function(word, pttn, type) {
	if ( fnIsEmpty(word) ) { return ''; }
	if ( fnIsEmpty(pttn) ) { return word; }
	if ( type === false ) { return word; }
	var regex = new RegExp(pttn, 'gim');
	return word.replace(regex, '<span style="background-color:yellow;">' + pttn + '</span>');
};

// --------------------------------------------------------------------------------
// fnUserInfo
// --------------------------------------------------------------------------------
var fnUserInfo = function() {
	var userInfo = $.cookie('user-info');
	if ( fnIsEmpty(userInfo) === true ) { return false; }
	return JSON.parse(decodeURIComponent(window.atob(userInfo)));
};

$.fn.Ajax = function (type, URL, jsonData, callback, errorCallback) {

    if (type=="GET") {
        if(!$.isEmptyObject(jsonData)){
            jsonData = jQuery.param(jsonData)
        }
    }else{
        jsonData  = JSON.stringify(jsonData);
    };

    var $xhr = jQuery.ajax({
        type: type == undefined ? "GET" : type,
        url: gContextPath + URL,
        headers: API_HEADERS,
        contentType: CONTENT_TYPE,
        data: jsonData,
        dataType: 'json',
        success: function (response) {
            console.log(URL);
            console.log(response);
            if($.fn.errorMsg(response)) return;
            callback && callback(response);
        },
        error: function (req, status, error) {
            console.log('Ajax ERROR > : ',type, URL, jsonData);

            if(req.status === 401){
                commonMsg.confirm("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.","로그인 오류",
                    function (){
                        location.href="login.html";
                    });
                return false;
            }

            if(typeof errorCallback === "function") {
                errorCallback(req, status, error);
            }
        }
    });
    return $xhr;
};

$.fn.errorMsg = function(response) {
    var flag = false;
    if( response.resultCode !== undefined && response.resultCode == "NG"){
        if($(".pop_common_alert").length == 0){
            alert(response.resultMsg);
            flag = true;
        }else{
            commonMsg.alert(response.resultMsg);
            flag = true;
        }
    };
    return flag;
};

var isEmpty = function(value) {
    return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}

var ifNullEmpty = function(value)
{
    if(value == null || typeof value == "undefined" || value == undefined)
        return "";

    return value;
}


/**
 * 날짜 세팅
 * @param type
 * @param from
 * @param to
 * @param dateSeperator
 * @returns
 */
function shiftDate(date, y, m, d, separator) {
    
    date.setFullYear(date.getFullYear() + y);
    date.setMonth(date.getMonth() + m);
    date.setDate(date.getDate() + d);
    
    var d = "";
    d += date.getFullYear();
    d += separator;
    d += ("0" + (date.getMonth() + 1)).slice(-2);
    d += separator;
    d += ("0" + date.getDate()).slice(-2);
    
    return d;
}

/**
 * 현재월조회
 * @returns
 */
function getMonth() {
    var date = new Date();
    var month = date.getMonth() + 1;
    return month; 
}

//--------------------------------------------------------------------------------
//fnSendAjaxFile
//--------------------------------------------------------------------------------
var fnSendAjaxFile = function(url, formData, success, error, complete, loading) {
  
  $.ajax({
      type : "POST",
      url: gContextPath + gVersionPath + url,
      data : formData,
      crossDomain: true,
      xhrFields: {withCredentials: true},
      headers: {'X-Requested-With':'XMLHttpRequest'},
      cache: false,
      contentType: false,
      processData: false,
      success : function(data) {
          console.log(data);
          fileData(data);
      },
      error : function(xhr, status, thrown) {

      },
      complete: function(xhr, status) {
              if (typeof complete !== 'undefined' && $.isFunction(complete)) {
                  complete(xhr, status);
              }
      }
  });
};

/**
 * 페이지 네비게이션 바 세팅
 */
var fnSetPageNavigationBar = function(navigation) {
    var navigationBarHtml = '';
    navigationBarHtml += '<ul>';
    navigationBarHtml += '<li><a href="'+navigation[0][0]+'"><img src="../images/common/ico_home.png" alt="home"></a></li>';
    for (var i=1; i<navigation.length; i++) {
        navigationBarHtml += '<li><a href="'+navigation[i][0]+'">'+navigation[i][1]+'</a></li>';
    }
//    navigationBarHtml += '<li><a href="'+navigation[1][0]+'">'+navigation[1][1]+'</a></li>';
//    navigationBarHtml += '<li><a href="'+navigation[2][0]+'">'+navigation[2][1]+'</a></li>';
//    navigationBarHtml += '<li><a href="'+navigation[3][0]+'">'+navigation[3][1]+'</a></li>';
    navigationBarHtml += '</ul>';
    $('.navigationBar').html(navigationBarHtml);
};


