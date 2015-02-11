package org.openxava.web.dwr;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.*;
import javax.swing.*;

import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.View;
import org.openxava.web.*;
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
	
	transient private HttpServletRequest request; 
	transient private HttpServletResponse response; 
	private String application;
	private String module;
	private ModuleManager manager;
	private boolean firstRequest;
	private String baseFolder = null;
	
	public Result request(HttpServletRequest request, HttpServletResponse response, String application, String module, String additionalParameters, Map values, Map multipleValues, String [] selected, String [] deselected, Boolean firstRequest, String baseFolder) throws Exception {
		Result result = new Result(); 
		result.setApplication(application); 
		result.setModule(module);
		try {
			Servlets.setCharacterEncoding(request, response);
			this.request = request;
			this.response = response;
			this.application = application;
			this.module = module;
			this.firstRequest = firstRequest==null?false:firstRequest;
			this.baseFolder = baseFolder==null?"/xava/":"/" + baseFolder + "/"; 
			checkSecurity(request, application, module);
			setPageReloadedLastTime(false);
			Users.setCurrent(request);
			this.manager = (ModuleManager) getContext(request).get(application, module, "manager");
			restoreLastMessages();  			
			request.setAttribute("style", Style.getInstance(request));
			getURIAsStream("execute.jsp", values, multipleValues, selected, deselected, additionalParameters);
			setDialogLevel(result); 
			Map changedParts = new HashMap();
			result.setChangedParts(changedParts);
			String forwardURI = (String) request.getSession().getAttribute("xava_forward");
			String[] forwardURIs = (String[]) request.getSession().getAttribute("xava_forwards");
			if (!Is.emptyString(forwardURI)) {
				memorizeLastMessages();
				if (forwardURI.startsWith("http://") || forwardURI.startsWith("https://") || forwardURI.startsWith("javascript:")) {
					result.setForwardURL(forwardURI);
				}else{
					result.setForwardURL(request.getScheme() + "://" + 
						request.getServerName() + ":" + request.getServerPort() + 
						request.getContextPath() + forwardURI);
				}
				result.setForwardInNewWindow("true".equals(request.getSession().getAttribute("xava_forward_inNewWindow")));
				request.getSession().removeAttribute("xava_forward");
				request.getSession().removeAttribute("xava_forward_inNewWindow");
			}
			else if (forwardURIs!=null) {
				memorizeLastMessages();
				for (int i=0; i<forwardURIs.length; i++) {
					if (!(forwardURIs[i].startsWith("http://") || forwardURIs[i].startsWith("https://"))) {
						forwardURIs[i]=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + forwardURIs[i];
					}
				}
				request.getSession().removeAttribute("xava_forwards");	
				result.setForwardURLs(forwardURIs);
			}			
			else if (manager.getNextModule() != null) {
				changeModule(result);
			}
			else {
				fillResult(result, values, multipleValues, selected, deselected, additionalParameters);
			}							
			result.setViewMember(getView().getMemberName());
			result.setStrokeActions(getStrokeActions());
			result.setSelectedRows(getSelectedRows());
			return result;
		}
		catch (SecurityException ex) {
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
			result.setError(ex.getMessage());
			return result;
		}		
		finally {			
			if (manager != null) manager.commit(); // If hibernate, jpa, etc is used to render some value here is commit
		}
	}
	
	private Map getSelectedRows() { 
		Map<String, int[]> result = getView().getChangedCollectionsSelectedRows();
		return result.isEmpty()?null:result;
	}

	private void setPageReloadedLastTime(boolean b) { 
		// Http session is used instead of ox context because context may not exists at this moment
		if (b) request.getSession().setAttribute(PAGE_RELOADED_LAST_TIME, Boolean.TRUE);
		else request.getSession().removeAttribute(PAGE_RELOADED_LAST_TIME);
	}
	
	private boolean wasPageReloadedLastTime() { 
		// Http session is used instead of ox context because context may not exist at this moment
		return request.getSession().getAttribute(PAGE_RELOADED_LAST_TIME) != null;
	}
	
	public Map getStrokeActions(HttpServletRequest request, HttpServletResponse response, String application, String module) throws Exception { 
		this.manager = (ModuleManager) getContext(request).get(application, module, "manager");
		return getStrokeActions();
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
			result.put(id, new StrokeAction(action.getQualifiedName(), action.getConfirmMessage(Locales.getCurrent()), action.isTakesLong()));
		}
		return result;
	}
	
	private void changeModule(Result result) {
		String nextModule = manager.getNextModule();
		boolean previousModule = IChangeModuleAction.PREVIOUS_MODULE.equals(nextModule);
		if (previousModule) {
			nextModule = manager.getPreviousModules().peek().toString();
			manager.getPreviousModules().pop();
			getContext(request).remove(application, module, "xava_currentModule"); 
			getContext(request).remove(application, nextModule, "xava_currentModule");
		}
		else {			
			if (manager.getPreviousModules().contains(nextModule)) {
				throw new XavaException("module_reentrance_not_allowed", nextModule);
			}
			manager.getPreviousModules().push(module);
		}
		

		if (!manager.getPreviousModules().isEmpty() && !previousModule) {			
			getContext(request).put(application, module, "xava_currentModule", nextModule);
		}

		ModuleManager nextManager = (ModuleManager) getContext(request).get(application, nextModule, "manager", "org.openxava.controller.ModuleManager");
		
		nextManager.setPreviousModules(manager.getPreviousModules());
		
		manager.setNextModule(null);
		memorizeLastMessages(nextModule);		
		result.setNextModule(nextModule);
	}	
	
	public void requestMultipart(HttpServletRequest request, HttpServletResponse response, String application, String module) throws Exception {
		request(request, response, application, module, null, null, null, null, null, false, null);   		
		memorizeLastMessages();
		manager.setResetFormPostNeeded(true);
	}

	private InputStream getURIAsStream(String jspFile, Map values, Map multipleValues, String[] selected, String[] deselected, String additionalParameters) throws Exception {
		return Servlets.getURIAsStream(request, response, getURI(jspFile, values, multipleValues, selected, deselected, additionalParameters));
	}
	
	private String getURIAsString(String jspFile, Map values, Map multipleValues, String[] selected, String[] deselected, String additionalParameters) throws Exception {
		if (jspFile == null) return "";
		if (jspFile.startsWith("html:")) return jspFile.substring(5); // Using html: prefix the content is returned as is
		return Servlets.getURIAsString(request, response, getURI(jspFile, values, multipleValues, selected, deselected, additionalParameters));
	}
	

	private void fillResult(Result result, Map values, Map multipleValues, String[] selected, String[] deselected, String additionalParameters) throws Exception {
		Map changedParts = result.getChangedParts();
		getView().resetCollectionsCache(); 

		if (manager.isShowDialog() || manager.isHideDialog() || firstRequest) {
			if (manager.getDialogLevel() > 0) {
				changedParts.put(decorateId("dialog" + manager.getDialogLevel()),   
					getURIAsString("core.jsp?buttonBar=false", values, multipleValues, selected, deselected, additionalParameters)					
				);		
				getView().resetCollectionsCache(); 
				result.setFocusPropertyId(getView().getFocusPropertyId());
				return;
			}			
		}
				
		for (Iterator it = getChangedParts(values).entrySet().iterator(); it.hasNext(); ) {
			Map.Entry changedPart = (Map.Entry) it.next();
			changedParts.put(changedPart.getKey(),
				getURIAsString((String) changedPart.getValue(), values, multipleValues, selected, deselected, additionalParameters)	
			);
		}
		// We tried the errors again because errors could be produced and added when rendering JSPs
		Messages errors = (Messages) request.getAttribute("errors");
		if (errors.contains() && changedParts.get("errors") == null) {
			put(changedParts, "errors",
				getURIAsString("errors.jsp", values, multipleValues, selected, deselected, additionalParameters)	
			);
		}
		if (!manager.isListMode()) {			
			result.setFocusPropertyId(getView().getFocusPropertyId());
		}		
		else {
			result.setFocusPropertyId(Lists.FOCUS_PROPERTY_ID);
		}
		
		if (manager.isSplitMode()) {
			int row = (Integer) getContext(request).get(application, module, "xava_row");
			result.setCurrentRow(row);
		}
		getView().resetCollectionsCache();		
	}

	private void setDialogLevel(Result result) {
		result.setDialogLevel(manager.getDialogLevel());
		if (manager.isShowDialog() && manager.isHideDialog()) return;
		if (firstRequest && manager.getDialogLevel() > 0) {
			result.setShowDialog(true);			
			restoreDialogTitle(result); 
		}		
		else if (manager.isShowDialog()) {
			result.setShowDialog(manager.isShowDialog());						
			setDialogTitle(result);
		}
		else if (manager.isHideDialog()) { 
			result.setHideDialog(true);
			restoreDialogTitle(result); 						
		}		
		result.setResizeDialog(manager.getDialogLevel() > 0 && (getView().isReloadNeeded() || manager.isReloadViewNeeded()));		
	}
	
	private void restoreDialogTitle(Result result) {
		result.setDialogTitle((String) getView().getObject("xava.dialogTitle"));
	}

	private void setDialogTitle(Result result) {				
		if (!Is.emptyString(getView().getTitle())) {
			result.setDialogTitle(getView().getTitle());
		}
		else {	
			MetaAction lastAction = manager.getLastExecutedMetaAction();
			String model = Labels.get(getView().getModelName());
			if (lastAction == null) result.setDialogTitle(model);
			else if (Is.emptyString(model)) result.setDialogTitle(lastAction.getDescription()); 
			else result.setDialogTitle(lastAction.getDescription() + " - " + model);
		}		
		getView().putObject("xava.dialogTitle", result.getDialogTitle());
	}

	private Map getChangedParts(Map values) { 
		Map result = new HashMap();
		if (values == null || manager.isReloadAllUINeeded() || manager.isFormUpload()) {
			put(result, "core", "core.jsp");
		}
		else {			
			if (manager.isActionsChanged()) {									
				if (manager.getDialogLevel() > 0) { 
					put(result, "bottom_buttons", "bottomButtons.jsp?buttonBar=false");					
				}		
				else {						
					put(result, "button_bar", "buttonBar.jsp");
					put(result, "bottom_buttons", "bottomButtons.jsp");
					if (manager.isSplitMode()) {
						put(result, "list_button_bar", "buttonBar.jsp?xava_mode=list");
						put(result, "list_bottom_buttons", "bottomButtons.jsp?xava_mode=list");					
					}
				}
			}					
			Messages errors = (Messages) request.getAttribute("errors");
			put(result, "errors", errors.contains()?"errors.jsp":null);
			Messages messages = (Messages) request.getAttribute("messages");
			put(result, "messages", messages.contains()?"messages.jsp":null);
						
			if (manager.isReloadViewNeeded() || getView().isReloadNeeded()) {
				put(result, "view", manager.getViewURL());
			}
			else {
				fillChangedPropertiesActionsAndReferencesWithNotCompositeEditor(result);
				fillChangedCollections(result);
				fillChangedCollectionsTotals(result); 
				fillChangedSections(result);
				fillChangedErrorImages(result);
				fillChangedLabels(result);
			}
			
			if (manager.isSplitMode() && 
				((manager.getLastExecutedMetaAction() != null 
					&& manager.getLastExecutedMetaAction().appliesToMode(IChangeModeAction.LIST)) ||
					getView().descriptionsListsRefreshed())) 
			{
				put(result, "list_view", "list.jsp");
			}
			
		}	
		return result;
	}

	private void fillChangedLabels(Map result) {
		for (Iterator it=getView().getChangedLabels().entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			put(result, "label_" + en.getKey(),	"html:" + en.getValue());
		}
	}
	
	private void fillChangedErrorImages(Map result) {
		if (getContext(request).exists(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST)) {
			View view = getView();			
			Collection lastErrors = (Collection) getContext(request).get(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST);
			for (Iterator it=lastErrors.iterator(); it.hasNext(); ) {
				String member = (String) it.next();
				if  (view.getQualifiedNameForDisplayedPropertyOrReferenceWithNotCompositeEditor(member) != null) { 
					put(result, "error_image_" + member, null);
				}				
			}
			getContext(request).remove(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST);
		}
			
		Messages errors = (Messages) request.getAttribute("errors");
		String errorImageClass = "";
		if (!Is.emptyString(Style.getInstance().getErrorImage())) {
			errorImageClass = "class='" + Style.getInstance().getErrorImage() + "' ";
		}
		String imageHTML = "html:<img " + errorImageClass  + "src='" + request.getContextPath() +"/xava/images/error.gif'/>";
		if (!errors.isEmpty()) {
			View view = getView();
			Collection members = new HashSet();
			for (Iterator it=errors.getMembers().iterator(); it.hasNext(); ) {
				String member = (String) it.next();
				String qualifiedMember = view.getQualifiedNameForDisplayedPropertyOrReferenceWithNotCompositeEditor(member);
				if (qualifiedMember != null) { 
					String id = "error_image_" + qualifiedMember;				
					put(result, id, imageHTML);
					members.add(qualifiedMember);	
				}
				if (!members.isEmpty()) {
					getContext(request).put(application, module, MEMBERS_WITH_ERRORS_IN_LAST_REQUEST, members);
				}
			}				
		}
	}

	private void fillChangedPropertiesActionsAndReferencesWithNotCompositeEditor(Map result) { 		
		View view = getView();			
		Collection changedMembers = view.getChangedPropertiesActionsAndReferencesWithNotCompositeEditor().entrySet();		
		for (Iterator it = changedMembers.iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			String qualifiedName = (String) en.getKey();
			String name = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
			View containerView = (View) en.getValue();
			MetaModel metaModel = containerView.getMetaModel();
			boolean isReference = metaModel.containsMetaReference(name);
			boolean isInsideElementCollection = false;
			if (qualifiedName.contains(":")) {
				isInsideElementCollection = true;
				name = qualifiedName.substring(qualifiedName.lastIndexOf(':') + 1);
				qualifiedName = qualifiedName.replace(":", "");
				try {
					containerView.getMetaReference(name);
					isReference = true;
				}
				catch (ElementNotFoundException ex) {
					isReference = false;
				}
			}
			if (isReference) { 
				String referenceKey = decorateId(qualifiedName); 
				MetaReference metaReference = containerView.getMetaReference(name);
				if (isInsideElementCollection) {
					metaReference = metaReference.cloneMetaReference();
					metaReference.setName(name);
				}
				request.setAttribute(referenceKey, metaReference);
				put(result, "reference_editor_" + qualifiedName,   
					"reference.jsp?referenceKey=" + referenceKey + 
					"&onlyEditor=true&viewObject=" + containerView.getViewObject());
			}
			else {
				put(result, "editor_" + qualifiedName, 
					"editorWrapper.jsp?propertyName=" + name + 
					"&editable=" + containerView.isEditable(name) +
					"&throwPropertyChanged=" + containerView.throwsPropertyChanged(name) +
					"&viewObject=" + containerView.getViewObject() + 
					"&propertyPrefix=" + containerView.getPropertyPrefix());
				if ((containerView.hasEditableChanged() || 
					(containerView.hasKeyEditableChanged() && metaModel.isKeyOrSearchKey(name))) &&
					containerView.propertyHasActions(name) ||
					containerView.propertyHasChangedActions(name))					
				{
					put(result, "property_actions_" + qualifiedName, 
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
			put(result, "frame_" + qualifiedName + "header", 
				"collectionFrameHeader.jsp?collectionName=" + name + 
				"&viewObject=" + containerView.getViewObject() +		
				"&propertyPrefix=" + containerView.getPropertyPrefix()); 
			put(result, "collection_" + qualifiedName + ".", 
				"collection.jsp?collectionName=" + name + 
				"&viewObject=" + containerView.getViewObject() + 
				"&propertyPrefix=" + containerView.getPropertyPrefix());									
		}
	}
	
	private void fillChangedCollectionsTotals(Map result) { 
		View view = getView();			
		Collection changedCollections = view.getChangedCollectionsTotals().entrySet(); 		
		for (Iterator it = changedCollections.iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			String [] key = ((String) en.getKey()).split(":");
			String qualifiedName = key[0];
			String row = key[1];
			String column = key[2];
			String name = qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
			View containerView = (View) en.getValue();			
			put(result, "collection_total_" + row + "_" + column + "_" + qualifiedName + ".", 
				"editors/collectionTotal.jsp?collectionName=" + name + 
				"&viewObject=" + containerView.getViewObject() +
				"&row=" + row +
				"&column=" + column +
				"&propertyPrefix=" + containerView.getPropertyPrefix());  									
		}
	}

	
	private void fillChangedSections(Map result) {
		View view = getView();			
		View changedSections = view.getChangedSectionsView();		
		if (changedSections != null) {			
			put(result, "sections_" + changedSections.getViewObject(), 
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
	

	public static void restoreLastMessages(HttpServletRequest request, String application, String module) {  
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
	
	private void restoreLastMessages() {
		restoreLastMessages(request, application, module); 
	}	
	
	private String getURI(String jspFile, Map values, Map multipleValues, String[] selected, String[] deselected, String additionalParameters) throws UnsupportedEncodingException {
		StringBuffer result = new StringBuffer(getURIPrefix());
		result.append(jspFile);
		if (jspFile.endsWith(".jsp")) result.append('?');
		else result.append('&');
		result.append("application=");
		result.append(application);
		result.append("&module=");
		result.append(module);
		addValuesQueryString(result, values, multipleValues, selected, deselected);
		if (!Is.emptyString(additionalParameters)) result.append(additionalParameters);
		return result.toString();
	}

	private String getURIPrefix() { 
		return isPortlet()?"/WEB-INF/jsp" + baseFolder:baseFolder;
	}
	
	private void put(Map result, String key, Object value) {
		result.put(decorateId(key), value);
	}
	
	private String decorateId(String name) { 
		return Ids.decorate(application, module, name);
	}
	
	private void addValuesQueryString(StringBuffer sb, Map values, Map multipleValues, String [] selected, String[] deselected) throws UnsupportedEncodingException {
		if (values == null) return;
		if (multipleValues != null) {
			SortedMap sortedMultipleValues = new TreeMap(multipleValues);  			
			for (Iterator it=sortedMultipleValues.entrySet().iterator(); it.hasNext(); ) { 
				Map.Entry en = (Map.Entry) it.next();			
				String addedKey = addMultipleValuesQueryString(sb, en.getKey(), en.getValue());
				values.remove(decorateId(addedKey)); 				
			}
			values.remove(decorateId("xava_multiple"));
		}
		for (Iterator it=values.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			if (!en.getKey().toString().equals(decorateId("xava_selected"))) { 
				sb.append('&');				
				sb.append(filterKey(en.getKey())); 
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
		if (deselected != null) {	
			for (int i=0; i<deselected.length; i++) {
				if (!deselected[i].contains("[")) continue;
				// 'ox_OpenXavaTest_Color__xava_tab:[false0][false2][false3]' -> 'deselected=ox_OpenXavaTest_Color__xava_tab:0,2,3'
				String r = deselected[i].replace("[false", "").replace("]", ",");
				r = r.substring(0, r.length()-1);
				sb.append('&');				
				sb.append("deselected=");
				sb.append(r);				
			}
		}
	}
	
	private String filterKey(Object key) {
		String skey = (String) key;
		int idx = skey.indexOf("::");
		if (idx < 0) return Ids.undecorate(skey);
		return Ids.undecorate(skey.substring(0, idx));
	}

	private String addMultipleValuesQueryString(StringBuffer sb, Object key, Object value) {		
		if (value == null) return null;
		String filteredKey = filterKey((String) key); 
		if (key.toString().indexOf("::") >= 0) {
			sb.append('&');
			sb.append(filteredKey); 
			sb.append('=');			
			sb.append(value);
		}
		else {
			String [] tokens = value.toString().split("\n");
			for (int i=1; i< tokens.length - 1; i++) {
				sb.append('&'); 
				sb.append(filteredKey);
				sb.append('=');			
				sb.append(tokens[i].substring(tokens[i].indexOf('"') + 1, tokens[i].lastIndexOf('"')));
			}
		}
		return filteredKey; 
	}

	private Object filterValue(Object value) throws UnsupportedEncodingException {
		if (value == null) return null;
		if (value.toString().startsWith("[reference:")) {
			return "true";
		} 
		String charsetName = request.getCharacterEncoding(); 
		if (charsetName == null) { 
			charsetName = XSystem.getEncoding();
		} 
		return URLEncoder.encode(value.toString(), charsetName);
	}
	
	public static boolean isPortlet() { 
		return portlet;
	}

	public static void setPortlet(boolean portlet) {
		Module.portlet = portlet;
	}
		
}
