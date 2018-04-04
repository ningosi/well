<%
    ui.decorateWith("kenyaui", "panel", [heading: (config.heading ?: "Edit Patient"), frameOnly: true])
    ui.includeJavascript("wellness", "jquery-2.1.0.js", 30)
    ui.includeJavascript("wellness", "select2.min.css", 31)
    ui.includeCss("wellness", "select2.min.css", 29)
    ui.includeCss("wellness", "normalize.css", 25)
    ui.includeCss("wellness", "animate.css", 28)
    ui.includeCss("wellness", "font-awesome.min.css", 27)
    ui.includeCss("wellness", "style2.css", 26)

    def nameFields = [
            [
                    [object: command, property: "personName.givenName", label: "First Name *"],
                    [object: command, property: "personName.middleName", label: "Middle Name"],
                    [object: command, property: "personName.familyName", label: "Last Name *"]
            ],
    ]

    def addressFieldRows = [
            [
                    [object: command, property: "personAddress.address1", label: "Postal Address", config: [size: 60]],
                    [object: command, property: "personAddress.cityVillage", label: "Town/City"]


            ],
            [
                    [object: command, property: "personAddress.address2", label: "Delivery Address"],
                    [object: command, property: "personAddress.address3", label: "Email Address", config: [size: 100]],
            ]
    ]
    def myWellness = [
            [
                [object: command, property: "wellness", label: "My Wellness" , config: [style: "list", options : wellnesAnswers ]]
            ]
    ]
    def vitalsFields = [
            [
                    [object: command, property: "height", label: "Height" ],
                    [object: command, property: "weight", label: "Weight" ],
                    [object: command, property: "goal_weight", label: "Goal Weight" ],
                    [object: command, property: "systolic", label: "Systolic BP" ],
                    [object: command, property: "diastolic", label: "Diastolic BP" ]
            ],
    ]
    def weightBands = [
            [
                    [object: command, property: "weightBand", label: "Weight Band" , config: [style: "list", options : weightBandAnswers ]]
            ]
    ]
%>

