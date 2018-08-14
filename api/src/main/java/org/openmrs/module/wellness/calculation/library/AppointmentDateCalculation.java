package org.openmrs.module.wellness.calculation.library;

import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.appointmentscheduling.api.AppointmentService;
import org.openmrs.module.kenyacore.CoreUtils;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.util.OpenmrsUtil;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AppointmentDateCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();

        Date today = OpenmrsUtil.firstSecondOfDay(new Date());
        Date tomorrow = CoreUtils.dateAddDays(today, 1);
        Date startOfDay = DateUtil.getStartOfDay(today);
        Date endOfDay = DateUtil.getEndOfDay(tomorrow);
        for(Integer ptId:cohort){
            Date appointmentDate = null;
            List<Appointment> allPatientsAppointment = Context.getService(AppointmentService.class).getAppointmentsOfPatient(Context.getPatientService().getPatient(ptId));

            for(Appointment appointment : allPatientsAppointment) {
                if ((appointment.getStartDateTime() != null && appointment.getStartDateTime().after(startOfDay)) && (appointment.getEndDateTime() != null && appointment.getEndDateTime().before(endOfDay))) {

                    appointmentDate = appointment.getStartDateTime();
                    ret.put(ptId, new SimpleResult(appointmentDate, this));
                }
            }

        }
        return ret;
    }
}
