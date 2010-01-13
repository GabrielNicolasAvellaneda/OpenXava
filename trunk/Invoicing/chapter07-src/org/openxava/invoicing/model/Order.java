package org.openxava.invoicing.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
@Views({
	@View( extendsView="super.DEFAULT",
		members="invoice { invoice } "
	),	
	@View( name="NoCustomerNoInvoice", members=			
		"year, number, date;" +
		"details;" +
		"remarks" 
	)
})
public class Order extends CommercialDocument {
	
	@ManyToOne(fetch=FetchType.LAZY)
	@ReferenceView("NoCustomerNoOrders")
	private Invoice invoice;

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
}
