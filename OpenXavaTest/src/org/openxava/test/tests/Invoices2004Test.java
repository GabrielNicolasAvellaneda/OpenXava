package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Invoices2004Test extends ModuleTestBase {
	
	public Invoices2004Test(String testName) {
		super(testName, "OpenXavaTest", "Invoices2004");		
	}
	
		
	public void testFilterUsingEnvironmentVariable() throws Exception {
		int cantidad = getListRowCount();
		for (int i = 0; i < cantidad; i++) {
			assertValueInList(i, "year", "2004");	
		}		
	}
	
}
