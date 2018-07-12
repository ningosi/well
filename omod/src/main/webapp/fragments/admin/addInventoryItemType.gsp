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
    <div class="ke-panel-heading">Add Item Type</div>

    <div class="ke-panel-content">
        <div class="row">
            <div class="col-lg-6">
                <!-- start form -->

                <div class="card">

                    <div class="card-body">
                        <div id="item-details">
                            <div class="card-body">
                                <div class="card-title">
                                    <h3 class="text-center">Item Type Details</h3>
                                </div>
                                <hr>

                                <form action="${ui.actionLink("wellness", "admin/addInventoryItemType", "post")}" method="post" novalidate="novalidate">
                                    <div class="form-group">
                                        <label for="name" class="control-label mb-1">Item Name</label>
                                        <input id="name" name="name" type="text" class="form-control"
                                               aria-required="true" aria-invalid="false">
                                    </div>

                                    <div class="form-group has-success">
                                        <label for="code" class="control-label mb-1">Description</label>
                                        <input id="code" name="code" type="text"
                                               class="form-control cc-name valid" data-val="true"
                                               data-val-required="Please enter the name on card"
                                               aria-required="true">
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
