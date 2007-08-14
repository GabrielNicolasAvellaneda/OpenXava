package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class Invoice2Test extends ModuleTestBase {
	
	public Invoice2Test(String testName) {
		super(testName, "Invoice2");		
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
							
}
