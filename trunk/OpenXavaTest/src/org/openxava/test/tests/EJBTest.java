package org.openxava.test.tests;

import java.math.*;
import java.util.*;

import javax.rmi.*;

import org.openxava.test.ejb.*;

import junit.framework.*;

/**
 * @author Javier Paniza
 */

public class EJBTest extends TestCase {

	public EJBTest(String name) {
		super(name);
	}
	
	public void testAggregatesInValues() throws Exception {
		CustomerValue v = new CustomerValue();
		v.getAddress(); // Only for test no compile error
	}
	
	public void testImplementInterfaces() throws Exception {
		assertTrue("Customer should implement IWithName", IWithName.class.isAssignableFrom(Customer.class));
		assertTrue("Address should implemenr IWithState", IWithCity.class.isAssignableFrom(Address.class));
	}
	
	public void testValueObjectWithCalculatedProperties() throws Exception {
		InvoiceKey key = new InvoiceKey();		
		key.year = 2002;
		key.number = 1;		
		Invoice invoice = InvoiceUtil.getHome().findByPrimaryKey(key);
		InvoiceValue v = invoice.getInvoiceValue();
		assertEquals("amountsSum", new BigDecimal("2500.00"), v.getAmountsSum());
		assertEquals("vat", new BigDecimal("400.00"), v.getVat());
		assertEquals("detailsCount", 2, v.getDetailsCount());
		assertEquals("importance", "Normal", v.getImportance());		
	}
	
	public void testCreateWithValueObject() throws Exception {
		Family2Value value = new Family2Value();
		value.setNumber(66);
		value.setDescription("PROVA JUNIT");
		Family2Util.getHome().create(value);
		Family2 f = Family2Util.getHome().findByPrimaryKey(new Family2Key(66));
		assertEquals("number", 66, f.getNumber());
		assertEquals("description", "PROVA JUNIT", f.getDescription());
		Family2Value obtainedValue = f.getFamily2Value();
		assertEquals("number", 66, obtainedValue.getNumber());
		assertEquals("description", "PROVA JUNIT", obtainedValue.getDescription());
		f.remove();  
	}
	
	public void testKeyReferences() throws Exception {
		DeliveryValue value = new DeliveryValue();
		value.setInvoice_year(2099);
		value.setInvoice_number(99);
		value.setNumber(66);
		value.setDescription("JUNIT EJB");
		DeliveryUtil.getHome().create(value);
		DeliveryKey key = new DeliveryKey();
		key.setInvoice_year(2099);
		key.setInvoice_number(99);
		key.setNumber(66);
		Delivery delivery = DeliveryUtil.getHome().findByPrimaryKey(key);
		InvoiceKey invoiceKey = delivery.getInvoiceKey();
		assertEquals("invoice_year", 2099, invoiceKey.getYear());
		assertEquals("invoice_number", 99, invoiceKey.getNumber());
		assertEquals("number", 66, delivery.getNumber());
		assertEquals("description", "JUNIT EJB", delivery.getDescription());
		DeliveryValue deliveryValue = delivery.getDeliveryValue();
		assertEquals("value.invoice_year", 2099, deliveryValue.getInvoice_year());
		assertEquals("value.invoice_number", 99, deliveryValue.getInvoice_number());
		assertEquals("value.number", 66, deliveryValue.getNumber());
		assertEquals("value.description", "JUNIT EJB", deliveryValue.getDescription());		
		delivery.remove(); 
	}
	
	public void testXavaMethods() throws Exception {
		ProductValue value = new ProductValue();
		value.setNumber(66);
		value.setFamilyNumber(1);
		value.setSubfamilyNumber(1);
		value.setDescription("DESCRIPTION JUNIT");
		value.setUnitPrice(new BigDecimal("100.00"));
		Product product = ProductUtil.getHome().create(value);
		try {		
			assertEquals(new BigDecimal("100.00"), product.getUnitPrice());
			product.increasePrice();
			assertEquals(new BigDecimal("102.00"), product.getUnitPrice());

			assertEquals(new BigDecimal("102.00"), product.getPrice("España", new BigDecimal("0.00")));			 			
			assertEquals(new BigDecimal("103.00"), product.getPrice("Guatemala", new BigDecimal("1.00")));
			try {
				product.getPrice("El Puig", new BigDecimal("2.00"));
				fail("It should fail: 'El Puig' is not recognized as country");
			}
			catch (PriceException ex) {
			}		
		}
		finally {
			product.remove();	
		}		  		
	}
	
	public void testConvertersByDefault() throws Exception {
		Iterator it = InvoiceUtil.getHome().findAll().iterator();
		Invoice invoice = null;
		while (it.hasNext()) {
			invoice  = (Invoice) PortableRemoteObject.narrow(it.next(), Invoice.class);
			if (invoice.getDetailsCount() > 0) break;
			invoice = null;
		}
		assertNotNull("At least ones invoice is required for run this test", invoice);
		invoice.setComment(" INVOICE WITH SPACES ");
		InvoiceDetail detail = (InvoiceDetail) PortableRemoteObject.narrow(
				invoice.getDetails().iterator().next(), InvoiceDetail.class);
		detail.setRemarks(" DETAIL WITH SPACES ");
		
		assertEquals("INVOICE WITH SPACES", invoice.getComment());
		assertEquals("DETAIL WITH SPACES", detail.getRemarks());
	}
	
	public void testConvertersAllPropertiesOnCreate() throws Exception { // On way to avoid nulls
		// Subfamily use default converter for remarks
		Map v = new HashMap();
		v.put("number", new Integer(77));
		v.put("description", "PROVA JUNIT 77");
		Subfamily f = SubfamilyUtil.getHome().create(v);
		// Subfamily use converter NoConversion for remarks
		Subfamily2 f2 = Subfamily2Util.getHome().findByPrimaryKey(new Subfamily2Key(77));
		assertEquals("PROVA JUNIT 77", f2.getDescription());
		// Since it does not have converter we read what there is in the data base,
		// that it has to is empty string, not null. Because on create from subfamily
		// the converte is applied		
		assertEquals("", f2.getRemarks()); 
		
		f.remove();
	}

}
