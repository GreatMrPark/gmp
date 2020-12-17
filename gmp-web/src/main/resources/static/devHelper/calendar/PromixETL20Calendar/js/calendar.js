//--------------------------------------------------------------------------------
// 공통 변수
//--------------------------------------------------------------------------------
var gThisYear = new Date().getFullYear();
var gCurrentYear = new Date().getFullYear();
var gToday = shiftDate(new Date(), 0, 0, 0,  "-");
var gDayDiff = -10;
var gDecimalPoint = 1;
var gDataSource = {};

//--------------------------------------------------------------------------------
//fnSetYear
//--------------------------------------------------------------------------------
var fnSetYear = function(currentYear) {
    gCurrentYear = currentYear;
    $('#thisYear').html(currentYear);
}

//--------------------------------------------------------------------------------
// 초기화
//--------------------------------------------------------------------------------
$( document ).ready(function() {
    $('#calendar').calendar();
    $('#thisYear').html(gThisYear);
});

//--------------------------------------------------------------------------------
//fnInitDataSource
//--------------------------------------------------------------------------------
var fnInitDataSource = function() {
    $('#calendar').data('calendar').setDataSource(null);
}

//--------------------------------------------------------------------------------
//fnGetDataSource
//--------------------------------------------------------------------------------
var fnGetDataSource = function(currentYear) {
    return gDataSource[currentYear];
}

//--------------------------------------------------------------------------------
//fnSetDataSource
//--------------------------------------------------------------------------------
var fnSetDataSource = function(currentYear) {
    var aName = ["조이사","박이사","강이사","윤과장","Promix"];
    var aLocation = ["서울","대구","부산","대전","인천","제주","울산"];
    var dataSource = $('#calendar').data('calendar').getDataSource();
    var id = 0;
    var month = 0;
    var day = 1;

    if (fnIsEmpty(currentYear)===true) {
        currentYear = gThisYear;
    }
    for(var month = 0; month < 12; month++) {
        for (var day = 1; day < 20; day++) {

            var event = {
                id: id,
                name: aName[0],
                location: aLocation[0],
                startDate: new Date(currentYear, month, day),
                endDate: new Date(currentYear, month, day)
            }
            dataSource.push(event);            
            id++;
        }
    }

    gDataSource[currentYear] = dataSource;
        
    $('#calendar').data('calendar').setDataSource(dataSource);
}

//--------------------------------------------------------------------------------
//fnRenderEnd
//--------------------------------------------------------------------------------
var fnRenderEnd = function(e) {
    console.log(e.currentYear);
    fnSetYear(e.currentYear);
}

//--------------------------------------------------------------------------------
//editEvent
//--------------------------------------------------------------------------------
function editEvent(event) {
    $('#event-modal input[name="event-index"]').val(event ? event.id : '');
    $('#event-modal input[name="event-name"]').val(event ? event.name : '');
    $('#event-modal input[name="event-location"]').val(event ? event.location : '');
    $('#event-modal input[name="event-start-date"]').datepicker('update', event ? event.startDate : '');
    $('#event-modal input[name="event-end-date"]').datepicker('update', event ? event.endDate : '');
    $('#event-modal').modal();
}

//--------------------------------------------------------------------------------
//deleteEvent
//--------------------------------------------------------------------------------
function deleteEvent(event) {   
    
    if(confirm("삭제하시겠습니까?" + "\n"+JSON.stringify(event))===false){
        return false;
    };

    var dataSource = $('#calendar').data('calendar').getDataSource();

    for(var i in dataSource) {
        if(dataSource[i].id == event.id) {
            dataSource.splice(i, 1);
            break;
        }
    }
    
    $('#calendar').data('calendar').setDataSource(dataSource);
}

//--------------------------------------------------------------------------------
//saveEvent
//--------------------------------------------------------------------------------
function saveEvent() {
    var event = {
        id: $('#event-modal input[name="event-index"]').val(),
        name: $('#event-modal input[name="event-name"]').val(),
        location: $('#event-modal input[name="event-location"]').val(),
        startDate: $('#event-modal input[name="event-start-date"]').datepicker('getDate'),
        endDate: $('#event-modal input[name="event-end-date"]').datepicker('getDate')
    }

    if(confirm("저장하시겠습니까?" + "\n"+JSON.stringify(event))===false){
        return false;
    };
    
    var dataSource = $('#calendar').data('calendar').getDataSource();

    if(event.id) {
        for(var i in dataSource) {
            if(dataSource[i].id == event.id) {
                dataSource[i].name = event.name;
                dataSource[i].location = event.location;
                dataSource[i].startDate = event.startDate;
                dataSource[i].endDate = event.endDate;
            }
        }
    }
    else
    {
        var newId = 0;
        for(var i in dataSource) {
            if(dataSource[i].id > newId) {
                newId = dataSource[i].id;
            }
        }
        
        newId++;
        event.id = newId;
    
        dataSource.push(event);
    }
    
    $('#calendar').data('calendar').setDataSource(dataSource);
    $('#event-modal').modal('hide');
}

