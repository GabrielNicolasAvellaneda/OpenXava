package org.openxava.util;

import javax.servlet.http.*;

/**
 * Create on 16/01/2014 (16:26:35)
 * @author Ana Andres
 */
public interface IReportsProvider {

	/**
	 * To display or not the adminReports action
	 */
	boolean isCurrentUserAdminForReports(HttpServletRequest request);
	
}
