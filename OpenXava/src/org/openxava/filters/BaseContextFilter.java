package org.openxava.filters;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;


/**
 * Filtro base que permite acceder a objeto del contexto
 * del módulo actual y también de otros módulos.
 * 
 * @author Javier Paniza
 */

abstract public class BaseContextFilter implements IRequestFilter {

	private HttpServletRequest request;	
	private ModuleContext context;

	public void setRequest(HttpServletRequest request) {
		this.request = request;		
		this.context = null; 
	}
	
	protected ModuleContext getContext() {
		if (context == null) {
			context = (ModuleContext) request.getSession().getAttribute("context");
			Assert.assertNotNull("Debería existir un objeto de sesión llamado 'contexto'", context);
		}
		return context;		
	}	
	
	protected Object get(String nombre) throws XavaException {
		return getContext().get(request, nombre);
	}
	
	protected String getString(String nombre) throws XavaException {
		return (String) get(nombre);
	}
	
	protected Integer getInteger(String nombre) throws XavaException {
		return (Integer) get(nombre);
	}

	protected Long getLong(String nombre) throws XavaException {
		return (Long) get(nombre);
	}
	

}
