package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

public class FilterAction extends BaseAction {
	
	private Tab tab;
	private static Log log = LogFactory.getLog(FilterAction.class);
	
	public void execute() throws Exception {
		getTab().setRowsHidden(false);
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
