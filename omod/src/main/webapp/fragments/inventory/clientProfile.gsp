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
<%
    ui.includeJavascript("wellness", "chosen.jquery.min.js")
%>
