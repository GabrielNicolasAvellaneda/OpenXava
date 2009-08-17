package org.openxava.test.calculators;

import java.math.*;
import java.rmi.*;
import java.util.*;

import org.openxava.calculators.*;
import org.openxava.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class OrderAmountCalculator implements ICalculator {
	
	private int quantity; // tmp falta getter and setters
	private int productNumber;

	public Object calculate() throws Exception {
		return new BigDecimal(quantity).multiply(getProduct().getUnitPrice());
	}
	
	public IProduct getProduct() throws RemoteException {
		// Using MapFacade in order to work with both EJB2 CMP and Hibernate
		Map key = new HashMap();
		key.put("number", productNumber);
		return (IProduct) MapFacade.findEntity("Product", key);
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}
	
}
