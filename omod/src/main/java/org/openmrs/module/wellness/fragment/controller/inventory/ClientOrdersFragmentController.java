package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.Patient;
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

public class ClientOrdersFragmentController {
    private Log log = LogFactory.getLog(ClientOrdersFragmentController.class);

    public void controller(@FragmentParam("patient") Patient patient,
                           PageRequest pageRequest,
                           UiUtils ui,
                           FragmentModel model) {

        List<ItemOrder> clientOrders = new ArrayList<ItemOrder>();
        try {
            InventoryOrderService orderService = Context.getService(InventoryOrderService.class);
            clientOrders = orderService.getClientOrders(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("clientOrders", clientOrders);

    }

    public String post(HttpServletRequest request,
                       FragmentModel model, UiUtils ui) {

        return null;
    }
}
