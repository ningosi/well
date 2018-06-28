package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.wellnessinventory.Item;
import org.openmrs.module.wellnessinventory.api.WellnessinventoryService;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class AddStockFragmentController {
    private Log log = LogFactory.getLog(AddStockFragmentController.class);

    public void controller(FragmentModel model) {

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "description", required = false) String description) {
        log.error("Posting " + name);
        InventoryItem inventoryItem = new InventoryItem();
        User user = Context.getAuthenticatedUser();
        inventoryItem.setDescription(description);
        inventoryItem.setName(name);
        inventoryItem.setItemCode(code);

        try{
            InventoryItemService wellnessinventoryService = Context.getService(InventoryItemService.class);
            wellnessinventoryService.saveInventoryItem(inventoryItem);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public class AddStockForm extends AbstractWebForm {

        @Override
        public Object save() {
            return null;
        }

        @Override
        public void validate(Object target, Errors errors) {

        }
    }
}
