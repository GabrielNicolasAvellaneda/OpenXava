package org.openxava.actions;

import org.openxava.tab.*;



/**
 * @author Javier Paniza
 */

public class CancelReferenceSearchAction extends ViewBaseAction implements INavigationAction {
	
	private boolean keyEditable = false;
	private boolean editable = true;
	private Tab tab;
		 	
	public void execute() throws Exception {
		getTab().setModelName(getView().getModelName());		
	}
	
	public String [] getNextControllers() {
		return PREVIOUS_CONTROLLERS;
	}
	
	public String getCustomView() {
		return "xava/detail";
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
