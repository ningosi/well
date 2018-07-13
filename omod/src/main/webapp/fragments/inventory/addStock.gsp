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
<style>
.card {
    padding-left: 20px;
}
</style>

<div class="ke-panel-frame">
    <div class="ke-panel-heading">Add Stock</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-6">
                <!-- start form -->

                <div class="card">

                    <div class="card-body">
                        <div id="item-details">
                            <div class="card-body">
                                <div class="card-title">
                                    <h3 class="text-center">Item Details</h3>
                                </div>
                                <hr>

                                <form action="${ui.actionLink("wellness", "inventory/addStock", "post")}" method="post"
                                      novalidate="novalidate">
                                    <div class="form-group">
                                        <label for="name" class="control-label mb-1">Item Name</label>
                                        <input id="name" name="name" type="text" class="form-control"
                                               aria-required="true" aria-invalid="false">
                                    </div>

                                    <div class="form-group has-success">
                                        <label for="code" class="control-label mb-1">Item code</label>
                                        <input id="code" name="code" type="text"
                                               class="form-control cc-name valid" data-val="true"
                                               data-val-required="Please enter the name on card"
                                               aria-required="true">
                                    </div>

                                    <div class="form-group">
                                        <label for="description" class="control-label mb-1">Description</label>
                                        <input id="description" name="description" type="text"
                                               class="form-control cc-number identified visa" data-val="true"
                                               data-val-required="Please enter the card number"
                                               data-val-cc-number="Please enter a valid card number">
                                        <span class="help-block" data-valmsg-for="cc-number"
                                              data-valmsg-replace="true"></span>
                                    </div>

                                    <div class="form-group">
                                        <label for="type"
                                               class=" form-control-label">Item Type</label>

                                        <select name="type" id="type" class="form-control">
                                            <% itemTypes.each { %>
                                            <option value="${it.type_id}">${it.name}</option>
                                            <% } %>
                                        </select>
                                    </div>


                                    <div class="form-group">

                                        <div class="col-6">
                                            <div class="form-group">
                                                <label for="quantity" class="control-label mb-1">Quantity</label>
                                                <input id="quantity" name="quantity" type="number"
                                                       class="form-control cc-exp" value="" data-val="true"
                                                       data-val-required="Please enter the card expiration"
                                                       data-val-cc-exp="Please enter a valid month and year"
                                                       placeholder="Quantity" autocomplete="cc-exp">
                                                <span class="help-block" data-valmsg-for="cc-exp"
                                                      data-valmsg-replace="true"></span>
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
