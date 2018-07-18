package org.openmrs.module.wellness.fragment.controller.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.wellness.fragment.controller.inventory.AddStockFragmentController;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.model.ItemUnit;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.ItemUnitService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public class AddInventoryItemUnitFragmentController {
    private Log log = LogFactory.getLog(AddStockFragmentController.class);

    public void controller(FragmentModel model) {

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = true) String name,
                       @RequestParam(value = "description", required = false) String description) {
        ItemUnit itemUnit = new ItemUnit();
        itemUnit.setName(name);
        itemUnit.setDescription(description);

        try {
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            itemUnitService.saveItemUnit(itemUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
