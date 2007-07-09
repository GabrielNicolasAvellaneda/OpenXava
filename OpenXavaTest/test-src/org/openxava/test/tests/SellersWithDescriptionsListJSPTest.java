package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellersWithDescriptionsListJSPTest extends ModuleTestBase {
	
	public SellersWithDescriptionsListJSPTest(String testName) {
		super(testName, "SellersWithDescriptionsListJSP");		
	}
	
	public void testDescriptionsListJSPTag() throws Exception {
		execute("Mode.detailAndFirst");
		String [][] levelValues = {
			{ "", "" },
			{ "C", "JUNIOR" },
			{ "A", "MANAGER" },
			{ "B", "SENIOR" }	
		};			
		assertValue("level.id", "A");
		assertValidValues("level.id", levelValues);
	}	
			
}
