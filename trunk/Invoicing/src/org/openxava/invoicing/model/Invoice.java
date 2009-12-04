package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;
import org.openxava.annotations.*;

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

}
