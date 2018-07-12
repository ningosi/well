<%
	ui.includeJavascript("wellness", "kenyaemr.js", 50)
    ui.includeCss("wellness", "kenyaemr.css", 50)
    ui.includeCss("wellness", "bootstrap.css", 31)
    ui.includeCss("wellness", "bootstrap.min.css", 30)
    ui.includeCss("wellness", "background.css", 29)

	if (config.patient) {
		config.context = "patientId=${ config.patient.id }"
	}

	config.beforeContent = ui.includeFragment("wellness", "header/pageHeader", config)

	config.beforeContent += ui.includeFragment("wellness", "header/headerMenu", config)

	if (config.patient) {
		config.beforeContent += ui.includeFragment("wellness", "header/patientHeader", [ patient: config.patient, closeChartUrl: config.closeChartUrl ])
	}
	if (config.visit) {
		config.beforeContent += ui.includeFragment("wellness", "header/visitHeader", [ visit: config.visit ])
	}

	config.pageTitle = "The Wellness"
	config.faviconIco = ui.resourceLink("wellness", "images/logos/favicon.ico")
	config.faviconPng = ui.resourceLink("wellness", "images/logos/favicon.png")
	config.angularApp = "wellness"

	ui.decorateWith("kenyaui", "standardPage", config)
%>

<!-- Override content layout from kenyaui based on the layout config value -->
<style>
    html{
        height: 100%;
    }
</style>
<style type="text/css">

<% if (config.layout == "sidebar") { %>
html {
	//background: #FFF url('${ ui.resourceLink("kenyaui", "images/background.png") }') repeat-y;
}
<% } %>

</style>

<%= config.content %>

<!-- Required for the wellness.ensureUserAuthenticated(...) method -->
<div id="authdialog" title="Login Required" style="display: none">
	<div class="ke-panel-content">
		<table border="0" align="center">
			<tr>
				<td colspan="2" style="text-align: center; padding-bottom: 12px">
					Your session has expired so please authenticate

					<div class="error" style="display: none;">Invalid username or password. Please try again.</div>
				</td>
			</tr>
			<tr>
				<td align="right"><b>Username:</b></td>
				<td><input type="text" id="authdialog-username"/></td>
			</tr>
			<tr>
				<td align="right"><b>Password:</b></td>
				<td><input type="password" id="authdialog-password"/></td>
			</tr>
		</table>
	</div>
	<div class="ke-panel-controls">
		<button type="button"><img src="${ ui.resourceLink("kenyaui", "images/glyphs/login.png") }" /> Login</button>
	</div>
</div>
