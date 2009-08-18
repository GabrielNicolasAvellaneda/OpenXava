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
@EntityValidator(value=DeliveredToBeInInvoiceValidator.class, properties= {
	@PropertyValue(name="year"),
	@PropertyValue(name="number"),
    @PropertyValue(name="invoice"),
    @PropertyValue(name="delivered")
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
	
	@AssertTrue
	private boolean isDeliveredToBeInInvoice() {
		if (invoice == null) return true;
		return isDelivered();
	}
	
	/*
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
	*/
	
}
