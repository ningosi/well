<%
    ui.decorateWith("wellness", "standardPage", [layout: "sidebar"])

    def menuItems = [
            [label: "Add Supplement", iconProvider: "wellness", icon: "buttons/notepad.svg", href: ui.pageLink("wellness", "inventory/addStock")],
            [label: "Create Order", iconProvider: "wellness", icon: "buttons/notepad.svg", href: ui.pageLink("wellness", "inventory/createOrderHome")],
            [label: "Inventory stock", iconProvider: "wellness", icon: "buttons/list.svg", href: ui.pageLink("wellness", "inventory/inventoryList")],
            [label: "All Orders ", iconProvider: "wellness", icon: "buttons/orders.svg", href: ui.pageLink("wellness", "inventory/ordersList")]
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
