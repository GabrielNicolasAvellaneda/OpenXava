package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class DeliveryTypesJSPTest extends ModuleTestBase {
	
	public DeliveryTypesJSPTest(String testName) {
		super(testName, "DeliveryTypesJSP");		
	}
	
	public void testHandmadeWebViewNotLost() throws Exception {
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
	}
			
}
