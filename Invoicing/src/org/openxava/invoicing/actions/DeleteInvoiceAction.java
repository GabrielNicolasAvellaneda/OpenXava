package org.openxava.invoicing.actions;

import static org.openxava.jpa.XPersistence.*;
import org.openxava.actions.*;
import org.openxava.invoicing.model.*;

public class DeleteInvoiceAction extends ViewBaseAction {

	public void execute() throws Exception {
		if (getView().getValue("oid") == null) {
			addError("no_delete_not_exists");
			return;
		}
		Invoice invoice = getManager().find(
				Invoice.class, getView().getValue("oid")); 		
		invoice.setDeleted(true);
		addMessage("object_deleted", "Invoice");
		getView().clear();		
	}

}
