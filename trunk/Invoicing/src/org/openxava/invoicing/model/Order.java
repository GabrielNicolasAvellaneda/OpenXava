package org.openxava.invoicing.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
/*
@View( extendsView="super.DEFAULT",
	members="invoice { invoice } "
)
*/
@View( members=
	"year, number, date;" +
	"data {" +
		"customer;" +
		"details;" +
		"remarks" +
	"}" +
	"invoice { invoice } "			
)
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
