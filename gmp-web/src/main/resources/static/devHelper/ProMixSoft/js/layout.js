$(function(){
	//(s)공통 : GNB
	$("ul.nav>li").hover(function(){    
        $(this).addClass("hover");
        $('ul:first',this).css('display', 'block');    
    }, function(){    
        $(this).removeClass("hover");
        $('ul:first',this).css('display', 'none');    
    });
	//(e)공통 : GNB
	
	//전체메뉴보기 이외의 영역 클릭시 닫기
	/*$(document).mouseup(function (e){
		var container = $('.all_menu_wrap');
		if( container.has(e.target).length === 0){
			container.css('display','none');
		}
		$('.btn_all').toggleClass('off');		
	});*/


	//공통 : selectbox를 위한 selectric 플러그인
	/*if ( $('.sel').length ) { 	//컨텐츠의 기본 셀렉트박스
		$('.sel').selectric({
		   //inheritOriginalWidth: true, //텍스트 길이에 따라 selectbox의 길이가 가변적임
		});
	}*/
	
	//공통 : 탭메뉴
	$('.tabs li').on('click',function() {
		$(this).addClass('on').siblings().removeClass('on');
		var tab_idx = $(this).index();
		$(this).closest('.tabs_wrap').find('.tab_content').children('.tab_pane').eq(tab_idx).addClass("on").siblings().removeClass("on");		
	})

	//공통 : 아코디언(폴딩)
	$('.btn_folding').on('click',function() {		
		$(this).toggleClass("on");
		if ($(this).hasClass("on")) {
			$(this).attr("title","펼치기");
		} else {
			$(this).attr("title","닫기");
		}		
		//"folding_wrap"에 "up"클래스 추가하면 SMGW 디바이스 창의 사이즈가 길어진다.
		$(this)
			.closest('.folding_wrap')
			.toggleClass('up')
			//.find('.folding_ct')
			//.toggle()
		
		//예외처리 (같은 레벨의 위치에 버튼이 있을 경우, 컨텐츠와 같이 토글처리함.)
		//$(this).siblings('button').toggle();
	})

	// (s)checkbox 리스트 
	$('.check_grp_wrap .checkAll').click(function() {		
		var $t_name = $(this).closest('.check_grp_wrap');		
		var sub_chk = $t_name.find('.checkMe')
		
		if ($(this).is(":checked")) {
			$(sub_chk).prop('checked', true);			
		} else {
			$(sub_chk).prop('checked', false);			
		}
	})
	//테이블 리스트 중, 체크박스에 모두 체크된 경우, 전체선택도 체크, 하나이상 체크되지 않은경우 전체선택 해제
	$('.check_grp_wrap .checkMe').click(function() {		
		var $t_name = $(this).closest('.check_grp_wrap');
		var all_chk = $t_name.find('.checkAll');
		var cnt_chk = $t_name.find('.checkMe').length;
		var cnt_chked = $t_name.find('.checkMe:checked').length;
		
		if(cnt_chk == cnt_chked) {
			$(all_chk).prop('checked', true);
		} else {
			$(all_chk).prop('checked', false);
		}
	})
	
	//LNB 닫기, 펼치기 버튼
	$('.btn_tree').on('click',function() {
		$(this).toggleClass('on');
		$('.device_wrap').toggle();
		$('.content_wrap').toggleClass('on_tree');

		if ($(this).hasClass("on")) {
			$(this).attr("title","검색조건/대상선택 펼치기");
		} else {
			$(this).attr("title","검색조건/대상선택 닫기");
		}
	})


	//레이어팝업 dragable
    $('.popup_cont').draggable({handle: ".dragbar" });	
	
})

// 레이어팝업 띄우기
function popup_open(pop_nm, disableBodyScroll) {
	if ( disableBodyScroll === true ) {
		// 백그라운드 스크롤 disable
		$('body').off('wheel.modal mousewheel.modal');
		$('body').on('wheel.modal mousewheel.modal', function () { return false; });
		$('body').off('keydown');
		$('body').keydown(function(e) { if ( event.keyCode == 38 || event.keyCode == 40 ) { e.preventDefault(); } });
	}
	
    //레이어 팝업창을 중앙에 띄우기 위해서 너비 계산
    var pop_w = ($(window).width() - $(pop_nm).outerWidth() ) / 2;
	var pop_h = ($(window).height() - $(pop_nm).outerHeight() ) / 2;	
	
	pop_h = pop_h > 0 ? pop_h : 0;
    $(pop_nm).css({'top':$(document).scrollTop()+pop_h,'left':pop_w});	//스크롤 된 현재 위치에서 레이어 팝업이 보이도록 하기위해 scrollTop값 을 더한다

    //레이어팝업 띄둘때, 페이지 disable시키기
	if (!$('div').hasClass('bg_dark')) {
		$("body").append('<div class="bg_dark"></div>');
	}	
	$('.bg_dark').fadeIn();			
	if ($(pop_nm).find('.dragbar').length == 0) {
	    $(pop_nm).append('<div class="dragbar"></div>');
	}
    $(pop_nm).show();
}

// 레이어팝업 띄우기
function popup_fixed_open(pop_nm, disableBodyScroll, pop_h, pop_w, popid) {
    if ( disableBodyScroll === true ) {
        // 백그라운드 스크롤 disable
        $('body').off('wheel.modal mousewheel.modal');
        $('body').on('wheel.modal mousewheel.modal', function () { return false; });
        $('body').off('keydown');
        $('body').keydown(function(e) { if ( event.keyCode == 38 || event.keyCode == 40 ) { e.preventDefault(); } });
    }

    pop_h = pop_h > 0 ? pop_h : 0; 
    $(pop_nm).css({'top': pop_h, 'left': pop_w});

    //레이어팝업 띄둘때, 페이지 disable시키기
    if (!$('div').hasClass('bg_dark')) {
        $("body").append('<div class="bg_dark"></div>');
    }
    $('.bg_dark').fadeIn();    
	if ($(pop_nm).find('.dragbar').length == 0) {
   	    $(pop_nm).append('<div class="dragbar"></div>');
   	}
    $(pop_nm).show();
}

// 레이어팝업 닫기 (수정 : 2019-01)
function popup_close(pop_nm,disableBodyScroll){
	if ( disableBodyScroll === true ) {
		// 백그라운드 스크롤 enable
		$('body').off('wheel.modal mousewheel.modal');
		$('body').off('keydown');
	}
	if(!pop_nm) {
		pop_nm = '.popup_cont'
	}  
	console.log(pop_nm);
	$(pop_nm).hide();	
	
	if ($(".popup_cont:visible").length ==0 ) {
		$('.bg_dark').fadeOut();
	}
}
