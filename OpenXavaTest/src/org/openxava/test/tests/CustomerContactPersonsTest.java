package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class CustomerContactPersonsTest extends ModuleTestBase {
	
	public CustomerContactPersonsTest(String testName) {
		super(testName, "CustomerContactPersons");		
	}
	
	public void testSearchingObjectWithASingleReferenceAsKeyAndWhichRefencesHasOnlyAProperty() throws Exception {
		execute("CRUD.new");
		setValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "Pepe");		
	}
		
}
