package org.openxava.actions;



import org.openxava.tab.*;

public class FilterAction extends BaseAction {
	
	private Tab tab;
	
	
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
