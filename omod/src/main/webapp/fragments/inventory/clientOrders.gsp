<%
    ui.includeJavascript("wellness", "jquery-2.1.0.js", 30)
    ui.includeCss("wellness", "select2.min.css", 29)
    ui.includeCss("wellness", "normalize.css", 25)
    ui.includeCss("wellness", "animate.css", 28)
    ui.includeCss("wellness", "font-awesome.min.css", 27)
    ui.includeCss("wellness", "style2.css", 26)
    ui.includeCss("wellness", "style.css", 26)
    ui.includeCss("wellness", "bootstrap.min.css", 26)
    ui.includeCss("wellness", "chosen.min.css", 26)
    ui.includeCss("wellness", "dataTables.bootstrap.min.css", 25)

%>
<style>
.card {
    padding-left: 20px;
}
</style>

<div class="ke-panel-frame">
    <div class="ke-panel-heading">All Client Orders/Dispensations</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="content mt-3">
                <div class="animated fadeIn">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card">
                                <div class="card-header">
                                    <strong class="card-title">Orders</strong>
                                </div>

                                <div class="card-body">
                                    <table id="bootstrap-data-table" class="table table-striped table-bordered">
                                        <thead>
                                        <tr>
                                            <th>Client</th>
                                            <th>Supplement</th>
                                            <th>Quantity</th>
                                            <th>Order Date</th>
                                            <th>Dispensation Date</th>
                                            <th>Pick up mode</th>
                                            <th>Payment Mode</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <% clientOrders.each { %>
                                        <tr>
                                            <% if (it.client) { %>
                                            <td>${it.client.givenName} ${it.client.familyName}</td>
                                            <% } else { %>
                                            <td>Unknown</td>
                                            <% } %>
                                            <td>${it.inventoryItem.name}</td>
                                            <td>${it.quantity}</td>
                                            <td>${it.dateCreated}</td>
                                            <% if (it.deliveryDate) { %>
                                            <td>${it.deliveryDate}</td>
                                            <% } else { %>
                                            <td>${it.dateCreated}</td>
                                            <% } %>
                                            <% if (it.isDelivery) { %>
                                            <td>Delivery</td>
                                            <% } else { %>
                                            <td>Walk In</td>
                                            <% } %>
                                            <td>${it.paymentMode}</td>
                                        </tr>
                                        <% } %>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                    </div>
                </div><!-- .animated -->
            </div><!-- .content -->
        </div>
    </div>
</div>
<%
    ui.includeJavascript("wellness", "chosen.jquery.min.js")
%>
