package com.deeplinkengine.portlets.offer;

import java.io.IOException;
import java.util.Arrays;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class CalendarManager
 */
public class CalendarManager extends MVCPortlet {
	enum RESOURCES {
		LOAD, SAVE
	};

	@Override
	public void doEdit(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletURL settings = renderResponse.createActionURL();
		settings.setParameter("settings", "settings");
		renderRequest.setAttribute("settingsUrl", settings.toString());
		PortletPreferences prefs = renderRequest.getPreferences();
		renderRequest.setAttribute("jsid", prefs.getValue("jsid", null));

		super.doEdit(renderRequest, renderResponse);
	}

	@Override
	public void processAction(ActionRequest actionRequest,
			ActionResponse actionResponse) throws IOException, PortletException {
		if (actionRequest.getParameter("settings") != null) {
			PortletPreferences prefs = actionRequest.getPreferences();
			prefs.setValue("jsid", actionRequest.getParameter("jsid"));
			prefs.store();
			actionResponse.setPortletMode(PortletMode.VIEW);
		}
		super.processAction(actionRequest, actionResponse);
	}

	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
		// Add ajax actionURL
		for (RESOURCES r : RESOURCES.values()) {
			ResourceURL ajaxCall = renderResponse.createResourceURL();
			ajaxCall.setResourceID(r.toString());
			renderRequest.setAttribute(r.toString(), ajaxCall.toString());
		}
		// settings
		PortletPreferences prefs = renderRequest.getPreferences();
		renderRequest.setAttribute("jsid", prefs.getValue("jsid", ""));
		
		renderRequest.setAttribute("calendars", Arrays.asList(new String[]{"Calendar 1", "Calendar 2", "Calendar 3"}));

		super.doView(renderRequest, renderResponse);
	}

	@Override
	public void serveResource(ResourceRequest resourceRequest,
			ResourceResponse resourceResponse) throws IOException,
			PortletException {
		switch (RESOURCES.valueOf(resourceRequest.getResourceID())) {
		case LOAD:
			resourceResponse
					.getWriter()
					.write("[{from:\"01/01/2010\", to:\"01/02/2010\"}, {from:\"01/10/2010\", to:\"01/12/2010\"}]");
			return;
		case SAVE:
			resourceResponse.getWriter().write("OK");
			return;
		}
		super.serveResource(resourceRequest, resourceResponse);
	}

}
