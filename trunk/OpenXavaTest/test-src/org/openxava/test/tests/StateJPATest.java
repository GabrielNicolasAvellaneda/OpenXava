package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Mª Carmen Gimeno
 */

public class StateJPATest extends ModuleTestBase {
	
	
	public StateJPATest(String testName) {
		super(testName, "StatesJPA");		
	}

	public void testCreateReadUpdateDeleteWithHandmadeJPA() throws Exception {
		assertListNotEmpty();
		// Create		
		execute ("StatesJPA.new");
		setValue("id","66");
		setValue("name","State JUnit");
		execute("StatesJPA.save");
		assertNoErrors();
		assertValue("id", "");
		assertValue("name", "");
		
		// Read
		setValue("id", "66");
		execute("StatesJPA.search");
		assertNoErrors();
		assertValue("id","66");
		assertValue("name","STATE JUNIT");
		
		// Modify
		setValue("name","State JUnit Modified");
		execute("StatesJPA.save");
		assertNoErrors();
		setValue("id", "66");
		execute("StatesJPA.search");
		assertNoErrors();
		assertValue("id","66");
		assertValue("name","STATE JUNIT MODIFIED");
		
		// Delete		
		execute("StatesJPA.delete");
		assertNoErrors();
	}
}
