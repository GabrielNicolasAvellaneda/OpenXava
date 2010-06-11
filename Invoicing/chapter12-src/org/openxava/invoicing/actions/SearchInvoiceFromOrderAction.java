package org.openxava.invoicing.actions;

import org.openxava.actions.*;

public class SearchInvoiceFromOrderAction extends ReferenceSearchAction {

	public void execute() throws Exception {
		super.execute();
		int customerNumber = getView().getValueInt("customer.number");
		if (customerNumber > 0) {
			getTab().setBaseCondition("${customer.number} = " + customerNumber);
		}
	}
}
