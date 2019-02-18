<%
    ui.decorateWith("wellness", "standardPage", [ layout: "sidebar" ])

    def menuItems = [
            [ label: "Create new client", extra: "Client can't be found", iconProvider: "kenyaui", icon: "buttons/patient_add.png", href: ui.pageLink("wellness", "registration/createPatient2") ],
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("wellness", "inventory/inventoryHome") ]
    ]
%>

<div class="ke-page-sidebar">
    ${ ui.includeFragment("wellness", "patient/patientSearchForm", [ defaultWhich: "all" ]) }

    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>

<div class="ke-page-content">
    ${ ui.includeFragment("wellness", "inventory/clientSearchResults", [ pageProvider: "wellness", page: "inventory/inventoryClientView" ]) }
</div>

<script type="text/javascript">
    jQuery(function() {
        jQuery('input[name="query"]').focus();
    });
</script>
