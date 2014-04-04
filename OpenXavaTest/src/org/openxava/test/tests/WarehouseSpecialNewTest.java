package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class WarehouseSpecialNewTest extends ModuleTestBase {
	
		
	public WarehouseSpecialNewTest(String testName) {
		super(testName, "WarehouseSpecialNew");		
	}
	
	public void testExecutingOnChangeActionDoesNotClosePersistentSession_noCustomizeList() throws Exception {
		assertNoAction("List.customize");
		execute("WarehouseSpecialNew.new");
		assertNoErrors();
		assertMessagesCount(2);
	}
	
}