<form id="edit-patient-form" method="post" action="${ui.actionLink("wellness", "patient/editPatient", "savePatient")}">
    <% if (command.original) { %>
    <input type="hidden" name="personId" value="${command.original.id}"/>
    <% } %>

    <div class="ke-panel-content">

        <div class="ke-form-globalerrors" style="display: none"></div>
        <fieldset>
            <p>
                After successful saving the client demographic details, please remember to upload their passport photograph at registration>>passport photo upload. Please find other photo options there.
            </p>
        </fieldset>
        <fieldset>
            <legend>Identifiers</legend>

            <table width="100%">
                <tr>
                    <td>
                        <table>
                        <% if (command.inNutritionProgram) { %>
                        <tr>
                            <td class="ke-field-label">Nutrition Number</td>
                            <td>${
                                    ui.includeFragment("kenyaui", "widget/field", [object: command, property: "uniquePatientNumber"])}</td>
                            <td class="ke-field-instructions">(Nutrition program<% if (!command.uniquePatientNumber) { %>, if assigned<%
                                    } %>)</td>
                        </tr>
                        <% } %>
                            <tr>
                                <td class="ke-field-label">Account Number</td>
                                <td>${ui.includeFragment("kenyaui", "widget/field", [object: command, property: "patientClinicNumber"])}</td>
                                <td class="ke-field-instructions"><% if (!command.patientClinicNumber) { %>(if available)<%
                                    } %></td>
                            </tr>
                            <tr>
                                <td class="ke-field-label">ID Number</td>
                                <td>${ui.includeFragment("kenyaui", "widget/field", [object: command, property: "nationalIdNumber"])}</td>
                                <td class="ke-field-instructions"><% if (!command.nationalIdNumber) { %>(if available)<% } %></td>
                            </tr>

                            <tr>
                                <td class="ke-field-label">Passport Number</td>
                                <td>${ui.includeFragment("kenyaui", "widget/field", [object: command, property: "passportNumber"])}</td>
                                <td class="ke-field-instructions"><% if (!command.passportNumber) { %>(if available)<% } %></td>
                            </tr>
                            <tr>
                                <td class="ke-field-label">Mobile Number</td>
                                <td>${ui.includeFragment("kenyaui", "widget/field", [object: command, property: "mobileNumber"])}</td>
                                <td class="ke-field-instructions"><% if (!command.mobileNumber) { %>(required)<% } %></td>
                            </tr>
                            <tr>
                                <td class="ke-field-label">Other Number</td>
                                <td>${ui.includeFragment("kenyaui", "widget/field", [object: command, property: "otherNumber"])}</td>
                                <td class="ke-field-instructions"><% if (!command.otherNumber) { %>(if available)<% } %></td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>

        </fieldset>

        <fieldset>
            <legend>Demographics</legend>

            <% nameFields.each { %>
            ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
            <% } %>

            <table>
                <tr>
                    <td valign="top">
                        <label class="ke-field-label">Sex *</label>
                        <span class="ke-field-content">
                            <input type="radio" name="gender" value="F"
                                   id="gender-F" ${command.gender == 'F' ? 'checked="checked"' : ''}/> Female
                            <input type="radio" name="gender" value="M"
                                   id="gender-M" ${command.gender == 'M' ? 'checked="checked"' : ''}/> Male
                            <span id="gender-F-error" class="error" style="display: none"></span>
                            <span id="gender-M-error" class="error" style="display: none"></span>
                        </span>
                    </td>
                    <td valign="top"></td>
                    <td valign="top">
                        <label class="ke-field-label">Birthdate *</label>
                        <span class="ke-field-content">
                            ${ui.includeFragment("kenyaui", "widget/field", [id: "patient-birthdate", object: command, property: "birthdate"])}

                            <span id="patient-birthdate-estimated">
                                <input type="radio" name="birthdateEstimated"
                                       value="true" ${command.birthdateEstimated ? 'checked="checked"' : ''}/> Estimated
                                <input type="radio" name="birthdateEstimated"
                                       value="false" ${!command.birthdateEstimated ? 'checked="checked"' : ''}/> Exact
                            </span>
                            &nbsp;&nbsp;&nbsp;

                            <span id="from-age-button-placeholder"></span>
                        </span>
                    </td>
                </tr>
            </table>
        </fieldset>

        <fieldset>
            <legend>Address</legend>

            <% addressFieldRows.each { %>
            ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
            <% } %>

        </fieldset>
        <fieldset>
            <legend>Assign Partner</legend>
            <table width="100%">
                <tr>
                    <td>
                        <select class="provider" name="provider" id="provider">
                            <% providerList.each{%>
                            <option value="${it.providerId}">${it.name}</option>
                            <%}%>
                        </select>
                    </td>
                </tr>
            </table>
        </fieldset>
    <!-- Enrollment -->
    <fieldset>
        <legend> ENROLL CLIENT</legend>
        <% myWellness.each { %>
        ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
        <% } %>
    </fieldset>
    <fieldset>
        <legend> Vitals </legend>
        <% vitalsFields.each { %>
        ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
        <% } %>

        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 5px;">
                    <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)">Would You Like to be part of a wellness project Whatsapp group?</p></span></div>
                    <div class="col-sm-4">
                        <input type="radio" name="whatsappGroup" value="${ yesCondition }" ${ command.whatsappGroup != null &&  command.whatsappGroup.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> Yes<br>
                        <input type="radio" name="whatsappGroup" value="${ noCondition }" ${ command.whatsappGroup != null &&  command.whatsappGroup.uuid == noCondition.uuid ? 'checked="checked"' : ''}> No<br>
                    </div>
                </div>
            </tr>
        </table>
    </fieldset>
    <fieldset>
        <legend> Medical History</legend>
        <table width="80%">
            <div class="row">
                <div class="col-lg-3">
                    <input type="checkbox" name="hbp" id="hbp" value="${high_pressure }" ${command.hbp ? 'checked="checked"' : ''}>High Blood Pressure
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="heartCondition" id="heartCondition" value="${heart_condition }" ${command.heartCondition ? 'checked="checked"' : ''}>Heart Condition
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="kidney" id="kidney" value="${kidney }" ${command.kidney ? 'checked="checked"' : ''}>Kidney
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="hbp" id="hysterectomy" value="${hysterectomy }" ${command.hysterectomy ? 'checked="checked"' : ''}>Hysterectomy
                </div>
                <div class="col-lg-3">
                        <input type="checkbox" name="highColesteral" id="highColesteral" value="${highCholesteral }" ${command.highColesteral ? 'checked="checked"' : ''}>High Cholesteral
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="depression" id="depression" value="${depression }" ${command.depression ? 'checked="checked"' : ''}>Depression
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="hypothyrodism" id="hypothyrodism" value="${hypothrodism }" ${command.hypothyrodism ? 'checked="checked"' : ''}>Hypothyrodism
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="liver" id="liver" value="${liver }" ${command.liver ? 'checked="checked"' : ''}>Liver
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="gallBladder" id="gallBladder" value="${gallBladder }" ${command.gallBladder ? 'checked="checked"' : ''}>Gall Bladder
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="diabetes" id="diabetes" value="${diabetes }" ${command.diabetes ? 'checked="checked"' : ''}>Diabetes
                </div>
                <div class="col-lg-3">
                    <input type="checkbox" name="hyperthyrodism" id="hyperthyrodism" value="${hyperthyrodism }" ${command.hyperthyrodism ? 'checked="checked"' : ''}>Hyperthyrodism
                </div>
            </div>
        </table>
        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 0px;">
                    <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)">Other conditions?</p></span></div>
                    <div class="col-md-2">
                        <input type="radio" name="otherCondition" value="${yesCondition}" ${command.otherCondition != null &&  command.otherCondition.uuid == yesCondition.uuid ? 'checked="checked"' : ''}/> Yes<br>
                        <input type="radio" name="otherCondition" value="${noCondition}" ${command.otherCondition != null &&  command.otherCondition.uuid == noCondition.uuid ? 'checked="checked"' : ''}/> No<br>
                    </div>
                    <div class="col-8 col-md-8">
                        Please Explain
                        <textarea name="otherConditionExplanation" id="otherConditionExplanation" rows="3" placeholder="Please Explain" class="form-control" >
                            ${command.otherConditionExplanation ? command.otherConditionExplanation : ''}
                        </textarea>
                    </div>
                </div>
            </tr>
        </table>
        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 0px;">
                    <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)">Other operations</p></span></div>
                    <div class="col-md-2">
                        <input type="radio" name="otherOperations" value="${yesCondition}" ${command.otherOperations != null &&  command.otherOperations.uuid == yesCondition.uuid ? 'checked="checked"' : ''}/> Yes<br>
                        <input type="radio" name="otherOperations" value="${noCondition}" ${command.otherOperations != null &&  command.otherOperations.uuid == noCondition.uuid ? 'checked="checked"' : ''}/> No<br>
                    </div>
                    <div class="col-8 col-md-8">
                        Please Explain
                        <textarea name="otherOperationsExplanation" id="otherOperationsExplanation" rows="3" placeholder="Please Explain" class="form-control" >
                            ${command.otherOperationsExplanation ? command.otherOperationsExplanation : ''}
                        </textarea>
                    </div>
                </div>
            </tr>
        </table>
        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 0px;">
                    <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)">Allergies</p></span></div>
                    <div class="col-md-2">
                        <input type="radio" name="allergies" value="${yesCondition}" ${command.allergies != null &&  command.allergies.uuid == yesCondition.uuid ? 'checked="checked"' : ''}/> Yes<br>
                        <input type="radio" name="allergies" value="${noCondition}" ${command.allergies != null &&  command.allergies.uuid == noCondition.uuid ? 'checked="checked"' : ''}/> No<br>
                    </div>
                    <div class="col-8 col-md-8">
                        Please Explain
                        <textarea name="allergiesExplanation" id="allergiesExplanation" rows="3" placeholder="Please Explain" class="form-control" >
                            ${command.allergiesExplanation ? command.allergiesExplanation : ''}
                        </textarea>
                    </div>
                </div>
            </tr>
        </table>
        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 0px;">
                    <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)"> Medication </p></span></div>
                    <div class="col-md-2">
                        <input type="radio" name="medication" value="${yesCondition}" ${command.medication != null &&  command.medication.uuid == yesCondition.uuid ? 'checked="checked"' : ''}/> Yes<br>
                        <input type="radio" name="medication" value="${noCondition}" ${command.medication != null &&  command.medication.uuid == noCondition.uuid ? 'checked="checked"' : ''}/> No<br>
                    </div>
                    <div class="col-8 col-md-8">
                        If yes Specify
                        <textarea name="medicationExplanation" id="medicationExplanation" rows="3" placeholder="Specify" class="form-control" >
                            ${command.medicationExplanation ? command.medicationExplanation : ''}
                        </textarea>
                    </div>
                </div>
            </tr>
        </table>

    </fieldset>
    <fieldset>
        <legend> Please Specify if you are </legend>
        <table>
            <tr>
                <div class="row" style="width: 100%; margin-left: 0px;">
                    <div class="row col-sm-4" style="width: 100%; margin-left: 0px;">
                        <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)"> Pregnant </p></span></div>
                        <div class="col-md-4">
                            <input type="radio" name="pregnant" value="${yesCondition}" ${command.pregnancyStatus != null &&  command.pregnancyStatus.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> Yes<br>
                            <input type="radio" name="pregnant" value="${noCondition}" ${command.pregnancyStatus != null &&  command.pregnancyStatus.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> No<br>
                        </div>
                    </div>
                    <div class=" row col-sm-4" style="width: 100%; margin-left: 0px;">
                        <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)"> Breast Feeding </p></span></div>
                        <div class="col-md-4">
                            <input type="radio" name="breastFeeding" value="${yesCondition}" ${command.breastFeeding != null &&  command.breastFeeding.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> Yes<br>
                            <input type="radio" name="breastFeeding" value="${noCondition}" ${command.breastFeeding != null &&  command.breastFeeding.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> No<br>
                        </div>
                    </div>
                    <div class="row col-sm-4" style="width: 100%; margin-left: 0px;">
                        <div class="col-sm-12"> <span><p style="color: rgba(0,0,0,0.82)"> Vegeterian </p></span></div>
                        <div class="col-md-4">
                            <input type="radio" name="vegeterian" value="${yesCondition}" ${command.vegeterian != null &&  command.vegeterian.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> Yes<br>
                            <input type="radio" name="vegeterian" value="${noCondition}" ${command.vegeterian != null &&  command.vegeterian.uuid == yesCondition.uuid ? 'checked="checked"' : ''}> No<br>
                        </div>
                    </div>
                </div>
            </tr>
        </table>
    </fieldset>
    <fieldset>
        <legend> If vegeterian please indicate if you eat any of the following foods </legend>
        <table width="100%">
            <div class="row">
                <div class="col-lg-4">
                    <input type="checkbox" name="fish" id="fish" value="${foodOptionFish }" ${command.fish ? 'checked="checked"' : ''}>Fish
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="chicken" id="chicken" value="${foodOptionChicken }" ${command.chicken ? 'checked="checked"' : ''}>Chicken
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="cheese" id="cheese" value="${foodOptionCheese }" ${command.cheese ? 'checked="checked"' : ''}>Cheese
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="eggs" id="eggs" value="${foodOptionEggs }" ${command.eggs ? 'checked="checked"' : ''}>Eggs
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="youghurt" id="youghurt" value="${foodOptionYoughurt }" ${command.youghurt ? 'checked="checked"' : ''}>Youghurt
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="tofu" id="tofu" value="${foodOptionTofu }" ${command.tofu ? 'checked="checked"' : ''}>Tofu
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="soya" id="soya" value="${foodOptionSoya }" ${command.soya ? 'checked="checked"' : ''}>Soya
                </div>
            </div>
        </table>
    </fieldset>
    <fieldset>
        <legend> Please indicate how you heard of us </legend>
        <table>
            <div class="row">
                <div class="form-group col-sm-6" style="display: inline-flex">
                    <label for="heardFromPerson" class="pr-1  form-control-label">Friend/Family (Who)  </label>
                    <input type="text" id="heardFromPerson" placeholder="Specify Who"  name="heardFromPerson"  class="form-control" ${command.heardFromPerson ? 'value="'+  command.heardFromPerson + '"': ''}>
                </div>
            </div>
        </table>
        <br/>
        <table width="100%">
            <div class="row">
                <div class="col-lg-4">
                    <input type="checkbox" name="facebook" id="facebook" value="${facebook }" ${command.facebook ? 'checked="checked"' : ''}>Facebook
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="instagram" id="instagram" value="${instagram }" ${command.instagram ? 'checked="checked"' : ''}>Instagram
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="website" id="website" value="${website }" ${command.website ? 'checked="checked"' : ''}>Website
                </div>
                <div class="col-lg-4">
                    <input type="checkbox" name="magazine" id="magazine" value="${magazine }" ${command.magazine ? 'checked="checked"' : ''}>Magazine
                </div>
            </div>

        </table>
        <br/>
        <table>
            <div class="row">
                <div class="form-group col-sm-6" style="display: inline-flex">
                    <label for="heardFromOther" class="pr-1  form-control-label">Other</label>
                    <input type="text" id="heardFromOther" placeholder="Indicate" name="heardFromOther" class="form-control" ${command.heardFromOther ? 'value="'+  command.heardFromOther + '"': ''}>
                </div>
            </div>
        </table>
    </fieldset>
    <fieldset>
        <legend> Support Period</legend>
        <table>
            <div class="row">
                <div class="col-sm-3">
                    <input type="radio" name="supportPeriod" value="${ threeMonths }" ${command.supportPeriod != null &&  command.supportPeriod.uuid == threeMonths.uuid ? 'checked="checked"' : ''} >Three Months
                    <input type="radio" name="supportPeriod" value="${ sixMonths }" ${command.supportPeriod != null &&  command.supportPeriod.uuid == sixMonths.uuid ? 'checked="checked"' : ''} >Six Months
                    <input type="radio" name="supportPeriod" value="${ twelveMonths }" ${command.supportPeriod != null &&  command.supportPeriod.uuid == threeMonths.uuid ? 'checked="checked"' : ''} >Twelve Months <br/>
                </div>
            </div>
        </table>

        <% weightBands.each { %>
        ${ui.includeFragment("kenyaui", "widget/rowOfFields", [fields: it])}
        <% } %>
    </fieldset>

<!-- End of enrollment -->
    </div>
    <div class="ke-panel-footer">
        <button type="submit">
            <img src="${ui.resourceLink("kenyaui", "images/glyphs/ok.png")}"/> ${command.original ? "Save Changes" : "Create Client"}
        </button>
        <% if (config.returnUrl) { %>
        <button type="button" class="cancel-button"><img
                src="${ui.resourceLink("kenyaui", "images/glyphs/cancel.png")}"/> Cancel</button>
        <% } %>
    </div>

</form>
<script type="text/javascript">
    jQuery(function () {

        jQuery('#edit-patient-form .cancel-button').click(function () {
            ui.navigate('${ config.returnUrl }');
        });

        kenyaui.setupAjaxPost('edit-patient-form', {
            onSuccess: function (data) {
                if (data.id) {
                    <% if (config.returnUrl) { %>
                    ui.navigate('${ config.returnUrl }');
                    <% } else { %>
                    ui.navigate('wellness', 'registration/registrationViewPatient', {patientId: data.id});
                    <% } %>
                } else {
                    kenyaui.notifyError('Saving client was successful, but unexpected response');
                }
            }
        });
    });

</script>
<script>
    jQuery(document).ready(function() {
        jQuery('.provider2').select2();
    });
</script>
