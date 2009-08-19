package org.openxava.invoicing.model;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.invoicing.validators.*;
import org.openxava.util.*;

@Entity
@Views({
	/*
	@View( extendsView="super.DEFAULT",
		members="delivered; invoice { invoice } "
	),
	*/
	@View( members=
		"year, number, date, delivered;" +
		"data {" +
			"customer;" +
			"details;" +
			"amounts [ vatPercentage, baseAmount, vat, totalAmount ];" + // tmp move a papa
			"remarks" +
		"}" +
		"invoice { invoice } "			
	),
	@View( name="NoCustomerNoInvoice", members=			
		"year, number, date;" +
		"details;" +
		"remarks" 
	)
})
@RemoveValidator(OrderRemoveValidator.class)
public class Order extends CommercialDocument {
	
	@ManyToOne(fetch=FetchType.LAZY)
	@ReferenceView("NoCustomerNoOrders")
	private Invoice invoice;
	
	private boolean delivered;

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	@AssertTrue
	private boolean isDeliveredToBeInInvoice() {		
		return invoice == null || isDelivered();
	}	
	
}
