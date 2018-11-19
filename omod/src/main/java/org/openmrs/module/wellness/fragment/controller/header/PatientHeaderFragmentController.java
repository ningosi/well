/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.wellness.fragment.controller.header;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.kenyacore.program.ProgramDescriptor;
import org.openmrs.module.kenyacore.program.ProgramManager;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.ui.framework.WebConstants;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Banner showing which patient this page is in the context of
 */
public class PatientHeaderFragmentController {
    Log log = LogFactory.getLog(PatientHeaderFragmentController.class);

    public void controller(@FragmentParam("patient") Patient patient,
                           FragmentModel model,
                           PageRequest pageRequest,
                           @SpringBean KenyaUiUtils kenyaUi,
                           @SpringBean ProgramManager programManager) throws ParseException {


        List<ProgramDescriptor> programs = new ArrayList<ProgramDescriptor>();
        if (!patient.isVoided()) {
            Collection<ProgramDescriptor> activePrograms = programManager.getPatientActivePrograms(patient);
//            Collection<ProgramDescriptor> eligiblePrograms = programManager.getPatientEligiblePrograms(patient);

            // Display active programs on top
            programs.addAll(activePrograms);

        }

        PatientProgram currentEnrollment = null;
        Date date = new Date();
        if (programs.size() == 0) {
            log.error("Program descriptor is empty");

        }else{
            log.error("Program descriptor is NOT empty");
            List<PatientProgram> enrollments = programManager.getPatientEnrollments(patient, programs.get(0).getTarget());
            for (PatientProgram enrollment : enrollments) {
                if (enrollment.getActive()) {
                    currentEnrollment = enrollment;
                    log.error("Enrolled : " + currentEnrollment.getDateEnrolled());
                    date = currentEnrollment.getDateEnrolled();
                }
            }

        }
        for (ProgramDescriptor descriptor : programs) {
            log.error("Program Name " + descriptor.getTarget().getName());
        }
        // Gather all program enrollments for this patient and program

        model.addAttribute("patient", patient);
        log.error("Date is " + date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date now = dateFormat.parse(new Date().toString());
        Date enrolledDate = dateFormat.parse(date.toString());
        long diffInMillies = Math.abs(System.currentTimeMillis() - enrolledDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        model.addAttribute("dateEnrolled", diff);
        //Encounter code

        AppDescriptor currentApp = kenyaUi.getCurrentApp(pageRequest);

        if (currentApp != null) {
            model.addAttribute("appHomepageUrl", "/" + WebConstants.CONTEXT_PATH + "/" + currentApp.getUrl());
        } else {
            model.addAttribute("appHomepageUrl", null);
        }
    }
}
