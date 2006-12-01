package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class GoPreviousPageAction extends BaseAction {
	
	private Tab tab;
	private Log log = LogFactory.getLog(GoPreviousPageAction.class);

	public void execute() throws Exception {
		tab.pageBack();
		tab.setNotResetNextTime(true);		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

}
