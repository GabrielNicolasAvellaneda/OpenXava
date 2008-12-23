package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CompositeTest extends ModuleTestBase {
	
	public CompositeTest(String testName) {
		super(testName, "Composite");		
	}
	
	public void testSelfReferenceCollectionWithCascadeRemove() throws Exception {
		execute("CRUD.new");
		assertNoErrors();
		assertCollectionRowCount("children", 0);
	}		
		
}
