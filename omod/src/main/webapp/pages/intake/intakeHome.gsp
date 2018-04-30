<%
	ui.decorateWith("wellness", "standardPage", [ layout: "sidebar" ])
    ui.includeCss("wellness", "AppointmentBlockStyle.css", 31)
    ui.includeCss("wellness", "fullcalendar.min.css", 30)
    ui.includeCss("wellness", "opentip.css", 29)
    ui.includeJavascript("wellness", "moment.min.js", 20)
    ui.includeJavascript("wellness", "fullcalendar.min.js", 19)
    ui.includeJavascript("wellness", "controllers/appointments.js")

	def menuItems = [
			[ label: "Partner scheduling", iconProvider: "wellness", icon: "buttons/providers.png", href: ui.pageLink("wellness", "intake/providerAvailability") ],
            [ label: "Appointment types", iconProvider: "wellness", icon: "buttons/appointmenttype.png", href: ui.pageLink("wellness", "intake/appointmentTypes") ],
			[ label: "Manage Appointments", iconProvider: "wellness", icon: "buttons/manage.png", href: ui.pageLink("wellness", "intake/manageAppointments") ]
				]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
	${ ui.includeFragment("wellness", "providerAvailability") }
</div>

<script type="text/javascript">
	jQuery(function() {
		jQuery('input[name="query"]').focus();
	});
</script>
