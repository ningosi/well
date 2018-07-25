package org.openmrs.module.wellness.fragment.controller.inventory;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.wellness.metadata.CommonMetadata;
import org.openmrs.module.wellness.wrapper.PatientWrapper;
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
import org.openmrs.util.OpenmrsUtil;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InventoryDispenseFragmentController {
    private Log log = LogFactory.getLog(InventoryDispenseFragmentController.class);

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

        for (InventoryItem inventoryItem : inventoryItems) {
            Set<ItemStockDetails> detailsSet = inventoryItem.getDetails();
            if (detailsSet.iterator().hasNext()) {
                ItemStockDetails stockDetails = detailsSet.iterator().next();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", inventoryItem.getId());
                jsonObject.put("val", stockDetails.getQuantity());
                itemStock.add(jsonObject);
            }
        }
        model.addAttribute("itemStock", itemStock.toString());
        log.error("Item stock " + itemStock.toJSONString());
        model.addAttribute("inventoryItems", inventoryItems);
        model.addAttribute("itemUnits", itemUnits);
        model.addAttribute("client", patient);

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
