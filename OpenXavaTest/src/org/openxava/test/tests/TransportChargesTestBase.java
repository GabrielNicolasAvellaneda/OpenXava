package org.openxava.test.tests;

import java.math.*;
import java.util.*;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class TransportChargesTestBase extends ModuleTestBase {
	
	private TransportCharge charge1;
	private TransportCharge charge2;
		
	public TransportChargesTestBase(String testName, String moduleName) {
		super(testName, "OpenXavaTest", moduleName);		
	}
	
	
	protected void deleteAll() throws Exception {
		XHibernate.getSession().createQuery("delete from TransportCharge").executeUpdate();
		XHibernate.commit(); 
		
	}	
	
	protected void createSome() throws Exception {
		Collection deliveries = XHibernate.getSession().createQuery("select d from Delivery as d").list();	
		assertTrue("At least 2 deliveries is required to run this test", deliveries.size() > 1);
		Iterator it = deliveries.iterator();
		
		Delivery delivery1 = (Delivery) it.next();		
		charge1 = new TransportCharge();
		
		charge1.setDelivery(delivery1);
		charge1.setAmount(new BigDecimal("100.00"));
		XHibernate.getSession().save(charge1);
						
		Delivery delivery2 = (Delivery) it.next();		
		charge2 = new TransportCharge();
		charge2.setDelivery(delivery2);
		charge2.setAmount(new BigDecimal("200.00"));			
		XHibernate.getSession().save(charge2);
		XHibernate.getSession().flush();
	}
	
	protected TransportCharge getCharge1() {
		return charge1;
	}

	protected TransportCharge getCharge2() {
		return charge2;
	}

}
