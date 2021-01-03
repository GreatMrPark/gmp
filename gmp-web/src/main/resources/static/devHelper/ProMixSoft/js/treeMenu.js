/*
 *  Copyright (c) 2019 KEPCO, Inc. All right reserved.
 *  This software is the confidential and proprietary information of KEPCO, Inc. You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with KEPCO.
 *
 *  Revision History
 *  Author Date Description
 *   ------------------ -------------- ------------------
 *   doson               2019.10.1
 *
 */

var gelixCommon = gelixCommon || {};

/**
 * TreeView 메뉴 모듈
 * created by doson  2019-05-30
 * @type {{treeviewRootUrl: string, treeviewSubUrl: string, displayTree: (function(*=): boolean), getSelectedNode: (function(*=): (boolean|jQuery|Mixed)), getCheckedNode: (function(*=): (boolean|jQuery|Mixed))}}
 */
gelixCommon.treeMenuArea = (function(){

    var baseUrl = CONTEXT_PATH + "/v1.0/common" ;     // 로컬

    var treeviewRootUrl = baseUrl + "/treeView";
    var treeviewSubUrl = baseUrl + "/treeView";

    /**
     * tree 메뉴 출력
     * @param treeOption
     */
    var displayTree = function (treeOption) {

/*        var treeViewAttr = {
            dong : treeOption.dong,
            ho : treeOption.ho,
            deviceType : treeOption.deviceType,
            area : treeOption.area,
            platform: treeOption.platform,
            smgwManufCode : treeOption.smgwManufCode,
            isCheckBox : false,
            depth : treeOption.depth,
            deviceViewType : typeof(treeOption.deviceViewType) === "undefined" ? "meter" : treeOption.deviceViewType
        };*/


        var isCheckBox = treeOption.isChecked == "true" ? true : false;

        var $jsTree = $(treeOption.displayObj);

        if($jsTree.jstree(true)) $jsTree.jstree("destroy");

        // 트리 생성
        var $treeView = $jsTree.jstree(
            {
                "plugins": [
                    "wholerow",
                    "checkbox",
                    "changed",
/*                    "state",*/
                    "themes",
                    "search",
/*                    "contextmenu",*/
                    "json_data"
                ],
                "state" : { "key" : (typeof treeOption.stateKey === "undefined") ? treeOption.container : treeOption.stateKey},
                "core": {
                    'data': {
                        'cache': false,
                        'method' : 'post',
                        'url': function (node) {
                            if(node.id === '#') {
                                return treeviewRootUrl;
                            } else {
                                //return treeviewSubUrl + node.li_attr.deviceType;
                                return treeviewSubUrl;
                            }
                        },
                        "contentType": "application/json",
                        "dataType": "json",
                        "data": function (node) {
                            var reqData;
                            if(node.id === '#') {
                                //reqData =  JSON.stringify(treeViewAttr);
                                reqData =  JSON.stringify(treeOption);
                            } else {
                                reqData = JSON.stringify(node.li_attr);
                            }
                            return reqData;

/*                            if (node.id !== "#") {
                                return {"id": node.li_attr.smgwId, "groupType": node.li_attr.groupType};
                            }*/
                        },
                        "success": function (node) {
                            return node;
                        }
                    },

                    "themes": {"name": "default"},
                    "check_callback": true,
                    "opened" : true,
/*                    "icon" : "../img/icon_tree_all.png",*/
                    "restore_focus" : false
                },
                "checkbox": {
                    "whole_node": false,
                    "visible": isCheckBox,
                    "cascade_to_disabled": false,
                    "tie_selection": false
                },
                "search": {
                    "case_insensitive": false,
                    "show_only_matches": true
                }
            }
        );

        // 검색시 없는 node hide
        $jsTree.on('search.jstree', function (nodes, str, res) {
            //console.log("search:" + nodes);
            if (str.nodes.length === 0) {
                $jsTree.jstree(true).hide_all();
            }
        }).jstree();

        // 트리 메뉴 클릭 이벤트
        if(typeof treeOption.onSelectFunction === "function"){
            $jsTree.on('changed.jstree', function (e, data) {
                // 선택 되었을때만 호출한다..
                if(data.action === "select_node") treeOption.onSelectFunction(data);
            }).jstree(true);
        }

        /** tree open event */
/*
        $jsTree.on('after_open.jstree', function (e, data) {
        });
*/

/*
        $jsTree.on('delete_node.jstree', function (e, data) {
        });
*/

/*
        $jsTree.on('redraw.jstree', function (e, data) {

        });
*/

        /** tree open event */
        /*        $jsTree.on('after_close.jstree', function(e, data){
                    isActiveRed();
                });*/

        /** 트리 체크 선택시 */
        if(typeof treeOption.onCheckNodeFunction === "function") {
            $jsTree.on('check_node.jstree', function (e, data) {
                treeOption.onCheckNodeFunction(data);
            }).jstree(true);
        }

        /** 체크 all */
        if(typeof treeOption.onCheckAllFunction === "function") {
            $jsTree.on('check_all.jstree', function (e, data) {
                treeOption.onCheckAllFunction(data);
            }).jstree(true);
        }

        /** 노드 uncheck */
        if(typeof treeOption.onUnCheckNodeFunction === "function") {
            $jsTree.on('uncheck_node.jstree', function (e, data) {
                treeOption.onUnCheckNodeFunction(data);
            }).jstree(true);
        }

        /** 노드 uncheck All */
        if(typeof treeOption.onUnCheckAllFunction === "function") {
            $jsTree.on('uncheck_all.jstree', function (e, data) {
                treeOption.onUnCheckAllFunction(data);
            }).jstree(true);
        }

        // tree 로딩 완료 이벤트
        if(typeof treeOption.onLoadCompleteFunction === "function") {
            $jsTree.on('loaded.jstree', function (e, data) {
                setTimeout(function (){
                    treeOption.onLoadCompleteFunction($treeView);
                },100)
            }).jstree(true);
        }

        // 검색 이벤트 등록
        if(typeof treeOption.searchTextObj !== "undefined" && $(treeOption.searchTextObj).length > 0){
            $(treeOption.searchTextObj).off().keyup(function(){
                $jsTree.jstree(true).show_all();
                $jsTree.jstree('search', $(this).val());
            });
        } else {
            console.log("검색 input id 가 없거나 Object 가 존재하지 않습니다.");
        }

    }

    /**
     * 선택된(클릭한) node 정보 return
     * @param treeObj
     * @returns {boolean|jQuery|Mixed}
     */
    var getSelectNode = function(treeObj){
        if(typeof treeObj === "undefined" || $(treeObj).length <= 0){
            console.error("displayObj 값이 없거나 object 가 없습니다.");
            return false;
        }
        return $(treeObj).jstree("get_selected", true);
    }

    /**
     * 체크된 node 정보 return
     * @param treeObj
     * @returns {boolean|jQuery|Mixed}
     */
    var getCheckedNode = function (treeObj){
        if(typeof treeObj === "undefined" || $(treeObj).length <= 0){
            console.error("displayObj 값이 없거나 object 가 없습니다.");
            return false;
        }
        return $(treeObj).jstree("get_checked", true);
    }

    return {
        treeviewRootUrl : treeviewRootUrl,
        treeviewSubUrl : treeviewSubUrl,

        displayTree : function (treeOption) {
            return displayTree(treeOption);
        },
        getSelectedNode : function (treeObj) {
            return getSelectNode(treeObj);
        },
        getCheckedNode : function (treeObj){
            return getCheckedNode(treeObj);
        }
    }

})();




