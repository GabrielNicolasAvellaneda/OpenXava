package org.openxava.test.tests;

import java.math.*;
import java.util.*;

import javax.rmi.*;

import org.hibernate.*;
import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class TransportChargesTest extends ModuleTestBase {
	
	private TransportCharge charge1;
	private TransportCharge charge2;
		
	public TransportChargesTest(String testName) {
		super(testName, "OpenXavaTest", "TransportCharges");		
	}
	
	public void testKeyNestedReferences() throws Exception {
		deleteAll();
		
		execute("CRUD.new");
		execute("Reference.search", "keyProperty=xava.TransportCharge.delivery.number");
		String year = getValueInList(0, 0);
		String number = getValueInList(0, 1);
		assertTrue("It is required that year in invoice has value", !Is.emptyString(year));
		assertTrue("It is required that number in invoice has value", !Is.emptyString(number));
		
		execute("ReferenceSearch.choose", "row=0");
		assertNoErrors();
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		
		setValue("amount", "666");
		execute("CRUD.save");
		assertNoErrors();
				
		assertValue("delivery.invoice.year", "");
		assertValue("delivery.invoice.number", "");
		assertValue("amount", "");
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		assertValue("amount", "666");
		
		setValue("amount", "777");
		execute("CRUD.save");
		assertNoErrors();

		assertValue("delivery.invoice.year", "");
		assertValue("delivery.invoice.number", "");
		assertValue("amount", "");
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		assertValue("amount", "777");
						
		execute("CRUD.delete");										
		assertMessage("TransportCharge deleted successfully");
	}
	
	public void testKeyNestedReferencesInList() throws Exception {
		deleteAll();
		createSome();
		execute("List.filter"); // For refresh
		execute("List.orderBy", "property=amount");
		
		assertListRowCount(2);
		
		assertValueInList(0, 0, String.valueOf(getCharge1().getDelivery().getInvoice().getYear()));
		assertValueInList(0, 1, String.valueOf(getCharge1().getDelivery().getInvoice().getNumber()));
		assertValueInList(0, 2, String.valueOf(getCharge1().getDelivery().getNumber()));
		assertValueInList(0, 3, "100");

		assertValueInList(1, 0, String.valueOf(getCharge2().getDelivery().getInvoice().getYear()));
		assertValueInList(1, 1, String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()));
		assertValueInList(1, 2, String.valueOf(getCharge2().getDelivery().getNumber()));
		assertValueInList(1, 3, "200");

		String [] condition = {
				String.valueOf(getCharge2().getDelivery().getInvoice().getYear()),
				String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()),
				String.valueOf(getCharge2().getDelivery().getNumber())
		};		
		setConditionValues(condition);
		execute("List.filter");
		
		assertListRowCount(1);
		
		assertValueInList(0, 0, String.valueOf(getCharge2().getDelivery().getInvoice().getYear()));
		assertValueInList(0, 1, String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()));
		assertValueInList(0, 2, String.valueOf(getCharge2().getDelivery().getNumber()));
		assertValueInList(0, 3, "200");		
	}

	private void deleteAll() throws Exception {
		XHibernate.getSession().createQuery("delete from TransportCharge").executeUpdate();
		XHibernate.commit(); 
		
	}	
	
	private void createSome() throws Exception {
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
	
	private TransportCharge getCharge1() {
		return charge1;
	}

	private TransportCharge getCharge2() {
		return charge2;
	}

}
