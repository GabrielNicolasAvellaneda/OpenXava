package org.openxava.invoicing.actions;

import org.openxava.actions.*;

public class GoAddOrdersToInvoiceAction 
	extends GoAddElementsToCollectionAction {
												
	public void execute() throws Exception {
		super.execute();  
		int customerNumber = 
			getPreviousView()
				.getValueInt("customer.number");
												
		getTab().setBaseCondition(  	
			"${customer.number} = " + customerNumber +
			" and ${delivered} = true and ${invoice.oid} is null"
		);
	}
	
	public String getNextController() {  
		return "AddOrdersToInvoice"; 	
	}											

}