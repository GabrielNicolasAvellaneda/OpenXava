package org.openxava.formatters;

import javax.servlet.http.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class BooleanFormatter implements IFormatter {
	
	public String format(HttpServletRequest request, Object booleano) {
		try {
			if (booleano == null) {
				return Labels.get("no", request.getLocale());
			}
			else {
				boolean r = ((Boolean) booleano).booleanValue();
				return r?Labels.get("yes", request.getLocale()):Labels.get("no", request.getLocale());
			}		
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			return "?";
		}
	}
	
	public Object parse(HttpServletRequest request, String cadena) {
		if (Is.emptyString(cadena)) return Boolean.FALSE;
		if (
			"Sí".equalsIgnoreCase(cadena) ||
			"SÍ".equalsIgnoreCase(cadena) ||
			"Si".equalsIgnoreCase(cadena) ||
			"true".equalsIgnoreCase(cadena) ||
			"verdadero".equalsIgnoreCase(cadena)) return Boolean.TRUE;	 				
		return Boolean.FALSE;
	}
	
}
