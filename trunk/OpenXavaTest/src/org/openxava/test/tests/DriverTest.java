package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class DriverTest extends ModuleTestBase {
	
	public DriverTest(String testName) {
		super(testName, "Driver");		
	}
		
	public void testSearchFromOverlappedReferenceWithStringKey() throws Exception {
		execute("CRUD.new");
		setValue("type", "C ");
		execute("Reference.search", "keyProperty=xava.Driver.drivingLicence.level");
		assertNoErrors();
		assertListRowCount(2);		
		assertValueInList(0, 0, "C");
		assertValueInList(1, 0, "C");
	}
			
}
