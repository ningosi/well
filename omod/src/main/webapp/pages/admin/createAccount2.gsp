<%
	ui.decorateWith("wellness", "standardPage", [ layout: "sidebar" ])

	def menuItems = [
			[ label: "Back to previous step", iconProvider: "kenyaui", icon: "buttons/back.png", href: ui.pageLink("wellness", "admin/createAccount1") ]
	]
%>

<div class="ke-page-sidebar">
	${ ui.includeFragment("kenyaui", "widget/panelMenu", [ heading: "Create Account", items: menuItems ]) }

	<div class="ke-panel-frame">
		<div class="ke-panel-heading">Help</div>
		<div class="ke-panel-content">
			If the account is for a user who will login into the system, you must specify login details. If the account
			is for someone who can provide care to patients, then you must specify a partner ID.
		</div>
	</div>
</div>

<div class="ke-page-content">
	${ ui.includeFragment("wellness", "account/newAccount", [ person: patient ]) }
</div>
