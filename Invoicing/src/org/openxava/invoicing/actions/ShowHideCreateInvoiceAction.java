package org.openxava.invoicing.actions;

import org.openxava.actions.*;

public class ShowHideCreateInvoiceAction 
	extends OnChangePropertyBaseAction 
	implements IShowActionAction, IHideActionAction {
	
	private boolean show;

	public void execute() throws Exception {
		// tmp show =  isOrderCreated() && isDelivered() && !hasInvoice();
		show = true; // tmp
	}

	private boolean isOrderCreated() {
		return getView().getValue("oid") != null;
	}
	
	private boolean isDelivered() {
		Boolean delivered = (Boolean) getView().getValue("delivered");
		return delivered == null?false:delivered;
	}
	
	private boolean hasInvoice() {
		return getView().getValue("invoice.oid") != null;
	}

	public String getActionToShow() {		
		return show?"Order.createInvoice":"";
	}

	public String getActionToHide() {
		return !show?"Order.createInvoice":"";
	}
	
}
