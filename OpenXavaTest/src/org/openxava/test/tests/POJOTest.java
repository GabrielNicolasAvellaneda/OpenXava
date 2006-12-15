package org.openxava.test.tests;

import java.math.*;
import java.util.*;

import org.openxava.hibernate.*;
import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.util.*;

import junit.framework.*;

/**
 * 
 * @author Javier Paniza
 */

public class POJOTest extends TestCase {
	
	static {
		XHibernate.setConfigurationFile("hibernate-junit.cfg.xml");
		XPersistence.setPersistenceUnit("junit");
		DataSourceConnectionProvider.setUseHibernateConnection(true);
	}
	
	public POJOTest(String name) {
		super(name);
	}
	
	protected void tearDown() throws Exception {
		XHibernate.commit();
		XPersistence.commit();
	}
	
	public void testCalculatedPropertyOnAggregateDependsOnPropertyOfContainerModel() throws Exception {
		Customer c = Customer.findByNumber(1);
		assertEquals("DOCTOR PESSET46540EL PUIGNew York1", c.getAddress().getAsString());
	}
					
	public void testFinderByAggregateProperty() throws Exception {
		Collection customers = Customer.findByStreet("XXX");
		for (Iterator it = customers.iterator(); it.hasNext(); it.next()); // This mustn't fail 
	}
	
	public void testFinderOrderedByReferencePropertyInAnAggregate() throws Exception {
		Collection customers = Customer.findOrderedByState();
		for (Iterator it = customers.iterator(); it.hasNext(); it.next()); // This mustn't fail
	}
	
	
	public void testFinderThrowsObjectNotFound() throws Exception {
		// Finders in POJOs has the semantics of EJB CMP2 
		// in order to help in translation from EJB2 to POJO+Hibernate
		try {
			Customer.findByNumber(66); // 66 doesn't exist
			fail("ObjectNotFoundException expected");
		}
		catch (javax.ejb.ObjectNotFoundException ex) {
			// All fine
		}
	}
		
	public void testOrderBy() throws Exception {
		Collection customers = Customer.findByNameLike("%");		
		String previous = "{}";
		for (Iterator it = customers.iterator(); it.hasNext();) {
			Customer customer = (Customer) it.next();
			String name = customer.getName();			
			if (name.compareToIgnoreCase(previous) > 0) {				
				fail("The names must to be ordered");
			}
			previous = name;
		}
	}
	
	public void testCalculatedPropertyDependOnMultiLevelProperty() throws Exception {
		Invoice invoice = new Invoice();
		invoice.setYear(2005);
		invoice.setNumber(66);				
		assertEquals(null,  invoice.getSellerDiscount());
	}
	
	public void testImplementInterfaces() throws Exception {
		assertTrue("Customer should implement IWithName", IWithName.class.isAssignableFrom(Customer.class));
		assertTrue("Address should implemenr IWithState", IWithCity.class.isAssignableFrom(Address.class));
	}
	
			
	public void testXavaMethods() throws Exception {
		Product product = new Product();
		product.setNumber(66);
		product.setFamilyNumber(1);
		product.setSubfamilyNumber(1);
		product.setDescription("DESCRIPTION JUNIT");
		product.setUnitPrice(new BigDecimal("100.00"));		
		
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
	
	public void testConvertersByDefault() throws Exception {
		Iterator it = Invoice.findAll().iterator();
		Invoice invoice = null;
		while (it.hasNext()) {
			invoice  = (Invoice) it.next();
			if (invoice.getDetailsCount() > 0) break;
			invoice = null;
		}
		assertNotNull("At least one invoice with details is required for run this test", invoice);
		invoice.setComment(" INVOICE WITH SPACES ");
		InvoiceDetail detail = (InvoiceDetail) invoice.getDetails().iterator().next();
		detail.setRemarks(" DETAIL WITH SPACES ");
		
		assertEquals("INVOICE WITH SPACES", invoice.getComment());
		assertEquals("DETAIL WITH SPACES", detail.getRemarks());
	}
		
}
