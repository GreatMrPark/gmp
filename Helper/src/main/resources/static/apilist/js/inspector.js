// 마우스 바꾸기
document.doby.style.cursor = "crosshair";

// 새로운 클래스 추가하기
var style = document.createElement('style');
style.type = 'text/css';
style.innerHTML = '.overTargetClass { border: 2px sold red !important; }';

document.getElementsByTageName('head')[0].appendChild(style);

// 이벤트 델리게이션 패턴
var targets = "*";
$("body").on("mouseenter", targets, activeFocus)
         .on("mouseleave", targets, deactiveFocus)
         .on("click", targets, onclick)

// mouseenter(mouseover) 이벤트
function activeFocus(e) {
	if(e) {
		e.stopPropagation();
	}
	var $this = $(this);
	$this.addClass("overTargetClass");
	var clickDefine = $this.attr("onclick");
	if(clickDefine) {
		$this.attr("data-org-click", clickDefine);
		$this.removeAttr("onclick");
	}
	$this.parents(targets).each(function()) {
		deactiveFocus.call(this);
	}
}   

// mouseenter(mouseover) 이벤트
function deactiveFocus(e) {
	if (e) {
		e.stopPropagation();
	}
	var $this = $(this);
	$this.removeClass("overTargetClass");
	var clickDefine = $this.attr("data-prg-click");
	if(clickDefine) {
		$this.attr("onclick", clickDefine);
		$this.removeAttr("data-org-click");
	}	
}

// click 이벤트
function onclick(e) {
	e.preventDefault();
	e.stopPropagation();
	
	if (readyClick) {
		document.body.style.cursor = 'default';
		deactiveFocus.call(this, e);
		var targets = "*";
		$("body").off("mouseenter", targets, activeFocus)
		         .off("mouseleave", targets, deactiveFocus)
		         .off("click", targets, onclick)
		cb(this);
		return;
	}
	readyClick = true;
}
         
