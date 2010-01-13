package org.openxava.invoicing.actions;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import static org.openxava.jpa.XPersistence.*;

public class CreateInvoiceFromOrderAction extends ViewBaseAction {

	public void execute() throws Exception {
		Order order = getManager().find(  
				Order.class, 			
				getView().getValue("oid"));
		order.createInvoice();
	}

}
