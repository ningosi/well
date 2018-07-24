package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemStockDetails;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.model.ItemUnit;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.module.wellnessinventory.api.service.ItemUnitService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EditStockFragmentController {
    private Log log = LogFactory.getLog(EditStockFragmentController.class);

    public void controller(@FragmentParam(value = "itemId", required = false) int itemId,
                           FragmentModel model){
        List<ItemType> itemTypes = new ArrayList<ItemType>();
        List<ItemUnit> itemUnits = new ArrayList<ItemUnit>();
        InventoryItem item = null;
        try {
            InventoryService inventoryService = Context.getService(InventoryService.class);
            InventoryItemTypeService itemTypeService = Context.getService(InventoryItemTypeService.class);
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            item = inventoryService.getInventoryItem(itemId);
            itemTypes = itemTypeService.getAllItemTypes();
            itemUnits = itemUnitService.getAllUnits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(ItemType itemType :itemTypes){
            log.error(" Id " + itemType.getId());
        }
        model.addAttribute("item", item);
        model.addAttribute("itemTypes", itemTypes);
        model.addAttribute("itemUnits", itemUnits);

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "itemId") int itemId,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "description", required = false) String description,
                       @RequestParam(value = "quantity") int quantity,
                       @RequestParam(value = "type", required = false) int type,
                       @RequestParam(value = "unit", required = false) int unit,
                       @RequestParam(value = "expiration", required = false) String expiration) {

        try {
            InventoryService inventoryService = Context.getService(InventoryService.class);
            InventoryItem inventoryItem = inventoryService.getInventoryItem(itemId);
            Iterator<ItemStockDetails> iterator = inventoryItem.getDetails().iterator();
            if(iterator.hasNext()){
                ItemStockDetails stockDetails = iterator.next();
                int newQuantity = stockDetails.getQuantity() + quantity;
                stockDetails.setQuantity(newQuantity);
                inventoryService.saveItemStockDetail(stockDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("Posting " + name);
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
