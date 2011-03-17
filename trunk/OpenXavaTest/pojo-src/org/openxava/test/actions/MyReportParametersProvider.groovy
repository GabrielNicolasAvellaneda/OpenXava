package org.openxava.test.actions

import org.openxava.util.*;

/**
 * Create on 11/03/2011 (13:30:32)
 * @author Ana Andres
 */
class MyReportParametersProvider implements IReportParametersProvider {
	
	String getOrganization() {
		return "report to " + Users.getCurrent()
	}
	
}