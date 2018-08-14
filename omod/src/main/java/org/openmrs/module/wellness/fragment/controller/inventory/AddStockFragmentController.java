package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.wellness.EmrConstants;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemStockDetails;
import org.openmrs.module.wellnessinventory.api.model.ItemType;
import org.openmrs.module.wellnessinventory.api.model.ItemUnit;
import org.openmrs.module.wellnessinventory.api.service.InventoryItemTypeService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.module.wellnessinventory.api.service.ItemUnitService;
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
        List<ItemUnit> itemUnits = new ArrayList<ItemUnit>();
        try {

            InventoryItemTypeService itemTypeService = Context.getService(InventoryItemTypeService.class);
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            itemTypes = itemTypeService.getAllItemTypes();
            itemUnits = itemUnitService.getAllUnits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("itemTypes", itemTypes);
        model.addAttribute("itemUnits", itemUnits);

    }

    public void post(FragmentModel model, UiUtils ui,
                     @RequestParam(value = "name") String name,
                     @RequestParam(value = "code", required = false) String code,
                     @RequestParam(value = "description", required = false) String description,
                     @RequestParam(value = "quantity") int quantity,
                     @RequestParam(value = "type") int type,
                     @RequestParam(value = "unit") int unit,
                     @RequestParam(value = "expiration", required = false) String expiration,
                     @RequestParam(value = "minStock", required = false) String minStock) {
        log.error("Posting " + name);
        InventoryItem inventoryItem = new InventoryItem();
        User user = Context.getAuthenticatedUser();
        inventoryItem.setDescription(description);
        inventoryItem.setName(name);
        inventoryItem.setItemCode(code);
        inventoryItem.setUuid(String.valueOf(UUID.randomUUID()));

        ItemStockDetails itemStockDetails = new ItemStockDetails();
        String stockName = String.format("Stock_%s", name);
        itemStockDetails.setName(stockName);
        itemStockDetails.setQuantity(quantity);
        itemStockDetails.setUuid(String.valueOf(UUID.randomUUID()));
        itemStockDetails.setInventoryItem(inventoryItem);
        if(minStock != null) {
            itemStockDetails.setMinStock(Integer.parseInt(minStock));
        }


        try {
            InventoryService inventoryService = Context.getService(InventoryService.class);
            InventoryItemTypeService itemTypeService = Context.getService(InventoryItemTypeService.class);
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            ItemType itemType = itemTypeService.getItemType(type);
            inventoryItem.setItemType(itemType);
            ItemUnit itemUnit = itemUnitService.getItemUnit(unit);
            itemStockDetails.setItemUnit(itemUnit);

            inventoryService.saveInventoryItem(inventoryItem);
            inventoryService.saveItemStockDetail(itemStockDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return "redirect:" + ui.pageLink(EmrConstants.MODULE_ID, "inventory/inventoryList" );
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
