package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class DeliveriesGroupsInSectionsTest extends ModuleTestBase {
	
		
	public DeliveriesGroupsInSectionsTest(String testName) {
		super(testName, "DeliveriesGroupsInSections");		
	}
	
	public void testOnChangeActionOnSearchNotMoreThanOneTime() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertMessagesCount(1);
	}
	
}
