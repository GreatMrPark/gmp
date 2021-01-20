var CONTENT_TYPE = 'application/json;charset=UTF-8';
var API_HEADERS = {};
var paramObj = {};

const CONTEXT_PATH = "/";

/*var alert = function (msg) {
    commonMsg.alert(msg);
};*/


// page parameter 값 가져오기
// ex) app_history_detail.html?appStoreSeq=1
//     paramObj.appStoreSeq
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

$.fn.exists = function() {
  return this.length > 0;
};

// view에 데이터 세팅 : <td data={keyName}></td>
$.fn.setData= function(_url, paramObj, callback, method) {

  if(method == undefined ) method = "GET";

  var comp = this;
  $.fn.Ajax(method,_url, paramObj, function(result) {
    obj = result.data.resultData;
    $.each(obj, function(key,_d) {
      $(comp).find('[data="'+key+'"]').html(_d);

      //dataFormat 추가
      var dataFormat = $(comp).find('[data="'+key+'"]').attr('data-format');
      if(dataFormat != undefined){
        if(dataFormat == 'yyyy.mm.dd'){
            var _onlyNum = _d.replace(/[^0-9]/g,"").substr(0,8);
            $(comp).find('[data="'+key+'"]').html(_onlyNum.substr(0,4)+'.'+_onlyNum.substr(4,2)+'.'+_onlyNum.substr(6,2));
        }
      }

    });
    if (callback) {
      callback(result);
    };
  })
},

$.fn.setDataOnly= function(data, callback) {

    var comp = this;
    $.each(data, function(key,_d) {
        $(comp).find('[data="'+key+'"]').html(_d);
    });

    if (callback) {
        callback(data);
    };
},


$.ajaxSetup({
    error: function (req, status, error) {
        console.log('Ajax ERROR > : ', type, URL, jsonData);

        if (req.status === 401) {
            console.log("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.");
            location.href = "login.html";
            //return false;
        }
    }
});

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
    url: CONTEXT_PATH + URL,
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
            console.log("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.");
            location.href="login.html";
            //return false;
        }
/*
        if(req.status === 401){
            commonMsg.confirm("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.","로그인 오류",
                function (){
                    location.href="login.html";
                });
            return false;
        }
*/

        if(typeof errorCallback === "function") {
            errorCallback(req, status, error);
        }
    }
  });
  return $xhr;
};

$.fn.AjaxFile = function (URL, formData, callback) {
    var $xhr = jQuery.ajax({
        url: CONTEXT_PATH + URL,
        processData: false,
        contentType: false,
        data: formData,
        type: 'POST',
        headers: API_HEADERS,
        success: function (response) {
            if($.fn.errorMsg(response)) return;
            callback && callback(response);
        },
        error: function (req, status, error) {
            console.log('AjaxFile ERROR : ', URL);

            if(req.status === 401){
                console.log("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.");
                location.href="login.html";
                return false;
            }
        }
    });
    return $xhr;
};

$.fn.serializeArray = function() {
  var counter = 1;
  var _this = this;
  var rCRLF = /\r?\n/g;
  return this.map(function() {
    return this.elements ? jQuery.makeArray(this.elements) : this;
  }).map(function(i, elem) {

    var val = jQuery(this).val();
    if (elem.name != '') {
      if (val == null) {
        return val == null
      } else if (this.type == "checkbox") {
        return {
          name: this.name,
          value: [this.value, $(_this).find('#' + this.id).is(":checked") ? "true" : "false"]
        }
      } else if (this.type == "radio") { //&& this.checked == false
        var length = $(_this).find('input:radio[name=' + this.name + ']').length;
        if (counter == length) {
          counter = 1;
          return {
            name: this.name,
            value: $(_this).find('input:radio[name=' + this.name + ']:checked').val()
          }
        }
        counter++;
      } else if (this.type == "textarea") {
        var editorId = $(_this).find('[name=' + this.name + ']').attr('name');
        if ($(_this).find('[name=' + this.name + ']').hasClass('ckeditor')) {} else {
          return {
            name: this.name,
            //value: val.replace(rCRLF, "\r\n").replace(/-/gi, '')
              value: val.replace(rCRLF, "\r\n")
          };
        }
      } else {
        return jQuery.isArray(val) ?
            jQuery.map(val, function(val, i) {
              console.log('isArrayCheck true : val:', val)
              return {
                name: elem.name,
                value: val.replace(rCRLF, "\r\n")
              };
            }) : {
          name: elem.name,
          value: val.replace(rCRLF, "\r\n")
        };
      }
    }
  }).get();
};

$.fn.extend({
  setForm: function(_url, paramObj, callback) {
        var _frm = this;
        var _form = _frm.serializeArray();
        $.fn.Ajax("GET",_url, paramObj, function(result) {
            obj = result.resultData;
            if (obj == null) {
                return false;
            }
            _frm.clearForm();
            $.each(_form, function() {
                var _key = this.name;
                var _val = obj[_key];
                var find_form = "#" + _frm[0].id;
                var tmp = "[name$='" + _key + "']";
                var slt = "select[name='" + _key + "']";
                var _textArea = "textarea[name='" + _key + "']";
                var checkedVal = _val == 'Y' ? true : false;
                var _type = $(find_form).find($(tmp)).prop("type")
                //console.log('setForm _type:', _type, '_key:', _key, '_val:', _val, 'checkedVal:', checkedVal);
                if ('checkbox' === _type) {
                    $(_frm).find('input:checkbox[name=' + _key + ']').attr("checked", checkedVal);
                } else if ('radio' === _type) {
                    if (_val != 'Y' && _val != 'N') {
                        checkedVal = _val;
                    }
                    if (_val != undefined && _val != '') {
                        $(_frm).find('input:radio[name=' + _key + ']:input[value="' + checkedVal + '"]').attr("checked", true);
                    }
                } else {
                    if (_val !== undefined) {
                        if ($(tmp).exists()) {
                            $(tmp).val(_val);
                        }
                        if ($(_textArea).exists()) {
                            $(_textArea).val(_val);
                            //$('.autosize-textarea').trigger('autosize.resize');
                        }
                    }
                }

                if ($(slt).exists()) {
                    console.log('setForm > select _key:', _key, ' _val:', _val);
                    $(slt).val(_val).prop('selected', true);
                }
            });
            if (callback) {
                callback(result);
            }
        })
    },

    setFormData: function(obj) {
        var _frm = this;
        var _form = _frm.serializeArray();

        _frm.clearForm();
        $.each(_form, function() {
            var _key = this.name;
            var _val = obj[_key];
            var find_form = "#" + _frm[0].id;
            var tmp = "[name$='" + _key + "']";
            var slt = "select[name='" + _key + "']";
            var _textArea = "textarea[name='" + _key + "']";
            var checkedVal = _val == 'Y' ? true : false;
            var _type = $(find_form).find($(tmp)).prop("type")
            //console.log('setForm _type:', _type, '_key:', _key, '_val:', _val, 'checkedVal:', checkedVal);
            if ('checkbox' === _type) {
                $(_frm).find('input:checkbox[name=' + _key + ']').attr("checked", checkedVal);
            } else if ('radio' === _type) {
                /*if (_val != 'Y' && _val != 'N') {
                    checkedVal = _val;
                }*/

                if (_val != undefined && _val != '') {
                    $(_frm).find('input:radio[name=' + _key + ']:input[value="' + _val + '"]').prop("checked", true);
                }
            } else {
                if (_val !== undefined) {
                    if ($(tmp).exists()) {
                        $(tmp).val(_val);
                    }
                    if ($(_textArea).exists()) {
                        $(_textArea).val(_val);
                        //$('.autosize-textarea').trigger('autosize.resize');
                    }
                }
            }

            if ($(slt).exists()) {
                console.log('setForm > select _key:', _key, ' _val:', _val);
                $(slt).val(_val).prop('selected', true);
            }
        });


    },




  fnFormAction: function(type, URL, callback) {
    var formId = this;
    var formData = $(formId).formToJson();
    //console.log('fnFormAction > formData:', formData, ' callback:', callback);
    jQuery.ajax({
      type: type == undefined ? API_POST : type,
      url: CONTEXT_PATH + URL,
      headers: API_HEADERS,
      contentType: CONTENT_TYPE,
      data: JSON.stringify(formData),
      dataType: 'json',
      success: function(response) {
          if($.fn.errorMsg(response)) return;

        //$(formId).clearForm();
        setTimeout(function() {
          //console.log('sleep callback !!')
          callback && callback(response);
        }, 1000);
      },
      error: function(req, status, error) {

          if(req.status === 401){
              commonMsg.confirm("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.","로그인 오류",
                  function (){
                      location.href="login.html";
                  });
              return false;
          }

        alert('처리중 오류가 발생하였습니다. 관리자에게 문의 바랍니다.');
        $.gritter.add({
          title: "처리중 오류가 발생하였습니다. ",
          text: "관리자에게 문의 바랍니다."
        });
        console.log('fnFormAction ERROR > URL: ', URL, 'params:', formData);
      }
    });
  },

  csvToJson: function(csvText) {
    //Split all the text into seperate lines on new lines and carriage return feeds
    var allTextLines = csvText.split(/\r\n|\n/);
    var headers = allTextLines[0].split(/\t|,/);
    var locations = [];

    for (var i = 1; i < allTextLines.length; i++) {
      var data = allTextLines[i].split(/\t|,/);
      var row = {};
      if (data.length == headers.length) {
        for (var j = 0; j < headers.length; j++) {
          row[headers[j]] = data[j] || '';
        }
        locations.push(row);
      }
    }
    return locations;
  },

  formToJson: function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
      //this.value = this.value.toString().replace(/\-/g, '');
      if (this.name !== '' && this.name !== undefined) {
        if (o[this.name] !== undefined) {
          if (!o[this.name].push) {
            o[this.name] = [o[this.name]];
          }
          o[this.name].push(this.value || '');
        } else {
          o[this.name] = this.value || '';
        }
      }

    });
    return o;
  },

  clearForm: function() {
    var _jsonForm = this.serializeArray();
    $.each(_jsonForm, function() {
      var _tmp = "[name$='" + this.name + "']";
      var _type = $(_tmp).attr('type');
      var _textArea = "textarea[name='" + this.name + "']";
      if (_type !== 'radio') {
        $(_tmp).val('');
        if ($(_textArea).exists()) {
          $(_textArea).val('');
        }
      }
    });
  },

  saveForm: function(option) {
    var _form = this;
    var keyVal = $("input[name=" + option.key + "]").val();
    //console.log('saveForm keyVal:',keyVal)
    if (keyVal !== '' && keyVal != undefined) {
      _form.fnFormAction(API_PUT, option.url + keyVal, function() {
        grid.add(option)
      });
    } else {
      this.fnFormAction(API_POST, option.url, function() {
        grid.add(option)
      });
    }
  },

  deleteForm: function(option, callback) {
    console.log('deleteForm > callback:', callback);

    var keyVal = $("input[name=" + option.key + "]").val();
    if (keyVal != '' && keyVal != undefined) {
      option.form.fnFormAction("DELETE", option.url + keyVal, callback);
    }
  }

});


