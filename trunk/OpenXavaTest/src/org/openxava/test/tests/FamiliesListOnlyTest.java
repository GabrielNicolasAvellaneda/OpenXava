package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamiliesListOnlyTest extends ModuleTestBase {
	
	
	public FamiliesListOnlyTest(String testName) {
		super(testName, "FamiliesListOnly");		
	}	
	
	public void testWithoutXavaListAction() throws Exception {
		assertNoAction("Mode.detailAndFirst");
		assertNoAction("Mode.list");
		assertNoAction("List.viewDetail");
	}
					
}
