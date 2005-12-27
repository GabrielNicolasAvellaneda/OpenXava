package org.openxava.web.portlets;

import java.io.*;
import javax.portlet.*;
import org.openxava.web.style.*;

/**
 * Allows define an OpenXava as a standard JSR-168 portlet. <p>
 * 
 * You only need to define the OpenXava application and module.<br> 
 * In this way:
 *
 * <pre>
 *  <!-- Portlet Preferences -->
 *  <portlet-preferences>
 *    <preference>
 *      <description>OpenXava application name</description>
 *      <name>Application</name>
 *      <value>MyApplication</value>
 *      <non-modifiable/>
 *    </preference>
 *    <preference>
 *      <description>OpenXava module name</description>
 *      <name>Module</name>
 *      <value>MyModule</value>
 *      <non-modifiable/>
 *    </preference>
 *  </portlet-preferences>
 * </pre>
 *
 * @author  Javier Paniza
 */

public class XavaPortlet extends GenericPortlet {

	/**
	 * Name of portlet preference for OpenXava application. 
	 */
	public static final String PARAM_APPLICATION = "Application";
	
	/**
	 * Name of portlet preference for OpenXava module. 
	 */
	public static final String PARAM_MODULE = "Module";
	
	private String moduleURL;

	private static Style style;
	
	public void init(PortletConfig config) throws PortletException {
		super.init(config);		
		this.moduleURL = "/WEB-INF/jsp/xava/module.jsp?application=" +
			config.getInitParameter(PARAM_APPLICATION) + "&module=" +			
			config.getInitParameter(PARAM_MODULE);
	}

	/**
	 * Executes the OpenXava module as defined by the init parameters PARAM_APPLICATION
	 * and PARAM_MODULE.
	 * 
	 * @throws PortletException
	 * @throws IOException
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		Object style = getStyle(request);		
		request.setAttribute("style", style);		
		PortletURL url = response.createRenderURL();
		request.setAttribute("xava.formAction", "action='" + url.toString() + "'");		
		PortletContext context = getPortletContext();
		PortletRequestDispatcher rd = context.getRequestDispatcher(moduleURL);
		rd.include(request, response);
	}
	
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException {
		PortletMode mode = request.getPortletMode();
		if (mode.equals(PortletMode.EDIT)) {
			response.setPortletMode(PortletMode.VIEW);
		}
	} 	
		
	private Style getStyle(RenderRequest request) {
		if (style == null) {
			String portal = request.getPortalContext().getPortalInfo().toLowerCase();			
			if (portal.indexOf("jetspeed") >= 0) style = JetSpeed2Style.getInstance();
			else if (portal.indexOf("websphere") >= 0) style = WebSpherePortalStyle.getInstance();
			else style = Style.getInstance();
		}
		return style;
	}
	
}
