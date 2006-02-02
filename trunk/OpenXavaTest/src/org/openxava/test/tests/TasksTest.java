package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class TasksTest extends ModuleTestBase {
			
	public TasksTest(String testName) {
		super(testName, "OpenXavaTest", "Tasks");		
	}
	
	public void testUsersAndUserFilter() throws Exception {
		// In order to run this test you need an user 'junit' in your portal
		login("junit", "junit");		
		assertValueInList(0, "user", "junit");
		assertValueInList(0, "summary", "FOR USING IN JUNIT TEST");		
		execute("CRUD.new");
		assertValue("user", "junit");
		logout();
	}
			
}