var grid = {

  dataAdd: function(option, callback, totalCnt) {

      var style = option.style;
      var _this = option.table;
      var data = option.data;
      var tbody = '';

      _this.find('tbody').remove();
      $(_this).append('<tbody></tbody>');

      if (style == 'check-table') {
          tbody = grid.cbody(data, option, totalCnt);
      } else {
          tbody = grid.body(data, option, totalCnt);
      }

      $(_this).find('tbody').append(tbody);

      if (callback) {
          callback(data);
      }

      switch (style) {
          case 'data-table':
              $(_this).dataTable().fnDestroy();
              tableOption = {
                  "sDom": "t<'row'<'center'><p>>",
                  "order": [
                      [0, "desc"]
                  ],
              };
              $(_this).dataTable(tableOption);
              break;
          case 'check-table':
              grid.page(option, data, callback, type);
              break;
          case 'page-table':
              grid.page(option, data, callback, type);
              break;
          case 'script-page-table':
              //id, 현재 페이지, 페이지 사이즈, 전체 갯수
              grid.scriptPage(_this.attr("id"), 1, option.pageSize, totalCnt);
              break;
          default: //'table'
              break;
      }

      //값이 없을 때
      if(isEmpty(tbody)){
          var theadLength = $(_this).find('thead > tr > th').length;
          $(_this).find('tbody').append("<tr empty='Y'><td colspan='" + theadLength + " '>데이터가 없습니다.</td></tr>");
      }
  },

  add: function(option, callback, type) {

    var style = option.style;
    var _this = option.table;
    var _pageSize = option.pageSize;
    var tbody = '';

    _this.find('tbody').remove();
    $(_this).append('<tbody></tbody>');
    if (option.parameters !== undefined) {
      if ($.isEmptyObject(option.param)) {
        option.param = option.parameters;
      } else {
        option.param = $.extend({}, option.param, option.parameters);
      }
    }

    if(type == undefined) type = "GET";
    $.fn.Ajax(type, option.url, option.param, function(result) {
      data = result.data.resultData;
      //console.log(data);
      //토탈 카운트
      var totalCnt = result.data.totalCnt;
      if (style == 'check-table') {
        tbody = grid.cbody(data, option, totalCnt);
      } else {
        tbody = grid.body(data, option, totalCnt);
      }
      /*if (callback) {
        callback(result);
      }*/
    }).done(function(data) {
      $(_this).find('tbody').append(tbody);

      switch (style) {
        case 'data-table':
          $(_this).dataTable().fnDestroy();
          tableOption = {
            "sDom": "t<'row'<'center'><p>>",
            "order": [
              [0, "desc"]
            ],
          };
          $(_this).dataTable(tableOption);
          break;
        case 'check-table':
          /*$(_this).dataTable().fnDestroy();
          tableOption = {
            "sDom": "t<'row'<'center'><p>>",
            "order": [
              [0, "desc"]
            ],
          };
          $(_this).dataTable(tableOption);*/
          grid.page(option, data, callback, type);
          break;
        case 'page-table':
          grid.page(option, data, callback, type);
          break;
          case 'script-page-table':
              //id, 현재 페이지, 페이지 사이즈, 전체 갯수
              var _totalCnt = isEmpty(data.data.resultData) ? 0 : data.data.resultData.length;
              grid.scriptPage(_this.attr("id"), 1, _pageSize, _totalCnt);
              break;
        default: //'table'

          break;
      }


      //값이 없을 때
      if(isEmpty(tbody)){
          var theadLength = $(_this).find('thead > tr > th').length;
          $(_this).find('tbody').append("<tr empty='Y'><td colspan='" + theadLength + " '>데이터가 없습니다.</td></tr>");
      }

      if (callback) {
            callback(data);
      }

    });
  },
  page: function(option, data, callback, type) {
        option.table.closest('.table_wrap').next('.pagination').remove();

         if(isEmpty(data.data) || isEmpty(data.data.totalCnt) || data.data.totalCnt == 0){
             return;
         }

        var _pageNumberUnit = 10;
        var _pageUnit = option.param.pageSize;
        if (_pageUnit == undefined) {
            _pageUnit = 10;
        }
        var _totCnt = data.data.totalCnt;
        var _currentPage = option.param.page;
        if (_currentPage == undefined) {
            _currentPage = 1;
        }
        var _totPage = _totCnt % _pageUnit == 0 ? Math.floor(_totCnt / _pageUnit) : Math.floor(_totCnt / _pageUnit) + 1;
        //var _startPage = 1;
        var _startPage = _currentPage % _pageNumberUnit == 0 ? Math.floor((Number(_currentPage)-1) / _pageNumberUnit) * _pageNumberUnit +1 : Math.floor(_currentPage / _pageNumberUnit) * _pageNumberUnit + 1;
        var _endPage = Number(_startPage) + Number(_pageNumberUnit) -1;

        if (_endPage > _totPage) {
            _endPage = _totPage;
        }
        var _prevPage = _currentPage > 1 ? Number(_currentPage) - 1 : _currentPage;
        var _nextPage = _currentPage < _totPage ? Number(_currentPage) + 1 : _currentPage;
        //console.log("_totCnt:", _totCnt, "_currentPage:", _currentPage, "_pageUnit:", _pageUnit, "_totPage:", _totPage, "_endPage:", _endPage);

        var _page = '';
        var paginId =  option.table.attr("id") + "_paginId";
        _page += '<div class="pagination" id="' + paginId + '">                                 ';
        _page += '<ul class="clearfix">                                    ';
        _page += '<li class="p_img page-first"><a href="javascript:void(0)">1</a></li>     ';
        _page += '<li class="p_img page-prev"><a href="javascript:void(0)">' + _prevPage + '</a></li>      ';
        for (var i = _startPage; i <= _endPage; i++) {
            if (i == _currentPage) {
                _page += '<li class="on"><a href="javascript:void(0)">' + i + '</a></li>                      ';
            } else {
                _page += "<li class=''><a href='javascript:void(0)'>" + i + "</a></li>                      ";
            }
        }
        _page += '<li class="p_img page-next"><a href="javascript:void(0)">' + _nextPage + '</a></li>      ';
        _page += '<li class="p_img page-last"><a href="javascript:void(0)">' + _totPage + '</a></li>     ';
        _page += '</ul>                                                    ';
        _page += '</div>                                                   ';

        option.table.closest('.table_wrap').after(_page);

        $('#'+ paginId + ' > ul > li').on("click", function() {
            var _currentPage = $(this).closest(".pagination").find(".on").text();
            var _page = $(this).text();
            if(_currentPage != _page){
                option.param.page = _page;
                option.param.pageSize = _pageUnit;
                grid.add(option, callback, type);

                console.log('_page:', _page, 'param:', option, 'callback:', callback)
            }
        })
    },
    scriptPage: function(tableId, page, pageSize, totalCnt) {

        $("#" + tableId).closest('.table_wrap').next('.pagination').remove();

        if(isEmpty(totalCnt) || totalCnt == 0){
            return;
        }

        var _pageNumberUnit = 10;
        var _pageUnit = pageSize;
        if (_pageUnit == undefined) {
            _pageUnit = 10;
        }
        var _totCnt = totalCnt;
        var _currentPage = page;
        if (_currentPage == undefined) {
            _currentPage = 1;
        }
        var _totPage = _totCnt % _pageUnit == 0 ? Math.floor(_totCnt / _pageUnit) : Math.floor(_totCnt / _pageUnit) + 1;
        //var _startPage = 1;
        var _startPage = _currentPage % _pageNumberUnit == 0 ? Math.floor((Number(_currentPage)-1) / _pageNumberUnit) * _pageNumberUnit +1 : Math.floor(_currentPage / _pageNumberUnit) * _pageNumberUnit + 1;
        var _endPage = Number(_startPage) + Number(_pageNumberUnit) -1;

        if (_endPage > _totPage) {
            _endPage = _totPage;
        }
        var _prevPage = _currentPage > 1 ? Number(_currentPage) - 1 : _currentPage;
        var _nextPage = _currentPage < _totPage ? Number(_currentPage) + 1 : _currentPage;
        //console.log("_totCnt:", _totCnt, "_currentPage:", _currentPage, "_pageUnit:", _pageUnit, "_totPage:", _totPage, "_endPage:", _endPage);

        var _page = '';
        var paginId =  tableId + "_paginId";
        _page += '<div class="pagination" id="' + paginId + '">                                 ';
        _page += '<ul class="clearfix">                                    ';
        _page += '<li class="p_img page-first"><a href="javascript:void(0)">1</a></li>     ';
        _page += '<li class="p_img page-prev"><a href="javascript:void(0)">' + _prevPage + '</a></li>      ';
        for (var i = _startPage; i <= _endPage; i++) {
            if (i == _currentPage) {
                _page += '<li class="on"><a href="javascript:void(0)">' + i + '</a></li>                      ';
            } else {
                _page += "<li class=''><a href='javascript:void(0)'>" + i + "</a></li>                      ";
            }
        }
        _page += '<li class="p_img page-next"><a href="javascript:void(0)">' + _nextPage + '</a></li>      ';
        _page += '<li class="p_img page-last"><a href="javascript:void(0)">' + _totPage + '</a></li>     ';
        _page += '</ul>                                                    ';
        _page += '</div>                                                   ';

        $("#" + tableId).closest('.table_wrap').after(_page);
        $("#" + tableId).find("tbody > tr").hide();

        var startIndex = ( Number(_currentPage) * Number(_pageUnit) ) - Number(_pageUnit);
        var endIndex = ( Number(_currentPage) * Number(_pageUnit) );

        for (var i = startIndex; i < endIndex; i++) {
            $("#" + tableId).find("tbody > tr").eq(i).show();
        }


        $('#'+ paginId + ' > ul > li').on("click", function() {
            var _currentPage = $(this).closest(".pagination").find(".on").text();
            var _page = $(this).text();
            if(_currentPage != _page){
                grid.scriptPage(tableId, _page, _pageUnit, _totCnt);
            }
        })
    },




  reload: function(option, callback) {
    var style = option.style;
    var _this = option.table;
    var tbody = '';
    _this.find('tbody > tr').remove();

    if (option.parameters !== undefined) {
      if ($.isEmptyObject(option.param)) {
        option.param = option.parameters;
      } else {
        option.param = $.extend({}, option.param, option.parameters);
      }
    }

    $.fn.Ajax("GET",option.url, option.param, function(data) {
      if (style == 'check-option') {
        tbody = grid.cbody(data, option);
      } else {
        tbody = grid.body(data, option);
      }
      if (callback) {
        callback(data);
      }
    }).done(function() {
      $(_this).find('tbody').append(tbody);
    });
  },

  row: function(option, callback) {
    //console.log('row click',option)
    option.table.on('click', 'tr', function() {
      var _keyIndex = grid.key(option);
      var rowData = grid.select(this);
      callback(rowData)
    });
  },

  delete: function(option, callback) {
    console.log('option delete > callback:', callback);
    if (option.style == 'check-option') { // delete checked row
      $(option.table).find('tr').each(function(i, row) {
        var checked = $(this).find('input[type="checkbox"]').eq(0).is(":checked") ? true : false;
        //console.log('checked:',checked);
        var tds = $(this).closest('tr').find('td');
        var rowData = []
        tds.each(function(i, td) {
          rowData.push($(td).text())
        })
        var _keyIndex = grid.key(option);

        //console.log('i','checked',checked,'_keyIndex:',_keyIndex,rowData[_keyIndex]);
        if (checked && rowData[_keyIndex] !== undefined) {
          fnJsonAction("DELETE", option.url + rowData[_keyIndex], callback);
          $(this).remove();
        }
      });
    }
  },

  headerJson: function(option) {
    var thJson = [];
    var _table = option.table;
    _table.find('thead').find('th').each(function() {
      // table header 값을 data로 변경함
      if ($(_table).find('data')) {
        th = $(this).attr('data');
      } else {
        th = $(this).text().trim();
      }

      /*
      // id사용 > data 사용으로 변경
      if ($(this).attr('id')) {
        th = $(this).attr('id');
      } else {
        th = $(this).text().trim();
      }*/

      //if (th != '') {
      if (!isEmpty(th)){
        thJson.push(th);
      }
    });

    return thJson;
  },

  headerArray: function(option) {
    var thArr = [];
    var _table = option.table;
    _table.find('thead').find('th').each(function() {
      if ($(this).attr('id')) {
        th = $(this).attr('id');
      } else {
        th = $(this).text().trim();
      }
      if (th != '') {
        thArr.push(th);
      }
    })
    return thArr;
  },

  body: function(data, option, totalCnt) {
    var tbody = '';
    var thJson = grid.headerJson(option);

    var seqNumber = 1;
    $.each(data, function(i, el) {
      tbody = tbody + '<tr';
      if(option.setRespParmTr != undefined){
          $.each(option.setRespParmTr, function(i, name) {
              if(el[name] != undefined) tbody = tbody + ' ' + name + '=\"' + el[name] + '\" ';
          });
      }
      tbody = tbody + '>';


      $.each(thJson, function(value, th) {

        var eachFlag = true;

        for (key in el) {
          if (key == th) {
            var visible = ( option.table.find('[data="'+th+'"]').css("display") == "none" ) ? false : true;
            var style = '';
            if(!visible){
              style='style=\'display:none\'';
            }
            tbody = tbody + "<td "+style+"data="+key+">" + grid.cdata(key, el[key], option, el) + "</td>";
            //tbody = tbody + "<td>" + el[key] + "</td>";
          }else{

            //순번이 필요할때 리턴값에는 없지만 필요할때
            //th id를 _GRID_CUSTORM_ 으로 포함되도록 만든다.
            if(eachFlag && th.indexOf("GRID_CUSTORM_") != -1){

                var gridData = "";
                // 순번 역순
                if(th.indexOf("GRID_CUSTORM_SEQ_DESC") != -1){

                    if(isEmpty(option.param) || isEmpty(option.param.page))  {
                        if(isEmpty(totalCnt)){
                            totalCnt = data.length;
                        }
                        gridData = totalCnt;
                        totalCnt--;

                    }else{
                        gridData = Number(totalCnt) - ( (Number(option.param.page) -1) * Number(option.param.pageSize) + seqNumber-1);
                    }
                    seqNumber++

                }else if(th.indexOf("GRID_CUSTORM_SEQ") != -1){

                  if(isEmpty(option.param) || isEmpty(option.param.page))  {
                      gridData = seqNumber;
                  }else{
                      gridData = (Number(option.param.page) -1) * Number(option.param.pageSize) + seqNumber;
                  }
                  seqNumber++;
                }

                tbody = tbody + "<td>" + grid.cdata(th, gridData, option, el) + "</td>";
                eachFlag= false;
            }
          }
        }
      });
      tbody = tbody + '</tr>';
    });
    return tbody;
  },

  cbody: function(data, option, totalCnt) {
    var tbody = '';
    var _key = option.key;
    var thJson = grid.headerJson(option);
    var seqNumber = 1;

    $.each(data, function(i, el) {
      tbody = tbody + '<tr><td class="center"><input type="checkbox" class="checkMe" id="' + option.table.attr("id") + i + '" ><label for="' + option.table.attr("id") + i + '"></label></td>';
      $.each(thJson, function(value, th) {
        var eachFlag = true;

        for (key in el) {
          if (key == th) {
              var visible = ( option.table.find('[data="'+th+'"]').css("display") == "none" ) ? false : true;
              var style = '';
              if(!visible){
                  style='style=\'display:none\'';
              }
              tbody = tbody + "<td "+style+"data="+key+">" + grid.cdata(key, el[key], option, el) + "</td>";
          }else{

              //순번이 필요할때 리턴값에는 없지만 필요할때
              //th id를 _GRID_CUSTORM_ 으로 포함되도록 만든다.
              if(eachFlag && th.indexOf("GRID_CUSTORM_") != -1){

                  var gridData = "";
                  // 순번 역순
                  if(th.indexOf("GRID_CUSTORM_SEQ_DESC") != -1){

                      if(isEmpty(option.param) || isEmpty(option.param.page))  {
                          if(isEmpty(totalCnt)){
                              totalCnt = data.length;
                          }
                          gridData = totalCnt;
                          totalCnt--;

                      }else{
                          gridData = Number(totalCnt) - ( (Number(option.param.page) -1) * Number(option.param.pageSize) + seqNumber-1);
                      }
                      seqNumber++

                  }else if(th.indexOf("GRID_CUSTORM_SEQ") != -1){

                      if(isEmpty(option.param) || isEmpty(option.param.page))  {
                          gridData = seqNumber;
                      }else{
                          gridData = (Number(option.param.page) -1) * Number(option.param.pageSize) + seqNumber;
                      }
                      seqNumber++;
                  }

                  tbody = tbody + "<td>" + grid.cdata(th, gridData, option, el) + "</td>";
                  eachFlag= false;
              }
          }
        }
      });
      tbody = tbody + '</tr>';
    });
    return tbody;
  },

  select: function(obj) {
    var $row = $(obj).closest("tr");
    var rowData = [];
    $tds = $row.find("td");
    $.each($tds, function() {
      _col = $(this).text();
      if ($(this).find('input').length) {
        _col = $(this).find('input').is(':checked');
      }
      rowData.push(_col);
    });
    return rowData;
  },

  selectData: function(obj) {
      var $row = $(obj).closest("tr");
      var rowData = {};
      var $th = $(obj).closest("table").find("thead > tr:eq(0) > th");

      $tds = $row.find("td");
      $.each($tds, function(index, data) {
          _col = $(this).text();
          if ($(this).find('input').length) {
              _col = $(this).find('input').is(':checked');
          }
          var _key = $th.eq(index).attr("data");
          rowData[_key] = _col;
      });
      return rowData;
  },

  key: function(_option) {
    var index = 0;
    var keyIndex = 0;
    var ths = grid.headerJson(_option);
    var _key = _option.key;
    $.each(ths, function(th, el) {
      if (_key == th && _key != "") {
        keyIndex = index;
      }
      index++;
    });
    //console.log('keyIdx keyIndex:',keyIndex);
    return keyIndex;
  },

  pop: function(option, callback) {
    var _src = option.popUrl;
    if (option.style == 'check-option') { // delete checked row

      var _keyIndex = grid.key(option);
      var checkRows = checkedrawToArray(option);
      //console.log('checkedrawToArray:',checkRows);
      $.map(checkRows, function(_rows, i) {
        $.map(_rows, function(row, i) {
          //console.log('row:', row);
          _src += '?' + option.key + '=' + row[_keyIndex];
          //var callback = $(this).attr('data-callback');
          $("#frameModal iframe").attr({
            'src': _src,
            'data-callback': callback,
            'allowfullscreen': '',
          });
          $('#frameModal').modal({
            show: true
          })
        })
      });

    } else {
      var keyVal = $("input[name=" + option.key + "]").val();
      option.form.fnFormAction("DELETE", option.url + keyVal, callback);
    }
  },

  cdata: function(key, data, option, el) {
    var retVal = data;
    retVal = convChar(retVal);
    if ($.isFunction(option.callback)) {
      retVal = option.callback(key, retVal, el);
    }
    if (data != '' && data != null && data.length > 300) {
      retVal = data.substring(0, 300) + '...';
    }
    if (isEmpty(retVal)) {
      retVal = '';
    }
    return retVal;
  }

}

