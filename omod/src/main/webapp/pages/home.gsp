<%
    ui.decorateWith("wellness", "standardPage", [patient: currentPatient, closeChartUrl: ui.pageLink("wellness", "home")])
    ui.includeCss("wellness", "card.css", 28)
%>
<div class="ke-page-content">
    <div class="container">
            <% apps.eachWithIndex { app, i ->
                def onClick = "ui.navigate('/" + contextPath + "/" + app.url + (currentPatient ? ("?patientId=" + currentPatient.id) : "") + "')"
                def iconTokens = app.icon.split(":")
                def iconProvider, icon
                if (iconTokens.length == 2) {
                    iconProvider = iconTokens[0]
                    icon = "images/" + iconTokens[1]
                }
            %>
            <div class="section style_prevu_kit">
                <button type="button" class="launch" onclick="${onClick}">
                    <img src="${ui.resourceLink(iconProvider, icon)}"/>
                    <span>${app.label}</span>
                </button>
            </div>
            <% } %>
    </div>
</div>
