package org.openxava.test.calculators;

import java.math.*;
import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;

/**
 * @author Javier Paniza
 */
public class IncreasePriceCalculator implements IEntityCalculator {
	
	private IProduct product;

	public Object calculate() throws Exception {
		product.setUnitPrice(product.getUnitPrice().multiply(new BigDecimal("1.02")).setScale(2));
		return null;				
	}

	public void setEntity(Object entity) throws RemoteException {
		this.product = (IProduct) entity;
	}

}
