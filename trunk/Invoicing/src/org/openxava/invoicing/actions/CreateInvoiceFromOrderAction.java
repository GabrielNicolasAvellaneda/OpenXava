package org.openxava.invoicing.actions;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;

import static org.openxava.jpa.XPersistence.*;

public class CreateInvoiceFromOrderAction extends ViewBaseAction {

	public void execute() throws Exception {
		Object oid = getView().getValue("oid");
		if (oid == null) {
			addError(""); // tmp i18n
			return;
		}
		MapFacade.setValues("Order", getView().getKeyValues(), getView().getValues());
		Order order = getManager().find(  
				Order.class, 			
				// tmp getView().getValue("oid"));
				oid); // tmp
		order.createInvoice();
		getView().refresh();
		addMessage("invoice_created_from_order", 
			order.getInvoice().getYear(), 
			order.getInvoice().getNumber());
	}

}
