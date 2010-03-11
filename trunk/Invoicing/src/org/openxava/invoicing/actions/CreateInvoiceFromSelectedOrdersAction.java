package org.openxava.invoicing.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;

public class CreateInvoiceFromSelectedOrdersAction 
	extends TabBaseAction	
	implements IChangeModuleAction
{
		
	private Map currentInvoiceKey;

	public void execute() throws Exception {
		Collection<Order> orders = getSelectedOrders();
		Invoice invoice = Invoice.createFromOrders(orders);
		addMessage("invoice_created_from_orders", invoice, orders);
		setCurrentInvoiceKey(toKey(invoice));
	}
	
	private Map toKey(Invoice invoice) { 
		Map key = new HashMap();
		key.put("oid",invoice.getOid());
		return key;
	}

	private Collection<Order> getSelectedOrders() throws FinderException { 
		Collection<Order> result = new ArrayList<Order>(); 
		for (Map key: getTab().getSelectedKeys()) {
			Order order = (Order) MapFacade.findEntity("Order", key);
			result.add(order);
		}
		return result;
	}
	
	public String getNextModule() {
		return "CurrentInvoiceEdition";
	}

	public boolean hasReinitNextModule() { 
		return true;
	}	

	public void setCurrentInvoiceKey(Map currentInvoiceKey) {
		this.currentInvoiceKey = currentInvoiceKey;
	}

	public Map getCurrentInvoiceKey() {
		return currentInvoiceKey;
	}

}

