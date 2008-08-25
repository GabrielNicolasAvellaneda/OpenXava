package org.openxava.web.dwr;

import javax.servlet.http.*;

import org.openxava.util.*;

/**
 * For accessing to the Tab from DWR. <p>
 * 
 * @author Javier Paniza
 */

public class Tab extends DWRBase {

	public static void setFilterVisible(HttpServletRequest request, String application, String module, boolean filterVisible, String tabObject) {
		checkSecurity(request, application, module);
		Users.setCurrent(request);
		org.openxava.tab.Tab tab = (org.openxava.tab.Tab) 
			getContext(request).get(application, module, tabObject);
		tab.setFilterVisible(filterVisible);
	}
	
}
