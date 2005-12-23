package org.openxava.controller;

import java.util.*;

import javax.servlet.http.*;

import org.hibernate.*;
import org.openxava.actions.*;
import org.openxava.application.meta.*;
import org.openxava.controller.meta.*;
import org.openxava.hibernate.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class ModuleManager {
	
	static {
		MetaControllers.setContext(MetaControllers.WEB);		
	}
	
	private Collection metaActionsOnInit;
	private boolean moduleInitiated;
	private String form;
	private static int nextOid = 0; 
	private static String DEFAULT_MODE = IChangeModeAction.LIST;
		
	private int oid;	
	private String defaultActionQualifiedName;
	private MetaModule metaModule;
	private String[] controllersNames;
	private Collection metaActions;
	private String applicationName;
	private String moduleName;
	
	private String modeControllerName = "Mode";
	private Collection metaControllers;
	private MetaController metaControllerSections;
	private HttpSession session;
	private String viewName = null;
	private String modeName;	
	private String nextModule;
	
	private boolean formUpload = false;

	public ModuleManager() {
		oid = nextOid++;
	}
	
	/**
	 * Html form name associated to this controller. 
	 */
	public String getForm() {
		if (form == null) {
			form = "form" + oid;
		}
		return form;
	}

	public Collection getMetaActions() {
		if (metaActions == null) {
			try {			
				Iterator it = getMetaControllers().iterator();
				metaActions = new ArrayList();
				while (it.hasNext()) {
					MetaController contr = (MetaController) it.next();										
					metaActions.addAll(contr.getMetaActionsForMode(getModeName()));									
				} 										
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(XavaResources.getString("controller_actions_error"));
				return new ArrayList();
			}	
		}
		return metaActions;		
	}
	
	public Collection getMetaActionsOnInit() {
		if (metaActionsOnInit == null) {
			try {			
				Iterator it = getMetaControllers().iterator();
				metaActionsOnInit = new ArrayList();
				while (it.hasNext()) {
					MetaController contr = (MetaController) it.next();										
					metaActionsOnInit.addAll(contr.getMetaActionsOnInit());									
				} 										
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(XavaResources.getString("controller_init_action_error"));
				return Collections.EMPTY_LIST;
			}	
		}
		return metaActionsOnInit;		
	}
	
	
	public Collection getMetaActionsSections() {
		try {			
			return getMetaControllerSections().getMetaActions();						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("controllers_actions_error", getModeControllerName()));
			return new ArrayList();
		}			
	}
	
	
	private MetaController getMetaControllerSections() throws XavaException {
		if (metaControllerSections == null) {
			metaControllerSections = MetaControllers.getMetaController(getModeControllerName());
		}
		return metaControllerSections;
	}
	
	
	private Collection getMetaControllers() throws XavaException {
		if (metaControllers == null) {
			metaControllers = new ArrayList();
			String [] names = getControllersNames();
			for (int i = 0; i < names.length; i++) {
				metaControllers.add(MetaControllers.getMetaController(names[i]));				
			}						
		}
		return metaControllers;
	}

	private void setupModuleControllers() throws XavaException {					
		Collection controllers = getMetaModule().getControllersNames();		
		String [] names = new String[controllers.size()];
		controllers.toArray(names);		
		setControllersNames(names);			
	}
		
	private String getModeControllerName() {		
		return modeControllerName;
	}
	
	private void setModeControllerName(String controllerName) {
		metaControllerSections = null;
		this.modeControllerName = controllerName;
	}
	
	public boolean actionOfThisModule(HttpServletRequest request) {
		return isFormUpload() || // May be that in this way upload forms does not work well in multimodule 
			( 
				Is.equal(request.getParameter("xava_action_module"),getModuleName()) &&
				Is.equal(request.getParameter("xava_action_application"),getApplicationName())
			);				
	}
	
	public void execute(HttpServletRequest request, Messages errors, Messages messages) {
		try {									
			if (errors.isEmpty()) { // Only it's executed the action if there aren't errors
				// The next condition is for avoid to execute a action of another module
				// displayed in the same web page (for example inside a portal)
				if (actionOfThisModule(request)) {
					String xavaAction = request.getParameter("xava_action");					
					if (isFormUpload()) {
						xavaAction = getDefaultActionQualifiedName(); // In upload form we execute the default action						
					}																				
					if (!Is.emptyString(xavaAction)) {						
						String actionValue = request.getParameter("xava_action_argv");
						if ("undefined".equals(actionValue)) actionValue = null;						
						MetaAction a = MetaControllers.getMetaAction(xavaAction);						
						long ini = System.currentTimeMillis();
						executeAction(a, errors, messages, actionValue, request);
						long time = System.currentTimeMillis() - ini;
						System.out.println("[ModuleManager.execute] " + xavaAction + "=" + time + " ms");						
					}
				}									
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errors.add("no_execute_action");
		}
	}
	
	private void executeAction(MetaAction metaAction, Messages errors, Messages message, HttpServletRequest request) {
		executeAction(metaAction, errors, message, null, request);
	}
	
	private void executeAction(MetaAction metaAction, Messages errors, Messages message, String propertyValues, HttpServletRequest request) {		
		try {
			IAction action = metaAction.createAction();
			executeAction(action, metaAction, errors, message, propertyValues, request);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errors.add("no_execute_action", metaAction.getId());			
		}							
	}
	
	public void executeAction(IAction action, Messages errors, Messages messages, HttpServletRequest request) {
		executeAction(action, null, errors, messages, null, request);
	}

	private void executeAction(IAction action, MetaAction metaAction, Messages errors, Messages messages, String propertyValues, HttpServletRequest request) {		
		try {						
			action.setErrors(errors);
			action.setMessages(messages);
			action.setEnvironment(getEnvironment());
			if (metaAction != null) {
				setObjectsInAction(action, metaAction);
			}
			setPropertyValues(action, propertyValues);
			if (action instanceof IModuleContextAction) {
				((IModuleContextAction) action).setContext(getContext());
			}						
			if (action instanceof IModelAction) {
				((IModelAction) action).setModel(getModelName());
			}
			if (action instanceof IRequestAction) {
				((IRequestAction) action).setRequest(request);
			}
			
			if (action instanceof IJDBCAction) {
				((IJDBCAction) action).setConnectionProvider(DataSourceConnectionProvider.getByComponent(getModelName()));
			}			
						
			if (action instanceof IRemoteAction) {
				IRemoteAction remote = (IRemoteAction) action;
				remote.executeBefore();					
				if (remote.isExecuteOnServer()) {
					remote = Server.execute(remote, remote.getPackageName());
					errors.removeAll();
					messages.removeAll();
					errors.add(remote.getErrors());
					messages.add(remote.getMessages());
					remote.setErrors(errors);
					remote.setMessages(messages);	
				}								
				remote.executeAfter();
				action = remote;
			}
			else {
				action.execute();				
			}
			if (action instanceof IChangeModeAction) {
				IChangeModeAction modeChange = (IChangeModeAction) action;
				String nextMode = modeChange.getNextMode();				
				if (!Is.emptyString(nextMode)) {					
					if (!nextMode.equals(getModeName())) {											
						setModeName(nextMode);
					}
				}																
			}
			setFormUpload(false);						
			if (action instanceof ICustomViewAction) {
				ICustomViewAction customViewAction = (ICustomViewAction) action;
				String newView = customViewAction.getCustomView();				
				if (!Is.emptyString(newView)) {
					setViewName(newView);
				}
			}
			if (action instanceof IChangeControllersAction) {
				IChangeControllersAction changeControllersAction = (IChangeControllersAction) action;
				String [] nextControllers = changeControllersAction.getNextControllers();				
				if (nextControllers != IChangeControllersAction.SAME_CONTROLLERS) {
					if (nextControllers.equals(IChangeControllersAction.DEFAULT_CONTROLLERS)) {
						setupModuleControllers();															
					}
					else if (nextControllers.equals(IChangeControllersAction.PREVIOUS_CONTROLLERS)) {
						restorePreviousControllers();															
					}													
					else {
						memorizeControllers();										
						setControllersNames(nextControllers);
						executeInitAction(request, errors, messages);
					}
				}
			}			
			if (action instanceof ILoadFileAction) {					
				setFormUpload(((ILoadFileAction) action).isLoadFile());
			}			
			if (metaAction != null) {
				getObjectFromAction(action, metaAction);
			}
			if (action instanceof IForwardAction) {				
				IForwardAction forward = (IForwardAction) action;
				String uri = forward.getForwardURI();
				if (!Is.emptyString(uri)) {
					request.getSession().setAttribute("xava_forward", uri);
					request.getSession().setAttribute("xava_forward_inNewWindow", String.valueOf(forward.inNewWindow()));
				}
			}
			if (action instanceof IChainAction) {
				IChainAction chainable = (IChainAction) action;				
				String nextAction = chainable.getNextAction();				
				if (!Is.emptyString(nextAction)) {
					MetaAction nextMetaAction = null;
					if (nextAction.indexOf('.') < 0 && metaAction != null) {
						nextMetaAction = metaAction.getMetaController().getMetaAction(nextAction);
					}
					else {
						nextMetaAction = MetaControllers.getMetaAction(nextAction);
					}
					executeAction(nextMetaAction, action.getErrors(), action.getMessages(), request);
				}
			}
			
			nextModule = null;			
			if (action instanceof IChangeModuleAction) {				
				IChangeModuleAction moduleChange = (IChangeModuleAction) action;
				nextModule = moduleChange.getNextModule();				
				if (!Is.emptyString(nextModule)) {					
					if (moduleChange.hasReinitNextModule()) {						
						getContext().put(getApplicationName(), nextModule, "manager", null);
					}
					request.setAttribute("xava.sendParametersToTab", "false");
				}
			}			
			XHibernate.commit();
		}
		catch (ValidationException ex) {
			errors.add(ex.getErrors());
			XHibernate.rollback();
		}
		catch (Exception ex) {			
			ex.printStackTrace();
			if (metaAction != null) {
				errors.add("no_execute_action", metaAction.getId());
			}
			else {
				errors.add("no_execute_action");
			}
			XHibernate.rollback();
		}				
		
	}

	public Environment getEnvironment() throws XavaException {		
		return getMetaModule().getEnvironment();
	}

	private void setControllersNames(String [] names) {				
		metaControllers = null;
		metaActions = null;
		metaActionsOnInit = null;
		defaultActionQualifiedName = null;
		this.controllersNames = names;
	}
	
	private void restorePreviousControllers() throws XavaException {
		Stack previousControllers = (Stack) getObjectFromContext("xava_previousControllers");
		if (previousControllers.isEmpty()) {
			setupModuleControllers();
			return;						
		}
		
		String [] controllers = (String []) previousControllers.pop();
		setControllersNames(controllers);		
	}
	
	private void memorizeControllers() throws XavaException {
		Stack previousControllers = (Stack) getObjectFromContext("xava_previousControllers");
		previousControllers.push(this.controllersNames);
	}

	private void setPropertyValues(IAction action, String propertyValues) throws Exception {
		if (Is.emptyString(propertyValues)) return;
		StringTokenizer st = new StringTokenizer(propertyValues, ",");
		Map values = new HashMap();		
		while (st.hasMoreTokens()) {
			String propertyValue = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(propertyValue, "()=");
			if (!st2.hasMoreTokens()) {
				System.err.println(XavaResources.getString("action_property_warning"));
				break;
			}
			String name = st2.nextToken().trim();
			if (!st2.hasMoreTokens()) {
				System.err.println(XavaResources.getString("action_property_warning"));
				break;
			}
			String value = st2.nextToken().trim();
			values.put(name, value);			
		}		
		PropertiesManager mp = new PropertiesManager(action);
		mp.executeSetsFromStrings(values);				
	}

	private void getObjectFromAction(IAction action, MetaAction metaAction) throws XavaException {
		if (!metaAction.usesObjects()) return;
		PropertiesManager mp = new PropertiesManager(action);
		Iterator it = metaAction.getMetaUseObjects().iterator();
		while (it.hasNext()) {
			MetaUseObject metaUseObject = (MetaUseObject) it.next();
			String property = metaUseObject.getActionProperty();
			Object value = null;
			try {
				value = mp.executeGet(property);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("get_property_action_value_error", property, metaAction.getName());
			}
			if (value != null) {
				// The nulls are not assigned 
				// los nulos no los asignamos and thus we allow to have trasient attributes
				// that it can lost on go and return from server without danger of alter
				// the session value 				
				setObjectInContext(metaUseObject.getName(), value);
			}
		}
	}

	private void setObjectsInAction(IAction action, MetaAction metaAction) throws XavaException {		
		if (!metaAction.usesObjects()) return;		
		PropertiesManager mp = new PropertiesManager(action);
		Iterator it = metaAction.getMetaUseObjects().iterator();		
		while (it.hasNext()) {
			MetaUseObject metaUseObject = (MetaUseObject) it.next();
			String objectName = metaUseObject.getName();			 
			String property = metaUseObject.getActionProperty();			
			Object value = getObjectFromContext(objectName);			
			if (value == null) {				
				value = createObject(objectName);
				setObjectInContext(objectName, value);
			}
			
			try {
				mp.executeSet(property, value);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("set_property_action_value_error", property, metaAction.getName());
			}
			getSession().setAttribute(objectName, value);
		}		
	}
		
	private Object getObjectFromContext(String objectName) throws XavaException {
		return getContext().get(getApplicationName(), getModuleName(), objectName);				
	}
	
	private void setObjectInContext(String objectName, Object value) throws XavaException {
		getContext().put(getApplicationName(), getModuleName(), objectName, value);				
	}
	
	private ModuleContext getContext() {
		return (ModuleContext) getSession().getAttribute("context");		
	}

	private Object createObject(String objectName) throws XavaException {		
		MetaObject metaObject =	MetaControllers.getMetaObject(objectName);
		return metaObject.createObject();		
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getViewURL() {
		String viewName = getViewName();
		String r = null;
		if (viewName.startsWith("xava/")) r = viewName.substring(5);
		else r = "../" + viewName;
		if (!r.endsWith(".jsp")) r = r + ".jsp";				
		return r;	
	}

	private String getViewName() {
		if (IChangeModeAction.LIST.equals(getModeName())) {
			return "xava/list";
		}		
		if (viewName == null) {			
				viewName = "xava/detail"; 
		}		
		return viewName;		
	}

	private void setViewName(String newView) throws XavaException {
		if (ICustomViewAction.DEFAULT_VIEW.equals(newView)) {					
			viewName = null; 
		}
		else {		
			viewName = newView;
		}						
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String newName) throws XavaException {
		if (Is.equal(applicationName, newName)) return;
		moduleInitiated = false;
		applicationName = newName;
		moduleName = null;
		metaControllers = null;
		metaActions = null;
		defaultActionQualifiedName = null;
		metaModule = null;
		controllersNames = null;
	}

	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @return <tt>true</tt> if is new. 
	 */
	public boolean setModuleName(String newModule) throws XavaException {		
		if (Is.equal(moduleName, newModule)) return false; 
		moduleInitiated = false;
		moduleName = newModule;
		metaControllers = null;
		metaControllerSections = null;
		metaActions = null;
		defaultActionQualifiedName = null;
		metaModule = null;		
		setupModuleControllers();
		if (!Is.emptyString(getMetaModule().getModeControllerName())) {
			setModeControllerName(getMetaModule().getModeControllerName());
			modeName = null;						
		}
		if (!Is.emptyString(getMetaModule().getWebViewURL())) {
			setViewName(getMetaModule().getWebViewURL());
			
		}		
		return true; 		
	}
	
	private String [] getControllersNames() {		
		return controllersNames==null?new String[0]:controllersNames;
	}
	
	private MetaModule getMetaModule() throws ElementNotFoundException, XavaException {
		if (metaModule == null) {
			if (Is.emptyString(applicationName, moduleName)) {
				throw new XavaException("application_and_module_required");
			}
			metaModule = MetaApplications.getMetaApplication(applicationName).getMetaModule(moduleName);
			
		}
		return metaModule;
	}
	
	public String getModuleDescription() {
		try {
			return getMetaModule().getDescription();
		}
		catch (Exception ex) {
			return XavaResources.getString("unknow_module");
		}
	}
	
	public String getModelName() throws XavaException {
		return getMetaModule().getModelName();
	}
	
	public String getTabName() throws XavaException {
		return getMetaModule().getTabName();
	} 

	public boolean isListMode() {
		return IChangeModeAction.LIST.equals(getModeName());
	}
	
	public String getModeName() {
		return modeName==null?DEFAULT_MODE:modeName;
	}

	private void setModeName(String newModelName) {
		if (Is.equal(modeName, newModelName)) return;
		modeName = newModelName;
		metaActions = null;
		defaultActionQualifiedName = null;
	}

	public String getDefaultActionQualifiedName() {
		if (defaultActionQualifiedName == null) {
			Iterator it = getMetaActions().iterator();
			int max = -1;
			while (it.hasNext()) {
				MetaAction a = (MetaAction) it.next();
				if (a.getByDefault() > max) {
					max = a.getByDefault();					
					defaultActionQualifiedName = a.getQualifiedName();					
				}
			}			
		}		
		return defaultActionQualifiedName;
	}
	
	public boolean isXavaView() throws XavaException{
		// For that a upload form does not delete the view data.
		// It's a ad hoc solution. It can be improved 
		if (isFormUpload()) return false; 
											
		return "xava/detail".equals(getViewName());
	}

	public String getXavaViewName() throws XavaException {
		return getMetaModule().getViewName();
	}
	
	public String toString() {		
		return "ModuleManager:" + oid;
	}
	
	public void initModule(HttpServletRequest request, Messages errors, Messages messages) {
		if (!moduleInitiated) {
			modeName = getMetaActionsSections().isEmpty()?IChangeModeAction.DETAIL:null;
			moduleInitiated = true;
			executeInitAction(request, errors, messages);
		}
	}
	
	private void executeInitAction(HttpServletRequest request, Messages errors, Messages messages) {
		Iterator it = getMetaActionsOnInit().iterator();
		while (it.hasNext()) {
			MetaAction a = (MetaAction) it.next();			
			executeAction(a, errors, messages, request); 
		}		
	}
	
	public String getEnctype() { 		
		return isFormUpload()?"ENCTYPE='multipart/form-data'":"";		
	}
	
	private boolean isFormUpload() {
		return formUpload;
	}

	private void setFormUpload(boolean b) {		
		formUpload = b;
	}

	public String getNextModule() {
		return nextModule;
	}
	public void setNextModule(String nextModule) {
		this.nextModule = nextModule;
	}
	
}
