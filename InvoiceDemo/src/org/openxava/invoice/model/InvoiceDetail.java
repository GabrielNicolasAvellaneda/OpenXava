package org.openxava.invoice.model;

import java.math.*;
import javax.persistence.*;
import org.openxava.annotations.*;

@Embeddable
public class InvoiceDetail {
		
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Product product;
		
	@Required
	private int quantity;
	
	@Depends("product.unitPrice, quantity")
	public BigDecimal getAmount() {
		return new BigDecimal(getQuantity()).multiply(getProduct().getUnitPrice());
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
