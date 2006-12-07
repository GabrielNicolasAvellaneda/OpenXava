package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class GoNextPageAction extends BaseAction {
	
	private Tab tab;
	private static Log log = LogFactory.getLog(GoNextPageAction.class);

	public void execute() throws Exception {
		tab.pageForward();
		tab.setNotResetNextTime(true);		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

}
