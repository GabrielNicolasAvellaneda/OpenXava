package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;



/**
 * @author Javier Paniza
 */

public class CancelReferenceSearchAction extends ViewBaseAction implements INavigationAction {
	
	private Tab tab;
	
	private Log log = LogFactory.getLog(CancelReferenceSearchAction.class);
	
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
