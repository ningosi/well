package org.openmrs.module.wellness.fragment.controller.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.wellness.fragment.controller.inventory.AddStockFragmentController;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemStockDetails;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddInventoryItemTypeFragmentController {
    private Log log = LogFactory.getLog(AddStockFragmentController.class);

    public void controller(FragmentModel model) {

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = true) String name,
                       @RequestParam(value = "description", required = false) String description) {
        log.error("Posting " + name);
        ItemType itemType = new ItemType();
        itemType.setName(name);
        itemType.setUuid(String.valueOf(UUID.randomUUID()));

        try {
            InventoryItemTypeService itemTypeService = Context.getService(InventoryItemTypeService.class);
            itemTypeService.saveItemType(itemType);
            log.error("Saving " + itemType.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
