package org.openxava.invoicing.actions;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;

import static org.openxava.jpa.XPersistence.*;

public class CreateInvoiceFromOrderAction extends ViewBaseAction 
	implements IHideActionAction 
{
	
	private boolean hideAction = false;

	public void execute() throws Exception {
		Object oid = getView().getValue("oid");
		if (oid == null) {
			addError("impossible_create_invoice_order_not_exist");
			return;
		}
		MapFacade.setValues("Order", 
			getView().getKeyValues(), getView().getValues());
		Order order = getManager().find(  
			Order.class, oid); 
		order.createInvoice();
		getView().refresh();
		addMessage("invoice_created_from_order", order.getInvoice()); // tmp
		/* tmp
		addMessage("invoice_created_from_order", 
			order.getInvoice().getYear(), 
			order.getInvoice().getNumber());
			*/
		hideAction = true;
	}

	public String getActionToHide() {
		return hideAction?"Order.createInvoice":null;		
	}

}
