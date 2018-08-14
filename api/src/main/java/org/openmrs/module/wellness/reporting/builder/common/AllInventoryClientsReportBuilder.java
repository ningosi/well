package org.openmrs.module.wellness.reporting.builder.common;

import org.openmrs.PatientIdentifierType;
import org.openmrs.Program;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
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
import org.openmrs.module.wellness.calculation.library.InProgramCalculation;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.metadata.NutritionMetadata;
import org.openmrs.module.wellness.reporting.data.converter.IdentifierConverter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Builds({"wellness.common.report.allPatients"})
public class AllInventoryClientsReportBuilder extends CalculationReportBuilder {
    @Override
    protected void addColumns(CohortReportDescriptor report, PatientDataSetDefinition dsd) {
        PatientIdentifierType mobileNumber = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.MOBILE_NUMBER);
        DataDefinition mobileDefConv = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(mobileNumber.getName(), mobileNumber), new IdentifierConverter());
        addStandardColumns(report, dsd);
        dsd.addColumn("Mobile Number", mobileDefConv, "");
        dsd.addColumn("Program", program(), "onDate=${endDate}");
    }


DataDefinition program(){
    ProgramWorkflowService ps = Context.getProgramWorkflowService();
    CalculationDataDefinition cd = new CalculationDataDefinition("program", new InProgramCalculation());
    cd.addParameter(new Parameter("onDate", "On Date", Date.class));
    cd.addCalculationParameter("program", MetadataUtils.existing(Program.class, NutritionMetadata._Program.NUTRITION));
    return  cd;

}
}
