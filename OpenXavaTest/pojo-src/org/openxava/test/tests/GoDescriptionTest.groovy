package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class GoDescriptionTest extends ModuleTestBase {
	
	GoDescriptionTest(String testName) {
		super(testName, "GoDescription")		
	}
	
	void testForwardActionOnEachRequestOnLoadModuleFirstTime() {
		assertTrue (getHtml().contains("is used to test all OpenXava features"))
	}
	
}
