package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class SubfamiliesSelectTest extends ModuleTestBase {
	
	public SubfamiliesSelectTest(String testName) {
		super(testName, "OpenXavaTest", "SubfamiliesSelect");		
	}
	
	public void testTabWithSelect() throws Exception {
		assertNoErrors();
		assertListNotEmpty();
	}
	
}
