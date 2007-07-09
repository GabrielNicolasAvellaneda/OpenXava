package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Mª Carmen Gimeno
 */

public class StateHibernateTest extends ModuleTestBase {
	
	
	public StateHibernateTest(String testName) {
		super(testName, "StatesHibernate");		
	}

	public void testCreateReadUpdateDeleteWithHandmadeHibernate() throws Exception {		
		assertListNotEmpty();
		// Create				
		execute ("StatesHibernate.new");
		setValue("id","66");
		setValue("name","State JUnit");
		execute("StatesHibernate.save");
		assertNoErrors();
		assertValue("id", "");
		assertValue("name", "");
		
		// Read
		setValue("id", "66");
		execute("StatesHibernate.search");
		assertNoErrors();
		assertValue("id","66");
		assertValue("name","STATE JUNIT");
		
		// Modify
		setValue("name","State JUnit Modified");
		execute("StatesHibernate.save");
		assertNoErrors();
		setValue("id", "66");
		execute("StatesHibernate.search");
		assertNoErrors();
		assertValue("id","66");
		assertValue("name","STATE JUNIT MODIFIED");
		
		// Delete
		
		execute("StatesHibernate.delete");
		assertNoErrors();
	}
}
