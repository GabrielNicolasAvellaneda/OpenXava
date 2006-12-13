package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class DescriptionTest extends ModuleTestBase {
	
	
	public DescriptionTest(String testName) {
		super(testName, "OpenXavaTest", "Description");		
	}
	
	public void testDocModule() throws Exception {		
		assertTrue(getHtml().indexOf("application is used to test all OpenXava features")>=0);
	}
	
}
