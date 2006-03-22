package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class TransportChargesWithDistanceTest extends ModuleTestBase {
			
	public TransportChargesWithDistanceTest(String testName) {
		super(testName, "OpenXavaTest", "TransportChargesWithDistance");		
	}
	
	public void testValidValueInSecondLevelInList() throws Exception {
		assertListRowCount(2);
	}
	
}
