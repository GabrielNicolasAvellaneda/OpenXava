package org.openxava.actions;

import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class GoPageAction extends BaseAction {
	
	private Tab tab;
	private int page;

	public void execute() throws Exception {
		tab.goPage(page);
		tab.setNotResetNextTime(true);		
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int i) {
		page = i;
	}

}
