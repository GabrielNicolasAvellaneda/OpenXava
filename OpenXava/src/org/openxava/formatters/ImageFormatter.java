package org.openxava.formatters;

import java.text.*;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple implementation: Only it shows a icon to indicate that it's a image/photo. <p> 
 * 
 * @author Javier Paniza
 */

public class ImageFormatter implements IFormatter {
		
	private Log log = LogFactory.getLog(ImageFormatter.class);
	
	public String format(HttpServletRequest request, Object booleanValue) {		
		return "<img src='images/photo.gif'/>";		
	}
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {
		return null;		
	}
	
}