function getCheckedRowIndexList(gridId){
  var indexList = new Array();
  var grid = $("#"+gridId);

    grid.find("td input[type='checkbox']").each(function (idx, data) {
      if($(data).is(":checked")){
        indexList.push(idx);
      }
    });

  return indexList;
}

function getCheckedRowDataList(gridId){
    var dataList = new Array();
    var gridTable = $("#"+gridId);

    gridTable.find("td input[type='checkbox']").each(function (idx, data) {
        if($(data).is(":checked")){
            dataList.push( grid.selectData(this));
        }
    });

    return dataList;
}

function getRowData(gridId, index, key){
    var resultTxt;
    var grid = $("#"+gridId);

    var list =grid.find('tbody > tr').eq(index).find('td');

    list.each(function (idx, td) {
      if(key == $(td).attr('data')){
          resultTxt = $(td).text();
      }
    });

    return resultTxt;
}


function convChar(str) {
  if ($.type(str) === "string") {
    str = str.replace(/&/g, "&amp;");
    str = str.replace(/>/g, "&gt;");
    str = str.replace(/</g, "&lt;");
    str = str.replace(/"/g, "&quot;");
    str = str.replace(/'/g, "&#039;");

    str = str.replace(/[\"\'][\s]*javascript:(.*)[\"\']/g, "\"\"");
    str = str.replace(/eval\((.*)\)/g, "");
  }
  return str;
};

var isEmpty = function(value) {
  return typeof value == 'string' && !value.trim() || typeof value == 'undefined' || value === null;
}

var handleDataTableDefault = function() {
      "use strict";
      0 !== $("#data-option").length && $("#data-option").DataTable({
        sDom: "t<'row'<'col-sm-6 hidden-xs'lf><'col-sm-6 col-xs-12'p>>",
        responsive: !0
      })
    },
    TableManageDefault = function() {
      "use strict";
      return {
        init: function() {
          handleDataTableDefault()
        }
      }
    }();

/**
 * 리모트 Handlebars 템플릿 로딩
 * @param templateName
 * @param selector
 * @param data
 * @param callback
 */
function displayTemplateData(templateName, selector, data, callback){

    var templatePath = CONTEXT_PATH + "/html/" + templateName + ".hbs";
    console.log("template: " + templatePath);
    $.get(templatePath, "", "", "html")
        .fail(function (){
            $(selector).html("Server system error !!  Please Retry again a few minutes.");
        })
        .done(function (tmplData) {
            var template = Handlebars.compile(tmplData);
            $(selector).html(template(data));
            if(typeof callback === "function"){
                callback();
            }
        });
}

/**
 * 리모트 Handlebars 아이디별 템플릿 로딩
 * @param templateName
 * @param selector
 * @param data
 * @param callback
 */
function displayTemplateIdData(templateName, templateId, selector, data, callback){

    var templatePath = gServerAddress + "/html/" + templateName + ".hbs";
    console.log("template: " + templatePath);
    $.get(templatePath, "", "", "html")
        .fail(function (){
            $(selector).html("Server system error !!  Please Retry again a few minutes.");
        })
        .done(function (tmplData) {

            var templateHtml =  $($.parseHTML(tmplData)).filter("#" + templateId).html();
            var template = Handlebars.compile(templateHtml);
            $(selector).html(template(data));
            if(typeof callback === "function"){
                callback();
            }
        });
}

/**
 * handlbar 템플릿 로딩 (치환)
 * @param templatName
 * @param targetObj원
 * @param data
 */
function setTemplate(templatName, targetObj, data, callback){

    var template = Handlebars.compile(templatName.html());
    targetObj.html(template(data));
    if(typeof callback === "function") callback();

}

/**
 * 숫자에 콤마를 넣어준다
 * 99999 --> 99,999
 * @param val
 * @returns {string|*}
 */
function fnCommaNumberFormat(val) {
    if (typeof val == 'undefined') {
        return "-";
    }
    if($.isNumeric(val) === false) return val;

    var inNum = parseFloat(val);

    if(inNum===0) return val;

    var reg = /(^[+-]?\d+)(\d{3})/;
    var n = (inNum + '');

    while (reg.test(n)) n = n.replace(reg, '$1' + ',' + '$2');

    return n;
    // ecma script 미지원..
    //return new Intl.NumberFormat("en-US").format(inNum);
}


/**
 * 콤보box 데이터 setting
 * apiUrl : combo list 데이터 apiURl
 * obj : selectbox 객체
 * val : option의 value항목에 맵핑될 항목
 * name : option의 text로 노출될 항목
 * defaultMessage : 생략가능 기본 [전체]로 표기
 */
function setCombo(apiUrl, obj, val, name, callback, defaultMessage){

  $(obj).find('option').remove();

  if(!isEmpty(defaultMessage)){
      $(obj).append('<option value="">'+defaultMessage+'</option>');
  }

  $.fn.Ajax("POST",apiUrl,{},function(result){

      var result = result.data.resultData;

      $.each(result, function(i, data) {
          $(obj).append('<option value="'+data[val]+'">'+data[name]+'</option>');
      });

      $('.sel').selectric({
          //inheritOriginalWidth: true, //텍스트 길이에 따라 selectbox의 길이가 가변적임
      });

      if (callback) {
          callback(result);
      };

  });

}


//일자 출력
function getDateText(type, addDay) {
    var date = new Date();

    if(addDay != undefined) date.setDate(date.getDate() + addDay)

    var yyyy = date.getFullYear().toString();
    var MM = pad(date.getMonth() + 1,2);
    var dd = pad(date.getDate(), 2);
    var hh = pad(date.getHours(), 2);
    var mm = pad(date.getMinutes(), 2);
    var ss = pad(date.getSeconds(), 2);

    if(type == "fullDate") {
        return yyyy + "년 " + MM + "월 " +dd+ "일 "+ hh + ":" + mm ;
    }else if(type == "monthDay") {
        return MM + "월 " +dd+ "일";
    }else{
        return yyyy + "년 " + MM + "월 " +dd+ "일";
    }
}

function pad(number, length) {
    var str = '' + number;
    while (str.length < length) {
        str = '0' + str;
    }
    return str;
}


//챠트
var chart = {
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
                            return "0";
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
                    format: function(value, ratio, id) {	return value +" " + getItemUnit(energyType).displayUnit;       }
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

    chartDateStr : function(config, chartCycle) {

        if(chartCycle == 'HOUR'){
            config.axis.x.tick.format =function(index, axisVal) {
                console.log(axisVal);
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    tempStr = tempStr.substr(10);
                    tempStr = tempStr.split(":")[0] + "H";
                    console.log(axisVal);
                    return tempStr; // .substr(10);
                }
            };
        }
        else if(chartCycle ==='HOUR_BY_FULLSTRING'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    tempStr = tempStr.substr(10);
                    return tempStr;
                }
            };
        }
        else if(chartCycle == 'DAY'){
            config.axis.x.tick.format =function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr;
                }
            };
        }
        else if(chartCycle == 'WEEK') {
            config.axis.x.tick.format = function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    tempStr = tempStr.substr(5,10) + " 주차";
                    return tempStr;
                }
            };
        }
        else if(chartCycle == 'MONTH') {
            config.axis.x.tick.format = function(index, axisVal) {
                if(axisVal !== undefined){
                    var tempStr = axisVal + "";
                    return tempStr;//.substr(5,10);
                }
            };

        }

        return config;
    }
}


