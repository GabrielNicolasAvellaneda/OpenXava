package org.openxava.invoicing.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.beanutils.*;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.openxava.validators.*;

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
@Tabs({
	@Tab(baseCondition = "deleted = false"),
	@Tab(name="Deleted", baseCondition = "deleted = true")
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

	public void createInvoice() throws ValidationException {
		if (this.invoice != null) {
			throw new ValidationException("impossible_create_invoice_order_already_has_one"); 
		}
		if (!isDelivered()) {
			throw new ValidationException("impossible_create_invoice_order_is_not_delivered");
		}
		try {
			Invoice invoice = new Invoice();
			BeanUtils.copyProperties(invoice, this);
			invoice.setOid(null);		
			invoice.setDetails(new ArrayList());		
			XPersistence.getManager().persist(invoice);
			copyDetailsToInvoice(invoice);
			this.invoice = invoice;
		}
		catch (Exception ex) {
			throw new SystemException("impossible_create_invoice", ex);
		}
	}

	private void copyDetailsToInvoice(Invoice invoice) throws Exception {
		for (Detail orderDetail: getDetails()) {
			Detail invoiceDetail = (Detail) BeanUtils.cloneBean(orderDetail);							
			invoiceDetail.setOid(null);
			invoiceDetail.setParent(invoice);
			XPersistence.getManager().persist(invoiceDetail);
		}				
	}
		
}
