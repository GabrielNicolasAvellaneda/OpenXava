package org.openxava.controller;

import java.util.*;
import javax.servlet.http.*;

import org.openxava.controller.meta.*;
import org.openxava.util.*;

/**
 * Permite un contexto con vida de sesión privado para 
 * cada módulo.
 * 
 * @author Javier Paniza
 */

public class ModuleContext {
	
	static {
		MetaControllers.setContext(MetaControllers.WEB);		
	}
	
	
	private Map contexts = new HashMap();

	/**
	 * Return a object asociate to the specified modul
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
	 * Return a object asociate to the specified modul
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
		Map context = getContext(application, module); 
		Object o = context.get(objectName);
		if (o == null) {
			o = createObjectFromClass(className);
			context.put(objectName, o);			
		}
		return o;
	}
	
	

	private Object createObjectFromClass(String clase) throws XavaException {
		try {
			return Class.forName(clase).newInstance();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_error", clase);
		}
	}

	/**
	 * Si no existe lo crea, según está definido controladores.xml. <p>
	 */	
	public Object get(String application, String module, String objectName) throws XavaException {
		Map context = getContext(application, module); 
		Object o = context.get(objectName);
		if (o == null) {
			o = createObject(objectName);
			context.put(objectName, o);			
		}
		return o;
	}
	
	public boolean exists(String application, String module, String objectName) throws XavaException {
		Map context = getContext(application, module); 
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
		Map context = getContext(application, module); 
		context.put(objectName, value);
	}
		
	public void put(String application, String module, String objectName, Object value) throws XavaException {
		Map context = getContext(application, module); 
		context.put(objectName, value);
	}
	
	private Object createObject(String objectName) throws XavaException{			
		return MetaControllers.getMetaObject(objectName).createObject();
	}

	private Map getContext(String application, String module) throws XavaException {
		String id = application + "/" + module;
		Map context = (Map) contexts.get(id);
		if (context == null) {
			context = new HashMap();			
			contexts.put(id, context);
		}
		return context;
	}

}
