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
    ui.includeCss("wellness", "custom.css", 25)

%>
<script>
    function updateMaxStock() {
        console.log('clicked');
        var stock = ${itemStock};
        console.log(stock);
        var chosen = document.getElementById("item");
        console.log('passed');
        for (var i = 0; i < stock.length; i++) {
            var stockItem = stock[i];
            console.log('selected index ' + chosen.selectedIndex);
            if(i+1 === chosen.selectedIndex){
                console.log('clicked', stock);
                console.log('setting ' + stock[i].val);
                document.getElementById("quantity").setAttribute("max",stock[i].val)
            }

        }

    }
</script>
<div class="ke-panel-frame">
    <div class="ke-panel-heading">Dispense</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-6">
                <!-- start form -->

                <div class="card">

                    <div class="card-body">
                        <div id="item-details">
                            <div class="card-body">

                                <form action="${ui.actionLink("wellness", "inventory/inventoryDispense", "post")}"
                                      method="post"
                                      name="dispense">

                                    <div class="form-group">
                                        <label for="item"
                                               class=" form-control-label">Supplement to dispense</label>
                                        <select name="item" id="item" class="form-control"  onchange="updateMaxStock()" required>
                                            <option value="" disabled selected>Please select a product</option>
                                            <% inventoryItems.each { %>
                                            <option value="${it.id}">${it.name}</option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <input type="hidden" value="${client.id}" name="client">

                                    <div class="form-group">

                                        <label for="quantity" class="control-label mb-1">Quantity</label>
                                        <input id="quantity" name="quantity" type="number"
                                               class="form-control cc-exp"  data-val="true"
                                               placeholder="Quantity" required min="1" max="${initialMax}" step="1">
                                    </div>

                                    <div class="form-group">
                                        <label for="unit"
                                               class=" form-control-label">Unit</label>

                                        <select name="unit" id="unit" class="form-control" required>
                                            <option value="" disabled selected>Please select</option>
                                            <% itemUnits.each { %>
                                            <% if(it.id) { %>
                                            <option value="${it.id}">${it.name}</option>
                                            <% } %>
                                            <% } %>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label for="payment-mode"
                                               class=" form-control-label">Payment Method</label>

                                        <select name="payment-mode" id="payment-mode" class="form-control" required>
                                            <option value="" disabled selected>Select a payment method</option>
                                            <% paymentOptions.each { %>
                                            <option value="${it}">${it}</option>
                                            <% } %>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label mb-1">Walk in or delivery?</label>

                                        <div style="padding-left: 35px">
                                            <div class="form-check">
                                                <div class="radio">
                                                    <label for="radio1" class="form-check-label ">
                                                        <input type="radio" id="radio1" name="isDelivery" value="false"
                                                               checked class="form-check-input" >Walk In
                                                    </label>
                                                </div>

                                                <div class="radio">
                                                    <label for="radio2" class="form-check-label ">
                                                        <input type="radio" id="radio2" name="isDelivery" value="true"
                                                               class="form-check-input">To be delivered
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="address" class="control-label mb-1">Delivery Address</label>
                                        <input id="address" name="address" type="text" class="form-control"
                                               aria-required="true" ${delivery_addreess ? 'value="' + command.delivery_addreess + '"' : ''}>
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
<script>
    jQuery(document).ready(function () {
        jQuery(".standardSelect").chosen({
            disable_search_threshold: 10,
            no_results_text: "Oops, nothing found!",
            width: "100%"
        });
    });
</script>



