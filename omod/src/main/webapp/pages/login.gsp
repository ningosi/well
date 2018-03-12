<%
    ui.includeCss("wellness", "bootstrap.css", 31)
    ui.includeCss("wellness", "bootstrap.min.css", 30)
    ui.decorateWith("wellness", "standardPage")
    ui.includeCss("wellness", "normalize.css", 29)
    ui.includeCss("wellness", "animate.css", 28)
    ui.includeCss("wellness", "font-awesome.min.css", 27)
    ui.includeCss("wellness", "style2.css", 26)
    ui.includeCss("wellness", "login_style.css", 26)
%>
<style type="text/css">
body {
    background-color: #e0d8cd;
    height: 100%;
}

.ke-page-container {
    position: absolute;
    top: 100px;
    bottom: 0;
    width: 100%;
    background: #FFF url('${ ui.resourceLink("wellness", "images/logos/well_background.png") }') ;
    background-repeat: no-repeat;
    background-position: 0 0;
    background-size: cover;
}

.container {
    position: absolute;
    top: 30%;
    left: 0;
    right: 0;
}

.ke-toolbar {
    position: absolute;
    top: 0;
}

.ke-warning {
    position: absolute;
    top: 0;
}

.class-header {
    text-align: center;
    color: #000;
    font-size: 18px;
    text-transform: uppercase;
    margin-top: 0;
    margin-bottom: 20px;
}

</style>
<script type="text/javascript">
    jQuery(function () {
        var browser = kenyaui.getBrowser();
        var name = browser ? browser[0] : '?';

        if (jQuery.inArray(name, ['Chrome', 'Firefox']) < 0) {
            jQuery('#browser-warning').show();
        }
    });
</script>

<div id="browser-warning" class="ke-warning" style="display: none; text-align: center">
    Access from an unsupported browser detected. Please use <strong>Chrome</strong> or <strong>Firefox</strong> instead.
</div>

<div class="logo"></div>

<div class="login-block">
    <h1>Login</h1>
    <form method="post" action="${loginServletUrl}" autocomplete="off">
        <input type="text" value="" placeholder="Username" id="uname" name="uname"/>
        <input type="password" value="" placeholder="Password" id="pw" name="pw"/>
        <button type="submit">Submit</button>
    </form>
</div>

<div class="container mt-3">
    <div class="animated fadein">
        <!--
        <div class="row">
            <img src="${ui.resourceLink("wellness", "images/logos/logo2.png")}" width="" height="90px" style="padding: 10px;"/>
        </div>
        -->
        <!--
        <div class="row">
        <div class="col-lg-6" style="margin: auto">
            <div class="card">
                <div class="card-header">Log In</div>
                <div class="card-body card-block">
                    <form method="post" action="${loginServletUrl}" autocomplete="off">
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon"><i class="fa fa-user"></i></div>
                                <input type="text" id="uname" name="uname" placeholder="Username" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <div class="input-group-addon"><i class="fa fa-asterisk"></i></div>
                                <input type="password" id="pw" name="pw" placeholder="Password" class="form-control">
                            </div>
                        </div>
                        <div class="form-actions form-group"><button type="submit" class="btn btn-primary  btn-sm">Submit</button></div>
                    </>
                </div>
            </div>
        </div>
        </div>
    </div>
</div>
-->
        <!--
<div class="content mt-3" >
    <div class="animated fadeIn">
        <div class="row">
            <div class="col-lg-6" style="margin: auto">
                <div class="card-body card-block">
                    <form method="post" action="${loginServletUrl}" autocomplete="off">
                        <div class="row form-group">
                            <div class="col col-md-2"><label class=" form-control-label">Username</label>
                            </div>
                            <div class="col-6 col-md-5"><input type="text" id="uname" name="uname"
                                                                placeholder="Name" class="form-control">
                            </div>
                        </div>

                        <div class="row form-group">
                            <div class="col col-md-2"><label class=" form-control-label">Password</label>
                            </div>
                            <div class="col-6 col-md-5"><input type="password" name="pw"
                                                                placeholder="Name" class="form-control">
                            </div>
                        </div>

                        <div class="row form-group">
                            <div class="col col-md-2"></div>
                            <div class="col-6 col-md-5"><button type="submit" class="btn btn-primary btn-sm">
                                <i class="fa fa-dot-circle-o"></i> Submit
                            </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
-->
        <script type="text/javascript">
            jQuery(function () {
                jQuery('#uname').focus();
            });
        </script>
