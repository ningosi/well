package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemStockDetails;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddStockFragmentController {
    private Log log = LogFactory.getLog(AddStockFragmentController.class);

    public void controller(FragmentModel model) {
        List<ItemType> itemTypes = new ArrayList<ItemType>();
        try {

            InventoryItemTypeService itemTypeService = Context.getService(InventoryItemTypeService.class);
            itemTypes = itemTypeService.getAllItemTypes();
            if (itemTypes != null) {
                log.error("Not null" + " " + itemTypes.size());
            } else {
                log.error("Null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("itemTypes", itemTypes);

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = true) String name,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "description", required = false) String description,
                       @RequestParam(value = "quantity", required = true) int quantity,
                       @RequestParam(value = "expiration", required = false) String expiration) {
        log.error("Posting " + name);
        InventoryItem inventoryItem = new InventoryItem();
        User user = Context.getAuthenticatedUser();
        inventoryItem.setDescription(description);
        inventoryItem.setName(name);
        inventoryItem.setItemCode(code);
        inventoryItem.setUuid(String.valueOf(UUID.randomUUID()));

        ItemStockDetails itemStockDetails = new ItemStockDetails();
        itemStockDetails.setQuantity(quantity);
        itemStockDetails.setUuid(String.valueOf(UUID.randomUUID()));
        itemStockDetails.setInventoryItem(inventoryItem);


        try {
            InventoryService inventoryService = Context.getService(InventoryService.class);
            inventoryService.saveInventoryItem(inventoryItem);
            inventoryService.saveItemStockDetail(itemStockDetails);
        } catch (Exception e) {
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
