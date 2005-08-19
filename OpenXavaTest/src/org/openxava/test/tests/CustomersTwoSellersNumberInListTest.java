package org.openxava.test.tests;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersTwoSellersNumberInListTest extends ModuleTestBase {

	
	public CustomersTwoSellersNumberInListTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersTwoSellersNumberInList");				
	}
	
	public void testTwoReferencesToSameComponentButOnlyShowingKeyOfEach() throws Exception {
		assertListRowCount(CustomerUtil.getHome().findAll().size());
	}
		
}
