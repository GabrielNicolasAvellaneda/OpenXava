package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class ChangeProductsPriceTest extends ModuleTestBase {
	
	private String [] detailActions = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",		
		"Mode.list",									
		"ChangeProductsPrice.save",
	};
	
	private String [] listActions = {
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail",
		"List.hideRows"
	};

	public ChangeProductsPriceTest(String testName) {
		super(testName, "OpenXavaTest", "ChangeProductsPrice");		
	}

	

	public void testActionOnInitAndViewSetEditable() throws Exception {
		assertActions(listActions);
		
		execute("Mode.detailAndFirst");
		assertActions(detailActions);

		assertNoEditable("description");
		assertEditable("unitPrice");		
	}	
					
}
