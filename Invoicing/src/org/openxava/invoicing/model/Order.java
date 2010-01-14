package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;

import org.apache.commons.beanutils.*;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.jpa.*;
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

	public void createInvoice() throws Exception { // tmp Mostrar alternativas
		Invoice invoice = new Invoice();
		// invoice.setOrders(Collections.singleton(this));
		System.out.println("[Order.createInvoice] describe(this)=" + BeanUtils.describe(this)); // tmp
		BeanUtils.copyProperties(invoice, this);
		invoice.setOid(null);		
		XPersistence.getManager().persist(invoice);
		copyDetailsToInvoice(invoice);
				
	}

	private void copyDetailsToInvoice(Invoice invoice) throws Exception {
		Collection<Detail> invoiceDetails = null;		
		if (this.getDetails() != null) {
			invoiceDetails = new ArrayList<Detail>();
			for (Detail orderDetail: new ArrayList<Detail>(getDetails())) {
				Detail invoiceDetail = (Detail) BeanUtils.cloneBean(orderDetail);
				invoiceDetail.setOid(null);
				invoiceDetail.setParent(invoice);
				//XPersistence.getManager().persist(invoiceDetail);
				invoiceDetails.add(invoiceDetail);
			}
		}
		invoice.setDetails(invoiceDetails);		
	}
		
}
