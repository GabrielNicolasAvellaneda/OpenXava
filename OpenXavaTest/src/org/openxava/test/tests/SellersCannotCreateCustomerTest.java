package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellersCannotCreateCustomerTest extends ModuleTestBase {
	

	public SellersCannotCreateCustomerTest(String testName) {
		super(testName, "OpenXavaTest", "SellersCannotCreateCustomer");		
	}

	public void testNotCreateNewReferenceFromCollection() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_customers");
		assertNoAction("Reference.createNew");
	}
			
}
