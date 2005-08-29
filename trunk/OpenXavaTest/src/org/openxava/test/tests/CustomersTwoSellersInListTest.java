package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class CustomersTwoSellersInListTest extends ModuleTestBase {

	
	public CustomersTwoSellersInListTest(String testName) {
		super(testName, "OpenXavaTest", "CustomersTwoSellersInList");				
	}
	
	public void test2ReferenceToSameModelInList() throws Exception {
		assertListRowCount(4);
		assertValueInList(0, "name", "Javi");
		assertValueInList(0, "seller.name", "MANUEL CHAVARRI");
		assertValueInList(0, "seller.level.description", "MANAGER");
		assertValueInList(0, "alternateSeller.name", "JUANVI LLAVADOR");		
	}	
			
}
