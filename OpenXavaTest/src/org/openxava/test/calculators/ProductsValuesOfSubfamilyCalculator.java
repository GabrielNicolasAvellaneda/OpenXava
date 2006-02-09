package org.openxava.test.calculators;

import java.util.*;

import javax.rmi.*;

import org.hibernate.*;
import org.openxava.calculators.ICalculator;
import org.openxava.hibernate.*;
import org.openxava.test.model.*;

public class ProductsValuesOfSubfamilyCalculator implements ICalculator {

	private int subfamilyNumber;

	public Object calculate() throws Exception {
		// Hiberante version, returns POJOs.
		Query query = XHibernate.getSession().createQuery("from Product2 where subfamily.number = :subfamilyNumber");
		query.setInteger("subfamilyNumber", getSubfamilyNumber());
		return query.list();
		
		/* 
		// EJB version returns values (not remote objects)
		Collection result = new ArrayList();
		Collection products = Product2Util.getHome().findBySubfamily(getSubfamilyNumber());
		for (Iterator it = products.iterator(); it.hasNext();) {
			Product2Remote product = (Product2Remote) PortableRemoteObject.narrow(it.next(), Product2Remote.class);
			result.add(product.getProduct2Value());
		}
		return result;
		*/
	}

	public int getSubfamilyNumber() {
		return subfamilyNumber;
	}

	public void setSubfamilyNumber(int subfamilyNumber) {
		this.subfamilyNumber = subfamilyNumber;
	}

}
