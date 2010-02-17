package org.openxava.invoicing.actions;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;

public class CreateInvoiceFromSelectedOrdersAction extends TabBaseAction {

	public void execute() throws Exception {
		for (Map key: getTab().getSelectedKeys()) {
			Order order = (Order) MapFacade.findEntity("Order", key);
			order.c
			System.out
					.println("[CreateInvoiceFromSelectedOrdersAction.execute] " + key); // tmp
			
		}
	}
	
	private Collection<Order> getSelectedEntities() { // tmp ¿mover a Tab y poner OX4?
		Collection<Order> result = new ArrayList<Order>(); 
		for (Map key: getTab().getSelectedKeys()) {
			Order order = (Order) MapFacade.findEntity("Order", key);
			order.c
			System.out
					.println("[CreateInvoiceFromSelectedOrdersAction.execute] " + key); // tmp
			
		}		
	}

}
