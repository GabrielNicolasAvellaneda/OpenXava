package org.openxava.formatters;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;


/**
 * Formateador base que permite acceder a objeto del contexto
 * del módulo actual.
 * 
 * @author Javier Paniza
 */

abstract public class BaseFormatter implements IFormatter {
	
	private ModuleContext getContext(HttpServletRequest request) {
		ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");
		Assert.assertNotNull("Debería existir un objeto de sesión llamado 'contexto'", context);		
		return context;		
	}	
	
	/** En el contexto del módulo */
	protected void put(HttpServletRequest request, String nombre, Object valor) throws XavaException {
		getContext(request).put(request, nombre, valor);
	}
	
	/** Del contexto del módulo */
	protected Object get(HttpServletRequest request, String nombre) throws XavaException {
		return getContext(request).get(request, nombre);
	}
	
	/** Del contexto del módulo */
	protected Object get(HttpServletRequest request, String nombre, String clase) throws XavaException {
		return getContext(request).get(request, nombre, clase);
	}
	
	
	/** Del contexto del módulo */
	protected String getString(HttpServletRequest request, String nombre) throws XavaException {
		return (String) get(request, nombre);
	}
	
	/** Del contexto del módulo */
	protected Integer getInteger(HttpServletRequest request, String nombre) throws XavaException {
		return (Integer) get(request, nombre);
	}
	
}
