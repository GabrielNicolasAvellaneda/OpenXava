package org.openxava.actions;



import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class GoNextPageAction extends BaseAction {
	
	private Tab tab;
	

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
