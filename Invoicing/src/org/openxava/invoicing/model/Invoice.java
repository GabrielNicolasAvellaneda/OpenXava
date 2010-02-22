package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;
import org.openxava.annotations.*;
import org.openxava.validators.*;

@Entity
@Views({
	@View( extendsView="super.DEFAULT",
		members="orders { orders } "
	),	
	@View( name="NoCustomerNoOrders", members=			
		"year, number, date;" +
		"details;" +
		"remarks" 
	)
	
})
@Tabs({
	@Tab(baseCondition = "deleted = false"),
	@Tab(name="Deleted", baseCondition = "deleted = true")
})
public class Invoice extends CommercialDocument {
	
	@OneToMany(mappedBy="invoice")
	@CollectionView("NoCustomerNoInvoice")
	private Collection<Order> orders;
	
	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

	public static Invoice createFromOrders(Collection<Order> orders) throws ValidationException { // tmp
		Invoice invoice = null;
		for (Order order: orders) {
			if (invoice == null) {
				order.createInvoice();
				invoice = order.getInvoice();
			}
			else {
				order.setInvoice(invoice);
				order.copyDetailsToInvoice(invoice);
			}
		}
		if (invoice == null) {
			throw new ValidationException("No hay pedidos, colega"); // tmp i18n
		}
		return invoice;
	}
	
}
