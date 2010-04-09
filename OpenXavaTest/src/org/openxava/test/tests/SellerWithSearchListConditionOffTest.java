package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;


/**
 * 
 * @author Federico Alcantara
 */

public class SellerWithSearchListConditionOffTest extends ModuleTestBase {
		
	public SellerWithSearchListConditionOffTest(String testName) {
		super(testName, "SellerSearchListConditionOff");		
	}
	
	public void testSearchListCondition() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Reference.search", "keyProperty=level.id");
		assertListRowCount(3);
		closeDialog();

		execute("Collection.add", "viewObject=xava_view_customers");
		assertListRowCount(5);
	}
}
