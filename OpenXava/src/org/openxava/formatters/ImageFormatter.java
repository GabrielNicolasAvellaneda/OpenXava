package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;



/**
 * Implementación simple: solo saca un icono indicando que es una imagen/foto. <p> 
 * 
 * @author Javier Paniza
 */

public class ImageFormatter implements IFormatter {
		
	public String format(HttpServletRequest request, Object booleano) {		
		return "<img src='images/photo.gif'/>";		
	}
	
	public Object parse(HttpServletRequest request, String cadena) throws ParseException {
		return null;		
	}
	
}
