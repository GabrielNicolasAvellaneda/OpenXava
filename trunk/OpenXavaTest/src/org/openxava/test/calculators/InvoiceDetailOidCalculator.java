package org.openxava.test.calculators;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;
import org.openxava.test.ejb.*;


/**
 * @author Javier Paniza
 */
public class InvoiceDetailOidCalculator implements IAggregateOidCalculator {
	
	private InvoiceKey invoiceKey;
	private int counter;

	public void setContainerKey(Object containerKey) {
		invoiceKey = (InvoiceKey) containerKey;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public Object calculate() throws Exception {
		return invoiceKey.year + ":" + invoiceKey.number + ":" + counter;
	}

}
