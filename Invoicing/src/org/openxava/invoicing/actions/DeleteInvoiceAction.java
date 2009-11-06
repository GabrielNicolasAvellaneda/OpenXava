package org.openxava.invoicing.actions;

import org.openxava.actions.*;

public class DeleteInvoiceAction extends ViewBaseAction {

	public void execute() throws Exception {
		addMessage("Don't worry! I have cleared only the screen");
		getView().clear();		
	}

}
