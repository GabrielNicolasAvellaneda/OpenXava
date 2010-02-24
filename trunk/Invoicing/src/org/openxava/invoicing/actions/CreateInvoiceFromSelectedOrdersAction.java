package org.openxava.invoicing.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;

public class CreateInvoiceFromSelectedOrdersAction extends TabBaseAction {

	public void execute() throws Exception {
		Collection<Order> orders = getSelectedOrders();
		Invoice invoice = Invoice.createFromOrders(orders);
		addMessage("invoice_created_from_orders", invoice, orders);
	}
	
	private Collection<Order> getSelectedOrders() throws FinderException { 
		Collection<Order> result = new ArrayList<Order>(); 
		for (Map key: getTab().getSelectedKeys()) {
			Order order = (Order) MapFacade.findEntity("Order", key);
			result.add(order);
		}
		return result;
	}

}

