/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.wellness.page.controller.inventory;

import org.openmrs.Patient;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.wellness.EmrConstants;
import org.openmrs.module.wellness.EmrWebConstants;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Homepage for the inventory app
 */
@AppPage(EmrConstants.APP_INVENTORY)
public class InventoryHomePageController {

	public String controller(UiUtils ui, PageModel model) {

		Patient patient = (Patient) model.getAttribute(EmrWebConstants.MODEL_ATTR_CURRENT_PATIENT);

//		if (patient != null) {
//			return "redirect:" + ui.pageLink(EmrConstants.MODULE_ID, "registration/registrationViewPatient", SimpleObject.create("patientId", patient.getId()));
//		} else {
//			return null;
//		}
        return null;
	}
}
