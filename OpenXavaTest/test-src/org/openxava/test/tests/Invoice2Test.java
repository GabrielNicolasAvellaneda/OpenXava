package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class Invoice2Test extends ModuleTestBase {
	
	public Invoice2Test(String testName) {
		super(testName, "Invoice2");		
	}
	
	public void testTouchContainerFromCallback() throws Exception {
		if (!isOX3()) return; // This case is only implemented in JPA
		execute("CRUD.new");
		setValue("number", "66");
		setValue("vatPercentage", "16");
		setValue("customer.number", "1");
		assertCollectionRowCount("details", 0);
		
		// Creating a new detail
		execute("Collection.new", "viewObject=xava_view_details");
		setValue("details.quantity", "7");
		setValue("details.unitPrice", "8");
		assertValue("details.amount", "56.00");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "MULTAS DE TRAFICO");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		execute("CRUD.search");
		assertValue("amountsSum", "56.00");
		
		// Creating another one
		execute("Collection.new", "viewObject=xava_view_details");
		setValue("details.quantity", "10");
		setValue("details.unitPrice", "10");
		assertValue("details.amount", "100.00");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "MULTAS DE TRAFICO");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 2);
		execute("CRUD.search");
		assertValue("amountsSum", "156.00");
		
		// Modifiying
		execute("Collection.edit", "row=1,viewObject=xava_view_details");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", "10");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 2);
		execute("CRUD.search");
		assertValue("amountsSum", "256.00");
		
		// Removing
		execute("Collection.edit", "row=1,viewObject=xava_view_details");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", "10");
		execute("Collection.remove", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		execute("CRUD.search");
		assertValue("amountsSum", "56.00");
		
		execute("CRUD.delete");
		assertNoErrors();		
	}
	
	public void testInjectPropertiesOfContainerInOnCreateCalculatorOfAggregate() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		setValue("vatPercentage", "16");
		setValue("customer.number", "1");
		assertCollectionRowCount("details", 0);
		execute("Collection.new", "viewObject=xava_view_details");
		setValue("details.quantity", "7");
		setValue("details.unitPrice", "8");
		assertValue("details.amount", "56.00");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "MULTAS DE TRAFICO");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		
		execute("CRUD.delete");
		assertNoErrors();
	}
	
	public void testCollectionOrderedByAPropertyOfAReference_valueOfNestedRerenceInsideAnEmbeddedCollection() throws Exception {
		execute("CRUD.new");
		setValue("year", "2002");
		setValue("number", "1");
		execute("CRUD.search");
		assertCollectionRowCount("details", 2);
		assertValueInCollection("details", 0, "product.description", "XAVA");
		assertValueInCollection("details", 1, "product.description", "IBM ESERVER ISERIES 270");
		
		execute("Collection.edit", "row=0,viewObject=xava_view_details");
		assertValue("details.product.description", "XAVA");
		assertValue("details.product.family.description", "SOFTWARE");
		
		execute("Collection.edit", "row=1,viewObject=xava_view_details");
		assertValue("details.product.description", "IBM ESERVER ISERIES 270");
		assertValue("details.product.family.description", "HARDWARE");		
	}
							
}
