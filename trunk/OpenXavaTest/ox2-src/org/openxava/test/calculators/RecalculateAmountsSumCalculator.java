package org.openxava.test.calculators;

import java.math.*;
import java.rmi.*;
import java.util.*;

import org.openxava.calculators.*;
import org.openxava.test.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class RecalculateAmountsSumCalculator implements IModelCalculator {

	private IInvoiceDetail2 detail;
	
	public void setModel(Object model) throws RemoteException {
		detail = (IInvoiceDetail2) model;		
	}

	public Object calculate() throws Exception {
		IInvoice2 invoice = detail.getInvoice2();
		BigDecimal result = new BigDecimal("0.00");
		
		for (Iterator it = invoice.getDetails().iterator(); it.hasNext(); ) {
			IInvoiceDetail2 detail = (IInvoiceDetail2) it.next();
			result = result.add(detail.getAmount());
		}
		invoice.setAmountsSum(result);
		return null;
	}

}
