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

package org.openmrs.module.wellness.page.controller;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.wellness.EmrConstants;
import org.openmrs.module.wellness.api.KenyaEmrService;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsUtil;

import javax.servlet.http.HttpSession;

/**
 * Home page controller
 */
public class HomePageController {

	public String controller(PageModel model, UiUtils ui, HttpSession session, @SpringBean KenyaUiUtils kenyaUi) {

		// Redirect to setup page if module is not yet configured
		if (Context.getService(KenyaEmrService.class).isSetupRequired()) {
			kenyaUi.notifySuccess(session, "First-Time Setup Needed");
			return "redirect:" + ui.pageLink(EmrConstants.MODULE_ID, "admin/firstTimeSetup");
		}

		// Get apps for the current user
		List<AppDescriptor> apps = Context.getService(AppFrameworkService.class).getAppsForCurrentUser();

		// Sort by order property
		Collections.sort(apps, new Comparator<AppDescriptor>() {
			@Override
			public int compare(AppDescriptor left, AppDescriptor right) {
				return OpenmrsUtil.compareWithNullAsGreatest(left.getOrder(), right.getOrder());
			}
		});
        User user = Context.getAuthenticatedUser();
        Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(user.getPerson());
        String role = "";
//        if(user.isSuperUser()){
//            role = "Super User";
//        }else if(!providers.isEmpty()){
//            role="provider";
//            for (Provider provider: providers){
//                role  = role + "" + provider.getId();
//            }
//        }
        if(user.hasRole("Support")){
            role = "support";
        }else{
            role = "not support";
        }
		String user_name = Context.getAuthenticatedUser().getGivenName();

        for (Iterator<AppDescriptor> iterator = apps.iterator(); iterator.hasNext(); ) {
            AppDescriptor app = iterator.next();
            if (app.getIcon() == null) {
                iterator.remove();
            }
        }

		model.addAttribute("apps", apps);
		model.addAttribute("user",user_name);
		model.addAttribute("role",role);
		
		return null;
	}
}
