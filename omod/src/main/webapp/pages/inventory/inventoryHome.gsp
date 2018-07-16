<%
    ui.decorateWith("wellness", "standardPage", [layout: "sidebar"])
    ui.includeCss("wellness", "AppointmentBlockStyle.css", 31)
    ui.includeCss("wellness", "fullcalendar.min.css", 30)
    ui.includeCss("wellness", "opentip.css", 29)
    ui.includeJavascript("wellness", "moment.min.js", 20)
    ui.includeJavascript("wellness", "fullcalendar.min.js", 19)
    ui.includeJavascript("wellness", "controllers/appointments.js")

    def menuItems = [
            [label: "Dispensation", iconProvider: "wellness", icon: "buttons/notepad.svg", href: ui.pageLink("wellness", "inventory/dispensationHome")],
            [label: "Stock taking", iconProvider: "wellness", icon: "buttons/notepad.svg", href: ui.pageLink("wellness", "inventory/inventoryList")],
            [label: "Inventory list", iconProvider: "wellness", icon: "buttons/list.svg", href: ui.pageLink("wellness", "inventory/inventoryList")],
            [label: "All Orders ", iconProvider: "wellness", icon: "buttons/orders.svg", href: ui.pageLink("wellness", "inventory/inventoryList")]
    ]
%>
<style>
img {
    height: 28px;
    width: 28px;
    margin-left: 10px;
}

.ke-menu-item {
    padding-top: 10px;
    padding-bottom: 10px;
}
</style>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Tasks", items: menuItems])}
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("wellness", "inventory/addStock") }
</div>

<script type="text/javascript">
    ${ui.includeJavascript("wellness", "chosen.jquery.min.js")}
    jQuery(function () {
        jQuery('input[name="query"]').focus();
    });
</script>
