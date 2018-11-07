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

package org.openmrs.module.wellness.fragment.controller.patient;

import org.apache.commons.lang.StringUtils;
import org.openmrs.*;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.wellness.Dictionary;
import org.openmrs.module.wellness.api.KenyaEmrService;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.metadata.NutritionMetadata;
import org.openmrs.module.wellness.validator.TelephoneNumberValidator;
import org.openmrs.module.wellness.wrapper.PatientWrapper;
import org.openmrs.module.wellness.wrapper.PersonWrapper;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

/**
 * Controller for creating and editing patients in the registration app
 */
public class EditPatientFragmentController {

    // We don't record cause of death, but data model requires a concept
    private static final String CAUSE_OF_DEATH_PLACEHOLDER = Dictionary.UNKNOWN;

    /**
     * Main controller method
     *
     * @param patient the patient (may be null)
     * @param person  the person (may be null)
     * @param model   the model
     */
    public void controller(@FragmentParam(value = "patient", required = false) Patient patient,
                           @FragmentParam(value = "person", required = false) Person person,
                           FragmentModel model) throws IOException {

        PersonWrapper wrapper = new PersonWrapper(person);
        if (patient != null && person != null) {
            throw new RuntimeException("A patient or person can be provided, but not both");
        }

        User user = Context.getAuthenticatedUser();
        Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(user.getPerson());
        Integer provider_id = null;
        if(!user.isSuperUser() && providers.iterator().hasNext()){
            provider_id = providers.iterator().next().getProviderId();
        }
        model.addAttribute("provider_id", provider_id);

        Person existing = patient != null ? patient : person;

        model.addAttribute("command", newEditPatientForm(existing));

        model.addAttribute("civilStatusConcept", Dictionary.getConcept(Dictionary.CIVIL_STATUS));
        model.addAttribute("occupationConcept", Dictionary.getConcept(Dictionary.OCCUPATION));
        model.addAttribute("educationConcept", Dictionary.getConcept(Dictionary.EDUCATION));
        model.addAttribute("wellnessConcept", Dictionary.getConcept("c3ac2b0b-35ce-4cad-9586-095886f2335a"));

        // Create list of education answer concepts
        List<Concept> educationOptions = new ArrayList<Concept>();
        educationOptions.add(Dictionary.getConcept(Dictionary.NONE));
        educationOptions.add(Dictionary.getConcept(Dictionary.PRIMARY_EDUCATION));
        educationOptions.add(Dictionary.getConcept(Dictionary.SECONDARY_EDUCATION));
        educationOptions.add(Dictionary.getConcept(Dictionary.COLLEGE_UNIVERSITY_POLYTECHNIC));
        model.addAttribute("educationOptions", educationOptions);

        // Create a list of marital status answer concepts
        List<Concept> maritalStatusOptions = new ArrayList<Concept>();
        maritalStatusOptions.add(Dictionary.getConcept("159715AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
//		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.MARRIED_MONOGAMOUS));
//		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.DIVORCED));
//		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.WIDOWED));
//		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.LIVING_WITH_PARTNER));
//		maritalStatusOptions.add(Dictionary.getConcept(Dictionary.NEVER_MARRIED));
        model.addAttribute("maritalStatusOptions", maritalStatusOptions);

        // Create a list of cause of death answer concepts
        List<Concept> causeOfDeathOptions = new ArrayList<Concept>();
        causeOfDeathOptions.add(Dictionary.getConcept(Dictionary.UNKNOWN));
        model.addAttribute("causeOfDeathOptions", causeOfDeathOptions);

        //Get all providers
        model.addAttribute("providerList", Context.getProviderService().getAllProviders());

        //Create list of my wellness options
        List<Concept> wellnesAnswers = new ArrayList<Concept>();
        wellnesAnswers.add(Dictionary.getConcept("159310AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
        wellnesAnswers.add(Dictionary.getConcept(Dictionary.WLLNESS_MARATHON));
        wellnesAnswers.add(Dictionary.getConcept(Dictionary.WELLNESS_RUN));
        wellnesAnswers.add(Dictionary.getConcept(Dictionary.WELLNESS_SPRINT));
        wellnesAnswers.add(Dictionary.getConcept(Dictionary.WELLNESS_STROLL));
        model.addAttribute("wellnesAnswers", wellnesAnswers);

        List<Concept> wellnessVitals = new ArrayList<Concept>();
        wellnessVitals.add(Dictionary.getConcept(Dictionary.HEIGHT));
        model.addAttribute("wellnessVitals", wellnessVitals);

        List<Concept> weightBandAnswers = new ArrayList<Concept>();
        weightBandAnswers.add(Dictionary.getConcept(Dictionary.BAND_1));
        weightBandAnswers.add(Dictionary.getConcept(Dictionary.BAND_2));
        weightBandAnswers.add(Dictionary.getConcept(Dictionary.BAND_3));
        model.addAttribute("weightBandAnswers", weightBandAnswers);

        List<Concept> channellAnswers = new ArrayList<Concept>();
        channellAnswers.add(Dictionary.getConcept(Dictionary.CONTACT_WALK_IN));
        channellAnswers.add(Dictionary.getConcept(Dictionary.CONTACT_CALL));
        model.addAttribute("channellAnswers", channellAnswers);

//        HashMap<String,Concept> medic_history_map = new HashMap<String, Concept>();
//        medic_history_map.put("hbp", Dictionary.getConcept(Dictionary.HIGH_BLOOD_PRESSURE));
//        medic_history_map.put("heartCondition", Dictionary.getConcept(Dictionary.HEART_CONDITION));
//        medic_history_map.put("kidney", Dictionary.getConcept(Dictionary.KIDNEY));
//        medic_history_map.put("hysterectomy", Dictionary.getConcept(Dictionary.HYSTERECTOMY));
//        medic_history_map.put("highCholesteral", Dictionary.getConcept(Dictionary.HIGH_CHOLESTERAL));
//        medic_history_map.put("depression", Dictionary.getConcept(Dictionary.DEPRESSION));
//        medic_history_map.put("hypothyroidism", Dictionary.getConcept(Dictionary.HYPORTHYRODISM));
//        medic_history_map.put("liver", Dictionary.getConcept(Dictionary.KIDNEY));
//        medic_history_map.put("gallBladder", Dictionary.getConcept(Dictionary.GALL_BLADDER));
//        medic_history_map.put("diabetes", Dictionary.getConcept(Dictionary.DIABETES));
//        medic_history_map.put("hyperthyroidim", Dictionary.getConcept(Dictionary.HYPERTHYRODISM));
//        model.addAttribute("medic_history_map", medic_history_map);

        HashMap<String, Concept> foods_map = new HashMap<String, Concept>();
        foods_map.put("Fish", Dictionary.getConcept(Dictionary.FISH));
        foods_map.put("Chicken", Dictionary.getConcept(Dictionary.CHICKEN));
        foods_map.put("Cheese", Dictionary.getConcept(Dictionary.CHEESE));
        foods_map.put("Eggs", Dictionary.getConcept(Dictionary.EGGS));
        foods_map.put("Youghurt", Dictionary.getConcept(Dictionary.YOUGHURT));
        foods_map.put("Tofu", Dictionary.getConcept(Dictionary.TOFU));
        foods_map.put("Soya", Dictionary.getConcept(Dictionary.SOYA));
        model.addAttribute("foods_map", foods_map);

        HashMap<String, Concept> social_map = new HashMap<String, Concept>();
        social_map.put("Facebook", Dictionary.getConcept(Dictionary.FACEBOOK));
        social_map.put("Instagram", Dictionary.getConcept(Dictionary.INSTAGRAM));
        social_map.put("Website", Dictionary.getConcept(Dictionary.WEBSITE));
        social_map.put("Magazine", Dictionary.getConcept(Dictionary.MAGAZINE));
        model.addAttribute("social_map", social_map);

        HashMap<String, Concept> support_period_map = new HashMap<String, Concept>();
        support_period_map.put("3 Months", Dictionary.getConcept(Dictionary.THREE_MONTHS));
        support_period_map.put("6 Months", Dictionary.getConcept(Dictionary.SIX_MONTHS));
        model.addAttribute("weight_band_map", support_period_map);
        model.addAttribute("high_pressure", Dictionary.getConcept(Dictionary.HIGH_BLOOD_PRESSURE));
        model.addAttribute("heart_condition", Dictionary.getConcept(Dictionary.HEART_CONDITION));
        model.addAttribute("kidney", Dictionary.getConcept(Dictionary.KIDNEY));
        model.addAttribute("hysterectomy", Dictionary.getConcept(Dictionary.HYSTERECTOMY));
        model.addAttribute("highCholesteral", Dictionary.getConcept(Dictionary.HIGH_CHOLESTERAL));
        model.addAttribute("depression", Dictionary.getConcept(Dictionary.DEPRESSION));
        model.addAttribute("hypothrodism", Dictionary.getConcept(Dictionary.HYPORTHYRODISM));
        model.addAttribute("liver", Dictionary.getConcept(Dictionary.LIVER));
        model.addAttribute("gallBladder", Dictionary.getConcept(Dictionary.GALL_BLADDER));
        model.addAttribute("diabetes", Dictionary.getConcept(Dictionary.DIABETES));
        model.addAttribute("hyperthyrodism", Dictionary.getConcept(Dictionary.HYPERTHYRODISM));
        model.addAttribute("yesCondition", Dictionary.getConcept(Dictionary.YES));
        model.addAttribute("noCondition", Dictionary.getConcept(Dictionary.NO));
        model.addAttribute("otherConditionExplanation", Dictionary.getConcept(Dictionary.OTHER_CONDITION_EXPLANATION));
        model.addAttribute("foodOptionFish", Dictionary.getConcept(Dictionary.FISH));
        model.addAttribute("foodOptionChicken", Dictionary.getConcept(Dictionary.CHICKEN));
        model.addAttribute("foodOptionCheese", Dictionary.getConcept(Dictionary.CHEESE));
        model.addAttribute("foodOptionEggs", Dictionary.getConcept(Dictionary.EGGS));
        model.addAttribute("foodOptionYoughurt", Dictionary.getConcept(Dictionary.YOUGHURT));
        model.addAttribute("foodOptionTofu", Dictionary.getConcept(Dictionary.TOFU));
        model.addAttribute("foodOptionSoya", Dictionary.getConcept(Dictionary.SOYA));
        model.addAttribute("facebook", Dictionary.getConcept(Dictionary.FACEBOOK));
        model.addAttribute("instagram", Dictionary.getConcept(Dictionary.INSTAGRAM));
        model.addAttribute("website", Dictionary.getConcept(Dictionary.WEBSITE));
        model.addAttribute("magazine", Dictionary.getConcept(Dictionary.MAGAZINE));
        model.addAttribute("threeMonths", Dictionary.getConcept(Dictionary.THREE_MONTHS));
        model.addAttribute("sixMonths", Dictionary.getConcept(Dictionary.SIX_MONTHS));
        model.addAttribute("twelveMonths", Dictionary.getConcept(Dictionary.TWELVE_MONTHS));

    }

    /**
     * Saves the patient being edited by this form
     *
     * @param form the edit patient form
     * @param ui   the UI utils
     * @return a simple object { patientId }
     */
    public SimpleObject savePatient(@MethodParam("newEditPatientForm") @BindParams EditPatientForm form, UiUtils ui) {
        ui.validate(form, form, null);

        Patient patient = form.save();

        // if this patient is the current user i need to refresh the current user
        if (patient.getPersonId().equals(Context.getAuthenticatedUser().getPerson().getPersonId())) {
            Context.refreshAuthenticatedUser();
        }

        return SimpleObject.create("id", patient.getId());
    }

    /**
     * Creates an edit patient form
     *
     * @param person the person
     * @return the form
     */
    public EditPatientForm newEditPatientForm(@RequestParam(value = "personId", required = false) Person person) {
        if (person != null && person.isPatient()) {
            return new EditPatientForm((Patient) person); // For editing existing patient
        } else if (person != null) {
            return new EditPatientForm(person); // For creating patient from existing person
        } else {
            return new EditPatientForm(); // For creating patient and person from scratch
        }
    }

    /**
     * The form command object for editing patients
     */
    public class EditPatientForm extends AbstractWebForm {

        private Person original;
        private Location location;
        private PersonName personName;
        private Date birthdate;
        private Boolean birthdateEstimated;
        private String gender;
        private PersonAddress personAddress;
        private Concept maritalStatus;
        private Concept occupation;
        private Concept education;
        private Concept wellness;
        private Concept weightBand;
        private Concept hbp;
        private Concept heartCondition;
        private Concept kidney;
        private Concept hysterectomy;
        private Concept highColesteral;
        private Concept depression;
        private Concept hypothyrodism;
        private Concept liver;
        private Concept gallBladder;
        private Concept diabetes;
        private Concept hyperthyrodism;
        private Concept otherCondition;
        private Concept whatsappGroup;
        private Concept otherOperations;
        private Concept allergies;
        private Concept medication;
        private Concept pregnancyStatus;
        private Concept supportPeriod;
        private Concept breastFeeding;
        private Concept vegeterian;
        private Concept fish;
        private Concept chicken;
        private Concept eggs;
        private Concept youghurt;
        private Concept tofu;
        private Concept cheese;
        private Concept soya;
        private Concept facebook;
        private Concept instagram;
        private Concept website;
        private Concept magazine;
        private Concept contactChannell;

        private String otherConditionExplanation;
        private String otherOperationsExplanation;
        private String allergiesExplanation;
        private String medicationExplanation;
        private String height;
        private String weight;
        private String goal_weight;
        private String systolic;
        private String diastolic;
        private String heardFromPerson;
        private String heardFromOther;
        private Obs savedOtherConditionEplanation;
        private Obs savedMaritalStatus;
        private Obs savedOccupation;
        private Obs savedEducation;
        private Obs savedWellness;
        private Obs savedWeightBand;
        private Obs savedHeight;
        private Obs savedWeight;
        private Obs savedGoalWeight;
        private Obs savedSystolic;
        private Obs savedDiastolic;
        private Obs savedHearCondition;
        private Obs savedHpb;
        private Obs savedKidney;
        private Obs savedHysterectomy;
        private Obs savedHighColesteral;
        private Obs savedLiver;
        private Obs savedGallBladder;
        private Obs savedDiabetes;
        private Obs savedHyperthyrodism;
        private Obs savedDepressiion;
        private Obs savedHypothyrodism;
        private Obs savedOtherCondition;
        private Obs savedWhatsappGroup;
        private Obs savedOtherOperations;
        private Obs savedOtherOperationsExplanation;
        private Obs savedAllergies;
        private Obs savedAllergiesExplanation;
        private Obs savedmedication;
        private Obs savedmedicationEplanation;
        private Obs savedPregnancyStatus;
        private Obs savedSupportPeriod;
        private Obs savedBreastFeeding;
        private Obs savedVegeterian;
        private Obs savedFish;
        private Obs savedChicken;
        private Obs savedEggs;
        private Obs savedYoughurt;
        private Obs savedTofu;
        private Obs savedSoya;
        private Obs savedCheese;
        private Obs savedFacebook;
        private Obs savedInstagram;
        private Obs savedWebsite;
        private Obs savedMagazine;
        private Obs savedHeardFromPerson;
        private Obs savedheardFromOther;
        private Obs savedContactChannel;


        private List<Obs> savedMedicHistory;
        private List<Obs> savedFoods;
        private List<Obs> social;
        private Boolean dead = false;
        private Date deathDate;

        private String nationalIdNumber;
        private String patientClinicNumber;
        private String uniquePatientNumber;
        private String mobileNumber;
        private String otherNumber;
        private String passportNumber;

        private String telephoneContact;
        private String nameOfNextOfKin;
        private String nextOfKinRelationship;
        private String nextOfKinContact;
        private String nextOfKinAddress;
        private String subChiefName;
        private String provider;


        /**
         * Creates an edit form for a new patient
         */
        public EditPatientForm() {
            location = Context.getService(KenyaEmrService.class).getDefaultLocation();

            personName = new PersonName();
            personAddress = new PersonAddress();
        }

        /**
         * Creates an edit form for an existing patient
         */
        public EditPatientForm(Person person) {
            this();

            original = person;

            if (person.getPersonName() != null) {
                personName = person.getPersonName();
            } else {
                personName.setPerson(person);
            }

            if (person.getPersonAddress() != null) {
                personAddress = person.getPersonAddress();
            } else {
                personAddress.setPerson(person);
            }

            gender = person.getGender();
            birthdate = person.getBirthdate();
            birthdateEstimated = person.getBirthdateEstimated();
            dead = person.isDead();
            deathDate = person.getDeathDate();

            PersonWrapper wrapper = new PersonWrapper(person);
            telephoneContact = wrapper.getTelephoneContact();
            provider = wrapper.getProvider();
        }

        /**
         * Creates an edit form for an existing patient
         */
        public EditPatientForm(Patient patient) {
            this((Person) patient);

            PatientWrapper wrapper = new PatientWrapper(patient);

            patientClinicNumber = wrapper.getPatientClinicNumber();
            uniquePatientNumber = wrapper.getUniquePatientNumber();
            nationalIdNumber = wrapper.getNationalIdNumber();
            mobileNumber = wrapper.getMobileNumber();
            otherNumber = wrapper.getOtherMobileNumber();
            passportNumber = wrapper.getPassportNumber();

            nameOfNextOfKin = wrapper.getNextOfKinName();
            nextOfKinRelationship = wrapper.getNextOfKinRelationship();
            nextOfKinContact = wrapper.getNextOfKinContact();
            nextOfKinAddress = wrapper.getNextOfKinAddress();
            subChiefName = wrapper.getSubChiefName();

            savedMaritalStatus = getLatestObs(patient, Dictionary.CIVIL_STATUS);
            if (savedMaritalStatus != null) {
                maritalStatus = savedMaritalStatus.getValueCoded();
            }

            savedOccupation = getLatestObs(patient, Dictionary.OCCUPATION);
            if (savedOccupation != null) {
                occupation = savedOccupation.getValueCoded();
            }

            savedEducation = getLatestObs(patient, Dictionary.EDUCATION);
            if (savedEducation != null) {
                education = savedEducation.getValueCoded();
            }
            savedWellness = getLatestObs(patient, "c3ac2b0b-35ce-4cad-9586-095886f2335a");
            if (savedWellness != null) {
                wellness = savedWellness.getValueCoded();
            }

            savedHeight = getLatestObs(patient, Dictionary.HEIGHT);
            if (savedHeight != null) {
                height = String.valueOf(savedHeight.getValueNumeric());
            }

            savedWeight = getLatestObs(patient, Dictionary.WEIGHT);
            if (savedWeight != null) {
                weight = String.valueOf(savedWeight.getValueNumeric());
            }

            savedGoalWeight = getLatestObs(patient, Dictionary.GOAL_WEIGHT);
            if (savedGoalWeight != null) {
                goal_weight = String.valueOf(savedGoalWeight.getValueNumeric());
            }

            savedSystolic = getLatestObs(patient, Dictionary.SYSTOLIC);
            if (savedSystolic != null) {
                systolic = String.valueOf(savedSystolic.getValueNumeric());
            }

            savedDiastolic = getLatestObs(patient, Dictionary.DIASTOLIC);
            if (savedDiastolic != null) {
                diastolic = String.valueOf(savedDiastolic.getValueNumeric());
            }


            savedOtherCondition = getLatestObs(patient, Dictionary.OTHER_CONDITION);
            if (savedOtherCondition != null) {
                otherCondition = savedOtherCondition.getValueCoded();
            }

            savedOtherConditionEplanation = getLatestObs(patient, Dictionary.OTHER_CONDITION_EXPLANATION);
            if (savedOtherConditionEplanation != null) {
                otherConditionExplanation = savedOtherConditionEplanation.getValueText();
            }

            savedOtherOperations = getLatestObs(patient, Dictionary.OTHER_OPERATIONS);
            if (savedOtherOperations != null) {
                otherOperations = savedOtherOperations.getValueCoded();
            }
            savedOtherOperationsExplanation = getLatestObs(patient, Dictionary.OTHER_OPERATIONS_EXPLANATION);
            if (savedOtherOperationsExplanation != null) {
                otherOperationsExplanation = savedOtherOperationsExplanation.getValueText();
            }

            savedAllergies = getLatestObs(patient, Dictionary.ALLERGIES);
            if (savedAllergies != null) {
                allergies = savedAllergies.getValueCoded();
            }
            savedAllergiesExplanation = getLatestObs(patient, Dictionary.ALLERGIES_EXPLANATION);
            if (savedAllergiesExplanation != null) {
                allergiesExplanation = savedAllergiesExplanation.getValueText();
            }

            savedmedication = getLatestObs(patient, Dictionary.MEDICATION);
            if (savedmedication != null) {
                medication = savedmedication.getValueCoded();
            }

            savedmedicationEplanation = getLatestObs(patient, Dictionary.MEDICATION_EXPLANATION);
            if (savedmedicationEplanation != null) {
                medicationExplanation = savedmedicationEplanation.getValueText();
            }

            savedPregnancyStatus = getLatestObs(patient, Dictionary.PREGNANCY_STATUS);
            if (savedPregnancyStatus != null) {
                pregnancyStatus = savedPregnancyStatus.getValueCoded();
            }

            savedBreastFeeding = getLatestObs(patient, Dictionary.BREAST_FEEDING);
            if (savedBreastFeeding != null) {
                breastFeeding = savedBreastFeeding.getValueCoded();
            }

            savedVegeterian = getLatestObs(patient, Dictionary.VEGETERIAN);
            if (savedVegeterian != null) {
                vegeterian = savedVegeterian.getValueCoded();
            }

            savedHeardFromPerson = getLatestObs(patient, Dictionary.HEARD_FROM_PERSON);
            if (savedHeardFromPerson != null) {
                heardFromPerson = savedHeardFromPerson.getValueText();
            }

            savedheardFromOther = getLatestObs(patient, Dictionary.FREE_TEXT_GENERAL);
            if (savedheardFromOther != null) {
                heardFromOther = savedheardFromOther.getValueText();
            }

            savedSupportPeriod = getLatestObs(patient, Dictionary.SUPPORT_PERIOD);
            if (savedSupportPeriod != null) {
                supportPeriod = savedSupportPeriod.getValueCoded();
            }

            savedWeightBand = getLatestObs(patient, Dictionary.WEIGHT_BAND);
            if (savedWeightBand != null) {
                weightBand = savedWeightBand.getValueCoded();
            }

            savedWhatsappGroup = getLatestObs(patient, Dictionary.WATSAPP_GROUP);
            if (savedWhatsappGroup != null) {
                whatsappGroup = savedWhatsappGroup.getValueCoded();
            }

            savedContactChannel = getLatestObs(patient, Dictionary.CONTACT_CHANNEL);
            if (savedContactChannel != null) {
                contactChannell = savedContactChannel.getValueCoded();
            }

            savedMedicHistory = getLatestObsList(patient, Dictionary.MEDICAL_CONDITION);
            if (savedMedicHistory != null) {
                for (Obs obs : savedMedicHistory) {
                    if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HIGH_BLOOD_PRESSURE).getUuid())) {
                        savedHpb = obs;
                        hbp = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HEART_CONDITION).getUuid())) {
                        savedHearCondition = obs;
                        heartCondition = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.KIDNEY).getUuid())) {
                        savedKidney = obs;
                        kidney = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HYSTERECTOMY).getUuid())) {
                        savedHysterectomy = obs;
                        hysterectomy = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HIGH_CHOLESTERAL).getUuid())) {
                        savedHighColesteral = obs;
                        highColesteral = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.DEPRESSION).getUuid())) {
                        savedDepressiion = obs;
                        depression = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HYPORTHYRODISM).getUuid())) {
                        savedHypothyrodism = obs;
                        hypothyrodism = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.LIVER).getUuid())) {
                        savedLiver = obs;
                        liver = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.GALL_BLADDER).getUuid())) {
                        savedGallBladder = obs;
                        gallBladder = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.DIABETES).getUuid())) {
                        savedDiabetes = obs;
                        diabetes = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.HYPERTHYRODISM).getUuid())) {
                        savedHyperthyrodism = obs;
                        hyperthyrodism = obs.getValueCoded();
                    }
                }
            }
            savedFoods = getLatestObsList(patient, Dictionary.FOODS);
            if (savedFoods != null) {
                for (Obs obs : savedFoods) {
                    if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.FISH).getUuid())) {
                        savedFish = obs;
                        fish = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.CHICKEN).getUuid())) {
                        savedChicken = obs;
                        chicken = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.CHICKEN).getUuid())) {
                        savedChicken = obs;
                        chicken = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.CHEESE).getUuid())) {
                        savedCheese = obs;
                        cheese = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.EGGS).getUuid())) {
                        savedEggs = obs;
                        eggs = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.YOUGHURT).getUuid())) {
                        savedYoughurt = obs;
                        youghurt = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.TOFU).getUuid())) {
                        savedTofu = obs;
                        tofu = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.SOYA).getUuid())) {
                        savedSoya = obs;
                        soya = obs.getValueCoded();
                    }
                }
            }
            social = getLatestObsList(patient, Dictionary.SOCIAL_MEDIA);
            if (social != null) {
                for (Obs obs : social) {
                    if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.FACEBOOK).getUuid())) {
                        savedFacebook = obs;
                        facebook = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.INSTAGRAM).getUuid())) {
                        savedInstagram = obs;
                        instagram = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.WEBSITE).getUuid())) {
                        savedWebsite = obs;
                        website = obs.getValueCoded();
                    } else if (obs.getValueCoded().getUuid().equals(Dictionary.getConcept(Dictionary.MAGAZINE).getUuid())) {
                        savedMagazine = obs;
                        magazine = obs.getValueCoded();
                    }
                }
            }

        }

        private Obs getLatestObs(Patient patient, String conceptIdentifier) {
            Concept concept = Dictionary.getConcept(conceptIdentifier);
            List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
            if (obs.size() > 0) {
                // these are in reverse chronological order
                return obs.get(0);
            }
            return null;
        }

        private List<Obs> getLatestObsList(Patient patient, String conceptIdentifier) {
            Concept concept = Dictionary.getConcept(conceptIdentifier);
            List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
            if (obs.size() > 0) {
                // these are in reverse chronological order
                return obs;
            }
            return null;
        }

        /**
         * @see org.springframework.validation.Validator#validate(java.lang.Object,
         * org.springframework.validation.Errors)
         */
        @Override
        public void validate(Object target, Errors errors) {
            require(errors, "personName.givenName");
            require(errors, "personName.familyName");
            require(errors, "gender");
            require(errors, "mobileNumber");

            // Require death details if patient is deceased
            if (dead) {
                require(errors, "deathDate");

                if (deathDate != null) {
                    if (birthdate != null && deathDate.before(birthdate)) {
                        errors.rejectValue("deathDate", "Cannot be before birth date");
                    }
                    if (deathDate.after(new Date())) {
                        errors.rejectValue("deathDate", "Cannot be in the future");
                    }
                }
            } else if (deathDate != null) {
                errors.rejectValue("deathDate", "Must be empty if patient not deceased");
            }

            if (StringUtils.isNotBlank(telephoneContact)) {
                validateField(errors, "telephoneContact", new TelephoneNumberValidator());
            }
            if (StringUtils.isNotBlank(nextOfKinContact)) {
                validateField(errors, "nextOfKinContact", new TelephoneNumberValidator());
            }
            //TODO : Validate provider

            validateField(errors, "personAddress");

            validateIdentifierField(errors, "nationalIdNumber", CommonMetadata._PatientIdentifierType.NATIONAL_ID);
            validateIdentifierField(errors, "patientClinicNumber", CommonMetadata._PatientIdentifierType.CLIENT_ACCOUNT_NUMBER);
            validateIdentifierField(errors, "passportNumber", CommonMetadata._PatientIdentifierType.PASSPORT_NUMBER);
            validateIdentifierField(errors, "mobileNumber", CommonMetadata._PatientIdentifierType.MOBILE_NUMBER);
            validateIdentifierField(errors, "otherNumber", CommonMetadata._PatientIdentifierType.OTHER_MOBILE_NUMBER);

            // check birth date against future dates and really old dates
            if (birthdate != null) {
                if (birthdate.after(new Date()))
                    errors.rejectValue("birthdate", "error.date.future");
                else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.YEAR, -120); // person cannot be older than 120 years old
                    if (birthdate.before(c.getTime())) {
                        errors.rejectValue("birthdate", "error.date.nonsensical");
                    }
                }
            }
        }

        /**
         * Validates an identifier field
         *
         * @param errors
         * @param field
         * @param idTypeUuid
         */
        protected void validateIdentifierField(Errors errors, String field, String idTypeUuid) {
            String value = (String) errors.getFieldValue(field);

            if (StringUtils.isNotBlank(value)) {
                PatientIdentifierType idType = MetadataUtils.existing(PatientIdentifierType.class, idTypeUuid);
                if (!value.matches(idType.getFormat())) {
                    errors.rejectValue(field, idType.getFormatDescription());
                }

                PatientIdentifier stub = new PatientIdentifier(value, idType, null);

                if (original != null && original.isPatient()) { // Editing an existing patient
                    stub.setPatient((Patient) original);
                }

                if (Context.getPatientService().isIdentifierInUseByAnotherPatient(stub)) {
                    errors.rejectValue(field, "In use by another patient");
                }
            }
        }

        /**
         * @see org.openmrs.module.kenyaui.form.AbstractWebForm#save()
         */
        @Override
        public Patient save() {
            Patient toSave;

            if (original != null && original.isPatient()) { // Editing an existing patient
                toSave = (Patient) original;
            } else if (original != null) {
                toSave = new Patient(original); // Creating a patient from an existing person
            } else {
                toSave = new Patient(); // Creating a new patient and person
            }

            toSave.setGender(gender);
            toSave.setBirthdate(birthdate);
            toSave.setBirthdateEstimated(birthdateEstimated);
            toSave.setDead(dead);
            toSave.setDeathDate(deathDate);
            toSave.setCauseOfDeath(dead ? Dictionary.getConcept(CAUSE_OF_DEATH_PLACEHOLDER) : null);

            if (anyChanges(toSave.getPersonName(), personName, "givenName", "familyName")) {
                if (toSave.getPersonName() != null) {
                    voidData(toSave.getPersonName());
                }
                toSave.addName(personName);
            }

            if (anyChanges(toSave.getPersonAddress(), personAddress, "address1", "address2", "address5", "address6", "countyDistrict", "address3", "cityVillage", "stateProvince", "country", "postalCode", "address4")) {
                if (toSave.getPersonAddress() != null) {
                    voidData(toSave.getPersonAddress());
                }
                toSave.addAddress(personAddress);
            }

            PatientWrapper wrapper = new PatientWrapper(toSave);

            wrapper.getPerson().setTelephoneContact(telephoneContact);
            wrapper.getPerson().setProvider(provider);
            wrapper.setNationalIdNumber(nationalIdNumber, location);
            wrapper.setPatientClinicNumber(patientClinicNumber, location);
            wrapper.setUniquePatientNumber(uniquePatientNumber, location);
            wrapper.setMobileNumber(mobileNumber, location);
            wrapper.setOtherMobileNumber(mobileNumber, location);
            wrapper.setMobileNumber(mobileNumber, location);
            wrapper.setOtherMobileNumber(otherNumber, location);
            wrapper.setNextOfKinName(nameOfNextOfKin);
            wrapper.setNextOfKinRelationship(nextOfKinRelationship);
            wrapper.setNextOfKinContact(nextOfKinContact);
            wrapper.setNextOfKinAddress(nextOfKinAddress);
            wrapper.setSubChiefName(subChiefName);
            wrapper.setPassportNumber(passportNumber, location);

            // Make sure everyone gets an OpenMRS ID
            PatientIdentifierType openmrsIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID);
            PatientIdentifier openmrsId = toSave.getPatientIdentifier(openmrsIdType);

            if (openmrsId == null) {
                String generated = Context.getService(IdentifierSourceService.class).generateIdentifier(openmrsIdType, "Registration");
                openmrsId = new PatientIdentifier(generated, openmrsIdType, location);
                toSave.addIdentifier(openmrsId);

                if (!toSave.getPatientIdentifier().isPreferred()) {
                    openmrsId.setPreferred(true);
                }
            }

            Patient ret = Context.getPatientService().savePatient(toSave);

            // Explicitly save all identifier objects including voided
            for (PatientIdentifier identifier : toSave.getIdentifiers()) {
                Context.getPatientService().savePatientIdentifier(identifier);
            }

            // Save remaining fields as obs
            List<Obs> obsToSave = new ArrayList<Obs>();
            List<Obs> obsToVoid = new ArrayList<Obs>();

            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.CIVIL_STATUS), savedMaritalStatus, maritalStatus);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.OCCUPATION), savedOccupation, occupation);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.EDUCATION), savedEducation, education);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHpb, hbp);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHearCondition, heartCondition);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedKidney, kidney);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHysterectomy, hysterectomy);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHighColesteral, highColesteral);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedDepressiion, depression);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHypothyrodism, hypothyrodism);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedLiver, liver);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedGallBladder, gallBladder);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedDiabetes, diabetes);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICAL_CONDITION), savedHyperthyrodism, hyperthyrodism);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.OTHER_CONDITION), savedOtherCondition, otherCondition);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.OTHER_OPERATIONS), savedOtherOperations, otherOperations);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.ALLERGIES), savedAllergies, allergies);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICATION), savedmedication, medication);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.PREGNANCY_STATUS), savedPregnancyStatus, pregnancyStatus);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.BREAST_FEEDING), savedBreastFeeding, breastFeeding);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedFish, fish);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedChicken, chicken);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedCheese, cheese);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedEggs, eggs);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedYoughurt, youghurt);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedTofu, tofu);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FOODS), savedSoya, soya);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SOCIAL_MEDIA), savedFacebook, facebook);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SOCIAL_MEDIA), savedInstagram, instagram);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SOCIAL_MEDIA), savedWebsite, website);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SOCIAL_MEDIA), savedMagazine, magazine);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SUPPORT_PERIOD), savedSupportPeriod, supportPeriod);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.WEIGHT_BAND), savedWeightBand, weightBand);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.WATSAPP_GROUP), savedWhatsappGroup, whatsappGroup);
            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.CONTACT_CHANNEL), savedContactChannel, contactChannell);

            handleOncePerPatientObs(ret, obsToSave, obsToVoid, Dictionary.getConcept("c3ac2b0b-35ce-4cad-9586-095886f2335a"), savedWellness, wellness);
            if (height != null && StringUtils.isNotEmpty(height))
                handleOncePerPatientNumericObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.HEIGHT), savedHeight, Double.valueOf(height));

            if (weight != null && StringUtils.isNotEmpty(weight))
                handleOncePerPatientNumericObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.WEIGHT), savedWeight, Double.valueOf(weight));

            if (goal_weight != null && StringUtils.isNotEmpty(goal_weight))
                handleOncePerPatientNumericObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.GOAL_WEIGHT), savedGoalWeight, Double.valueOf(goal_weight));

            if (systolic != null && StringUtils.isNotEmpty(systolic))
                handleOncePerPatientNumericObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.SYSTOLIC), savedSystolic, Double.valueOf(systolic));

            if (diastolic != null && StringUtils.isNotEmpty(diastolic))
                handleOncePerPatientNumericObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.DIASTOLIC), savedDiastolic, Double.valueOf(diastolic));

            if (otherConditionExplanation != null && StringUtils.isNotEmpty(otherConditionExplanation))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.OTHER_CONDITION_EXPLANATION), savedOtherConditionEplanation, otherConditionExplanation);

            if (otherOperationsExplanation != null && StringUtils.isNotEmpty(otherOperationsExplanation))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.OTHER_OPERATIONS_EXPLANATION), savedOtherOperationsExplanation, otherOperationsExplanation);

            if (allergiesExplanation != null && StringUtils.isNotEmpty(allergiesExplanation))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.ALLERGIES_EXPLANATION), savedAllergiesExplanation, allergiesExplanation);

            if (medicationExplanation != null && StringUtils.isNotEmpty(medicationExplanation))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.MEDICATION_EXPLANATION), savedmedicationEplanation, medicationExplanation);

            if (heardFromPerson != null && StringUtils.isNotEmpty(heardFromPerson))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.HEARD_FROM_PERSON), savedHeardFromPerson, heardFromPerson);

            if (heardFromOther != null && StringUtils.isNotEmpty(heardFromOther))
                handleOncePerPatientStringObs(ret, obsToSave, obsToVoid, Dictionary.getConcept(Dictionary.FREE_TEXT_GENERAL), savedheardFromOther, heardFromOther);

            for (Obs o : obsToVoid) {
                Context.getObsService().voidObs(o, "Wellness edit patient");
            }

            for (Obs o : obsToSave) {
                Context.getObsService().saveObs(o, "Wellness edit patient");
            }

            return ret;
        }

        /**
         * Handles saving a field which is stored as an obs
         *
         * @param patient   the patient being saved
         * @param obsToSave
         * @param obsToVoid
         * @param question
         * @param savedObs
         * @param newValue
         */
        protected void handleOncePerPatientObs(Patient patient, List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
                                               Obs savedObs, Concept newValue) {
            if (!OpenmrsUtil.nullSafeEquals(savedObs != null ? savedObs.getValueCoded() : null, newValue)) {
                // there was a change
                if (savedObs != null && newValue == null) {
                    // treat going from a value to null as voiding all past civil status obs
                    obsToVoid.addAll(Context.getObsService().getObservationsByPersonAndConcept(patient, question));
                }
                if (newValue != null) {
                    Obs o = new Obs();
                    o.setPerson(patient);
                    o.setConcept(question);
                    o.setObsDatetime(new Date());
                    o.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                    o.setValueCoded(newValue);
                    obsToSave.add(o);
                }
            }
        }

        /**
         * Handles saving a field which is stored as an obs
         *
         * @param patient   the patient being saved
         * @param obsToSave
         * @param obsToVoid
         * @param question
         * @param savedObs
         * @param newValue
         */
        protected void handleOncePerPatientNumericObs(Patient patient, List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
                                                      Obs savedObs, Double newValue) {
            if (!OpenmrsUtil.nullSafeEquals(savedObs != null ? savedObs.getValueNumeric() : null, newValue)) {
                // there was a change
                if (savedObs != null && newValue == null) {
                    // treat going from a value to null as voiding all past civil status obs
                    obsToVoid.addAll(Context.getObsService().getObservationsByPersonAndConcept(patient, question));
                }
                if (newValue != null) {
                    Obs o = new Obs();
                    o.setPerson(patient);
                    o.setConcept(question);
                    o.setObsDatetime(new Date());
                    o.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                    o.setValueNumeric(newValue);
                    obsToSave.add(o);
                }
            }
        }

        /**
         * Handles saving a field which is stored as an obs
         *
         * @param patient   the patient being saved
         * @param obsToSave
         * @param obsToVoid
         * @param question
         * @param savedObs
         * @param newValue
         */
        protected void handleOncePerPatientStringObs(Patient patient, List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
                                                     Obs savedObs, String newValue) {
            if (!OpenmrsUtil.nullSafeEquals(savedObs != null ? savedObs.getValueText() : null, newValue)) {
                // there was a change
                if (savedObs != null && newValue == null) {
                    // treat going from a value to null as voiding all past civil status obs
                    obsToVoid.addAll(Context.getObsService().getObservationsByPersonAndConcept(patient, question));
                }
                if (newValue != null) {
                    Obs o = new Obs();
                    o.setPerson(patient);
                    o.setConcept(question);
                    o.setObsDatetime(new Date());
                    o.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                    o.setValueText(newValue);
                    obsToSave.add(o);
                }
            }
        }

        public boolean isInNutritionProgram() {
            if (original == null || !original.isPatient()) {
                return false;
            }
            ProgramWorkflowService pws = Context.getProgramWorkflowService();
            Program nutrition = MetadataUtils.existing(Program.class, NutritionMetadata._Program.NUTRITION);
            for (PatientProgram pp : pws.getPatientPrograms((Patient) original, nutrition, null, null, null, null, false)) {
                if (pp.getActive()) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @return the original
         */
        public Person getOriginal() {
            return original;
        }

        /**
         * @param original the original to set
         */
        public void setOriginal(Patient original) {
            this.original = original;
        }

        /**
         * @return the personName
         */
        public PersonName getPersonName() {
            return personName;
        }

        /**
         * @param personName the personName to set
         */
        public void setPersonName(PersonName personName) {
            this.personName = personName;
        }

        /**
         * @return the patientClinicNumber
         */
        public String getPatientClinicNumber() {
            return patientClinicNumber;
        }

        /**
         * @param patientClinicNumber the patientClinicNumber to set
         */
        public void setPatientClinicNumber(String patientClinicNumber) {
            this.patientClinicNumber = patientClinicNumber;
        }

        /**
         * @return the hivIdNumber
         */
        public String getUniquePatientNumber() {
            return uniquePatientNumber;
        }

        /**
         * @param uniquePatientNumber the uniquePatientNumber to set
         */
        public void setUniquePatientNumber(String uniquePatientNumber) {
            this.uniquePatientNumber = uniquePatientNumber;
        }

        /**
         * @return the nationalIdNumber
         */
        public String getNationalIdNumber() {
            return nationalIdNumber;
        }

        /**
         * @param nationalIdNumber the nationalIdNumber to set
         */
        public void setNationalIdNumber(String nationalIdNumber) {

            this.nationalIdNumber = nationalIdNumber;
        }

        /**
         * @return the mobile number
         */
        public String getOtherNumber() {
            return otherNumber;
        }

        /**
         * @param otherNumber
         */
        public void setOtherNumber(String otherNumber) {
            this.otherNumber = otherNumber;
        }

        /**
         * @return the mobile number
         */
        public String getMobileNumber() {
            return mobileNumber;
        }

        /**
         * @param mobileNumber
         */
        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        /**
         * @return the birthdate
         */
        public Date getBirthdate() {
            return birthdate;
        }

        /**
         * @param birthdate the birthdate to set
         */
        public void setBirthdate(Date birthdate) {
            this.birthdate = birthdate;
        }

        /**
         * @return the birthdateEstimated
         */
        public Boolean getBirthdateEstimated() {
            return birthdateEstimated;
        }

        /**
         * @param birthdateEstimated the birthdateEstimated to set
         */
        public void setBirthdateEstimated(Boolean birthdateEstimated) {
            this.birthdateEstimated = birthdateEstimated;
        }

        /**
         * @return the gender
         */
        public String getGender() {
            return gender;
        }

        /**
         * @param gender the gender to set
         */
        public void setGender(String gender) {
            this.gender = gender;
        }

        /**
         * @return the personAddress
         */
        public PersonAddress getPersonAddress() {
            return personAddress;
        }

        /**
         * @param personAddress the personAddress to set
         */
        public void setPersonAddress(PersonAddress personAddress) {
            this.personAddress = personAddress;
        }

        /**
         * @return the maritalStatus
         */
        public Concept getMaritalStatus() {
            return maritalStatus;
        }

        /**
         * @param maritalStatus the maritalStatus to set
         */
        public void setMaritalStatus(Concept maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        /**
         * @return the education
         */
        public Concept getEducation() {
            return education;
        }

        /**
         * @param education the education to set
         */
        public void setEducation(Concept education) {
            this.education = education;
        }

        /**
         * @return the occupation
         */
        public Concept getOccupation() {
            return occupation;
        }

        /**
         * @param occupation the occupation to set
         */
        public void setOccupation(Concept occupation) {
            this.occupation = occupation;
        }

        /**
         * @return the telephoneContact
         */
        public String getTelephoneContact() {
            return telephoneContact;
        }

        /**
         * @param telephoneContact the telephoneContact to set
         */
        public void setTelephoneContact(String telephoneContact) {
            this.telephoneContact = telephoneContact;
        }

        public Boolean getDead() {
            return dead;
        }

        public void setDead(Boolean dead) {
            this.dead = dead;
        }

        public Date getDeathDate() {
            return deathDate;
        }

        public void setDeathDate(Date deathDate) {
            this.deathDate = deathDate;
        }

        /**
         * @return the nameOfNextOfKin
         */
        public String getNameOfNextOfKin() {
            return nameOfNextOfKin;
        }

        /**
         * @param nameOfNextOfKin the nameOfNextOfKin to set
         */
        public void setNameOfNextOfKin(String nameOfNextOfKin) {
            this.nameOfNextOfKin = nameOfNextOfKin;
        }

        /**
         * @return the nextOfKinRelationship
         */
        public String getNextOfKinRelationship() {
            return nextOfKinRelationship;
        }

        /**
         * @param nextOfKinRelationship the nextOfKinRelationship to set
         */
        public void setNextOfKinRelationship(String nextOfKinRelationship) {
            this.nextOfKinRelationship = nextOfKinRelationship;
        }

        /**
         * @return the nextOfKinContact
         */
        public String getNextOfKinContact() {
            return nextOfKinContact;
        }

        /**
         * @param nextOfKinContact the nextOfKinContact to set
         */
        public void setNextOfKinContact(String nextOfKinContact) {
            this.nextOfKinContact = nextOfKinContact;
        }

        /**
         * @return the nextOfKinAddress
         */
        public String getNextOfKinAddress() {
            return nextOfKinAddress;
        }

        /**
         * @param nextOfKinAddress the nextOfKinAddress to set
         */
        public void setNextOfKinAddress(String nextOfKinAddress) {
            this.nextOfKinAddress = nextOfKinAddress;
        }

        /**
         * @return the subChiefName
         */
        public String getSubChiefName() {
            return subChiefName;
        }

        /**
         * @param subChiefName the subChiefName to set
         */
        public void setSubChiefName(String subChiefName) {
            this.subChiefName = subChiefName;
        }

        /**
         * @return the passportNumber
         */
        public String getPassportNumber() {
            return passportNumber;
        }

        /**
         * Set the passportNumber
         *
         * @param passportNumber
         */
        public void setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
        }

        /**
         *
         * @return provide id
         */
        public String getProvider() {
            return provider;
        }

        /**
         *
         * @param provider
         */
        public void setProvider(String provider) {
            this.provider = provider;
        }

        public Concept getWellness() {
            return wellness;
        }

        public void setWellness(Concept wellness) {
            this.wellness = wellness;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getGoal_weight() {
            return goal_weight;
        }

        public void setGoal_weight(String goal_weight) {
            this.goal_weight = goal_weight;
        }

        public String getSystolic() {
            return systolic;
        }

        public void setSystolic(String systolic) {
            this.systolic = systolic;
        }

        public String getDiastolic() {
            return diastolic;
        }

        public void setDiastolic(String diastolic) {
            this.diastolic = diastolic;
        }

        public Concept getHbp() {
            return hbp;
        }

        public void setHbp(Concept hbp) {
            this.hbp = hbp;
        }

        public Concept getHeartCondition() {
            return heartCondition;
        }

        public void setHeartCondition(Concept heartCondition) {
            this.heartCondition = heartCondition;
        }

        public Concept getKidney() {
            return kidney;
        }

        public void setKidney(Concept kidney) {
            this.kidney = kidney;
        }

        public Concept getHysterectomy() {
            return hysterectomy;
        }

        public void setHysterectomy(Concept hysterectomy) {
            this.hysterectomy = hysterectomy;
        }

        public Concept getHighColesteral() {
            return highColesteral;
        }

        public void setHighColesteral(Concept highColesteral) {
            this.highColesteral = highColesteral;
        }

        public Concept getDepression() {
            return depression;
        }

        public void setDepression(Concept depression) {
            this.depression = depression;
        }

        public Concept getHypothyrodism() {
            return hypothyrodism;
        }

        public void setHypothyrodism(Concept hypothyrodism) {
            this.hypothyrodism = hypothyrodism;
        }

        public Concept getLiver() {
            return liver;
        }

        public void setLiver(Concept liver) {
            this.liver = liver;
        }

        public Concept getGallBladder() {
            return gallBladder;
        }

        public void setGallBladder(Concept gallBladder) {
            this.gallBladder = gallBladder;
        }

        public Concept getDiabetes() {
            return diabetes;
        }

        public void setDiabetes(Concept diabetes) {
            this.diabetes = diabetes;
        }

        public Concept getHyperthyrodism() {
            return hyperthyrodism;
        }

        public void setHyperthyrodism(Concept hyperthyrodism) {
            this.hyperthyrodism = hyperthyrodism;
        }

        public Concept getOtherCondition() {
            return otherCondition;
        }

        public void setOtherCondition(Concept otherCondition) {
            this.otherCondition = otherCondition;
        }

        public String getOtherConditionExplanation() {
            return otherConditionExplanation;
        }

        public void setOtherConditionExplanation(String otherConditionExplanation) {
            this.otherConditionExplanation = otherConditionExplanation;
        }

        public Concept getOtherOperations() {
            return otherOperations;
        }

        public void setOtherOperations(Concept otherOperations) {
            this.otherOperations = otherOperations;
        }

        public Concept getAllergies() {
            return allergies;
        }

        public void setAllergies(Concept allergies) {
            this.allergies = allergies;
        }

        public Concept getMedication() {
            return medication;
        }

        public void setMedication(Concept medication) {
            this.medication = medication;
        }

        public String getOtherOperationsExplanation() {
            return otherOperationsExplanation;
        }

        public void setOtherOperationsExplanation(String otherOperationsExplanation) {
            this.otherOperationsExplanation = otherOperationsExplanation;
        }

        public String getAllergiesExplanation() {
            return allergiesExplanation;
        }

        public void setAllergiesExplanation(String allergiesExplanation) {
            this.allergiesExplanation = allergiesExplanation;
        }

        public String getMedicationExplanation() {
            return medicationExplanation;
        }

        public void setMedicationExplanation(String medicationExplanation) {
            this.medicationExplanation = medicationExplanation;
        }

        public Concept getPregnancyStatus() {
            return pregnancyStatus;
        }

        public void setPregnancyStatus(Concept pregnancyStatus) {
            this.pregnancyStatus = pregnancyStatus;
        }

        public Concept getBreastFeeding() {
            return breastFeeding;
        }

        public void setBreastFeeding(Concept breastFeeding) {
            this.breastFeeding = breastFeeding;
        }

        public Concept getVegeterian() {
            return vegeterian;
        }

        public void setVegeterian(Concept vegeterian) {
            this.vegeterian = vegeterian;
        }

        public Concept getFish() {
            return fish;
        }

        public void setFish(Concept fish) {
            this.fish = fish;
        }

        public Concept getChicken() {
            return chicken;
        }

        public void setChicken(Concept chicken) {
            this.chicken = chicken;
        }

        public Concept getEggs() {
            return eggs;
        }

        public void setEggs(Concept eggs) {
            this.eggs = eggs;
        }

        public Concept getYoughurt() {
            return youghurt;
        }

        public void setYoughurt(Concept youghurt) {
            this.youghurt = youghurt;
        }

        public Concept getTofu() {
            return tofu;
        }

        public void setTofu(Concept tofu) {
            this.tofu = tofu;
        }

        public Concept getCheese() {
            return cheese;
        }

        public void setCheese(Concept cheese) {
            this.cheese = cheese;
        }

        public Concept getSoya() {
            return soya;
        }

        public void setSoya(Concept soya) {
            this.soya = soya;
        }

        public Concept getFacebook() {
            return facebook;
        }

        public void setFacebook(Concept facebook) {
            this.facebook = facebook;
        }

        public Concept getInstagram() {
            return instagram;
        }

        public void setInstagram(Concept instagram) {
            this.instagram = instagram;
        }

        public Concept getWebsite() {
            return website;
        }

        public void setWebsite(Concept website) {
            this.website = website;
        }

        public Concept getMagazine() {
            return magazine;
        }

        public void setMagazine(Concept magazine) {
            this.magazine = magazine;
        }

        public String getHeardFromPerson() {
            return heardFromPerson;
        }

        public void setHeardFromPerson(String heardFromPerson) {
            this.heardFromPerson = heardFromPerson;
        }

        public String getHeardFromOther() {
            return heardFromOther;
        }

        public void setHeardFromOther(String heardFromOther) {
            this.heardFromOther = heardFromOther;
        }

        public Concept getSupportPeriod() {
            return supportPeriod;
        }

        public void setSupportPeriod(Concept supportPeriod) {
            this.supportPeriod = supportPeriod;
        }

        public Concept getWeightBand() {
            return weightBand;
        }

        public void setWeightBand(Concept weightBand) {
            this.weightBand = weightBand;
        }


        public Concept getWhatsappGroup() {
            return whatsappGroup;
        }

        public void setWhatsappGroup(Concept whatsappGroup) {
            this.whatsappGroup = whatsappGroup;
        }

        public Concept getContactChannell() {
            return contactChannell;
        }

        public void setContactChannell(Concept contactChannell) {
            this.contactChannell = contactChannell;
        }
    }


}

