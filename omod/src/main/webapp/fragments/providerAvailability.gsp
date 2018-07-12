<style>
table.toggle tr th {
    background-color: #ACD244;
    text-align: left;
}

table.toggle tr td {
    text-align: left;
}

table.toggle tr:nth-child(even) {
    background-color: #E3E4FA;
}

table.toggle tr:nth-child(odd) {
    background-color: #FDEEF4;
}

.twptooltip {
    background: #c0c0c0;
}
</style>
<script type="text/javascript">
    function InitializeCalendar() {
        jQuery('#calendarBlocks').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay'
            },
            selectable: true,
            selectHelper: true,
            height: 650,
            select: function (start, end, allDay) {
                document.getElementById('action').value = "addNewAppointmentBlock";
                var startDate = new Date(start);
                var endDate = new Date(end);
                var currentDateTime = new Date();
                if (startDate.getTime() == endDate.getTime()) { //Month vi
                    currentDateTime.setHours(0, 0, 0, 0);
                }

                if (startDate.getTime() < currentDateTime.getTime()) { //Can't save blocks in the past
                    var dialogContent = "Partners cannot be scheduled in the past, please try a different day.";
                    document.getElementById("notifyDialogText").innerHTML = dialogContent;
                    jQuery('#notifyDialog').dialog('open');
                    event.stopPropagation();
                }
                else {
                    document.getElementById('fromDate').value = startDate.getTime();
                    document.getElementById('toDate').value = endDate.getTime();
                    document.forms['appointmentBlockCalendarForm'].submit();
                    this.fullCalendar('unselect');
                }
            },
            theme: true,

            eventMouseout: function (calEvent, jsEvent, view) {
                jQuery(this).css('border-color', '');
                jQuery(this).css('z-index', 8);
                jQuery('.tooltiptopicevent').remove();
            },
            viewDisplay: function (view) {
                updateAppointmentBlockCalendar(view.visStart, view.visEnd);
            },
            events: ${ events },
            editable: false,
            eventMouseover: function (data, event, view) {
                var start = jQuery.fullCalendar.moment(data.start);
                var end = jQuery.fullCalendar.moment(data.end);
                var duration = jQuery.fullCalendar.formatRange(start, end, 'h:mm');
                tooltip = '<div class="tooltiptopicevent twptooltip" style="width:auto;height:auto;background:#c0c0c0;position:absolute;z-index:10001;padding:10px 10px 10px 10px ;  line-height: 200%;font-size: 20px;">' + 'Client: ' + ': ' + data.client + '</br>' + 'Type : ' + data.type + '</br>' + 'Time: ' + ': ' + duration + '</br>' + 'Phone No. ' + ': ' + data.mobile + '</br>'+ '</div>';


                jQuery("body").append(tooltip);
                jQuery(this).mouseover(function (e) {
                    jQuery(this).css('z-index', 10000);
                    jQuery('.tooltiptopicevent').fadeIn('500');
                    jQuery('.tooltiptopicevent').fadeTo('10', 1.9);
                }).mousemove(function (e) {
                    jQuery('.tooltiptopicevent').css('top', e.pageY + 10);
                    jQuery('.tooltiptopicevent').css('left', e.pageX + 20);
                });
            }
        });
    }

    function chooseBetweenFullCalendarAndTableView() {
        var selectValue = jQuery("#viewSelect").val();
        if (selectValue === "tableView") {
            jQuery("#calendarBlocks").hide();
            jQuery("#actionButton").show();
            jQuery("#tableBlocks").show();
        }
        else if (selectValue === "calendarView") {
            jQuery("#tableBlocks").hide();
            jQuery("#actionButton").hide();
            jQuery("#calendarBlocks").show();

        }
    }

    function filterEvents() {
        var events = ${ events };
        var new_events = [];
        var selected_partner = jQuery('#partnerSelect').val();

        if(selected_partner == 0){
            new_events = ${ events };
        }else {
            console.log("not all");
            for (var i = 0; i < events.length; i++) {
                console.log("iteration " + i);
                console.log("Parner id " +  events[i].Partner);
                if (events[i].Partner == selected_partner) {
                    new_events.push(events[i]);
                    console.log("Added " + events[i]);
                }
            }
        }
        console.log(selected_partner);
        jQuery('#calendarBlocks').fullCalendar('removeEvents');
//        jQuery('#calendarBlocks').fullCalendar('removeEvents', function (event) {
//            return event.Partner !== selected_partner;
//        });
        for (var i = 0; i < new_events.length; i++) {
            console.log(new_events[i]);
            jQuery('#calendarBlocks').fullCalendar('renderEvent', new_events[i], true);
        }
    }

    function initializeDialog() {
        jQuery('#notifyDialog').dialog({
            autoOpen: false,
            height: 150,
            width: 350,
            modal: true,
            resizable: false
        });
    }

    function changeToTableView() { //A function that updates the action to change the view to table view and submits the form
        //change action to table view
        document.getElementById('action').value = "changeToTableView";
        //POST back in order to redirect to the table view via the controller.
        document.forms['appointmentBlockCalendarForm'].submit();
    }

    function refreshCalendar() {
        var calendar = InitializeCalendar();
        var calendarView = calendar.fullCalendar('getView');
        updateAppointmentBlockCalendar(calendarView.visStart, calendarView.visEnd);
    }

    function locationInitialize() {
        //If the user is using "Simple" version
        if (jQuery('#locationId').length > 0) {
            var selectLocation = jQuery('#locationId');
            //Set the 'All locations' option text (Default is empty string)
            if (selectLocation[0][0].innerHTML == '')
                selectLocation[0][0].innerHTML = "(<spring:message code='appointmentscheduling.AppointmentBlock.filters.locationNotSpecified'/>)";
        }
    }

    function viewChange() {
        //Check if change to table view is needed
        var selectedView = document.getElementById("viewSelect");
        return (selectedView.options[selectedView.selectedIndex].value == "tableView");
    }

    function updateAppointmentBlockCalendar(fromDate, toDate) {
        var calendarContent;
        var providerId = ${providerId};
        var appointmentTypeId = ${appointmentTypeId};
    }

    function showAddScheduleView() {
        ui.navigate('wellness', 'intake/appointmentBlock');
    }

    jQuery(function () {
        initializeDialog();
        document.getElementById("viewSelect").selectedIndex = 1;
        InitializeCalendar();
        refreshCalendar();
        locationInitialize();
        chooseBetweenFullCalendarAndTableView();

    });
