<%
    ui.decorateWith("wellness", "standardPage", [ layout: "sidebar" ])
    ui.includeCss("wellness", "background.css", 29)
    def menuItems = [
            [ label: "Back to home", iconProvider: "kenyaui", icon: "buttons/back.png", label: "Back to home", href: ui.pageLink("wellness", "registration/registrationHome") ]
    ]

%>
<div class="ke-page-sidebar">
    ${ ui.includeFragment("wellness", "patient/patientSearchForm", [ defaultWhich: "all" ]) }
    ${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Tasks", items: menuItems ]) }
</div>
<div class="ke-page-content">
    ${ ui.includeFragment("wellness", "patient/patientSearchResults", [ pageProvider: "wellness", page: "registration/beforeUpload" ]) }
</div>

<script type="text/javascript">
    jQuery(function() {
        jQuery('input[name="query"]').focus();
    });
</script>
