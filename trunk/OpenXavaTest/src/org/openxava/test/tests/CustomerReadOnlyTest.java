package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomerReadOnlyTest extends ModuleTestBase {
	
	public CustomerReadOnlyTest(String testName) {
		super(testName, "CustomerReadOnly");				
	}
	
	public void testSearhReadOnlyAction() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoEditable("number");
		assertNoEditable("name");
		execute("Sections.change", "activeSection=1");
		assertNoAction("Collection.new");
	}

	
}
