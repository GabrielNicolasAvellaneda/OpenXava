package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;




/**
 * A simple implementation: Only it shows a icon to indicate that it's a image/photo. <p> 
 * 
 * @author Javier Paniza
 */

public class ImageFormatter implements IFormatter {
		
	
	
	public String format(HttpServletRequest request, Object booleanValue) {		
		return "<img src='" + request.getContextPath() + "/xava/images/photo.gif'/>";		
	}
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		return null;		
	}
	
}
