package org.openxava.actions;

import org.openxava.tab.*;
import org.openxava.view.*;


/**
 * @author Javier Paniza
 */

public class GoListAction extends BaseAction implements IChangeModeAction, INavigationAction {
		
	private View view;
	private Tab tab;
	private int page;
		
	public String getNextMode() {		
		return IChangeModeAction.LIST;
	}

	public void execute() throws Exception {		
		getView().clear();
		getTab().setAllSelected(null); // If add or remove not unsynchronize the selection		
		getTab().setBaseConditionForReference(null);	// For remove effect of use tab in references 
																			// This not affects to base condition of module, 
																			// which it is set again
		getTab().setTabName(null); // Thus list mode restore the default module tab name		
		getTab().goPage(page);		
	}

	public View getView() {
		return view;
	}

	public void setView(View vista) {		
		this.view = vista;
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

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
