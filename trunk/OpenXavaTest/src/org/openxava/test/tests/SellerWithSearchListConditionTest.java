package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;


/**
 * 
 * @author Federico Alcantara
 */

public class SellerWithSearchListConditionTest extends ModuleTestBase {
		
	public SellerWithSearchListConditionTest(String testName) {
		super(testName, "SellerSearchListCondition");		
	}
	
	public void testSearchListCondition() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Reference.search", "keyProperty=level.id");
		assertListRowCount(2);
		closeDialog();

		execute("Collection.add", "viewObject=xava_view_customers");
		assertListRowCount(4);
	}
	
}
