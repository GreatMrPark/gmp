// --------------------------------------------------------------------------------
// 전역변수
// --------------------------------------------------------------------------------
gDate = new Date();

// --------------------------------------------------------------------------------
// fnInit
// --------------------------------------------------------------------------------
var fnInit = function() {
//    fnSetCurrentDate();
}

//--------------------------------------------------------------------------------
//fnSetCurrentDate
//--------------------------------------------------------------------------------
var fnSetCurrentDate = function() {
    setCurrentDate = setInterval(function() {
        var currentDate = new Date();
        $("#currentDate").html(currentDate.toLocaleString());
    }, 1000);
}