package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamiliesTest extends ModuleTestBase {
	
	
	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail"				
	};

	public FamiliesTest(String testName) {
		super(testName, "OpenXavaTest", "Families");		
	}

	
	public void testTabDefaultOrder() throws Exception {
		assertActions(listActions);
		assertValueInList(0, "number", "1");
		assertValueInList(1, "number", "2");
		assertValueInList(2, "number", "3");
	}	
	
	public void testDependOnHiddenKey() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoErrors();
		
		String [][] productsOfFamily1 = {
				{ "", "" },				
				{ "1", "XAVA" }
		};		
		assertValidValues("products", productsOfFamily1);
		execute("Navigation.next");
		
		String [][] productsOfFamily2 = {
				{ "", "" },				
		};		
		assertValidValues("products", productsOfFamily2);
		execute("Navigation.next");
		
		String [][] productsOfFamily3 = {
				{ "", "" },				
				{ "2", "XAVA COURSE" }
		};		
		assertValidValues("products", productsOfFamily3);								
	}
					
}
