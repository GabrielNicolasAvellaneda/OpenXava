package org.openxava.test.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza 
 */

@Embeddable
public class QuoteDetail {
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private Product product;
	
	private BigDecimal unitPrice;
	 	
	private Date availabilityDate;
		
	@Column(length=30)
	private String remarks;
	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Date getAvailabilityDate() {
		return availabilityDate;
	}

	public void setAvailabilityDate(Date availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
