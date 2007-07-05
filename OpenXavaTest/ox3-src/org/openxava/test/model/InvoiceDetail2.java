package org.openxava.test.model;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@Table(name="INVOICEDETAIL")
public class InvoiceDetail2 {
	
	@ManyToOne(fetch=FetchType.LAZY)	 
	@JoinColumns({
		@JoinColumn(name="INVOICE_YEAR", referencedColumnName="YEAR"),
		@JoinColumn(name="INVOICE_NUMBER", referencedColumnName="NUMBER")
	})
	private Invoice2 invoice2;
	
	// It's calculAted in the method calculateOID
	@Id @Hidden 
	private String oid;
	
	@Column(length=4) @Required
	private int quantity;
	
	@Stereotype("MONEY") @Required
	private BigDecimal unitPrice;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@ReferenceView(name="Simple")
	private Product product;
	
	@Stereotype("MONEY") @Depends(properties="unitPrice, quantity")
	public BigDecimal getAmount() {
		return getUnitPrice().multiply(new BigDecimal(getQuantity()));
	}
	
	@PrePersist
	private void calculateOID() {
		// Thus we can calculate an oid in a custom way

		// In the XML version (OX2) we use InvoiceDetail2 for testing injection
		// in a calculator for the oid of an aggregate from the container entity.
		// But, because OX3 does not have default-value-calculator on-create="true"
		// the implementation of InvoiceDetail and InvoiceDetail2 is the same.
		oid = invoice2.getYear() + ":" + invoice2.getNumber() + ":" + System.currentTimeMillis();
	}


	public String getOid() {
		return oid;
	}


	public void setOid(String oid) {
		this.oid = oid;
	}


	public Invoice2 getInvoice2() {
		return invoice2;
	}


	public void setInvoice2(Invoice2 invoice2) {
		this.invoice2 = invoice2;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public BigDecimal getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}



	public Product getProduct() {
		return product;
	}



	public void setProduct(Product product) {
		this.product = product;
	}


}
