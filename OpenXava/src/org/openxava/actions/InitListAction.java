package org.openxava.actions;

import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */
public class InitListAction extends BaseAction {
	
	private Tab tab;
	private Tab mainTab;

	public void execute() throws Exception {
		setMainTab(getTab());
	}

	public Tab getMainTab() {
		return mainTab;
	}
	public void setMainTab(Tab mainTab) {
		this.mainTab = mainTab;
	}
	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
