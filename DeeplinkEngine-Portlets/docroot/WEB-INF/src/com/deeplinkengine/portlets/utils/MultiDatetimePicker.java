package com.deeplinkengine.portlets.utils;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class MultiDatetimePicker
 */
public class MultiDatetimePicker extends MVCPortlet {

	 
	
	@Override
	public void doEdit(RenderRequest renderRequest,
		RenderResponse renderResponse) throws IOException, PortletException {
		
		PortletURL settings = renderResponse.createActionURL();
		settings.setParameter("settings", "settings");
		renderRequest.setAttribute("settingsUrl", settings.toString());
		PortletPreferences prefs = renderRequest.getPreferences();
		renderRequest
				.setAttribute("settings", prefs.getValue("settings", "{}"));
		renderRequest
		.setAttribute("jsid", prefs.getValue("jsid", null));
		
		super.doEdit(renderRequest, renderResponse);
	}
	
	@Override
	public void processAction(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		if (actionRequest.getParameter("settings") != null) {
			PortletPreferences prefs = actionRequest.getPreferences();
			prefs.setValue("settings", actionRequest.getParameter("settings"));
			prefs.setValue("jsid", actionRequest.getParameter("jsid"));
			prefs.store();
			actionResponse.setPortletMode(PortletMode.VIEW);
		}
		super.processAction(actionRequest, actionResponse);
	}
	
	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		// Portlet id
		ThemeDisplay themeDisplay= (ThemeDisplay)renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
		PortletDisplay portletDisplay= themeDisplay.getPortletDisplay();
		String portletId= portletDisplay.getId(); 
		renderRequest.setAttribute("portletId", portletId);
		// settings
		PortletPreferences prefs = renderRequest.getPreferences();
		renderRequest.setAttribute("settings", prefs.getValue("settings", "{}"));
		renderRequest.setAttribute("jsid", prefs.getValue("jsid", null));
		
		super.doView(renderRequest, renderResponse);
	}
}
