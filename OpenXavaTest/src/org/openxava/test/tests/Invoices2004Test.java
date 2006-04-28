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
		int count = getListRowCount();
		for (int i = 0; i < count; i++) {
			assertValueInList(i, "year", "2004");	
		}		
		changeModule("Invoices2002Env");
		count = getListRowCount();
		for (int i = 0; i < count; i++) {
			assertValueInList(i, "year", "2002");	
		}				
	}
	
}
