package org.openxava.formatters;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;


/**
 * Formateador base que permite acceder a objeto del contexto
 * del m�dulo actual.
 * 
 * @author Javier Paniza
 */

abstract public class BaseFormatter implements IFormatter {
	
	private ModuleContext getContext(HttpServletRequest request) {
		ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");
		Assert.assertNotNull("Deber�a existir un objeto de sesi�n llamado 'contexto'", context);		
		return context;		
	}	
	
	/** En el contexto del m�dulo */
	protected void put(HttpServletRequest request, String nombre, Object valor) throws XavaException {
		getContext(request).put(request, nombre, valor);
	}
	
	/** Del contexto del m�dulo */
	protected Object get(HttpServletRequest request, String nombre) throws XavaException {
		return getContext(request).get(request, nombre);
	}
	
	/** Del contexto del m�dulo */
	protected Object get(HttpServletRequest request, String nombre, String clase) throws XavaException {
		return getContext(request).get(request, nombre, clase);
	}
	
	
	/** Del contexto del m�dulo */
	protected String getString(HttpServletRequest request, String nombre) throws XavaException {
		return (String) get(request, nombre);
	}
	
	/** Del contexto del m�dulo */
	protected Integer getInteger(HttpServletRequest request, String nombre) throws XavaException {
		return (Integer) get(request, nombre);
	}
	
}
