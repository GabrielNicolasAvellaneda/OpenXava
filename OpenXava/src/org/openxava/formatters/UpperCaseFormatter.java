package org.openxava.formatters;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class UpperCaseFormatter implements IFormatter {
	
	private static Log log = LogFactory.getLog(UpperCaseFormatter.class);
	
	public String format(HttpServletRequest request, Object string) {		
		return string==null?"":string.toString().toUpperCase();			
	}
	
	public Object parse(HttpServletRequest request, String string) {
		return string==null?"":string.toString().toUpperCase();		
	}
	
}
