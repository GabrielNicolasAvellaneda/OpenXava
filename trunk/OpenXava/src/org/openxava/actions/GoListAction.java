package org.openxava.actions;

import org.openxava.tab.*;
import org.openxava.view.*;


/**
 * @author Javier Paniza
 */

public class GoListAction extends BaseAction implements IChangeModeAction, INavigationAction {
		
	private View view;
	private Tab tab;
	private Tab mainTab;
		
	public String getNextMode() {		
		return IChangeModeAction.LIST;
	}

	public void execute() throws Exception {		
		getView().clear();
		setTab(getMainTab());
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {		
		this.view = view;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String[] getNextControllers() {		
		return DEFAULT_CONTROLLERS;
	}

	public String getCustomView() {		
		return DEFAULT_VIEW;
	}

	public Tab getMainTab() {
		return mainTab;
	}
	public void setMainTab(Tab maintTab) {
		this.mainTab = maintTab;
	}
}