</script>
<style>

</style>

<div class="ke-panel-frame">
    <div class="ke-panel-heading">Appointment scheduling</div>

    <div class="ke-page-content">
        <form method="post" name="appointmentBlockCalendarForm"
              action="">
            <fieldset id="propertiesFieldset" style="clear: both">
                <legend>Choose properties</legend>
                <table>
                    <tr>
                        <td>View type</td>
                        <td>
                            <select name="viewSelect" id="viewSelect">
                                <option value="tableView">Table View</option>
                                <option value="calendarView">Calendar View</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="button" class="appointmentBlockButton" value="Apply"
                                   onClick="chooseBetweenFullCalendarAndTableView()">
                        </td>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>Partner</td>
                        <td>
                            <select name="partnerSelect" id="partnerSelect">
                                <option value="0">All</option>
                                <% providerList.each { %>
                                <option value="${it.providerId}">${it.name}</option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="button" value="Filter" onclick="filterEvents()">
                        </td>
                    </tr>
                </table>

            </fieldset>
        </form>

        <form method="post" name="appointmentBlockCalendarForm">

            <input type="hidden" name="fromDate" id="fromDate" value="${fromDate}"/>
            <input type="hidden" name="toDate" id="toDate" value="${toDate}"/>
            <input type="hidden" name="appointmentBlockId" id="appointmentBlockId"/>
            <input type="hidden" name="action" id="action" value="addNewAppointmentBlock"/>
        </form>
        <br/>

        <div id="actionButton" style="display: none" align="right">
            <button type="button" id="addSchedule" onclick="showAddScheduleView()">
                <img src="${ui.resourceLink("kenyaui", "images/glyphs/ok.png")}"/> Add Schedule
            </button>
        </div>

        <div id="tableBlocks" style="display: none">
            <div class="ke-panel-frame">
                <div class="ke-panel-heading">Partner Availability</div>

                <div class="ke-page-content">
                    <table class="toggle" width="100%">
                        <tr>
                            <th>Partner</th>
                            <th>Start date</th>
                            <th>End date</th>
                            <th>Appointment type</th>
                            <th>Appointment type duration</th>
                        </tr>
                        <% providerSchedule.each { %>
                        <tr>
                            <td><a href="editAppointmentBlock.page?blockId=${it.appointmentBlockId}">${
                                    it.provider.name}</a></td>
                            <td>${it.startDate}</td>
                            <td>${it.endDate}</td>
                            <% it.types.each { %>
                            <td>${it.name}</td>
                            <td>${it.duration}</td>
                            <% } %>

                        </tr>
                        <% } %>
                    </table>
                </div>
            </div>
        </div>

        <div id='calendarBlocks'></div>

        <div id="notifyDialog" title="Warning"/>
        <table id='notifyDialogTable' class="dialogTable">
            <tr><td><div id="notifyDialogText"></div></td></tr>
        </table>
    </div>
</div>
</div>
