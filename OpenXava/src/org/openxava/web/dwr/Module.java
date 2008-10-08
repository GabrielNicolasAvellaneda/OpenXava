package org.openxava.web.dwr;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;
import org.openxava.view.*;
import org.openxava.web.servlets.*;
import org.openxava.web.style.*;

/**
 * For accessing to module execution from DWR. <p>
 * 
 * @author Javier Paniza
 */

public class Module extends DWRBase {

	final private static String MESSAGES_LAST_REQUEST ="xava_messagesLastRequest";
	final private static String ERRORS_LAST_REQUEST ="xava_errorsLastRequest";
	
	private static boolean portlet;
	private static Style style;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String application;
	private String module;
	private ModuleManager manager;
	
	public Map request(HttpServletRequest request, HttpServletResponse response, String application, String module, Map values, Map multipleValues, String [] selected) throws Exception {		
		try {
			checkSecurity(request, application, module);
			this.request = request;
			this.response = response;
			this.application = application;
			this.module = module;		
			this.manager = (ModuleManager) getContext(request).get(application, module, "manager");
			restoreLastMessages();
			request.setAttribute("style", getStyle());
			getURIAsStream("init.jsp", values, multipleValues, selected);
			Map result = new HashMap();
			String forwardURI = (String) request.getSession().getAttribute("xava_forward");		
			if (!Is.emptyString(forwardURI)) {
				result.put("xava_forward_url",  request.getScheme() + "://" + 
					request.getServerName() + ":" + request.getServerPort() + 
					request.getContextPath() + forwardURI);
				result.put("xava_forward_inNewWindow", request.getSession().getAttribute("xava_forward_inNewWindow"));
				request.getSession().removeAttribute("xava_forward");
				request.getSession().removeAttribute("xava_forward_inNewWindow");				
			}
			else {
				fillResult(result, values, multipleValues, selected);
			}								
			return result;
		}
		finally {			
			if (manager != null) manager.commit(); // If hibernate, jpa, etc is used to render some value here is commit
		}
	}	
	
	public void requestMultipart(HttpServletRequest request, HttpServletResponse response, String application, String module) throws Exception { 
		request(request, response, application, module, null, null, null);
		memorizeLastMessages();		
	}	

	private InputStream getURIAsStream(String jspFile, Map values, Map multipleValues, String[] selected) throws Exception {
		return Servlets.getURIAsStream(request, response, getURI(jspFile, values, multipleValues, selected));
	}
	
	private String getURIAsString(String jspFile, Map values, Map multipleValues, String[] selected) throws Exception {
		if (jspFile == null) return "";
		if (jspFile.startsWith("html:")) return jspFile.substring(5); // Using html: prefix the content is returned as is
		return InputStreams.toString(getURIAsStream(jspFile, values, multipleValues, selected));
	}
	

	private void fillResult(Map result, Map values, Map multipleValues, String[] selected) throws Exception {
		for (Iterator it = getChangedParts(values).entrySet().iterator(); it.hasNext(); ) {
			Map.Entry changedPart = (Map.Entry) it.next();			
			result.put(changedPart.getKey(),
				getURIAsString((String) changedPart.getValue(), values, multipleValues, selected)	
			);
			
		}		
	}


	private Map getChangedParts(Map values) { 
		Map result = new HashMap(); 
		if (values == null || manager.isReloadAllUINeeded() || manager.isFormUpload()) {  		
			result.put("xava_core", "core.jsp");
		}
		else {
			if (manager.isActionsChanged()) {
				result.put("xava_button_bar", "buttonBar.jsp");
				result.put("xava_bottom_buttons", "bottomButtons.jsp");
			}			
			Messages errors = (Messages) request.getAttribute("errors");
			result.put("xava_errors", errors.contains()?"errors.jsp":null);
			Messages messages = (Messages) request.getAttribute("messages");
			result.put("xava_messages", messages.contains()?"messages.jsp":null);
			if (manager.isReloadViewNeeded() || getView().isReloadNeeded()) { 
				result.put("xava_view", manager.getViewURL());
			}
			else {
				fillChangedPropertiesAndDescriptionsListReferences(result);
				fillChangedCollections(result);
				fillChangedSections(result);
				fillChangedErrorImages(result);
			}
		}
		return result;
	}

	private void fillChangedErrorImages(Map result) { 
		Messages errors = (Messages) request.getAttribute("errors");
		String imageHTML = "html:<img src='" + request.getContextPath() +"/xava/images/error.gif'/>";
		for (Iterator it=errors.getMembers().iterator(); it.hasNext(); ) {
			Object member = it.next();
			result.put("xava_error_image_" + member, imageHTML);							
		}				
	}

