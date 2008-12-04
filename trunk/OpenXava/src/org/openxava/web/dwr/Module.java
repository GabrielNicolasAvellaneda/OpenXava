package org.openxava.web.dwr;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.swing.*;

import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.model.meta.*;
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

	private static Log log = LogFactory.getLog(DWRBase.class); 
	final private static String MESSAGES_LAST_REQUEST ="xava_messagesLastRequest";
	final private static String ERRORS_LAST_REQUEST ="xava_errorsLastRequest";
	final private static String MEMBERS_WITH_ERRORS_IN_LAST_REQUEST ="xava_membersWithErrorsInLastRequest";
	final private static String PAGE_RELOADED_LAST_TIME = "xava_pageReloadedLastTime"; 
	
	private static boolean portlet;
	private static Style style;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String application;
	private String module;
	private ModuleManager manager;
	
	public Result request(HttpServletRequest request, HttpServletResponse response, String application, String module, Map values, Map multipleValues, String [] selected) throws Exception {
		try {
			Servlets.setCharacterEncoding(request, response);
			this.request = request;
			this.response = response;
			this.application = application;
			this.module = module;		
			checkSecurity(request, application, module);
			setPageReloadedLastTime(false);
			Users.setCurrent(request);
			this.manager = (ModuleManager) getContext(request).get(application, module, "manager");
			restoreLastMessages();
			request.setAttribute("style", getStyle());
			getURIAsStream("execute.jsp", values, multipleValues, selected);	
			Map changedParts = new HashMap();
			Result result = new Result(changedParts);
			String forwardURI = (String) request.getSession().getAttribute("xava_forward");		
			if (!Is.emptyString(forwardURI)) {
				result.setForwardURL(request.getScheme() + "://" + 
					request.getServerName() + ":" + request.getServerPort() + 
					request.getContextPath() + forwardURI);
				result.setForwardInNewWindow("true".equals(request.getSession().getAttribute("xava_forward_inNewWindow")));
				request.getSession().removeAttribute("xava_forward");
				request.getSession().removeAttribute("xava_forward_inNewWindow");				
			}
			else if (manager.getNextModule() != null) { 
				changeModule(result);
			}
			else {
				fillResult(result, values, multipleValues, selected);
			}			
			result.setStrokeActions(getStrokeActions());
			return result;
		}
		catch (SecurityException ex) {
			Result result = new Result();
			if (wasPageReloadedLastTime()) {
				setPageReloadedLastTime(false);
				result.setError(ex.getMessage());
			}
			else {
				setPageReloadedLastTime(true);			
				result.setReload(true);
				request.getSession().invalidate();
			}
			return result;			
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			Result result = new Result();
			result.setError(ex.getMessage());
			return result;
		}		
		finally {			
			if (manager != null) manager.commit(); // If hibernate, jpa, etc is used to render some value here is commit
		}
	}

	private void setPageReloadedLastTime(boolean b) { 
		// Http session is used instead of ox context because context may not exists at this moment
		if (b) request.getSession().setAttribute(PAGE_RELOADED_LAST_TIME, Boolean.TRUE);
		else request.getSession().removeAttribute(PAGE_RELOADED_LAST_TIME);
	}
	
	private boolean wasPageReloadedLastTime() { 
		// Http session is used instead of ox context because context may not exists at this moment
		return request.getSession().getAttribute(PAGE_RELOADED_LAST_TIME) != null;
	}

	private Map getStrokeActions() { 
		java.util.Iterator it = manager.getAllMetaActionsIterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			MetaAction action = (MetaAction) it.next();
			if (!action.hasKeystroke()) continue;	

			KeyStroke key = KeyStroke.getKeyStroke(action.getKeystroke());
			if (key == null) {
				continue;
			}	
			int keyCode = key.getKeyCode();
			boolean ctrl = (key.getModifiers() & InputEvent.CTRL_DOWN_MASK) > 0; 
			boolean alt = (key.getModifiers() & InputEvent.ALT_DOWN_MASK) > 0; 	
			boolean shift = (key.getModifiers() & InputEvent.SHIFT_DOWN_MASK) > 0;
			String id = keyCode + "," + ctrl + "," + alt + "," + shift;
			result.put(id, new StrokeAction(action.getQualifiedName(), action.isTakesLong()));
		}
		return result;
	}

	private void changeModule(Result result) {		
		String nextModule = manager.getNextModule();		
		if (IChangeModuleAction.PREVIOUS_MODULE.equals(nextModule)) {
			nextModule = manager.getPreviousModule();
			manager.setPreviousModule(null);
		}		
		ModuleManager nextManager = (ModuleManager) getContext(request).get(application, nextModule, "manager", "org.openxava.controller.ModuleManager");		
		if (!IChangeModuleAction.PREVIOUS_MODULE.equals(nextModule)) {
			nextManager.setPreviousModule(module);					
		}	
		manager.setNextModule(null);
		memorizeLastMessages(nextModule); 
		result.setModule(nextModule);
		result.setForm(nextManager.getForm());
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
		return Servlets.getURIAsString(request, response, getURI(jspFile, values, multipleValues, selected));
	}
	

	private void fillResult(Result result, Map values, Map multipleValues, String[] selected) throws Exception {
		Map changedParts = result.getChangedParts();
		for (Iterator it = getChangedParts(values).entrySet().iterator(); it.hasNext(); ) {
			Map.Entry changedPart = (Map.Entry) it.next();			
			changedParts.put(changedPart.getKey(),
				getURIAsString((String) changedPart.getValue(), values, multipleValues, selected)	
			);			
		}	
		if (!manager.isListMode()) {
			result.setFocusPropertyId(getView().getFocusPropertyId());
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
				fillChangedPropertiesActionsAndDescriptionsListReferences(result);
				fillChangedCollections(result);
				fillChangedSections(result);
				fillChangedErrorImages(result);
				fillChangedLabels(result); 
			}
		}		
		return result;
	}

	private void fillChangedLabels(Map result) {
		for (Iterator it=getView().getChangedLabels().entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			result.put("xava_label_" + en.getKey(),	"html:" + en.getValue());
		}
	}

	private void fillChangedErrorImages(Map result) {
		if (getContext(request).exists(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST)) {
			View view = getView();			
			Collection lastErrors = (Collection) getContext(request).get(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST);
			for (Iterator it=lastErrors.iterator(); it.hasNext(); ) {
				String member = (String) it.next();
				if  (view.getQualifiedNameForDisplayedPropertyOrDescriptionsListReference(member) != null) { 
					result.put("xava_error_image_" + member, null);
				}				
			}
			getContext(request).remove(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST);
		}
			
		Messages errors = (Messages) request.getAttribute("errors");
		
		String imageHTML = "html:<img src='" + request.getContextPath() +"/xava/images/error.gif'/>";
		if (!errors.isEmpty()) {
			View view = getView();
			Collection members = new HashSet();
			for (Iterator it=errors.getMembers().iterator(); it.hasNext(); ) {
				String member = (String) it.next();
				String qualifiedMember = view.getQualifiedNameForDisplayedPropertyOrDescriptionsListReference(member);
				if (qualifiedMember != null) { 
					String id = "xava_error_image_" + qualifiedMember;				
					result.put(id, imageHTML);
					members.add(qualifiedMember);	
				}
				if (!members.isEmpty()) {
					getContext(request).put(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST, members);
				}
			}				
		}
	}

	private void fillChangedPropertiesActionsAndDescriptionsListReferences(Map result) {		
		View view = getView();		
		Collection changedMembers = view.getChangedPropertiesActionsAndDescriptionsListReferences().entrySet();
		for (Iterator it = changedMembers.iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			String qualifiedName = (String) en.getKey();
			String name = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
			View containerView = (View) en.getValue();
			MetaModel metaModel = containerView.getMetaModel(); 
			if (metaModel.containsMetaReference(name)) {				
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
				if ((containerView.hasEditableChanged() || 
					(containerView.hasKeyEditableChanged() && metaModel.isKey(name))) &&
					containerView.propertyHasActions(name))					
				{
					result.put("xava_property_actions_" + qualifiedName, 
						"propertyActions.jsp?propertyKey=" + qualifiedName +
						"&propertyName=" + name +
						"&editable=" + containerView.isEditable(name) +					
						"&viewObject=" + containerView.getViewObject() +
						"&lastSearchKey=" + containerView.isLastSearchKey(name));
				}
			}
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
		View changedSections = view.getChangedSectionsView();		
		if (changedSections != null) {			
			result.put("xava_sections_" + changedSections.getViewObject(), 
				"sections.jsp?viewObject=" + changedSections.getViewObject() + 
				"&propertyPrefix=" + changedSections.getPropertyPrefix());
		}
	}			
		
	private View getView() {
		return (View) getContext(request).get(application, module, "xava_view");
	}
	
	private void memorizeLastMessages() { 
		memorizeLastMessages(module);
	}
	
	private void memorizeLastMessages(String module) {  
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
		if (selected != null) { 
			for (int i=0; i<selected.length; i++) {
				String [] s = selected[i].split(":");
				sb.append('&');
				sb.append(s[0]);
				sb.append('=');
				sb.append(s[1]);
			}					
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
