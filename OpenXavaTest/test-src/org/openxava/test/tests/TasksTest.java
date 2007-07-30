package org.openxava.test.tests;

import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class TasksTest extends ModuleTestBase {
			
	public TasksTest(String testName) {
		super(testName, "Tasks");		
	}
	
	public void testUsersAndUserFilter() throws Exception {
		// In order to run this test you need an user 'junit' in your portal
		login(getUserLoginName(), "junit");		
		assertValueInList(0, "user", getUserId());
		assertValueInList(0, "summary", "FOR USING IN JUNIT TEST");		
		execute("CRUD.new");
		assertValue("user", getUserId());
		logout();
	}
	
	public void testLogoutResetPortletState() throws Exception {
		login(getUserLoginName(), "junit");
		assertAction("Mode.detailAndFirst");
		assertNoAction("Mode.list");
		execute("CRUD.new");
		assertNoAction("Mode.detailAndFirst");
		assertAction("Mode.list");
		logout();
		login(getUserLoginName2(), "junit2");
		assertAction("Mode.detailAndFirst");
		assertNoAction("Mode.list");
		logout();
	}
	
	private String getUserLoginName() {
		return isLiferayEnabled()?"junit@openxava.org":"junit";
	}
	private String getUserLoginName2() {
		return isLiferayEnabled()?"junit2@openxava.org":"junit2";
	}	
	private String getUserId() {
		if (!isLiferayEnabled()) return "junit";
		return XavaPreferences.getInstance().isEMailAsUserNameInPortal()?"junit@openxava.org":"liferay.com.1001";
	}
			
}
