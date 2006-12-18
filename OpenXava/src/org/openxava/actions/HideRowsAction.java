package org.openxava.actions;



import org.openxava.tab.*;

public class HideRowsAction extends BaseAction {
	
	private Tab tab;

	
	
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
