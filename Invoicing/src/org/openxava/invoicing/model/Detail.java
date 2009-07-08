package org.openxava.invoicing.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
@View(members="product, quantity")
public class Detail extends Identifiable {
	
	@ManyToOne // Lazy fetching fails on removing a detail from parent
	private Invoice parent;
		
	private int quantity;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@ReferenceView("Simple")
	@NoFrame
	private Product product;
	
	// Getters and setters
	
	public Invoice getParent() {
		return parent;
	}

	public void setParent(Invoice parent) {
		this.parent = parent;
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
