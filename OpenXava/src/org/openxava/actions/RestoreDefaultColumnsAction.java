package org.openxava.actions;

import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */
public class RestoreDefaultColumnsAction extends BaseAction implements INavigationAction, IChangeModeAction {

	private Tab tab;


	public void execute() throws Exception {		
		getTab().restoreDefaultProperties();		
	}
	
	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String[] getNextControllers() throws Exception {
		return DEFAULT_CONTROLLERS;
	}

	public String getCustomView() throws Exception {
		return DEFAULT_VIEW;
	}

	public String getNextMode() {
		return LIST;
	}
}
