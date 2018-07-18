<%
    ui.decorateWith("wellness", "standardPage", [layout: "sidebar"])

    def menuItems = [
            [label: "Item Units", iconProvider: "kenyaui", icon: "buttons/users_manage.png", href: ui.pageLink("wellness", "admin/addInventoryItemUnit")],
            [label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("wellness", "admin/adminHome")]
    ]
%>

<div class="ke-page-sidebar">
    ${ui.includeFragment("kenyaui", "widget/panelMenu", [heading: "Tasks", items: menuItems])}
</div>

<div class="ke-page-content">
    ${ui.includeFragment("wellness", "admin/addInventoryItemType")}
    ${ui.includeFragment("wellness", "admin/allInventoryItemTypes")}
</div>