/**
 * 초를 00:00:00 로 바꾸어 준다
 * @param val
 * @returns {string}
 */
function fnSecToDateTime4Dgit(val) {

    val = Math.abs(val);
    var hh = parseInt(val/(60*60));
    var mm = parseInt((val-hh*(60*60))/60);
    var ss = val - (hh*(60*60)+mm*60);

    var retH, retM, retS;

    var return_val = "";
    if (hh > 0) {
        retH = fnCommaNumberFormat(hh) + ":";
    }

    if (mm >= 0 && !isNaN(mm)) {
        retM = mm+": ";
    }

    if (ss >= 0 && !isNaN(ss)) {
        retS = return_val + ss.toFixed(0)+"";
    }

    if(hh > 0){
        return_val = retH + retM;
    } else if (mm > 0){
        return_val = retM + retS;
    } else {
        return_val = retS;
    }

    if (return_val === "") {
        return_val = ""
    }

    return return_val;
}

function fnYmdHToDateTime4Dgit(val) {

    var year = val.substring(0,4);
    var month = val.substring(4,6);
    var day = val.substring(6,8);
    var hour = val.substring(8,10);
    var minute = "00";
    var second = "00";
    
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}

/**
 * 초를 00시 00분 00초 로 바꾸어 준다 (1시간 이상은 00시 00분, 이하는 00분 00초 )
 * @param val
 * @returns {string}
 */
function fnSecToDateTime(val) {

    var hh = parseInt(val/(60*60));
    var mm = parseInt((val-hh*(60*60))/60);
    var ss = val - (hh*(60*60)+mm*60);

    var return_val = "";
    if (hh > 0) {
        return_val = fnCommaNumberFormat(hh) + "시간 ";
    }

    if (mm > 0 && !isNaN(mm)) {
        return_val = return_val + mm+"분 ";
    }

    if (ss > 0 && !isNaN(ss)) {
        return_val = return_val + ss.toFixed(0)+"초";
    }

    if (return_val === "") {
        return_val = ""
    }

    return return_val;
}

/**
 * 엑셀다운
 * @param result
 * @returns
 */
