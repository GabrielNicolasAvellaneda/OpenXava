package org.openxava.test.tests;

/**
 * @author Javier Paniza
 */

public class WarehouseSplitTest extends WarehouseSplitTestBase {
	
		
	public WarehouseSplitTest(String testName) {
		super(testName, "WarehouseSplit");		
	}
		
	public void testSplitMode() throws Exception {
		assertNoAction("Mode.detailAndFirst");
		assertNoAction("Mode.list");
		assertNoAction("Mode.split");
		
		assertAction("List.filter"); // List is shown
		assertExists("zoneNumber"); // Detail is shown

		super.testSplitMode();
	}
		
}
