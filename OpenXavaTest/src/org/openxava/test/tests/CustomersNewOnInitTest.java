package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersNewOnInitTest extends ModuleTestBase {

	public CustomersNewOnInitTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersNewOnInit");		
	}
	
	public void testNewOnInit() throws Exception {
		assertNoErrors();
		assertAction("Mode.list");
		assertNoAction("Mode.detailAndFirst");
	}
				
}