function excelFileDown(result){
    location.href = '/mdmsHouse/v1.0/excelFile?fileName='+result.data.resultData.fileName+'&randomFileName='+result.data.resultData.randomFileName;
}

/**
 * bytes to KB, MB, GB
 * formatBytes(1024);       // 1 KB
 * formatBytes('1024');     // 1 KB
 * formatBytes(1234);       // 1.21 KB
 * formatBytes(1234, 3);    // 1.205 KB
 * @param bytes
 * @param decimals
 * @returns
 */
function formatBytes(bytes,decimals) {
   if(bytes == 0) return '0 Bytes';
   var k = 1024,
       dm = decimals <= 0 ? 0 : decimals || 2,
       sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
       i = Math.floor(Math.log(bytes) / Math.log(k));
   return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

/**
 * wh : Wh -> kWh -> MWh
 * @param wh
 * @param decimals
 * @param returnType ( "uniitOnly": 환산된 단위만 리턴, "valueOnly" : 환산된 값만 리턴, 그외 : 값 + 단위 리턴
 * @returns
 */
function formatWh(wh,decimals, returnType, forceUnit) {
    var sizes = ['Wh', 'kWh', 'MWh'];
    var maxUnitValue = 1000;
    return formatUnitAction(wh, sizes, decimals, maxUnitValue, returnType, forceUnit);

/*    var convertLevel = -1;
    if(typeof forceUnit !== "undefined" || (forceUnit !== "")){
        convertLevel = sizes.findIndex(function (item, idx) { return item === forceUnit});
    }


    if(wh == 0){
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType === "valueOnly"){
            return 0;
        } else {
            return '0 <span class="txt_unit">' + sizes[0] + '</span>';
        }
    }
    var k = 1000,
       dm = decimals <= 0 ? 0 : decimals || 2,
       i = Math.floor(Math.log(wh) / Math.log(k));

    if(returnType === "unitOnly"){
        if(sizes.length <= i){
            return sizes[sizes.length - 1]; // 최대 단위가 넘어가면 마지막 단위로 유지..
        } else {
            return sizes[i];
        }
    } else if (returnType ==="valueOnly"){
        return parseFloat((wh / Math.pow(k, i)).toFixed(dm));
    } else {
       return parseFloat((wh / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }*/
}

/**
 * W : W -> kW - MW
 * @param wh
 * @param decimals
 * @param returnType ( "uniitOnly": 환산된 단위만 리턴, "valueOnly" : 환산된 값만 리턴, 그외 : 값 + 단위 리턴
 * @param forceUnit : 해당 단위로 변환.
 * @returns
 */
function formatW(w,decimals, returnType, forceUnit) {

    var sizes = ['W', 'kW', 'MW'];

    var maxUnitValue = 1000;
    return formatUnitAction(w, sizes, decimals, maxUnitValue, returnType, forceUnit);

/*    var convertLevel = -1;
    if(typeof forceUnit !== "undefined" || (forceUnit !== "")){
        convertLevel = sizes.findIndex(function (item, idx) { return item === forceUnit});
    }

    if(w === 0){
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType === "valueOnly"){
            return 0;
        } else {
            return '0 <span class="txt_unit">' + sizes[0] + '</span>';
        }
    }

    var k = 1000,
       dm = decimals <= 0 ? 0 : decimals || 2,
       i = Math.floor(Math.log(w) / Math.log(k));

    if(returnType === "unitOnly"){
        if(sizes.length <= i){
            return sizes[sizes.length - 1]; // 최대 단위가 넘어가면 마지막 단위로 유지..
        } else {
            return sizes[i];
        }
    } else if (returnType === "valueOnly"){
        if(convertLevel >= 0){
            return parseFloat((w / Math.pow(k, convertLevel)).toFixed(dm));
        } else {
            if(sizes.length <= i ) i = sizes.length - 1; // 최대 단위가 넘어가면 마지막 단위로 유지..
            return parseFloat((w / Math.pow(k, i)).toFixed(dm));
        }
    } else {
        return parseFloat((w / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }*/
}

/**
 * varh : varh -> kvarh - Mvarh
 * @param varh
 * @param decimals
 * @returns
 */
function formatVarh(varh, decimals, returnType, forceUnit) {

    var sizes = ['varh', 'kvarh', 'Mvarh'];
    var maxUnitValue = 1000;
    return formatUnitAction(varh, sizes, decimals, maxUnitValue, returnType, forceUnit);


/*    if (varh == 0) {
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType === "valueOnly"){
            return 0;
        } else {
            return '0 <span class="txt_unit">' + sizes[0] + '</span>';
        }
    }

	var k = 1000,
        dm = decimals <= 0 ? 0 : decimals || 2,
        i = Math.floor(Math.log(varh) / Math.log(k));

    if(returnType === "unitOnly"){
        return sizes[i];
    } else if (returnType === "valueOnly") {
        return parseFloat((w / Math.pow(k, i)).toFixed(dm));
    } else {
        return parseFloat((varh / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }*/
}

/**
 * m³ : m³ -> km³
 * @param m
 * @param decimals
 * @returns
 */
function formatM(m, decimals, returnType, forceUnit) {

   var sizes = ['m³', 'km³'];

    var maxUnitValue = 1000;
    return formatUnitAction(m, sizes, decimals, maxUnitValue, returnType, forceUnit);

/*   if(m == 0){
       if(returnType === "unitOnly"){
           return sizes[0];
       } else if (returnType ==="valueOnly"){
           return 0;
       } else {
           return '0 <span class="txt_unit">' + sizes[0] + '</span>';
       }
   }
   var k = 1000,
       dm = decimals <= 0 ? 0 : decimals || 2,
       i = Math.floor(Math.log(m) / Math.log(k));

    if(returnType === "unitOnly"){
        return sizes[i];
    } else if (returnType === "valueOnly"){
        return parseFloat((m / Math.pow(k, i)).toFixed(dm));
    } else {
        return parseFloat((m / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }*/
}

/**
 * m³/h : m³/h -> km³/h
 * @param mh
 * @param decimals
 * @returns
 */
function formatMh(mh,decimals,returnType, forceUnit) {

    var sizes = ['m³/h', 'km³/h'];

    var maxUnitValue = 1000;
    return formatUnitAction(mh, sizes, decimals, maxUnitValue, returnType, forceUnit);

/*    if (mh == 0) {
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType ==="valueOnly"){
            return 0;
        } else {
            return '0 <span class="txt_unit">' + sizes[0] + '</span>';
        }
    }

   var k = 1000,
       dm = decimals <= 0 ? 0 : decimals || 2,
       i = Math.floor(Math.log(mh) / Math.log(k));

    if(returnType === "unitOnly"){
        return sizes[i];
    } else if (returnType === "valueOnly") {
        return parseFloat((mh / Math.pow(k, i)).toFixed(dm));
    } else {
        return parseFloat((mh / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }*/
}

function formatUnitAction(value, sizes, decimals, maxUnitValue, returnType, forceUnit){

    var convertLevel = -1;
    if(typeof forceUnit !== "undefined" && (forceUnit !== "")){
        convertLevel = sizes.findIndex(function (item, idx) { return item === forceUnit});
    }

    var k = maxUnitValue,
        dm = decimals <= 0 ? 0 : decimals || 2;

    if(value === 0){
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType === "valueOnly"){
            return 0;
        } else {
            return '0 <span class="txt_unit">' + sizes[0] + '</span>';
        }
    } else if (value < 1){
        if(returnType === "unitOnly"){
            return sizes[0];
        } else if (returnType === "valueOnly"){
            return value;
        } else {
            return value.toFixed(dm) + ' <span class="txt_unit">' + sizes[0] + '</span>';
        }
    }

    var i = Math.floor(Math.log(value) / Math.log(k));

    if(returnType === "unitOnly"){
        if(sizes.length <= i){
            return sizes[sizes.length - 1]; // 최대 단위가 넘어가면 마지막 단위로 유지..
        } else {
            return sizes[i];
        }
    } else if (returnType === "valueOnly"){
        if(convertLevel >= 0){
            return parseFloat((value / Math.pow(k, convertLevel)).toFixed(dm));
        } else {
            if(sizes.length <= i ) i = sizes.length - 1; // 최대 단위가 넘어가면 마지막 단위로 유지..
            return parseFloat((value / Math.pow(k, i)).toFixed(dm));
        }
    } else {
        return parseFloat((value / Math.pow(k, i)).toFixed(dm)) + ' <span class="txt_unit">' + sizes[i] + '</span>';
    }
}

/**
 * CRON EXPRESS TO DATE
 * @param cronExpression
 * @returns
 */
