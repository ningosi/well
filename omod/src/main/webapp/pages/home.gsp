<%
    ui.decorateWith("wellness", "standardPage", [patient: currentPatient, closeChartUrl: ui.pageLink("wellness", "home")])
%>
<div class="ke-page-content">
    <div class="container mt-3">
        <div class="row">
            <% apps.eachWithIndex { app, i ->
                def onClick = "ui.navigate('/" + contextPath + "/" + app.url + (currentPatient ? ("?patientId=" + currentPatient.id) : "") + "')"
                def iconTokens = app.icon.split(":")
                def iconProvider, icon
                if (iconTokens.length == 2) {
                    iconProvider = iconTokens[0]
                    icon = "images/" + iconTokens[1]
                }
            %>
            <div class="col-4 col-md-3">
                <div style="float: left; margin: 5px;">
                    <button type="button" class="ke-app" onclick="${onClick}"><img
                            src="${ui.resourceLink(iconProvider, icon)}"/>${app.label}</button>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>
