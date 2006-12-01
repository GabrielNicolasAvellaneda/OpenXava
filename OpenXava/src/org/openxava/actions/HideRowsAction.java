package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

public class HideRowsAction extends BaseAction {
	
	private Tab tab;

	private Log log = LogFactory.getLog(HideRowsAction.class);
	
	public void execute() throws Exception {
		getTab().hideRows();		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
