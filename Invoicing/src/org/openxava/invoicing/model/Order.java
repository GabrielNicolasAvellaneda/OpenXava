package org.openxava.invoicing.model;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.util.*;

@Entity
@Views({
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
	/*
	@View( extendsView="super.DEFAULT",
		members="delivered; invoice { invoice } "
	),
	*/	
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
		/* tmp
		if (invoice != null && !isDelivered()) {			
			//throw new XavaException("Açò ha kaskat");			
			throw new InvalidStateException(
				new InvalidValue[] {
					new InvalidValue("Ha caskat", getClass(), "delivered", true, this)
				}
			);
			
		}
		*/
		this.invoice = invoice;
	}
	
	@PreUpdate
	private void validate() throws Exception {
		if (invoice != null && !isDelivered()) {			
			//throw new XavaException("Açò ha kaskat");			
			throw new InvalidStateException(
				new InvalidValue[] {
					new InvalidValue("Ha caskat", getClass(), "delivered", true, this)
				}
			);
			
		}	
	}
	
}
