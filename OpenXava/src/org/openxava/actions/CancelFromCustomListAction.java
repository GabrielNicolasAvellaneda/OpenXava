package org.openxava.actions;



import org.openxava.tab.*;



/**
 * @author Javier Paniza
 */

public class CancelFromCustomListAction extends ViewBaseAction implements INavigationAction {
	
	private Tab tab;
	
	
	
	public void execute() throws Exception {		
		getTab().setModelName(getView().getModelName());		
	}
	
	public String [] getNextControllers() {
		return PREVIOUS_CONTROLLERS;
	}
	
	public String getCustomView() {
		return PREVIOUS_VIEW; 
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}

}
