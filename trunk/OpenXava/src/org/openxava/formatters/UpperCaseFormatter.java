package org.openxava.formatters;

import javax.servlet.http.*;

/**
 * @author Javier Paniza
 */

public class UpperCaseFormatter implements IFormatter {
	
	public String format(HttpServletRequest request, Object cadena) {		
		return cadena==null?"":cadena.toString().toUpperCase();			
	}
	
	public Object parse(HttpServletRequest request, String cadena) {
		return cadena==null?"":cadena.toString().toUpperCase();		
	}
	
}
