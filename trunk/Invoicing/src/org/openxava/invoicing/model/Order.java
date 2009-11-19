package org.openxava.invoicing.model;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity
@Views({
	@View( extendsView="super.DEFAULT",
		members="delivered; invoice { invoice } "
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
	@SearchAction("Order.searchInvoice") // tmp
	private CommercialDocument invoice;
	
	private boolean delivered;

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public CommercialDocument getInvoice() {
		return invoice;
	}

	public void setInvoice(CommercialDocument invoice) {
		this.invoice = invoice;
	}
	
	@AssertTrue
	private boolean isDeliveredToBeInInvoice() {		
		return invoice == null || isDelivered();
	}	
	
	@AssertTrue
	private boolean isCustomerOfInvoiceMustBeTheSame() {
		return invoice == null || 
			invoice.getCustomer().getNumber() == getCustomer().getNumber();
	}
	
	@PreRemove
	private void validateOnRemove() { 
		if (invoice != null) {
			throw new IllegalStateException(
				XavaResources.getString(
					"cannot_delete_order_with_invoice"));
		}
	}
	
}
