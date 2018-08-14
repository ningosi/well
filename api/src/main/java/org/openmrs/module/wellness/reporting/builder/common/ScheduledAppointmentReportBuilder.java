package org.openmrs.module.wellness.reporting.builder.common;

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.kenyacore.report.CohortReportDescriptor;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.builder.CalculationReportBuilder;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.wellness.calculation.library.ScheduledAppointmentCalculation;
import org.openmrs.module.wellness.calculation.library.ScheduledAppointmentProviderCalculation;
import org.openmrs.module.wellness.calculation.library.AppointmentDateCalculation;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.reporting.data.converter.CalculationResultConverter;
import org.openmrs.module.wellness.reporting.data.converter.IdentifierConverter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by codehub on 10/7/15.
 * Use to list patients who missed appointment
 */
@Component
@Builds({"wellness.common.report.scheduledAppointment"})
public class ScheduledAppointmentReportBuilder extends CalculationReportBuilder {

    @Override
    protected void addColumns(CohortReportDescriptor report, PatientDataSetDefinition dsd) {

        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.MOBILE_NUMBER);
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), new IdentifierConverter());

        addStandardColumns(report, dsd);
        dsd.addParameter(new Parameter("date","date",Date.class));
        dsd.addColumn("Mobile Number", identifierDef, "");
        dsd.addColumn("Appointment Status",status(), "", new CalculationResultConverter());
        dsd.addColumn("Partner",partiner(), "", new CalculationResultConverter());

        dsd.addColumn("Appointment date", appointmentDate(), "", new CalculationResultConverter());
//        dsd.addColumn("Number of days late", new CalculationDataDefinition("Number of days late", new NumberOfDaysLateCalculation()), "", new CalculationResultConverter());

    }
    private DataDefinition partiner(){
        CalculationDataDefinition cd = new CalculationDataDefinition("partner", new ScheduledAppointmentProviderCalculation());
        //cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        //cd.addCalculationParameter("program", MetadataUtils.existing(Program.class, NutritionMetadata._Program.NUTRITION));
        return  cd;

    }

    private DataDefinition status(){
        CalculationDataDefinition cd = new CalculationDataDefinition("Appointment Status", new ScheduledAppointmentCalculation());
        cd.addParameter(new Parameter("onDate", "On Date", Date.class));
        //cd.addCalculationParameter("program", MetadataUtils.existing(Program.class, NutritionMetadata._Program.NUTRITION));
        return  cd;

    }

    private DataDefinition appointmentDate(){
        CalculationDataDefinition cd = new CalculationDataDefinition("Appointment date", new AppointmentDateCalculation());
        cd.addParameter(new Parameter("date", "On Date", Date.class));
        //cd.addCalculationParameter("date", MetadataUtils.existing(Program.class, NutritionMetadata._Program.NUTRITION));
        return  cd;

    }
}
