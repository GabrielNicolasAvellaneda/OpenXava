package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;

/**
 * @author Javier Paniza
 */
public class DetailPostremoveCalculator implements IEntityCalculator {
	
	private IInvoice invoice;

	public Object calculate() throws Exception {		
		invoice.setComment(invoice.getComment() + "DETAIL DELETED");
		return null;
	}

	public void setEntity(Object entity) throws RemoteException {
		this.invoice = (IInvoice) entity;
	}

}
