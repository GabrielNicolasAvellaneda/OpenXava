package org.openxava.test.tests;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersTwoSellersInListTest extends ModuleTestBase {

	
	public CustomersTwoSellersInListTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersTwoSellersInList");				
	}
	
	public void testTwoReferencesToSameComponentButOnlyShowingKeyOfEach() throws Exception {
		assertListRowCount(CustomerUtil.getHome().findAll().size());
	}
		
}
