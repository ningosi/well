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
    ui.includeCss("wellness", "css/modal.css", 25)

%>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Client Information</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-3 col-md-3">
                <div class="card">
                    <div class="card-body" style="text-align: center; padding-top: 15px">
                        <div class="mx-auto d-block">
                            <% if (url) { %>
                            <img class="img-circle mx-auto d-block" src="data:image/jpeg;base64,${url}"
                                 style="width: 150px; height: 150px;"/>
                            <% } else { %>
                            <img class="img-circle mx-auto d-block" src="${fakeUrl}"
                                 style="width: 150px; height: 150px;"/>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-4">
                <div class="card">
                    <div class="card-body">
                        <% if (name) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Name : ${name} ${second_name}</h5>
                        <% } %>
                        <% if (email) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Email : ${email}</h5>
                        <% } %>
                        <% if (mobileNumber) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Phone Number : ${mobileNumber}</h5>
                        <% } %>
                        <% if (partner) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Partner : ${partner}</h5>
                        <% } else { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Partner : Not assigned a patner</h5>
                        <% } %>
                        <% if (email) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Town : ${email}</h5>
                        <% } %>
                        <% if (home) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Home : ${home}</h5>
                        <% } %>
                        <% if (box) { %>
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text">Address : ${box}</h5>
                        <% } %>
                        <button id="dispenseModal">Previous Dispensations</button>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- The Modal -->
<div class="ke-panel-frame">
    <div id="myModal" class="modal">

        <!-- Modal content -->
        <div class="modal-content">

        <div class="ke-panel-frame">
            <span class="close">&times;</span>
            <div class="ke-panel-heading">All Orders/Dispensations</div>

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
        </div>
    </div>
</div>

<script>
    // Get the modal
    var modal = document.getElementById('myModal');

    // Get the button that opens the modal
    var btn = document.getElementById("dispenseModal");


    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks the button, open the modal
    btn.onclick = function () {
        modal.style.display = "block";
    };

    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    };

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };

    function closeModal() {
        console.log('close modal');
        modal.style.display = "none";
    }
</script>

<%
    ui.includeJavascript("wellness", "chosen.jquery.min.js")
%>
