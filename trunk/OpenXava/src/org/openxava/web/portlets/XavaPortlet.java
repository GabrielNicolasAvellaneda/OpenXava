package org.openxava.web.portlets;

import java.io.*;
import java.util.*;

import javax.portlet.*;

import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.portlet.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	
	private static Style style;
	private String moduleURL;
	
	private Log log = LogFactory.getLog(XavaPortlet.class);
	
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
		
		request.setAttribute("xava.portlet.renderURL", response.createRenderURL());
		request.setAttribute("xava.portlet.actionURL", response.createActionURL());
		request.setAttribute("xava.upload.fileitems", request.getPortletSession().getAttribute("xava.upload.fileitems")); 
		request.setAttribute("xava.upload.error", request.getPortletSession().getAttribute("xava.upload.error"));
		request.getPortletSession().removeAttribute("xava.upload.fileitems");
		request.getPortletSession().removeAttribute("xava.upload.error");		
		
		PortletContext context = getPortletContext();
		PortletRequestDispatcher rd = context.getRequestDispatcher(moduleURL);		
		rd.include(request, response);		
	}
	
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException {		
		PortletMode mode = request.getPortletMode();
		if (mode.equals(PortletMode.EDIT)) {
			response.setPortletMode(PortletMode.VIEW);
		}
		
		String contentType = request.getContentType();		
		if (contentType != null && contentType.indexOf("multipart/form-data") >= 0) {				
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1000000);		
				PortletFileUpload upload = new PortletFileUpload(factory);
				List fileItems = upload.parseRequest(request);					
				request.getPortletSession().setAttribute("xava.upload.fileitems", fileItems); 
				request.getPortletSession().removeAttribute("xava.upload.error"); 
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				request.getPortletSession().removeAttribute("xava.upload.fileitems");
				request.getPortletSession().setAttribute("xava.upload.error", "upload_error");				
			}				
		}
	} 	
		
	private Style getStyle(RenderRequest request) {
		if (style == null) {
			String portal = request.getPortalContext().getPortalInfo().toLowerCase();
			if (portal.indexOf("jetspeed") >= 0) style = JetSpeed2Style.getInstance();
			else if (portal.indexOf("websphere") >= 0) style = WebSpherePortalStyle.getInstance();
			else if (portal.indexOf("liferay") >= 0) style = LiferayStyle.getInstance();
			else style = Style.getInstance();
		}
		return style;
	}
	
}
