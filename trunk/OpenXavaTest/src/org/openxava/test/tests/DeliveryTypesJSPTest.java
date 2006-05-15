package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class DeliveryTypesJSPTest extends ModuleTestBase {
	
	public DeliveryTypesJSPTest(String testName) {
		super(testName, "OpenXavaTest", "DeliveryTypesJSP");		
	}
	
	public void testHandmadeWebView() throws Exception {
		String number = getValueInList(0, "number");
		String description = getValueInList(0, "description");

		
		execute("Mode.detailAndFirst");
		assertExists("number");
		assertExists("description");
		assertNotExists("comboDeliveries");
		// Asserting that values are displayed
		assertValue("number", number);
		assertValue("description", description);
				
		// It does not lose the web-view changing mode
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertExists("number");
		assertExists("description");
		assertNotExists("comboDeliveries");		
		
		// Asserting the values are accepted
		execute("CRUD.new");
		assertValue("number", "");
		assertValue("description", "");
		assertEditable("number");
		assertEditable("description");		
		setValue("number", "66");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("number", "");
		assertValue("description", "");
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("description", "JUNIT CREATED");
		assertNoEditable("number");
		assertEditable("description");
				
		execute("CRUD.delete");
		assertMessage("Delivery type deleted successfully");
	}
			
}