function convertCronToString(cronExpression) {
	  var cron = cronExpression.split(" ");
	  var seconds = cron[0];
	  var minutes = cron[1];
	  var hours = cron[2];
	  var dayOfMonth = cron[3];
	  var month = cron[4];
	  var dayOfWeek = cron[5];

	  var cronToString = "";
	  var timeString = "";

	  // Formatting time if composed of zeros
	  if (seconds === "0") seconds = "00";
	  if (minutes === "0") minutes = "00";
	  if (hours === "0") hours = "00";
	  // If it's not past noon add a zero before the hour to make it look like "04h00" instead of "4h00"
	  else if (hours.length === 1 && hours !== "*") {
	    hours = "0" + hours;
	  }
	  // Our activities do not allow launching pipelines every minute. It won't be processed.
	  if (minutes === "*") {
	    cronToString = "시분초 포맷 오류 : " + cronExpression;
	  }
	  timeString = hours + " 시 " + minutes + " 분 " + seconds + " 초 ";
	  // cronToString = cronToString + 

	  if (dayOfWeek === "0,6") dayOfWeek = " 매주 ";
	  else if (dayOfWeek === "1-5") dayOfWeek = " 매주 ";
	  else if (dayOfWeek.length === 1) {
	    if (dayOfWeek === "*" && dayOfMonth === "*") dayOfWeek = " 매일 ";
	    else if ((dayOfWeek === "*" || dayOfWeek === "?" ) && dayOfMonth !== "*") {
	      cronToString = cronToString + " " + dayOfMonth;
//	      if (
//	        dayOfMonth === "1" ||
//	        dayOfMonth === "21" ||
//	        dayOfMonth === "31"
//	      ) {
//	        cronToString = cronToString + " st ";
//	      } else if (dayOfMonth === "2" || dayOfMonth === "22") {
//	        cronToString = cronToString + "nd ";
//	      } else if (dayOfMonth === "3" || dayOfMonth === "23") {
//	        cronToString = cronToString + " rd ";
//	      } else {
//	        cronToString = cronToString + " th ";
//	      }
	      cronToString = " 매월 " + cronToString + " 일 " + timeString;
	      return cronToString;
	    } else if ((dayOfWeek !== "*" || dayOfWeek !== "?" )&& dayOfMonth === "*") {
	      switch (parseInt(dayOfWeek)) {
	        case 1:
	          dayOfWeek = " 일요일 ";
	          break;
	        case 2:
	          dayOfWeek = " 월요일 ";
	          break;
	        case 3:
	          dayOfWeek = " 화요일 ";
	          break;
	        case 4:
	          dayOfWeek = " 수요일 ";
	          break;
	        case 5:
	          dayOfWeek = " 목요일 ";
	          break;
	        case 6:
	          dayOfWeek = " 금요일 ";
	          break;
	        case 7:
	          dayOfWeek = " 토요일 ";
	          break;
	        default:
	          cronToString = "요일 포맷 오류 : " + cronExpression;
	          return cronToString;
	      }
	    }
	    cronToString = cronToString + dayOfWeek + timeString;
	}
	return cronToString;
}

/**
 * NULL TO 공백
 * @returns
 */
function fnNullToEmpty(text) {
	return fnIsEmpty(text)?"":text;
}

function fnNullToEmptyFix(text,preFix,sufFix) {
	return fnIsEmpty(text)?"":preFix+text+sufFix;
}

//--------------------------------------------------------------------------------
//fnIsEmpty
//숫자, 문자열, 배열, 객체가 비었는지 확인
//Note) $('any')와 같은 jQuery 객체에 대해서는 사용하지 말 것
//--------------------------------------------------------------------------------
var fnIsEmpty = function(value) { 
	if ( value == "" ) { value += ''; } // 정수 0인 경우 
	if( value == "" || value == null || value == undefined ||
	 ( value != null && typeof value == "object" && !Object.keys(value).length ) ) {
	   return true 
	} else {
	   return false 
	} 
};

//--------------------------------------------------------------------------------
//fnPad
//--------------------------------------------------------------------------------
var fnPad = function(n, width) {
	n = n + '';
	return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
};

//--------------------------------------------------------------------------------
//fnGetLength
//--------------------------------------------------------------------------------
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

//--------------------------------------------------------------------------------
//fnDateFormat
//--------------------------------------------------------------------------------
var fnDateFormat = function(dt, nts) {
	return ( fnIsEmpty(dt) === false )?dt.split(" ")[0].replace(/-/g, '.'):nts;
};

//--------------------------------------------------------------------------------
//fnDateTimeFormat
//--------------------------------------------------------------------------------
var fnDateTimeFormat = function(dt, srch, frm, nts) {
	return ( fnIsEmpty(dt) === false )?dt.split(srch).join(frm):nts;
};

//--------------------------------------------------------------------------------
//fnGetCronExpression
//0-00-00-00 : 사용유무(1자리) + 주(2자리) + 일(2자리) + 시(2자리) 
//--------------------------------------------------------------------------------
var fnGetCronExpression = function(autoType, cronType, week, day, hour){
	var return_val = "0000000";
	
	if (autoType != "" && autoType != "0") {
		if (cronType=="W") {
			return_val = autoType + week + "00" + hour;
		}
		else if (cronType=="D") {
			return_val = autoType + "0000" + hour;
		} else {
			return_val = autoType + "00" + day + hour;
		}
	}
	return return_val;
}

//--------------------------------------------------------------------------------
//fnCycleSetStatus
//gwaTsCycle, selfCheckPeriod, confSyncPeriod
//0-00-00-00 : 사용유무(1자리) + 주(2자리) + 일(2자리) + 시(2자리) 
//--------------------------------------------------------------------------------
//자가 진단 설정 상태 : ON 
var fnCycleSetStatus = function(cycle) {
    var return_val = "OFF";
    if(fnIsEmpty(cycle) === false) {
    	if (fnGetLength(cycle)==7) {
			var t = cycle.split("");
			if(t[0]=="1") {
				return_val = "ON";
			}
    	}
    }
    return return_val;
}
var fnCycleToDateTime = function(cycle) {

    var return_val = "사용안함";
    
    var mm = "매월";
    var wk = "00";
    var dd = "00";
    var hh = "00";
    
	if(fnIsEmpty(cycle) === false) {
		if (fnGetLength(cycle)==7) {
			var t = cycle.split("");
			if(t[0]=="1") {
				wk = t[1] + "" + t[2];
				dd = t[3] + "" + t[4];
				hh = t[5] + "" + t[6];
				
				if(wk=="00") { 

					if(dd!="00") {

						// 월
						return_val = "매월";
						return_val += " " + dd + "일";
						return_val += " " + hh + "시";
					} 
					// 일
					else {

						return_val = "매일";
						return_val += " " + hh + "시";
					}
					 
				}
				else {
					return_val = "매주";
					
						 if (wk=="01") {return_val += " " + "일요일";}
					else if (wk=="02") {return_val += " " + "월요일";}
					else if (wk=="03") {return_val += " " + "화요일";}
					else if (wk=="04") {return_val += " " + "수요일";}
					else if (wk=="05") {return_val += " " + "목요일";}
					else if (wk=="06") {return_val += " " + "금요일";}
					else if (wk=="07") {return_val += " " + "토요일";}
					else {}

					return_val += " " + hh + "시";
				}
			}
		}
	}
    return return_val;
    
};
// 주기 : 일별 /12시
var fnCycleToDateTimeFormat = function(cycle) {

    var return_val = "-";
    
	if(fnIsEmpty(cycle) === false) {
		if (fnGetLength(cycle)==7) {
			var t = cycle.split("");
			if(t[0]=="1") {
				wk = t[1] + "" + t[2];
				dd = t[3] + "" + t[4];
				hh = t[5] + "" + t[6];
				
				// 월
				if(wk=="00") { 

					if(dd!="00") {

						return_val = "월별/" + dd + "일";
						return_val += " " + hh + "시";
					} 
					// 일
					else {
						return_val = "일별/" + hh + "시";
					}
				} 

				// 주
				else {
					return_val += "주별/";
						
						 if (wk=="01") {return_val += " " + "일요일";}
					else if (wk=="02") {return_val += " " + "월요일";}
					else if (wk=="03") {return_val += " " + "화요일";}
					else if (wk=="04") {return_val += " " + "수요일";}
					else if (wk=="05") {return_val += " " + "목요일";}
					else if (wk=="06") {return_val += " " + "금요일";}
					else if (wk=="07") {return_val += " " + "토요일";}
					else {}

					return_val = " " + hh + "시";
				}
				
			}
		}
	}
    return return_val;
    
};

//--------------------------------------------------------------------------------
//fnJsonKeyNum
//JSON ARRAY => MAP
//arry key, value 형태
//--------------------------------------------------------------------------------
var fnKeyValueListToJson = function(json, content) {
	content.forEach(function(e) {
		json[e.key] = e.value;
	});
	return json;
}

//--------------------------------------------------------------------------------
//fnJsonKeyNum
//JSON KEY가 숫자 문자열인 경우 
//--------------------------------------------------------------------------------
var fnJsonKeyNum = function(json, key) {
	return json[key];
}

//--------------------------------------------------------------------------------
//fnGetAreaCode
//설치지역코드
//--------------------------------------------------------------------------------
var fnGetAreaCode = function(bcode) {
	var bcodes = bcode.split("");
	
	var areaCodes = new Array();
	areaCodes[0] = "1100000000"; // 서울특별시
	areaCodes[1] = "2600000000";	// 부산광역시
	areaCodes[2] = "2700000000";	// 대구광역시
	areaCodes[3] = "2800000000";	// 인천광역시
	areaCodes[4] = "2900000000";	// 광주광역시
	areaCodes[5] = "3000000000";	// 대전광역시
	areaCodes[6] = "3100000000";	// 울산광역시
	areaCodes[7] = "3611000000";	// 세종특별자치시
	areaCodes[8] = "4100000000";	// 경기도
	areaCodes[9] = "4200000000";	// 강원도
	areaCodes[10] = "4300000000";	// 충청북도
	areaCodes[11] = "4400000000";	// 충청남도
	areaCodes[12] = "4500000000";	// 전라북도
	areaCodes[13] = "4600000000";	// 전라남도
	areaCodes[14] = "4700000000";	// 경상북도
	areaCodes[15] = "4800000000";	// 경상남도
	areaCodes[16] = "5000000000";	// 제주특별자치도
	
	var codes;
	var areaCode = "";
	for(var i=0; i<areaCodes.length; i++) {
		areaCode = areaCodes[i];
		codes = areaCode.split("");
		
		if(codes[0]==bcodes[0] && codes[1]==bcodes[1]) {
			areaCode
			break;
		}
	}
	console.log("bcode:", bcode);
	console.log("areaCode:", areaCode);
	return areaCode;
};

