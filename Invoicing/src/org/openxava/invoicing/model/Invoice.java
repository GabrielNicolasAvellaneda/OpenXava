package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
@Views({
	@View( members=
		"year, number, date;" +
		"data {" +
			"customer;" +
			"details;" +
			"amounts [ vatPercentage, baseAmount, vat, totalAmount ];" + // tmp move a papa
			"remarks" +
		"}" +
		"orders { orders } "			
	),	
	/*@View( extendsView="super.DEFAULT",
		members="orders { orders } "
	),
	*/
	@View( name="NoCustomerNoOrders", members=			
		"year, number, date;" +
		"details;" +
		"remarks" 
	)
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
