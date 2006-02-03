package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class TransportCharges2Test extends ModuleTestBase {
			
	public TransportCharges2Test(String testName) {
		super(testName, "OpenXavaTest", "TransportCharges2");		
	}
	
	public void testValidValueInSecondLevelInList() throws Exception {
		assertListRowCount(2);
	}
	
}