var commonMsg = {
    alert : function(msg, title, callback){

        $('.pop_common_alert').css("z-index", commonMsg.getZindex());
        $("#pop_common_alert_msg").html(msg);
        $("#pop_common_alert_id").html( isEmpty(title) ? "알림" : title);
        $(".pop_common_alert").find(".closeBtn, .pop_close").unbind("click");
        $(".pop_common_alert").find(".closeBtn, .pop_close").click(function(){
            popup_close('.pop_common_alert');
            if (callback) {
                callback();
            };

        });
        popup_open('.pop_common_alert');
    },

    confirm : function(msg, title, exeCallback, callback){

        $('.pop_common_confirm').css("z-index", commonMsg.getZindex());
        $("#pop_common_confirm_msg").html(msg);
        $("#pop_common_confirm_id").html( isEmpty(title) ? "알림" : title);

        $(".pop_common_confirm").find(".exeBtn").unbind("click");
        $(".pop_common_confirm").find(".exeBtn").click(function(){
            setTimeout(function() {
                popup_close('.pop_common_confirm');
            }, 100);

            if (exeCallback) {
                exeCallback();
            }
        });

        $(".pop_common_confirm").find(".closeBtn, .pop_close").unbind("click");
        $(".pop_common_confirm").find(".closeBtn, .pop_close").click(function(){
            popup_close('.pop_common_confirm');
            if (callback) {
                callback();
            };

        });
        popup_open('.pop_common_confirm');
    },

    getZindex : function(){
        var zIndex = 201;
        $(".popup_cont:visible").each(function(){
            console.log($(this).css("z-index"));
            zIndex = (zIndex < $(this).css("z-index") ) ? $(this).css("z-index") : zIndex;
            console.log(zIndex);
        });

        return Number(zIndex) + 1;
    }
};

//챠트 기본 설정
function getChartConfig(targerObj, chartData, keyArr, labelArr, typesArr, colorArr){

    // data 세팅
    var columnsArr = [];
    for(var i=0; i<chartData.length; i++){

        if(i == 0){
            for(var j=0; j<keyArr.length; j++){
                var obj = [];
                obj.push(chartData[i][ keyArr[j] ]);
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
    var colors = {}
    var types = {}
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
                        if (Math.floor(x) === x) {
                            return x;
                        }else{
                            return "";
                        }

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

        bar :{ padding: 2 },

        bindto: targerObj
    }
}

//--------------------------------------------------------------------------------
//fnSendAjaxFile
//--------------------------------------------------------------------------------
var fnSendAjaxFile = function(url, formData, success, error, complete, loading) {
    $.ajax({
        type : "POST",
        url : url,
        data : formData,
        crossDomain: true,
        xhrFields: {withCredentials: true},
        headers: {'X-Requested-With':'XMLHttpRequest'},
        cache: false,
        contentType: false,
        processData: false,
        success : function(data) {
            success(data);
        },
        error : function(req, status, thrown) {
            if(req.status === 401){
                commonMsg.confirm("로그인이 필요 합니다. 로그인 페이지로 이동 합니다.","로그인 오류",
                    function (){
                        location.href="login.html";
                    });
                return false;
            }

        },
        complete: function(xhr, status) {
            if (typeof complete !== 'undefined' && $.isFunction(complete)) {
                complete(xhr, status);
            }
        }
    });
};

function maxWindow() {
    window.moveTo(0, 0);

    if (1) {
        top.window.resizeTo(screen.availWidth, screen.availHeight);
    }

    else if (document.layers || document.getElementById) {
        if (top.window.outerHeight < screen.availHeight || top.window.outerWidth < screen.availWidth) {
            top.window.outerHeight = screen.availHeight;
            top.window.outerWidth = screen.availWidth;
        }
    }
}

function fullScreen(){
    var el = document.documentElement;
    var rfs = el.requestFullscreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullscreen;

    if(typeof rfs!="undefined" && rfs){
        rfs.call(el);
    } else if(typeof window.ActiveXObject!="undefined"){
        // for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript!=null) {
            wscript.SendKeys("{F11}");
        }
    }
}


/**
 * 페이지 로딩
 * @returns
 */
function pageLoad(url) {
    if(url==="") {
        url = "login.html"
    }
    $('body').load(url);
}

/**
 * 날짜 세팅
 * @param type
 * @param from
 * @param to
 * @param dateSeperator
 * @returns
 */
function shiftInit(day, y, m, d, separator) {
	var temp = day.split("-");
    var date = new Date(temp[0], temp[1]-1, 1);
    
    date.setFullYear(date.getFullYear() + y);
    date.setMonth(date.getMonth() + m);
    date.setDate(date.getDate() + d);
    
    return $.datepicker.formatDate('yy-mm-dd', date);    
}
function shiftDate(date, y, m, d, separator) {
	
    date.setFullYear(date.getFullYear() + y);
    date.setMonth(date.getMonth() + m);
    date.setDate(date.getDate() + d);

    return $.datepicker.formatDate('yy-mm-dd', date);    
}
function shiftDay(day, y, m, d, separator) {
	var temp = day.split("-");
    var date = new Date(temp[0], temp[1]-1, temp[2]);
    
    date.setFullYear(date.getFullYear() + y);
    date.setMonth(date.getMonth() + m);
    date.setDate(date.getDate() + d);
    
    return $.datepicker.formatDate('yy-mm-dd', date);    
}

function dateDiff(type, sday, eday) {

	var stemp = sday.split("-");
	var etemp = eday.split("-");
    var sdate = new Date(stemp[0], stemp[1]-1, stemp[2]);
    var edate = new Date(etemp[0], etemp[1]-1, etemp[2]);
     
    var interval = edate - sdate;
    var day = 1000*60*60*24;
    var month = day*30;
    var year = month*12;
    
    var diff = 0;
	switch(type) {
		case "D" : // 일
			diff = parseInt(interval/day);
			break;
		case "M" : // 월
			diff = parseInt(interval/month);
			break;
		case "Y" : // 년
			diff = parseInt(interval/year);
			break;
	}

    return diff;
}

/**
 * 오늘부터 선택 날짜 일,월,년 수
 * @param type
 * @param day
 * @returns
 */
function nowDiff(type, day) {
	
	var temp = day.split("-");
    var sdate = new Date(temp[0], temp[1]-1, temp[2]);
    var edate = new Date();
     
    var interval = edate - sdate;
    var day = 1000*60*60*24;
    var month = day*30;
    var year = month*12;
    
    var diff = 0;
	switch(type) {
		case "D" : // 일
			diff = parseInt(interval/day);
			break;
		case "M" : // 월
			diff = parseInt(interval/month);
			break;
		case "Y" : // 년
			diff = parseInt(interval/year);
			break;
	}

    return diff;
}

/**
 * 소수점 단위 설정 (문자열 리턴)
 * @param meterType
 * @param value
 * @param isConv (단위 변환 여부, true, false)
 */
function unitPoint(meterType, value, isConv, convertUnit) {

    if(value === null) return "0";
    if($.isNumeric(value) === false) return value;
    if(value <= 0) return "0";

    var isConvertUnit = "";
    if(typeof convertUnit !== "undefined") isConvertUnit = convertUnit;

    // 단위 환산
    if(isConv){
        switch (meterType) {
//            case "fap" :
//            case "insKcal":
//            case "elec":
//            case "etc":
//                value = formatW(value, 4, "valueOnly", isConvertUnit); break;
//            case "flow" :
//            case "gas":
//            case "water" :
//            case "hotw" :
//                value = formatM(value, 4, "valueOnly", isConvertUnit); break;
//            case "ap" :
//            case "kcal" :
//            case "heat" :
//                value = formatWh(value, 4, "valueOnly", isConvertUnit); break;
//            case "rap" :
//            case "larap" :
//            case "lerap" :
//                value = formatVarh(value, 4, "valueOnly", isConvertUnit); break;
//            case "instFlow" :
//                value = formatMh(value, 4, "valueOnly", isConvertUnit); break;
            default :
//                console.log("unitPoint(" + meterType + ", " + value + "," + isConv + ", " + convertUnit +") - 환산할 단위가 없습니다 ");
                return value;
        }
    }

    if(Number.isInteger(value)){
        value = Math.floor(value);  // 정수 이면 소수점 자리 없음..
    } else if(value < 1000){
        value = value.toFixed(4).substring(0,5);  // 1000보다 작으면 숫자 4자리로 맞춤
    } else {
        value = Math.floor(value);  // 1000 보다 크면 소수점 버림.
    }
    return value;
}

/**
 * 소수점 단위 설정 (숫자 리턴)
 * @param meterType
 * @param value
 */
function unitPointNumber(meterType, value, isConv) {

    if(value === null || value === 0) return 0;
    if($.isNumeric(value) === false) return value;

    switch (meterType) {
        case "elec" :
            if(isConv) value = formatW(value, 0, "valueOnly"); // 단위 환산
            return Math.round(value);
        case "heat" :
            return value.toFixed(2);
        default :
            return value.toFixed(4);
    }
}

/**
 * 평균 소수점 단위 설정
 * @param meterType
 * @param value
 */
function unitAvgPoint(meterType, value){

    if($.isNumeric(value) === false) return value;

    switch (meterType) {
	    case "elec" :
	        return fnCommaNumberFormat(value.toFixed(2));
	    case "heat" :
	        return fnCommaNumberFormat(value.toFixed(2));
        default :
            return fnCommaNumberFormat(value.toFixed(4));
    }
}

/**
 * 항목별 소수점 처리
 * @param item
 * @param value
 * @returns
 */
function unitItemPoint(item, value){

    if($.isNumeric(value) === false) return value;

    switch (item) {
	    case "fap" :
	        return fnCommaNumberFormat(value.toFixed(0));
	    case "rap" :
	        return fnCommaNumberFormat(value.toFixed(0));
	    case "ap" :
	        return fnCommaNumberFormat(value.toFixed(0));
	    case "kcal" :
	        return fnCommaNumberFormat(value.toFixed(0));
	    case "flow" :
	        return fnCommaNumberFormat(value.toFixed(4));
	    case "temp" :
	        return fnCommaNumberFormat(value.toFixed(2));
	    case "prs" :
	        return fnCommaNumberFormat(value.toFixed(2));
        default :
            return fnCommaNumberFormat(value.toFixed(4));
    }
}

/**
 * 백분율
 * @param meterType
 * @param value
 * @returns
 */
function unitPercentPoint(meterType, value){

    if($.isNumeric(value) === false) return value;

    switch (meterType) {
	    case "elec" :
	    	return fnCommaNumberFormat(value.toFixed(2));
	    case "heat" :
	    	return fnCommaNumberFormat(value.toFixed(2));
        default :
            return fnCommaNumberFormat(value.toFixed(3));
    }
}


/**
 * 단위
 * @param meterType
 * @Param value (value 값이 있으면 환산된 단위가 리턴된다.)
 * @returns
 */
function getReadingUnit(meterType, value){

    value = (typeof value === "undefined") ? 0 : value;
    value = (value === null) ? 0 : value;
    value = (value < 0) ? 0 : value;

    if(meterType === "elec"){
        return  {
            "displayName": "성공",
            "displayUnit" : "",
            "color" : "#696cff"
        };
    };
    if(meterType === "gas"){
        return  {
        "displayName": "실패",
        "displayUnit" : "",
        "color": "#25ce60"
        };
    };

    if(meterType === "water") {
        return {
            "displayName": "진행",
            "displayUnit": "",
            "color": "#00d0dd"
        };
    };

     if(meterType === "hotw") {
         return {
             "displayName": "로컬",
             "displayUnit": "",
             "color": "#f15959"
         };
     }

     if(meterType === "heat") {
         return {
             "displayName": "개발",
             "displayUnit": "",
             "color": "#fcb040"
         };
     };

    if(meterType === "etc") {
        return {
            "displayName": "검증",
            "displayUnit": "",
            "color": "#3abf9e"
        };
    };

    return {
     "displayName": "-",
     "displayUnit": "-",
     "color": "#eeeeee"
    };
}

/**
 * 항목별 단위
 * @param meterType
 * @Param value (value 값이 있으면 환산된 단위가 리턴된다.)
 * @returns
 */
function getItemUnit(item, value){
    var unit = {};
    value = (typeof value === "undefined") ? 0 : value;
    value = (value === null) ? 0 : value;
    value = (value < 0) ? 0 : value;

    unit.fap = {
        "displayName": "유효전력량",
        "displayUnit" : formatWh(value, 0, "unitOnly"),
    };

    unit.rap = {
        "displayName": "무효전력량",
        "displayUnit" : formatVarh(value, 0, "unitOnly"),
    };

    unit.larap = {
        "displayName": "지상무효전력량",
        "displayUnit" : formatVarh(value, 0, "unitOnly"),
    };

    unit.lerap = {
        "displayName": "진상무효전력량",
        "displayUnit" : formatVarh(value, 0, "unitOnly"),
    };

    unit.ap = {
        "displayName": "피상전력량",
        "displayUnit" : formatWh(value, 0, "unitOnly"),
    }

    unit.insKcal = {
        "displayName": "순간열량",
        "displayUnit" : formatW(value, 0, "unitOnly"),
    }

    unit.kcal = {
        "displayName": "적산열량",
        "displayUnit" : formatWh(value, 0, "unitOnly"),
    }

    unit.temp = {
        "displayName": "온도",
        "displayUnit" : "℃",
    }

    unit.flow = {
        "displayName": "적산유량",
        "displayUnit" : formatM(value, 0, "unitOnly"),
    }

    unit.instFlow = {
        "displayName": "순간유량",
        "displayUnit" : formatMh(value, 0, "unitOnly"),
    }

    unit.prs = {
        "displayName": "압력",
        "displayUnit" : "pa",
    }

    unit.pft = {
        "displayName": "평균역률",
        "displayUnit" : "%",
    }

    return unit[item];
}

function getTariffName(tariff){
    if(tariff === "A") return "경부하";
    if(tariff === "B") return "최대부하";
    if(tariff === "C") return "중부하";
    if(tariff === "D") return "기타";
    return "";
}

/**
 * 단위 변환 한다.
 * @param val
 * @param energyType : 단위 (fap, flow, pft, 등 )
 * @param convertUnit : 변환 해야할 단위 ( kw, kValh 등)
 * @returns {string|*|number}
 */
function convertUnit(val, energyType, convertUnit){
    if(Array.isArray(val)){
        var result = [];
        for(var i = 0; i <= val.length -1; i++){
            result.push(unitPoint(energyType, val[i], true, convertUnit));
        }
        return result;
    } else if ($.isNumeric(val)){
        return unitPoint(energyType, val, true, convertUnit);
    }
}

/**
 * 검침종류 목록
 * @returns {jQuery}
 */
function getMeterUnit()
{
    return $("#meterUnit").data();
}

/**
 * 체크 박스 리스트
 * @param target
 * @returns {any[]}
 */
function selectionValue(target) {

    var list = new Array();

    $('input:checkbox[name='+ target +']:checked').each(function () {
        list.push($(this).val());
    });

    return list;
}

function getUrlParams() {
    var params = {};
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) { params[key] = value; });
    return params;
}

