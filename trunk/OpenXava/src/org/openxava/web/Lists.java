package org.openxava.web;

import javax.servlet.http.*;

/**
 * Utilities used from JSP files for lists. 
 * 
 * @since 4.5.1
 * @author Javier Paniza 
 */
public class Lists {
	
	public static String getImageFilterPrefix(HttpServletRequest request) {
		return request.getContextPath() + "/xava/images/";
	}

}
