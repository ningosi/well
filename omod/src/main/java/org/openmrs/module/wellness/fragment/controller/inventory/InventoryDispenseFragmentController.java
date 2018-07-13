package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.wrapper.PatientWrapper;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class InventoryDispenseFragmentController {
    private Log log = LogFactory.getLog(InventoryDispenseFragmentController.class);

    public void controller(@FragmentParam("patient") Patient patient,
                           PageRequest pageRequest,
                           UiUtils ui,
                           FragmentModel model) {

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


    }

    public String post(HttpServletRequest request,
                       FragmentModel model, UiUtils ui,
                       @RequestParam(value = "address", required = false) String address,
                       @RequestParam(value = "quantity", required = false) String quantity,
                       @RequestParam(value = "payment-mode", required = false) String paymentMode) {

        String[] supplements = request.getParameterValues("item[]");

        return null;
    }
}
