package org.openxava.test.validators;

import java.math.*;

import org.openxava.test.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class InvoiceDetailValidator implements IValidator {
	
	private IInvoice invoice;
	private String oid; // At moment oid is only for verify that can be set without crash
	private IProduct product;	
	private BigDecimal unitPrice;

	public void validate(Messages errors) throws Exception {		
		if (invoice.isPaid()) {
			errors.add("not_invoice_detail_paid");
			return; // It's possible continue and acumulate more errors
		}		
		if (product == null) {
			errors.add("product_required"); // It is not necessary because product is required='true'
			return; 
		}
		if (getProduct().getUnitPrice().compareTo(getUnitPrice()) < 0) {
			errors.add("invoice_price_less_or_equal_to_product");			
		}		
	}
	
	public IProduct getProduct() {
		return product;
	}
	public void setProduct(IProduct object) {
		product = object;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal decimal) {
		unitPrice = decimal;
	}

	public String getOid() {
		return oid;
	}
	public void setOid(String string) {
		oid = string;		
	}

	public IInvoice getInvoice() {
		return invoice;
	}
	public void setInvoice(IInvoice invoice) {
		this.invoice = invoice;
	}
	
}
