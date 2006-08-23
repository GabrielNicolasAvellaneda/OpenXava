package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersReadOnlyTest extends ModuleTestBase {
	
	public CustomersReadOnlyTest(String testName) {
		super(testName, "CustomersReadOnly");				
	}
	
	public void testSearhReadOnlyAction() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoEditable("number");
		assertNoEditable("name");
		execute("Sections.change", "activeSection=1");
		assertNoAction("Collection.new");
	}

	
}