//--------------------------------------------------------------------------------
// calendar set
//--------------------------------------------------------------------------------
$(function() {
    
    var currentYear = new Date().getFullYear();
    
    $('#calendar').calendar({ 
//        style:'background',
        language: 'ko',
        enableContextMenu: true,
        enableRangeSelection: true,
        contextMenuItems:[
            {
                text: 'Update',
                click: editEvent
            },
            {
                text: 'Delete',
                click: deleteEvent
            }
        ],
        selectRange: function(e) {
            editEvent({ startDate: e.startDate, endDate: e.endDate });
        },
        mouseOnDay: function(e) {
            if(e.events.length > 0) {
                var content = '';
                
                for(var i in e.events) {
                    content += '<div class="event-tooltip-content">'
                                    + '<div class="event-name" style="color:' + e.events[i].color + '">' + e.events[i].name + '</div>'
                                    + '<div class="event-location">' + e.events[i].location + '</div>'
                                + '</div>';
                }
            
                $(e.element).popover({ 
                    trigger: 'manual',
                    container: 'body',
                    html:true,
                    content: content
                });
                
                $(e.element).popover('show');
            }
        },
        mouseOutDay: function(e) {
            if(e.events.length > 0) {
                $(e.element).popover('hide');
            }
        },
        dayContextMenu: function(e) {
            $(e.element).popover('hide');
        },
        renderEnd: function(e) {
            fnRenderEnd(e);
        },
        dataSource: [
            {
                id: 0,
                name: 'Google I/O',
                location: 'San Francisco, CA',
                startDate: new Date(currentYear, 4, 28),
                endDate: new Date(currentYear, 4, 29)
            },
            {
                id: 1,
                name: 'Microsoft Convergence',
                location: 'New Orleans, LA',
                startDate: new Date(currentYear, 2, 16),
                endDate: new Date(currentYear, 2, 19)
            },
            {
                id: 2,
                name: 'Microsoft Build Developer Conference',
                location: 'San Francisco, CA',
                startDate: new Date(currentYear, 3, 29),
                endDate: new Date(currentYear, 4, 1)
            },
            {
                id: 3,
                name: 'Apple Special Event',
                location: 'San Francisco, CA',
                startDate: new Date(currentYear, 8, 1),
                endDate: new Date(currentYear, 8, 1)
            },
            {
                id: 4,
                name: 'Apple Keynote',
                location: 'San Francisco, CA',
                startDate: new Date(currentYear, 8, 9),
                endDate: new Date(currentYear, 8, 9)
            },
            {
                id: 5,
                name: 'Chrome Developer Summit',
                location: 'Mountain View, CA',
                startDate: new Date(currentYear, 10, 17),
                endDate: new Date(currentYear, 10, 18)
            },
            {
                id: 6,
                name: 'F8 2015',
                location: 'San Francisco, CA',
                startDate: new Date(currentYear, 2, 25),
                endDate: new Date(currentYear, 2, 26)
            },
            {
                id: 7,
                name: 'Yahoo Mobile Developer Conference',
                location: 'New York',
                startDate: new Date(currentYear, 7, 25),
                endDate: new Date(currentYear, 7, 26)
            },
            {
                id: 8,
                name: 'Android Developer Conference',
                location: 'Santa Clara, CA',
                startDate: new Date(currentYear, 11, 1),
                endDate: new Date(currentYear, 11, 4)
            },
            {
                id: 9,
                name: 'LA Tech Summit',
                location: 'Los Angeles, CA',
                startDate: new Date(currentYear, 10, 17),
                endDate: new Date(currentYear, 10, 17)
            }
        ]
    });

    gDataSource[currentYear] = $('#calendar').data('calendar').getDataSource();
    
    /**
     * data get
     */
    fnGetDataSource();
    
    /**
     * data init
     */
    fnInitDataSource();
    
    /**
     * data set
     */
    fnSetDataSource( $('#calendar').data('calendar').getYear() );
    
    /**
     * 저장
     */
    $('#save-event').click(function() {
        saveEvent();
    });
});