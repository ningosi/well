package org.openmrs.module.wellness.fragment.controller.inventory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaui.form.AbstractWebForm;
import org.openmrs.module.wellnessinventory.api.model.InventoryItem;
import org.openmrs.module.wellnessinventory.api.model.ItemOrder;
import org.openmrs.module.wellnessinventory.api.service.InventoryOrderService;
import org.openmrs.module.wellnessinventory.api.service.InventoryService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class OrdersListFragmentController {
    private Log log = LogFactory.getLog(OrdersListFragmentController.class);

    public void controller(FragmentModel model) {
        List<ItemOrder> orders = new ArrayList<ItemOrder>();
        try {
            InventoryOrderService orderService = Context.getService(InventoryOrderService.class);
            orders = orderService.getAllOrders();
        }catch (Exception e){
            e.printStackTrace();
        }
        model.addAttribute("orders", orders);

    }

    public String post(FragmentModel model, UiUtils ui,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "description", required = false) String description) {

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
