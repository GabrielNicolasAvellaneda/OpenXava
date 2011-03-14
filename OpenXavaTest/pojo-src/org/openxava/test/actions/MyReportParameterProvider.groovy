package org.openxava.test.actions

import org.openxava.util.*;

/**
 * Create on 11/03/2011 (13:30:32)
 * @author Ana Andres
 */
class MyReportParameterProvider implements IReportParameterProvider{
	
	String getOrganization() {
		return "report to " + Users.getCurrent()
	}
	
}