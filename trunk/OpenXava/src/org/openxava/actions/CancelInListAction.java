package org.openxava.actions;


/**
 * In list mode return to initial state (if we have navigated)
 * 
 * @author Javier Paniza
 */

public class CancelInListAction extends BaseAction implements IChangeModeAction, INavigationAction {
		
	public String getNextMode() {		
		return IChangeModeAction.LIST;
	}

	public void execute() throws Exception {		
	}


	public String[] getNextControllers() {		
		return DEFAULT_CONTROLLERS;
	}

	public String getCustomView() {		
		return DEFAULT_VIEW;
	}

}
