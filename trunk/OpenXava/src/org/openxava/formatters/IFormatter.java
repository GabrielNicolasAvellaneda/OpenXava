package org.openxava.formatters;

import javax.servlet.http.*;


/**
 * @author Javier Paniza
 */

public interface IFormatter {
				
	public String format(HttpServletRequest request, Object object) throws Exception;
	public Object parse(HttpServletRequest request, String string) throws Exception;
	 
}
