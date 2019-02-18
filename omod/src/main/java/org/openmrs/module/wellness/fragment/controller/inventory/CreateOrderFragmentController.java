package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemOrder;
import org.openmrs.module.wellnessinventory.api.model.ItemStockDetails;
import org.openmrs.module.wellnessinventory.api.model.ItemUnit;
import org.openmrs.module.wellnessinventory.api.service.InventoryOrderService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.module.wellnessinventory.api.service.ItemUnitService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CreateOrderFragmentController {
    private Log log = LogFactory.getLog(CreateOrderFragmentController.class);

    public void controller(@FragmentParam("patient") Patient patient,
                           PageRequest pageRequest,
                           UiUtils ui,
                           FragmentModel model) {

        List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
        List<ItemUnit> itemUnits = new ArrayList<ItemUnit>();
        JSONArray itemStock = new JSONArray();
        try {

            InventoryService itemService = Context.getService(InventoryService.class);
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            inventoryItems = itemService.getAllInventoryItems();
            itemUnits = itemUnitService.getAllUnits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String initial = "1";
        for (InventoryItem inventoryItem : inventoryItems) {

            Set<ItemStockDetails> detailsSet = inventoryItem.getDetails();
            if (detailsSet.iterator().hasNext()) {
                ItemStockDetails stockDetails = detailsSet.iterator().next();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", inventoryItem.getId());
                jsonObject.put("val", stockDetails.getQuantity());
                itemStock.add(jsonObject);
                if(inventoryItems.indexOf(inventoryItem) == 0){
                    initial = String.valueOf(stockDetails.getQuantity());
                }
            }
        }
        for(ItemUnit itemUnit : itemUnits){
            log.error("Item unit" + itemUnit.getId());
        }
        log.error("item stock " + itemStock.toString());
        model.addAttribute("itemStock", itemStock.toString());
        model.addAttribute("inventoryItems", inventoryItems);
        model.addAttribute("itemUnits", itemUnits);
        model.addAttribute("client", patient);
        model.addAttribute("initialMax", initial);

        String delivery_addreess = null;
        if (patient.getAddresses().size() > 0) {
            for (PersonAddress address : patient.getAddresses()) {
                if (address.getAddress2() != null) {
                    delivery_addreess = address.getAddress2();
                }
            }
        }
        model.addAttribute("delivery_addreess", delivery_addreess);


        List<String> paymentOptions = new ArrayList<String>();
        paymentOptions.add("Cash");
        paymentOptions.add("Till");
        paymentOptions.add("PDQ");
        model.addAttribute("paymentOptions", paymentOptions);


    }

    public String post(HttpServletRequest request,
                       FragmentModel model, UiUtils ui,
                       @RequestParam(value = "address", required = false) String address,
                       @RequestParam(value = "quantity", required = false) int quantity,
                       @RequestParam(value = "payment-mode", required = false) String paymentMode,
                       @RequestParam(value = "client", required = false) int clientId,
                       @RequestParam(value = "unit") int unit,
                       @RequestParam(value = "isDelivery", required = false) Boolean isDelivery) {

        String supplements = request.getParameter("item");
        log.error("client id" + clientId);
        Patient patient = Context.getPatientService().getPatient(clientId);
        try {

            InventoryService itemService = Context.getService(InventoryService.class);
            InventoryOrderService orderService = Context.getService(InventoryOrderService.class);
            ItemUnitService itemUnitService = Context.getService(ItemUnitService.class);
            InventoryItem inventoryItem = itemService.getInventoryItem(Integer.valueOf(supplements));
            ItemUnit itemUnit = itemUnitService.getItemUnit(unit);

            ItemOrder order = new ItemOrder();
            order.setAddress(address);
            order.setInventoryItem(inventoryItem);
            order.setPaymentMode(paymentMode);
            order.setQuantity(quantity);
            order.setDelivery(isDelivery);
            order.setClient(patient);
            order.setItemUnit(itemUnit);
            orderService.saveOrder(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
