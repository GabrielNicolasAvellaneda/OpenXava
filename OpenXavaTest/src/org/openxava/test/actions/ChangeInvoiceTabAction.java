package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */
public class ChangeInvoiceTabAction extends BaseAction {

	private Tab tab;
	
	public void execute() throws Exception {
		getTab().setTabName("Simple");
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}
}
