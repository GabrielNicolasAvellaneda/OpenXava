package org.openxava.invoicing.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
@View(members="product, quantity")
public class Detail extends Identifiable {
	
	@ManyToOne // Lazy fetching fails on removing a detail from invoice
	private Invoice invoice;
		
	private int quantity;
	
	@ReferenceView("Simple")  
	@NoFrame  	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	private Product product;
	
	// Getters and setters
	
	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
