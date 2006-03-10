package org.openxava.test.tests;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.hibernate.*;
import org.openxava.hibernate.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.test.model.*;

import junit.framework.*;

/**
 * @author Javier Paniza
 */

public class HibernateTest extends TestCase {
	
	static {
		XHibernate.setConfigurationFile("hibernate-junit.cfg.xml");
	}
	
	public HibernateTest(String name) {
		super(name);
	}
	
	protected void tearDown() throws Exception {
		XHibernate.commit();
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
	
	public void testConvertersAllPropertiesOnCreate() throws Exception { // One way to avoid nulls	
		Subfamily sf = new Subfamily();
		sf.setNumber(77);
		sf.setDescription("PROVA JUNIT 77");
		XHibernate.getSession().save(sf);
		XHibernate.getSession().flush();
		
		Query query = XHibernate.getSession().createQuery(
				"select sf.remarks from Subfamily sf where sf.number = 77");
		
		String remarks = (String) query.uniqueResult();
				
		assertEquals("", remarks);
		
		XHibernate.getSession().delete(sf);
	}

}
