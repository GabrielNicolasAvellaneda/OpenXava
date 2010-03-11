package org.openxava.invoicing.actions;

import java.util.*;

import org.openxava.actions.*;

public class LoadCurrentInvoiceAction 
	extends SearchByViewKeyAction {
	
	private Map currentInvoiceKey;
	
	public void execute() throws Exception {
		getView().setValues(getCurrentInvoiceKey());
		super.execute();
	}

	public void setCurrentInvoiceKey(Map currentInvoiceKey) {
		this.currentInvoiceKey = currentInvoiceKey;
	}

	public Map getCurrentInvoiceKey() {
		return currentInvoiceKey;
	}

}
