package org.openxava.invoicing.tests;

import java.math.*;

import javax.persistence.*;

import org.openxava.invoicing.model.*;
import org.openxava.jpa.*;

public class InvoiceTest extends CommercialDocumentTest {
	
	public InvoiceTest(String testName) { 
		super(testName, "Invoice"); 				
	}
	
	public void testAddOrders() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("orders", 0);
		execute("Collection.add", 
			"viewObject=xava_view_section1_orders");
		execute("AddToCollection.add", "row=0");
		assertCollectionRowCount("orders", 1);
		checkRowCollection("orders", 0);
		execute("Collection.removeSelected", 
			"viewObject=xava_view_section1_orders");
		assertCollectionRowCount("orders", 0);
	}
	
	public void testQueryFormula() throws Exception { // tmp
		long ini = System.currentTimeMillis();
		Query query = XPersistence.
			getManager().
			createQuery("from Invoice i where " +
				"i.estimatedProfit > :estimatedProfit");
		query.setParameter("estimatedProfit", new BigDecimal(1000));
		for (Object o:  query.getResultList()) {
			Invoice i = (Invoice) o;
			i.doSomething();
		}
		long cuesta = System.currentTimeMillis() - ini;
		System.out.println("[InvoiceTest.testQuery] cuesta=" + cuesta); // tmp			
	}
	
	public void testHola() {
		System.out.println("[InvoiceTest.testQueryCalculated] HOLA"); // tmp
	}
	
	public void testQueryCalculated() throws Exception { // tmp
		long ini = System.currentTimeMillis();
		Query query = XPersistence
			.getManager()
			.createQuery("from Invoice");
		for (Object o:  query.getResultList()) {
			Invoice i = (Invoice) o;
			if (i.getEstimatedProfit().compareTo(new BigDecimal("1000")) > 0) {
				i.doSomething();
			}
		}
		long cuesta = System.currentTimeMillis() - ini;
		System.out.println("[InvoiceTest.testQuery] cuesta=" + cuesta); // tmp			
	}
	
			
}
