package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamiliesWithInheritanceControllerTest extends ModuleTestBase {

	private String [] detailActions = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",
		"Families.new",
		"CRUD.save",
		"CRUD.delete",
		"CRUD.search",			
		"Mode.list"					
	};
	
	
	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"Families.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail",
		"List.hideRows"
	};
	
	

	public FamiliesWithInheritanceControllerTest(String testName) {
		super(testName, "OpenXavaTest", "FamiliesWithInheritanceController");		
	}

	
	public void testOverwriteAction() throws Exception {
		assertActions(listActions);
		execute("Families.new");
		assertActions(detailActions);		
		assertValue("number", "99");
		assertValue("description", "NOVA FAMILIA");
	}	
					
}
