package org.openxava.test.tests;


import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class Offices2Test extends ModuleTestBase {
			

	public Offices2Test(String testName) {
		super(testName, "OpenXavaTest", "Offices2");		
	}
	
	public void testOverlappedKeyReference() throws Exception {
		// Creating
		execute("CRUD.new");
		setValue("number", "1");
		setValue("zoneNumber", "1");
		setValue("name", "JUNIT OFFICE");
		setValue("mainWarehouse.number", "1");
		assertValue("mainWarehouse.name", "CENTRAL VALENCIA");
		setValue("officeManager.number", "1");
		assertValue("officeManager.name", "PEPE");		
		execute("CRUD.save");
		assertNoErrors();
		
		// Searching
		assertValue("name", "");		
		setValue("number", "1");
		setValue("zoneNumber", "1");		
		setValue("mainWarehouse.number", "1");
		execute("CRUD.search");
		assertValue("name", "JUNIT OFFICE");
		
		// Deleting
		execute("CRUD.delete");
		assertMessage("Office2 deleted successfully");
	}
	
}
