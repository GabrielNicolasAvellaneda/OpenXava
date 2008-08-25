package org.openxava.controller;

import java.util.*;

import javax.servlet.http.*;



import org.apache.commons.logging.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;

/**
 * Context with life of session and private for every module.
 * 
 * @author Javier Paniza
 */

public class ModuleContext {
	
	private static Log log = LogFactory.getLog(ModuleContext.class);
	
	static {
		MetaControllers.setContext(MetaControllers.WEB);		
	}
	
	
	private Map contexts = new HashMap();
	private Map globalContext = null;
	

	/**
	 * Return a object associated to the specified module
	 * in 'application' and 'module' of request.
	 */
	public Object get(HttpServletRequest request, String objectName) throws XavaException {  
		String application = request.getParameter("application");
		if (Is.emptyString(application)) {
			throw new XavaException("application_and_module_required_in_request");
		}
		String module = request.getParameter("module");
		if (Is.emptyString(module)) {
			throw new XavaException("application_and_module_required_in_request");
		}		
		return get(application, module, objectName);		
	}
	
	/**
	 * Return a object asociate to the specified module
	 * in 'application' and 'module' of request.
	 */
	public Object get(HttpServletRequest request, String objectName, String className) throws XavaException { 
		String application = request.getParameter("application");
		if (Is.emptyString(application)) {
			throw new XavaException("application_and_module_required_in_request");
		}
		String module = request.getParameter("module");
		if (Is.emptyString(module)) {
			throw new XavaException("application_and_module_required_in_request");
		}		
		return get(application, module, objectName, className);		
	}
	
	
	public Object get(String application, String module, String objectName, String className) throws XavaException {
		Map context = getContext(application, module, objectName); 
		Object o = context.get(objectName);
		if (o == null) {
			o = createObjectFromClass(className);
			context.put(objectName, o);			
		}
		return o;
	}
	
	

	private Object createObjectFromClass(String className) throws XavaException {
		try {
			return Class.forName(className).newInstance();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(),ex);
			throw new XavaException("create_error", className);
		}
	}

	/**
	 * If does not exist the it create one, as defined in controllers.xml. <p>
	 */	
	public Object get(String application, String module, String objectName) throws XavaException {
		Map context = getContext(application, module, objectName); 
		Object o = context.get(objectName);
		if (o == null) {
			o = createObject(objectName);
			context.put(objectName, o);			
		}
		return o;
	}
	
	public boolean exists(String application, String module, String objectName) throws XavaException {
		Map context = getContext(application, module, objectName); 
		return context.containsKey(objectName);
	}
	
	public boolean exists(HttpServletRequest request, String objectName) throws XavaException {
		String application = request.getParameter("application");
		if (Is.emptyString(application)) {
			throw new XavaException("application_and_module_required_in_request");
		}
		String module = request.getParameter("module");
		if (Is.emptyString(module)) {
			throw new XavaException("application_and_module_required_in_request");
		}		
		return exists(application, module, objectName);		
	}	


	public void put(HttpServletRequest request, String objectName, Object value) throws XavaException {
		String application = request.getParameter("application");
		if (Is.emptyString(application)) {
			throw new XavaException("application_and_module_required_in_request");
		}
		String module = request.getParameter("module");
		if (Is.emptyString(module)) {
			throw new XavaException("application_and_module_required_in_request");
		}				
		Map context = getContext(application, module, objectName); 
		context.put(objectName, value);
	}
		
	public void put(String application, String module, String objectName, Object value) throws XavaException {
		Map context = getContext(application, module, objectName); 
		context.put(objectName, value);
	}

	public void remove(HttpServletRequest request, String objectName) throws XavaException {
		String application = request.getParameter("application");
		if (Is.emptyString(application)) {
			throw new XavaException("application_and_module_required_in_request");
		}
		String module = request.getParameter("module");
		if (Is.emptyString(module)) {
			throw new XavaException("application_and_module_required_in_request");
		}				
		Map context = getContext(application, module, objectName); 
		context.remove(objectName);
	}
		
	public void remove(String application, String module, String objectName) throws XavaException {
		Map context = getContext(application, module, objectName); 
		context.remove(objectName);
	}
	
		
	private Object createObject(String objectName) throws XavaException{			
		return MetaControllers.getMetaObject(objectName).createObject();
	}

	private Map getContext(String application, String module, String objectName) throws XavaException {
		if (isGlobal(objectName)) {
			return getGlobalContext();
		}
		String id = application + "/" + module;
		Map context = (Map) contexts.get(id);
		if (context == null) {
			context = new HashMap();			
			contexts.put(id, context);
		}
		return context;
	}

	private boolean isGlobal(String objectName) throws XavaException {
		try {
			return MetaControllers.getMetaObject(objectName).isGlobal();
		}
		catch (ElementNotFoundException ex) { 
			return false;
		}
	}

	/**
	 * Used for application scope objects.
	 */
	private Map getGlobalContext() {
		if (globalContext == null) {
			globalContext = new HashMap();
		}
		return globalContext;
	}

}
