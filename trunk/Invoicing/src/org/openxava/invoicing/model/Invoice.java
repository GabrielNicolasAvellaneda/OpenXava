package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
/*
@View( extendsView="super.DEFAULT",
	members="orders { orders } "
)
*/
@View( members=
	"year, number, date;" +
	"data {" +
		"customer;" +
		"details;" +
		"remarks" +
	"}" +
	"orders { orders } "			
)
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
