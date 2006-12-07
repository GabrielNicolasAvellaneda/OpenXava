package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

public class ShowRowsAction extends BaseAction {
	
	private Tab tab;
	private static Log log = LogFactory.getLog(ShowRowsAction.class);

	public void execute() throws Exception {
		getTab().showRows();		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
