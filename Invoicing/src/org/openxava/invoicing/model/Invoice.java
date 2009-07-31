package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
/*
@View( members=
	"year, number, date;" +
	"data {" +
		"customer;" +
		"details;" +
		"remarks" +
	"}" +
	"orders { orders } "			
)
*/

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
public class Invoice extends CommercialDocument {
	
	@OneToMany(mappedBy="invoice")
	private Collection<Order> orders;

	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}

}