/**
 * array lit 에서 해당 키값의 합계
 * @param data
 * @param key
 */
function getSumByArray(data, sumKey, ifKey, ifValue){
    var total = 0;
    for ( var i = 0, _len = data.length; i < _len; i++ ) {
        // 키가 없으면 전체 합계
        if(ifKey === "") {
            total += data[i][sumKey];
        } else {
            if (data[i][ifKey] === ifValue) {
                total += data[i][sumKey];
            }
        }
    }
    return total
}

/**
 * array list 에서 max 값
 * @param data
 * @param field
 */
function getMaxIndexByArray(data, field){

    var temp1 = 0;
    var maxIdx = 0;
    data.forEach(function (obj, idx){
        if(temp1 < obj[field]){
            maxIdx = idx;
            temp1 = obj[field];
        }
    });
    return maxIdx;

}

/**
 * color 변환 (rgb -> hex )
 * @param colorval
 * @returns {string|*}
 */
function rgbTohexc(colorval) {
    var parts = colorval.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    delete(parts[0]);
    for (var i = 1; i <= 3; ++i) {
        parts[i] = parseInt(parts[i]).toString(16);
        if (parts[i].length == 1) parts[i] = '0' + parts[i];
    }
    color = '#' + parts.join('');
    return color;
}


/**
 * 트리 메뉴 출력
 * @param container
 * @param type ("dongOnly" : 동만 리스트, "commonOnly" : 공용부만 리스트,  else : 모두 출력
 */
function goTree(container, type){
    var thisLevelType = "";
    if(type === "commonOnly") thisLevelType = "common"; // 공용부만 출력
    if(type === "dongOnly") thisLevelType = "dong"; // 공용부만 출력
    var treeOption = {

        // 출력할 obj
        displayObj : container,
        deviceType : "dong",
        searchTextObj : "",
        thisLevelType : thisLevelType,
        // 노드 선택시 callback function
        onSelectFunction : function (node){
            //// // console.log(node);
            // selectedTreeNode(node);

            if(container == '#pop_deviceTree') {
                selectedTreeNodePop(node);
            } 
            else {
                selectedTreeNode(node);
            }
        },

        // 노드 체크박스 체크시 callback
        onCheckNodeFunction : function (node){
            // // console.log("tree on check node checked  !!!");
            // // console.log(node);
        },
        // 노드 all 체크박스 체크시 callback
        onCheckAllFunction : function (node){
            // // console.log("tree on check All checked  !!!");
            // // console.log(node);
        },
        // 노드 체크박스 해제 callback
        onUnCheckNodeFunction : function (node){
            // // console.log("tree on uncheck node checked  !!!");
            // // console.log(node);
        },
        // 노드  모든 체크박스 해제시 callback
        onUnCheckAllFunction : function (node){
            // // console.log("tree all node unchecked  !!!");
            // // console.log(node);
        },
        // tree 로딩 완료 이벤트
        onLoadCompleteFunction: function (node){
            // function 이 없으면 최상단 노드 select 함
            if(typeof treeOnLoad === "function") {
                treeOnLoad(node);
            } else {
                var firstNode = $(container).jstree();
                // 기존 선택된게 없으면 맨 위에거 선택
                if($(container).jstree("get_selected").length === 0){
                    $(container).jstree('select_node', $(container).jstree("get_json")[0].id);
                }
                $(container)
            }
        },
    };
    gelixCommon.treeMenuArea.displayTree(treeOption);
}

/**
 * 로그인된 아파트 정보
 * @returns {jQuery|{parent: String, a_attr: {href: string}, original: boolean, data: null, children: [], li_attr: {id: boolean}, icon: boolean, id: boolean, text: boolean, state: {}, children_d: [], parents: *[]}}
 */
function getAptInfo(){
    return $("#aptInfo").data();
}


function setWeatherIcon(data){

    var images = "";

    switch (data.sky + ""){
        // 맑음
        case "1": images = "../img/weather_01.png"; break;
        // 구름조금
        case "2" : images = "../img/weather_02.png"; break;
        // 구름 많음
        case "3" : images = "../img/weather_03.png"; break;
        // 흐림
        case "4" : images = "../img/weather_04.png"; break;
    }
    switch (data.rainOrSnow + "") {
        // 비
        case "1" :
            images = "../img/weather_05.png"; break;
            break;
        // 비 , 눈
        case "2" :
            images = "../img/weather_06.png"; break;
            break;
        // 눈
        case "3" :
            images = "../img/weather_07.png"; break;
            break;
        // 소나기
        case "4" :
            images = "../img/weather_05.png"; break;

    }
    return images;
}

/**
 * datePicker 공통 설정
 * @returns {{buttonImage: string, dateFormat: string, nextText: string, showButtonPanel: boolean, dayNamesShort: string[], prevText: string, currentText: string, changeMonth: boolean, showOn: string, showMonthAfterYear: boolean, closeText: string, dayNamesMin: string[], monthNamesShort: string[], monthNames: string[], yearSuffix: string, buttonImageOnly: boolean}}
 * @constructor
 */
function DATEPICKERDEFAULTOPTION() {
    return {
        dateFormat: 'yy-mm-dd',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년',
        showOn: "both",
        buttonImage: "../img/icon_calendar02.png",
        buttonImageOnly: true,
        showButtonPanel: true,
        changeMonth: true,
        closeText: "닫기",
        prevText: "이전달",
        nextText: "다음달",
        currentText: "오늘",
    };
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
