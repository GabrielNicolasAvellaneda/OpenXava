package org.openxava.test.tests;

import java.math.*;
import java.util.*;

import javax.rmi.*;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.util.*;
import org.openxava.test.ejb.*;


/**
 * @author Javier Paniza
 */

public class TransportChargesTest extends ModuleTestBase {
	
	private TransportChargeValue charge1;
	private TransportChargeValue charge2;
		
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
		execute("ConfirmDelete.confirmDelete");											
		assertMessage("TransportCharge deleted successfully");
	}
	
	public void testKeyNestedReferencesInList() throws Exception {
		deleteAll();
		createSome();
		execute("List.filter"); // For refresh
		execute("List.orderBy", "property=amount");
		
		assertListRowCount(2);
		
		assertValueInList(0, 0, String.valueOf(getCharge1().getDelivery_invoice_year()));
		assertValueInList(0, 1, String.valueOf(getCharge1().getDelivery_invoice_number()));
		assertValueInList(0, 2, String.valueOf(getCharge1().getDelivery_number()));
		assertValueInList(0, 3, "100");

		assertValueInList(1, 0, String.valueOf(getCharge2().getDelivery_invoice_year()));
		assertValueInList(1, 1, String.valueOf(getCharge2().getDelivery_invoice_number()));
		assertValueInList(1, 2, String.valueOf(getCharge2().getDelivery_number()));
		assertValueInList(1, 3, "200");

		String [] condition = {
				String.valueOf(getCharge2().getDelivery_invoice_year()),
				String.valueOf(getCharge2().getDelivery_invoice_number()),
				String.valueOf(getCharge2().getDelivery_number())
		};		
		setConditionValues(condition);
		execute("List.filter");
		
		assertListRowCount(1);
		
		assertValueInList(0, 0, String.valueOf(getCharge2().getDelivery_invoice_year()));
		assertValueInList(0, 1, String.valueOf(getCharge2().getDelivery_invoice_number()));
		assertValueInList(0, 2, String.valueOf(getCharge2().getDelivery_number()));
		assertValueInList(0, 3, "200");		
	}

	private void deleteAll() throws Exception {
		Iterator it = TransportChargeUtil.getHome().findAll().iterator();
		while (it.hasNext()) {
			TransportCharge charge = (TransportCharge) PortableRemoteObject.narrow(it.next(), TransportCharge.class);
			charge.remove();
		}		
	}	
	
	private void createSome() throws Exception {
		Collection deliveries = DeliveryUtil.getHome().findAll();
		assertTrue("At least 2 deliveries is required to run this test", deliveries.size() > 1);
		Iterator it = deliveries.iterator();
		
		DeliveryValue delivery1 = ((Delivery) PortableRemoteObject.narrow(it.next(), Delivery.class)).getDeliveryValue();		
		charge1 = new TransportChargeValue();
		
		charge1.setDelivery_invoice_year(delivery1.getInvoice_year());		
		charge1.setDelivery_invoice_number(delivery1.getInvoice_number());		
		charge1.setDelivery_number(delivery1.getNumber());		
		charge1.setAmount(new BigDecimal("100.00"));					
		TransportChargeUtil.getHome().create(charge1);
		
		DeliveryValue delivery2 = ((Delivery) PortableRemoteObject.narrow(it.next(), Delivery.class)).getDeliveryValue();		
		charge2 = new TransportChargeValue();
		charge2.setDelivery_invoice_year(delivery2.getInvoice_year());
		charge2.setDelivery_invoice_number(delivery2.getInvoice_number());
		charge2.setDelivery_number(delivery2.getNumber());
		charge2.setAmount(new BigDecimal("200.00"));			
		TransportChargeUtil.getHome().create(charge2);		
	}
	
	private TransportChargeValue getCharge1() {
		return charge1;
	}

	private TransportChargeValue getCharge2() {
		return charge2;
	}

}
