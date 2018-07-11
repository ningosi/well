package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.form.FormManager;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.wrapper.PatientWrapper;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientProfileFragmentController {
    private Log log = LogFactory.getLog(ClientProfileFragmentController.class);

    public void controller(@FragmentParam("patient") Patient patient,
                           PageRequest pageRequest,
                           UiUtils ui,
                           FragmentModel model) {
        String first_name = patient.getGivenName();
        String second_name = "";
        second_name = patient.getFamilyName();
        model.addAttribute("name", first_name);
        model.addAttribute("second_name", second_name);

        String pobox = "";
        String town = "";
        String home = "";
        String email = "";
        if (patient.getAddresses().size() > 0) {
            for (PersonAddress address : patient.getAddresses()) {
                if (address.getAddress1() != null) {
                    pobox = address.getAddress1();
                }
                if (address.getCityVillage() != null) {
                    town = address.getCityVillage();
                }
                if (address.getAddress2() != null) {
                    home = address.getAddress2();
                }
                if (address.getAddress3() != null) {
                    email = address.getAddress3();
                }

            }

        }

        PatientWrapper patientWrapper = new PatientWrapper(patient);
        model.addAttribute("email", email);
        model.addAttribute("box", pobox);
        model.addAttribute("town", town);
        model.addAttribute("home", home);
        model.addAttribute("mobileNumber", patientWrapper.getMobileNumber());
        model.addAttribute("otherMobileNumber", patientWrapper.getOtherMobileNumber());
        model.addAttribute("passport", patientWrapper.getPassportNumber());

        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
        try {

            InventoryService itemService = Context.getService(InventoryService.class);
            inventoryItems = itemService.getAllInventoryItems();
            if (inventoryItems != null) {
                log.error("Not null" + " " + inventoryItems.size());
            } else {
                log.error("Null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("inventoryItems", inventoryItems);

        String url = "/openmrs/ms/uiframework/resource/wellness/images/logos/passport.png";
        if(patient.getGender().equals("F")){
            url = "/openmrs/ms/uiframework/resource/wellness/images/logos/F.png";
        }
        String actualPhoto = "";
        String imgDir = OpenmrsUtil.getApplicationDataDirectory() + "patient_images";
        PersonAttributeType type = MetadataUtils.existing(PersonAttributeType.class, CommonMetadata._PersonAttributeType.PATIENT_IMAGE);
        PersonAttribute attribute = patient.getAttribute(type);

        if(attribute != null) {
            try {
                byte[] binaryData = IOUtils.toByteArray(new FileInputStream(imgDir+"/"+attribute.getValue()));
                byte[] encodeBase64 = Base64.encodeBase64(binaryData);
                actualPhoto = new String(encodeBase64, "UTF-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        model.addAttribute("url", actualPhoto);
        model.addAttribute("fakeUrl", url);
    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "description", required = false) String description) {

        return null;
    }

}
