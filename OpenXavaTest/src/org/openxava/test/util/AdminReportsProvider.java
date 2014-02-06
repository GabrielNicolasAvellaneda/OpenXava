package org.openxava.test.util;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * Create on 16/01/2014 (17:28:57)
 * @author Ana Andres
 */
public class AdminReportsProvider implements IReportsProvider{
	private static Log log = LogFactory.getLog(AdminReportsProvider.class);
	
	public boolean isCurrentUserAdminForReports(HttpServletRequest request) {
		// display the adminReport action only in the Color module
		String module = request.getParameter("module");
		return "Color".equals(module);
	}
}
