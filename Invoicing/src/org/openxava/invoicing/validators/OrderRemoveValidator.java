package org.openxava.invoicing.validators;

import org.openxava.invoicing.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

public class OrderRemoveValidator implements IRemoveValidator {

	private Order order;
	
	public void setEntity(Object entity) throws Exception {
		this.order = (Order) entity;		
	}

	public void validate(Messages errors) throws Exception {
		if (order.getInvoice() != null) {
			errors.add("cannot_delete_order_with_invoice");
		}
	}

}
