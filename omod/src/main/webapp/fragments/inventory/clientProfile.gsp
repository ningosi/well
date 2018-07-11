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

%>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Client Information</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-3 col-md-3">
                <div class="card">
                    <div class="card-body" style="text-align: center;">
                        <div class="mx-auto d-block">
                            <% if (url) { %>
                            <img class="img-circle mx-auto d-block" src="data:image/jpeg;base64,${url}"
                                 style="width: 150px; height: 150px;"/>
                            <% } else { %>
                            <img class="img-circle mx-auto d-block" src="${fakeUrl}"
                                 style="width: 150px; height: 150px;"/>
                            <% } %>
                        </div>
                        <hr>
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
                        <h5 class="text-sm-center mt-2 mb-1 padding-5 text" >Phone Number : ${mobileNumber}</h5>
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

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<div class="ke-panel-frame" style="margin-top: 10px;">
    <div class="ke-panel-heading">Dispense</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-6">
                <!-- start form -->

                <div class="card">

                    <div class="card-body">
                        <div id="item-details">
                            <div class="card-body">
                                <form action="${ui.actionLink("wellness", "inventory/addStock", "post")}" method="post"
                                      novalidate="novalidate">
                                    <div class="form-group">
                                        <label for="address" class="control-label mb-1">Delivery Address</label>
                                        <input id="address" name="address" type="text"
                                               class="form-control cc-number identified visa" data-val="true">
                                    </div>

                                    <div class="row form-group">
                                        <div class="col col-md-3"><label for="type"
                                                                         class=" form-control-label">Supplement Item</label>
                                        </div>

                                        <div class="col-12 col-md-9">
                                            <select name="type" id="type" class="form-control">
                                                <% inventoryItems.each { %>
                                                <option value="${it.id}">${it.name}</option>
                                                <% } %>
                                            </select>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <div class="col-6">
                                            <div class="form-group">
                                                <label for="quantity" class="control-label mb-1">Quantity</label>
                                                <input id="quantity" name="quantity" type="tel"
                                                       class="form-control cc-exp"
                                                       value="" data-val="true"
                                                       data-val-required="Please enter the quantity"
                                                       data-val-cc-exp="Please enter a valid month and year"
                                                       placeholder="Quantity">
                                                <span class="help-block" data-valmsg-for="cc-exp"
                                                      data-valmsg-replace="true"></span>
                                            </div>
                                        </div>

                                        <div class="col-6">
                                            <label for="x_card_code" class="control-label mb-1">Unit</label>

                                            <div class="input-group">
                                                <input id="x_card_code" name="x_card_code" type="tel"
                                                       class="form-control cc-cvc" value="" data-val="true"
                                                       data-val-required="Please enter the security code"
                                                       data-val-cc-cvc="Please enter a valid security code"
                                                       autocomplete="off">

                                                <div class="input-group-addon">
                                                    <span class="fa fa-question-circle fa-lg" data-toggle="popover"
                                                          data-container="body" data-html="true"
                                                          data-title="Security Code"
                                                          data-content="<div class='text-center one-card'>The 3 digit code on back of the card..<div class='visa-mc-cvc-preview'></div></div>"
                                                          data-trigger="hover"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div>
                                        <button id="payment-button" type="submit" class="btn btn-lg btn-info btn-block">
                                            <span id="payment-button-amount">Submit</span>
                                            <span id="payment-button-sending" style="display:none;">Sending…</span>
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div> <!-- .card -->

            <!-- End form -->
            </div>
        </div>
    </div>
</div>
<%
    ui.includeJavascript("wellness", "chosen.jquery.min.js")
%>
