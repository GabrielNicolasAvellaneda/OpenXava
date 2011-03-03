package org.openxava.invoicing.actions;  

import java.util.*;
import org.openxava.actions.*;  
import org.openxava.invoicing.model.*;
import org.openxava.model.*;
import org.openxava.view.*;

public class OnChangeSearchInvoiceAction 
	extends OnChangeSearchAction {  	
												
	public void execute() throws Exception {
		super.execute();  
		Map keyValues = getView()
			.getKeyValuesWithValue();  
		if (keyValues.isEmpty()) return; 
		Invoice invoice = (Invoice)  
			MapFacade.findEntity(getView().getModelName(), keyValues);
		View customerView = getView().getRoot().getSubview("customer"); 
		int customerNumber = customerView.getValueInt("number");		
		if (customerNumber == 0) {  
			customerView.setValue("number", invoice.getCustomer().getNumber());
			customerView.refresh();
		}
		else {  
			if (customerNumber != invoice.getCustomer().getNumber()) {
				addError("invoice_customer_not_match", 
					invoice.getCustomer().getNumber(), invoice, customerNumber); 
				getView().clear();
			}
		}
	}
	
}
