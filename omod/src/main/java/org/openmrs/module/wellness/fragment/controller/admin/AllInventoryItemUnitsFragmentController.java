package org.openmrs.module.wellness.fragment.controller.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.wellness.fragment.controller.inventory.AddStockFragmentController;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.model.ItemUnit;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.ItemUnitService;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.List;

public class AllInventoryItemUnitsFragmentController {
    private Log log = LogFactory.getLog(AddStockFragmentController.class);

    public void controller(FragmentModel model) {
        List<ItemUnit> itemUnits = new ArrayList<ItemUnit>();
        try {

            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            itemUnits = itemUnitService.getAllUnits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("itemUnits", itemUnits);
    }
}
