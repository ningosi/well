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

package org.openmrs.module.wellness.calculation.library;

import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;
import org.openmrs.module.kenyacore.CoreUtils;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.PatientFlagCalculation;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.util.OpenmrsUtil;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Calculates whether patients have missed their last scheduled return visit. Calculation returns true if the patient is
 * alive, has a scheduled return visit in the past, and hasn't had an encounter since that date
 */
public class ScheduledAppointmentProviderCalculation extends AbstractPatientCalculation {

    /**
     * @see org.openmrs.calculation.patient.PatientCalculation#evaluate(Collection, Map, PatientCalculationContext)
     * @should calculate false for deceased patients
     * @should calculate false for patients with no return visit date obs
     * @should calculate false for patients with return visit date obs whose value is in the future
     * @should calculate false for patients with encounter after return visit date obs value
     * @should calculate true for patients with no encounter after return visit date obs value
     */
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {

        AppointmentService appointmentService = Context.getService(AppointmentService.class);
        PatientService patientService = Context.getPatientService();
        Date today = OpenmrsUtil.firstSecondOfDay(new Date());
        Date tomorrow = CoreUtils.dateAddDays(today, 1);
        Date startOfDay = DateUtil.getStartOfDay(today);
        Date endOfDay = DateUtil.getEndOfDay(tomorrow);

        CalculationResultMap ret = new CalculationResultMap();
        for (Integer ptId : cohort) {
            String partner = "";
            Patient patient = patientService.getPatient(ptId);
            List<Appointment> appointmentList = appointmentService.getAppointmentsOfPatient(patient);
            if (appointmentList.size() > 0) {
                for (Appointment appointment : appointmentList) {
                    if (appointment.getProvider() != null) {
                        partner = appointment.getProvider().getName();
                    }
                }
            }

            ret.put(ptId, new SimpleResult(partner, this));
        }
        return ret;

    }
}
