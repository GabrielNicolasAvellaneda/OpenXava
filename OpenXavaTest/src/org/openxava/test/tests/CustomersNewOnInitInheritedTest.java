package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersNewOnInitInheritedTest extends ModuleTestBase {

	public CustomersNewOnInitInheritedTest(String testName) {
		super(testName, "CustomersNewOnInitInherited");		
	}
	
	public void testNewOnInit() throws Exception {
		assertNoErrors();
		assertAction("Mode.list");
		assertNoAction("Mode.detailAndFirst");
	}
					
}
