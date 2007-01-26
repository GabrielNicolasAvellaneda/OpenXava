package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class CustomerContactPersonsAsAggregate3LevelsTest extends ModuleTestBase {
	
	public CustomerContactPersonsAsAggregate3LevelsTest(String testName) {
		super(testName, "CustomerContactPersonsAsAggregate3Levels");		
	}
	
	public void testAsAggregate3Levels() throws Exception {
		execute("CRUD.new");
		execute("Reference.search", "keyProperty=xava.CustomerContactPerson.customer.seller.level.id");
		assertListColumnCount(2);
		assertLabelInList(0, "Id");
		assertLabelInList(1, "Description");
		assertValueInList(0, 0, "A");
		assertValueInList(0, 1, "MANAGER");
	}
		
}
