package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersSimpleTest extends ModuleTestBase {
	
	
	public CustomersSimpleTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersSimple");		
	}
	
	public void testSearchReferenceInsideAggregate() throws Exception {
		execute("CRUD.new");
		execute("Reference.search", "keyProperty=xava.Customer.address.state.id");
		assertNoErrors();
	}
		
}
