package org.openxava.test.tests;

import org.hibernate.*;
import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersTwoSellersNumberInListTest extends ModuleTestBase {

	
	public CustomersTwoSellersNumberInListTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersTwoSellersNumberInList");				
	}
	
	public void testTwoReferencesToSameComponentButOnlyShowingKeyOfEach() throws Exception {		
		int customerCount = Customer.findAll().size();
		assertListRowCount(customerCount);
	}
		
}
