package org.openxava.actions;

import org.openxava.tab.*;


/**
 * @author Javier Paniza
 */
public class CustomizeListAction extends BaseAction {
	
	private Tab tab;

	public void execute() throws Exception {
		getTab().setCustomize(!getTab().isCustomize());
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