	private void fillChangedPropertiesAndDescriptionsListReferences(Map result) {
		View view = getView();		
		Collection changedMembers = view.getChangedPropertiesAndDescriptionsListReferences().entrySet();
		for (Iterator it = changedMembers.iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			String qualifiedName = (String) en.getKey();
			String name = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
			View containerView = (View) en.getValue();
			if (containerView.getMetaModel().containsMetaReference(name)) {				
				request.setAttribute(qualifiedName, containerView.getMetaReference(name));
				result.put("xava_descriptions_list_" + qualifiedName, 
					"descriptionsList.jsp?referenceKey=" + qualifiedName + 
					"&onlyEditor=true&viewObject=" + containerView.getViewObject());					
			}
			else {
				result.put("xava_editor_" + qualifiedName, 
					"editorWrapper.jsp?propertyName=" + name + 
					"&editable=" + containerView.isEditable(name) +
					"&throwPropertyChanged=" + containerView.throwsPropertyChanged(name) +
					"&viewObject=" + containerView.getViewObject() + 
					"&propertyPrefix=" + containerView.getPropertyPrefix());
			}
			result.put("xava_error_image_" + containerView.getMetaModel().getName() + "." + name, null);	
		}
	}
	
	private void fillChangedCollections(Map result) {				
		View view = getView();			
		Collection changedCollections = view.getChangedCollections().entrySet(); 		
		for (Iterator it = changedCollections.iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			String qualifiedName = (String) en.getKey();
			String name = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
			View containerView = (View) en.getValue();
			result.put("xava_collection_" + qualifiedName + ".", 
				"collection.jsp?collectionName=" + name + 
				"&viewObject=" + containerView.getViewObject() + 
				"&propertyPrefix=" + containerView.getPropertyPrefix());			
		}
	}
	
	private void fillChangedSections(Map result) {
		View view = getView();			
		String changedSections = view.getChangedSectionsViewObject();
		if (changedSections != null) {
			result.put("xava_sections_" + changedSections, 
				"sections.jsp?viewObject=" + changedSections);
		}
	}			
		
	private View getView() {
		return (View) getContext(request).get(application, module, "xava_view");
	}
	
	private void memorizeLastMessages() { 
		ModuleContext context = getContext(request);		
		Object messages = request.getAttribute("messages");
		if (messages != null) { 
			context.put(application, module, MESSAGES_LAST_REQUEST, messages);
		}
		Object errors = request.getAttribute("errors");
		if (errors != null) {
			context.put(application, module, ERRORS_LAST_REQUEST, errors);
		}			
	}
	
	private void restoreLastMessages() { 
		ModuleContext context = getContext(request);		
		if (context.exists(application, module, MESSAGES_LAST_REQUEST)) {
			Messages messages = (Messages) context.get(application, module, MESSAGES_LAST_REQUEST);
			request.setAttribute("messages", messages);
			context.remove(application, module, MESSAGES_LAST_REQUEST);			
		}
		if (context.exists(application, module, ERRORS_LAST_REQUEST)) {
			Messages errors = (Messages) context.get(application, module, ERRORS_LAST_REQUEST);
			request.setAttribute("errors", errors);
			context.remove(application, module, ERRORS_LAST_REQUEST);			
		}		
	}
	
	private String getURI(String jspFile, Map values, Map multipleValues, String[] selected) {
		StringBuffer result = new StringBuffer(getURIPrefix());
		result.append(jspFile);
		if (jspFile.endsWith(".jsp")) result.append('?');
		else result.append('&');
		result.append("application=");
		result.append(application);
		result.append("&module=");
		result.append(module);
		addValuesQueryString(result, values, multipleValues, selected);		
		return result.toString();
	}

	private static String getURIPrefix() {		
		return isPortlet()?"/WEB-INF/jsp/xava/":"/xava/";
	}

	private static void addValuesQueryString(StringBuffer sb, Map values, Map multipleValues, String [] selected) {
		if (values == null) return;
		if (multipleValues != null) {			
			for (Iterator it=multipleValues.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry en = (Map.Entry) it.next();			
				addMultipleValuesQueryString(sb, en.getKey(), en.getValue());
				values.remove(en.getKey());
			}					
		}
		for (Iterator it=values.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			if (!en.getKey().toString().equals("xava_selected")) { 
				sb.append('&');
				sb.append(en.getKey()); 
				sb.append('=');
				sb.append(filterValue(en.getValue()));
			}
		}
		for (int i=0; i<selected.length; i++) {
			String [] s = selected[i].split(":");
			sb.append('&');
			sb.append(s[0]);
			sb.append('=');
			sb.append(s[1]);
		}							
	}
	
	private static void addMultipleValuesQueryString(StringBuffer sb, Object key, Object value) {
		if (value == null) return;
		String [] tokens = value.toString().split("\n");
		for (int i=1; i< tokens.length - 1; i++) {
			sb.append('&');
			sb.append(key);
			sb.append('=');			
			sb.append(tokens[i].substring(tokens[i].indexOf('"') + 1, tokens[i].lastIndexOf('"')));
		}
	}

	private static Object filterValue(Object value) {
		if (value == null) return null;
		String s = value.toString()
			.replaceAll("%", "%25")
			.replaceAll("&", "%26")
			.replaceAll("=", "%3d");
		if (s.startsWith("[reference:")) return "true";
		return s;
	}
	
	private static boolean isPortlet() {
		return portlet;
	}

	public static void setPortlet(boolean portlet) {
		Module.portlet = portlet;
	}

	private static Style getStyle() {
		return style;
	}

	public static void setStyle(Style style) {
		Module.style = style;
	}
	
}
