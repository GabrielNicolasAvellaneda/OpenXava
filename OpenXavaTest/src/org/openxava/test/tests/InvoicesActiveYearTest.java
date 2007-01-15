package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class InvoicesActiveYearTest extends ModuleTestBase {
	
	public InvoicesActiveYearTest(String testName) {
		super(testName, "OpenXavaTest", "InvoicesActiveYear");		
	}
	
		
	public void testApplicationScopeSessionObject() throws Exception {
		int count = getListRowCount();
		for (int i = 0; i < count; i++) {
			assertValueInList(i, "year", "2004");	
		}		
		
		changeModule("ChangeActiveYear");
		setValue("year", "2002");
		execute("ChangeActiveYear.changeActiveYear");
		assertMessage("Active year set to 2,002");
		
		changeModule("InvoicesActiveYear");		
		count = getListRowCount();
		for (int i = 0; i < count; i++) {
			assertValueInList(i, "year", "2002");	
		}				
	}
	
}
