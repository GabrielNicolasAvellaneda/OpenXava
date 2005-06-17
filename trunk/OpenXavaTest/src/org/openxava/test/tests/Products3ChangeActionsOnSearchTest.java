package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Products3ChangeActionsOnSearchTest extends ModuleTestBase {
	
	public Products3ChangeActionsOnSearchTest(String testName) {
		super(testName, "OpenXavaTest", "Products3ChangeActionsOnSearch");		
	}
	
	public void testChangeActionWhenSearch() throws Exception {
		execute("CRUD.new");
		execute("Products3.showDescription"); // description is hide in a init action for test purpose
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("description", "ANATHEMA");
		assertNoEditable("description"); // well: on-change for make this not editable is thrown
	}
							
}
