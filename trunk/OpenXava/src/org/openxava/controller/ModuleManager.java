package org.openxava.controller;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.actions.*;
import org.openxava.application.meta.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;

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
	private static int siguienteOid = 0; 
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
	private String sectionName;	
	private String nextModule;
	
	private boolean formUpload = false;

	public ModuleManager() {
		oid = siguienteOid++;
	}
	
	/**
	 * Nombre del formulario html asociado a este controlador.
	 * @return
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
					metaActions.addAll(contr.getMetaActionsForSection(getSectionName()));									
				} 										
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("Imposible obtener acciones asociadas a los controladores");
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
				System.err.println("Imposible obtener acciones de inicio asociadas a los controladores");
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
			System.err.println("Imposible obtener acciones asociadas al controlador " + getModeControllerName());
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
			String [] nombres = getControllersNames();
			for (int i = 0; i < nombres.length; i++) {
				metaControllers.add(MetaControllers.getMetaController(nombres[i]));				
			}						
		}
		return metaControllers;
	}

	private void establecerControladoresModulo() throws XavaException {					
		Collection controladores = getMetaModule().getControllersNames();		
		String [] nombres = new String[controladores.size()];
		controladores.toArray(nombres);		
		setControllersNames(nombres);			
	}
	
	
	private String getModeControllerName() {		
		return modeControllerName;
	}
	
	private void setModeControllerName(String nombreControlador) {
		metaControllerSections = null;
		this.modeControllerName = nombreControlador;
	}
	
	public boolean actionOfThisModule(HttpServletRequest request) {
		return isFormUpload() || // Puede que así formularios upload no funcionen bien en multimodulo 
			( 
				Is.equal(request.getParameter("xava_action_module"),getModuleName()) &&
				Is.equal(request.getParameter("xava_action_application"),getApplicationName())
			);				
	}
	
	public void execute(HttpServletRequest request, Messages errores, Messages mensajes) {		
		try {									
			if (errores.isEmpty()) { // solo se ejecuta la acción si no hay errores acumulados
				// La siguiente condición es para evitar que se ejecuta una acción de otro
				// módulo visualizado en la misma página web (p. ej. en un portal)
				if (actionOfThisModule(request)) {
					String accionXava = request.getParameter("xava_action");										
					if (isFormUpload()) {
						accionXava = getDefaultActionQualifiedName(); // En los formulario upload ejecutamos la acción por defecto						
					}																				
					if (!Is.emptyString(accionXava)) {						
						String valorAccion = request.getParameter("xava_action_argv");						
						MetaAction a = MetaControllers.getMetaAction(accionXava);						
						long ini = System.currentTimeMillis();
						executeAction(a, errores, mensajes, valorAccion, request);
						long cuesta = System.currentTimeMillis() - ini;
						System.out.println("[ControladorWeb.ejecutar] " + accionXava + "=" + cuesta + " ms");						
					}
				}									
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errores.add("no_execute_action");
		}
	}
	
	private void executeAction(MetaAction metaAccion, Messages errores, Messages mensaje, HttpServletRequest request) {
		executeAction(metaAccion, errores, mensaje, null, request);
	}
	
	private void executeAction(MetaAction metaAccion, Messages errores, Messages mensajes, String valoresPropiedades, HttpServletRequest request) {		
		try {
			IAction accion = metaAccion.createAction();
			executeAction(accion, metaAccion, errores, mensajes, valoresPropiedades, request);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errores.add("no_execute_action", metaAccion.getId());			
		}							
	}
	
	public void executeAction(IAction accion, Messages errores, Messages mensajes, HttpServletRequest request) {
		executeAction(accion, null, errores, mensajes, null, request);
	}

	private void executeAction(IAction accion, MetaAction metaAccion, Messages errores, Messages mensajes, String valoresPropiedades, HttpServletRequest request) {		
		try {						
			accion.setErrors(errores);
			accion.setMessages(mensajes);
			accion.setEnvironment(getEnvironment());
			if (metaAccion != null) {
				ponerObjetosEnAccion(accion, metaAccion);
			}
			ponerValoresPropiedades(accion, valoresPropiedades);
			if (accion instanceof IModuleContextAction) {
				((IModuleContextAction) accion).setContext(getContext());
			}						
			if (accion instanceof IModelAction) {
				((IModelAction) accion).setModel(getModelName());
			}
			if (accion instanceof IRequestAction) {
				((IRequestAction) accion).setRequest(request);
			}
						
			if (accion instanceof IRemoteAction) {
				IRemoteAction remota = (IRemoteAction) accion;
				remota.executeBefore();					
				if (remota.isExecuteOnServer()) {
					remota = Server.execute(remota, remota.getPackageName());
					errores.removeAll();
					mensajes.removeAll();
					errores.add(remota.getErrors());
					mensajes.add(remota.getMessages());
					remota.setErrors(errores);
					remota.setMessages(mensajes);	
				}								
				remota.executeAfter();
				accion = remota;
			}
			else {
				accion.execute();				
			}
			if (accion instanceof IChangeModeAction) {
				IChangeModeAction cambioSeccion = (IChangeModeAction) accion;
				String siguienteSeccion = cambioSeccion.getNextMode();				
				if (!Is.emptyString(siguienteSeccion)) {					
					if (!siguienteSeccion.equals(getSectionName())) {											
						setSectionName(siguienteSeccion);
					}
				}																
			}
			setFormUpload(false);						
			if (accion instanceof ICustomViewAction) {
				ICustomViewAction customViewAction = (ICustomViewAction) accion;
				String newView = customViewAction.getCustomView();				
				if (!Is.emptyString(newView)) {
					setViewName(newView);
				}
			}
			if (accion instanceof IChangeControllersAction) {
				IChangeControllersAction changeControllersAction = (IChangeControllersAction) accion;
				String [] siguientesControladores = changeControllersAction.getNextControllers();				
				if (siguientesControladores != INavigationAction.SAME_CONTROLLERS) {
					if (siguientesControladores.equals(INavigationAction.DEFAULT_CONTROLLERS)) {
						establecerControladoresModulo();															
					}
					else if (siguientesControladores.equals(INavigationAction.PREVIOUS_CONTROLLERS)) {
						establecerControladoresAnteriores();															
					}													
					else {
						memorizarControladores();										
						setControllersNames(siguientesControladores);						
					}
				}
			}			
			if (accion instanceof ILoadFileAction) {					
				setFormUpload(((ILoadFileAction) accion).isLoadFile());
			}			
			if (metaAccion != null) {
				cogerObjetosDeAccion(accion, metaAccion);
			}
			if (accion instanceof IForwardAction) {				
				IForwardAction forward = (IForwardAction) accion;
				String uri = forward.getForwardURI();
				if (!Is.emptyString(uri)) {
					request.getSession().setAttribute("xava_forward", uri);
					request.getSession().setAttribute("xava_forward_inNewWindow", String.valueOf(forward.inNewWindow()));
				}
			}
			if (accion instanceof IChainAction) {
				IChainAction encadenable = (IChainAction) accion;				
				String siguienteAccion = encadenable.getNextAction();				
				if (!Is.emptyString(siguienteAccion)) {
					MetaAction siguienteMetaAccion = null;
					if (siguienteAccion.indexOf('.') < 0 && metaAccion != null) {
						siguienteMetaAccion = metaAccion.getMetaController().getMetaAction(siguienteAccion);
					}
					else {
						siguienteMetaAccion = MetaControllers.getMetaAction(siguienteAccion);
					}
					executeAction(siguienteMetaAccion, accion.getErrors(), accion.getMessages(), request);
				}
			}
			
			nextModule = null;			
			if (accion instanceof IChangeModuleAction) {				
				IChangeModuleAction moduleChange = (IChangeModuleAction) accion;
				nextModule = moduleChange.getNextModule();				
				if (!Is.emptyString(nextModule)) {					
					if (moduleChange.hasReinitNextModule()) {						
						getContext().put(getApplicationName(), nextModule, "manager", null);
					}
				}
			}			
		}
		catch (Exception ex) {			
			ex.printStackTrace();
			if (metaAccion != null) {
				errores.add("no_execute_action", metaAccion.getId());
			}
			else {
				errores.add("no_execute_action");
			}
		}				
		
	}

	public Environment getEnvironment() throws XavaException {		
		return getMetaModule().getEnvironment();
	}

	private void setControllersNames(String [] nombres) {				
		metaControllers = null;
		metaActions = null;
		defaultActionQualifiedName = null;
		this.controllersNames = nombres;
	}
	
	private void establecerControladoresAnteriores() throws XavaException {
		Stack controladoresAnteriores = (Stack) getObjectFromContext("xava_previousControllers");
		if (controladoresAnteriores.isEmpty()) {
			establecerControladoresModulo();
			return;						
		}
		
		String [] controladores = (String []) controladoresAnteriores.pop();
		setControllersNames(controladores);		
	}
	
	private void memorizarControladores() throws XavaException {
		Stack controladoresAnteriores = (Stack) getObjectFromContext("xava_previousControllers");
		controladoresAnteriores.push(this.controllersNames);
	}

	private void ponerValoresPropiedades(IAction accion, String valoresPropiedades) throws Exception {
		if (Is.emptyString(valoresPropiedades)) return;
		StringTokenizer st = new StringTokenizer(valoresPropiedades, ",");
		Map valores = new HashMap();		
		while (st.hasMoreTokens()) {
			String valorPropiedad = st.nextToken();
			StringTokenizer st2 = new StringTokenizer(valorPropiedad, "()=");
			if (!st2.hasMoreTokens()) {
				System.err.println("¡ADVERTENCIA! No es posible asignar valor a una propiedad de una acción: Se requiere formato nombre=valor,nombre=valor o nombre(valor),nombre(valor)");
				break;
			}
			String nombre = st2.nextToken().trim();
			if (!st2.hasMoreTokens()) {
				System.err.println("¡ADVERTENCIA! No es posible asignar valor a la propiedad "+nombre+" de una acción: Se requiere formato nombre=valor,nombre=valor o nombre(valor),nombre(valor)");
				break;
			}
			String valor = st2.nextToken().trim();
			valores.put(nombre, valor);			
		}		
		PropertiesManager mp = new PropertiesManager(accion);
		mp.executeSetsFromStrings(valores);				
	}

	private void cogerObjetosDeAccion(IAction accion, MetaAction metaAccion) throws XavaException {
		if (!metaAccion.usesObjects()) return;
		PropertiesManager mp = new PropertiesManager(accion);
		Iterator it = metaAccion.getMetaUseObjects().iterator();
		while (it.hasNext()) {
			MetaUseObject metaUsarObjeto = (MetaUseObject) it.next();
			String propiedad = metaUsarObjeto.getActionProperty();
			Object valor = null;
			try {
				valor = mp.executeGet(propiedad);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("get_property_action_value_error", propiedad, metaAccion.getName());
			}
			if (valor != null) { 
				// los nulos no los asignamos y así permitimos tener atributos trasients
				// que pueden perderse al ir y volver del servidor sin peligro de alterar
				// el valor de sesión 				
				setObjetoEnContexto(metaUsarObjeto.getName(), valor);
			}
		}
	}

	private void ponerObjetosEnAccion(IAction accion, MetaAction metaAccion) throws XavaException {		
		if (!metaAccion.usesObjects()) return;		
		PropertiesManager mp = new PropertiesManager(accion);
		Iterator it = metaAccion.getMetaUseObjects().iterator();
		String nombreControlador = metaAccion.getControllerName();
		while (it.hasNext()) {
			MetaUseObject metaUsaObjeto = (MetaUseObject) it.next();
			String nombreObjeto = metaUsaObjeto.getName();			 
			String propiedad = metaUsaObjeto.getActionProperty();			
			Object valor = getObjectFromContext(nombreObjeto);			
			if (valor == null) {				
				valor = crearObjeto(nombreObjeto);
				setObjetoEnContexto(nombreObjeto, valor);
			}
			
			try {
				mp.executeSet(propiedad, valor);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("set_property_action_value_error", propiedad, metaAccion.getName());
			}
			getSession().setAttribute(nombreObjeto, valor);
		}		
	}
		
	private Object getObjectFromContext(String objectName) throws XavaException {
		return getContext().get(getApplicationName(), getModuleName(), objectName);				
	}
	
	private void setObjetoEnContexto(String nombreObjeto, Object valor) throws XavaException {
		getContext().put(getApplicationName(), getModuleName(), nombreObjeto, valor);				
	}
	
	private ModuleContext getContext() {
		return (ModuleContext) getSession().getAttribute("context");		
	}

	private Object crearObjeto(String nombreObjeto) throws XavaException {		
		MetaObject metaObjeto =	MetaControllers.getMetaObject(nombreObjeto);
		return metaObjeto.createObject();		
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getViewURL() {
		String nombreVista = getViewName();
		String r = null;
		if (nombreVista.startsWith("xava/")) r = nombreVista.substring(5);
		else r = "../" + nombreVista;
		if (!r.endsWith(".jsp")) r = r + ".jsp";				
		return r;	
	}

	private String getViewName() {
		if (IChangeModeAction.LIST.equals(getSectionName()) && !getMetaActionsSections().isEmpty()) {
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

	public void setApplicationName(String nuevo) throws XavaException {
		if (Is.equal(applicationName, nuevo)) return;
		moduleInitiated = false;
		applicationName = nuevo;
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
	 * @return <tt>true</tt> si es nuevo. 
	 */
	public boolean setModuleName(String nuevo) throws XavaException {		
		if (Is.equal(moduleName, nuevo)) return false; 
		moduleInitiated = false;
		moduleName = nuevo;
		metaControllers = null;
		metaControllerSections = null;
		metaActions = null;
		defaultActionQualifiedName = null;
		metaModule = null;		
		establecerControladoresModulo();
		if (!Is.emptyString(getMetaModule().getModeControllerName())) {
			setModeControllerName(getMetaModule().getModeControllerName());
			sectionName = null;						
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
		return IChangeModeAction.LIST.equals(getSectionName());
	}
	
	public String getSectionName() {
		return sectionName==null?DEFAULT_MODE:sectionName;
	}

	private void setSectionName(String nueva) {
		if (Is.equal(sectionName, nueva)) return;
		sectionName = nueva;
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
		// para que en un formulario upload no borre los datos vista
		// Es una solución muy a propósito. Esto habrá que refinarlo.
		if (isFormUpload()) return false; 
											
		return "xava/detail".equals(getViewName());
	}

	public String getXavaViewName() throws XavaException {
		return getMetaModule().getViewName();
	}
	
	public String toString() {		
		return "ModuloManager:"+oid;
	}
	
	public void initModule(HttpServletRequest request, Messages errores, Messages mensajes) {
		if (!moduleInitiated) {
			Iterator it = getMetaActionsOnInit().iterator();
			while (it.hasNext()) {
				MetaAction a = (MetaAction) it.next();
				executeAction(a, errores, mensajes, request); 
			}
			moduleInitiated = true;
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
	public void setNextModule(String siguienteModulo) {
		this.nextModule = siguienteModulo;
	}
}
