package org.openxava.formatters;

import javax.servlet.http.*;



import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class BooleanFormatter implements IFormatter {
	
	
	
	public String format(HttpServletRequest request, Object booleanValue) {
		if (booleanValue == null) {
			return Labels.get("no", Locales.getCurrent());
		}
		else {
			boolean r = ((Boolean) booleanValue).booleanValue();
			return r?Labels.get("yes", Locales.getCurrent()):Labels.get("no", Locales.getCurrent());
		}		
	}
	
	public Object parse(HttpServletRequest request, String string) {
		if (Is.emptyString(string)) return Boolean.FALSE;
		if (
			"yes".equalsIgnoreCase(string) ||
			"Sí".equalsIgnoreCase(string) ||
			"SÍ".equalsIgnoreCase(string) ||
			"Si".equalsIgnoreCase(string) ||
			"true".equalsIgnoreCase(string) ||
			"verdadero".equalsIgnoreCase(string)) return Boolean.TRUE;	 				
		return Boolean.FALSE;
	}
	
}
