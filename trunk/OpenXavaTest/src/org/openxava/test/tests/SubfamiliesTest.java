package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class SubfamiliesTest extends ModuleTestBase {
	
	public SubfamiliesTest(String testName) {
		super(testName, "OpenXavaTest", "Subfamilies");		
	}
	
	
	public void testSaveHiddenKeyWithSections() throws Exception {
		assertTrue("For this test is required al least 2 families", getListColumnCount() >= 2);
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("CRUD.save");
		assertNoErrors();
	}	
	
	public void testNavigateHiddenKeyWithSections() throws Exception {
		assertTrue("For this test is required al least 2 families", getListColumnCount() >= 2);
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("Navigation.next");
		assertNoErrors();
	}
	
	public void testPropertiesTabByDefault() throws Exception {
		assertLabelInList(0, "Number");
		assertLabelInList(1, "Family");
		assertLabelInList(2, "Description");		
	}
	
							
}
