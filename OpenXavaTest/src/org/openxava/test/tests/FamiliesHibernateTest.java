package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamiliesHibernateTest extends ModuleTestBase {
	
	
	public FamiliesHibernateTest(String testName) {
		super(testName, "OpenXavaTest", "FamiliesHibernate");		
	}

	public void testCreateReadUpdateDeleteWithHandmadeHibernate() throws Exception {
		assertListRowCount(3);
		// Create
		execute ("FamiliesHibernate.new");
		setValue("number","66");
		setValue("description","Family JUnit");
		execute("FamiliesHibernate.save");
		assertNoErrors();
		execute("Mode.list");
		assertListRowCount(4);
		// Read
		execute("List.viewDetail", "row=3");
		assertNoErrors();
		assertValue("number","66");
		assertValue("description","FAMILY JUNIT");
		// Modify
		setValue("description","Family JUnit Modified");
		execute("FamiliesHibernate.save");
		assertNoErrors();
		execute("Mode.list");
		assertValueInList(3,"description","FAMILY JUNIT MODIFIED");
		// Delete
		execute("List.viewDetail", "row=3");
		assertNoErrors();
		execute("FamiliesHibernate.delete");
		assertNoErrors();
		execute("Mode.list");
		assertListRowCount(3);
	}
}
