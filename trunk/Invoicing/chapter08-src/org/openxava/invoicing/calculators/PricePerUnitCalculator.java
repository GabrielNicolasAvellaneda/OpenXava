package org.openxava.invoicing.calculators;

import org.openxava.calculators.*;
import org.openxava.invoicing.model.*;

import static org.openxava.jpa.XPersistence.*;

public class PricePerUnitCalculator implements ICalculator {
	
	private int productNumber;

	public Object calculate() throws Exception {
		Product product = getManager().find(Product.class, productNumber);
		return product.getPrice();		
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public int getProductNumber() {
		return productNumber;
	}

}
