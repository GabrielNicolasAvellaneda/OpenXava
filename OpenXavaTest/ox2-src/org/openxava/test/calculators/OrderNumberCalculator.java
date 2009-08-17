package org.openxava.test.calculators;

import java.math.*;
import java.rmi.*;
import java.util.*;

import org.openxava.calculators.*;

/**
 * 
 * @author Javier Paniza
 */

public class OrderAmountCalculator implements IModelCalculator {
	
	private IOrder order;

	public Object calculate() throws Exception {
		Query query = XHibernate.getSession()
			.createQuery("select max(o.number) from Order o " + 
				"where o.year = :year");
		query.setParameter("year", order.getYear());		
		Integer lastNumber = (Integer) query.getSingleResult();
		order.setNumber(lastNumber == null?1:lastNumber + 1);
		return null;
	}
	
	public void setModel(Object model) throws RemoteException {
		this.order = (IOrder) model;
	}

}
