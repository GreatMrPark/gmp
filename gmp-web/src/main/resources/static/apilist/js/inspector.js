// 뒤로
$("#btnBack").on("click", function() {
	iFrameInspect.history.back();
});

// 앞으로
$("#btnForward").on("click", function() {
	iFrameInspect.history.forward();
});

// DOM 선택
$("#btnInspect").on("click", function() {
    domInspector(function(obj) {
    	var html = obj.outerHTML;
        $('#responseResult').val(html);
    });
});

// DOM 선택 로직
var domInspector = function(cb) {

	var body =  $("#iFrameInspect").contents().find("body");
    
	iFrameInspect.document.body.style.cursor = "crosshair";
    var style = iFrameInspect.document.createElement('style');
    style.type = 'text/css';
    style.innerHTML = '.overTargetClass { border: 2px solid red !important; }';
  
    iFrameInspect.document.getElementsByTagName('head')[0].appendChild(style);
  
    var targets = "*";
    var readyClick = false;

//    $("#iFrameInspect").contents().find("body").on("mouseenter", targets, activeFocus).on("mouseleave", targets, deactiveFocus).on("click", targets, onclick);
    body.on("mouseenter", targets, activeFocus).on("mouseleave", targets, deactiveFocus).on("click", targets, onclick);

    function onclick(e) {
        e.preventDefault();
        e.stopPropagation();
    
        if(readyClick) {
        	iFrameInspect.document.body.style.cursor = 'default';
            deactiveFocus.call(this, e);
//            $("#iFrameInspect").contents().find("body").off("mouseenter", activeFocus).off("mouseleave", deactiveFocus).off("click", onclick);
            body.off("mouseenter", activeFocus).off("mouseleave", deactiveFocus).off("click", onclick);
            cb(this);
            return;
        }
        readyClick = true;
    }

    function activeFocus(e) {
        if(e)
            e.stopPropagation();
        
        var $this = $(this);
        $this.addClass("overTargetClass");
    
        var clickDefine = $this.attr("onclick");  //하드코딩된 onclick 이벤트는 잠시 다른이름으로 변경
        if(clickDefine) {
            $this.attr("data-org-click", clickDefine);
            $this.removeAttr("onclick");
        }

        $this.parents(targets).each(function() {
            deactiveFocus.call(this);
        });
    }

    function deactiveFocus(e) {
        if(e)
            e.stopPropagation();

        var $this = $(this);
        $this.removeClass("overTargetClass");

        var clickDefine = $this.attr("data-org-click");  //이름을 바꿔놨던 onclick 이벤트 복구
        if(clickDefine) {
            $this.attr("onclick", clickDefine);
            $this.removeAttr("data-org-click");
        }
    }
}