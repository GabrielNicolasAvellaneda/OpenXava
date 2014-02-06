package org.openxava.util;

import javax.servlet.http.*;


/**
 * By default the adminReports action only is displayed on users that start with 'admin'
 *  
 * Create on 16/01/2014 (16:37:47)
 * @author Ana Andres
 */
public class DefaultReportsProvider implements IReportsProvider{

	public boolean isCurrentUserAdminForReports(HttpServletRequest request) {
		if (Users.getCurrent() == null) return false;
		else return Users.getCurrent().startsWith("admin");
	}

}