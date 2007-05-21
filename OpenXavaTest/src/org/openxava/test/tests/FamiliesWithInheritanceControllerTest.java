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
		"HideShowCRUDActions.hideDelete",
		"HideShowCRUDActions.showDelete",
		"HideShowCRUDActions.hideSaveDelete",
		"HideShowCRUDActions.showSaveDelete",
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
	
	private String [] actionsWithoutDelete = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",
		"Families.new",
		"CRUD.save",
		"CRUD.search",
		"HideShowCRUDActions.hideDelete",
		"HideShowCRUDActions.showDelete",
		"HideShowCRUDActions.hideSaveDelete",
		"HideShowCRUDActions.showSaveDelete",
		"Mode.list"					
	};
	
	private String [] actionsWithoutSaveDelete = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",
		"Families.new",
		"CRUD.search",
		"HideShowCRUDActions.hideDelete",
		"HideShowCRUDActions.showDelete",
		"HideShowCRUDActions.hideSaveDelete",
		"HideShowCRUDActions.showSaveDelete",
		"Mode.list"					
	};
	
	public FamiliesWithInheritanceControllerTest(String testName) {
		super(testName, "FamiliesWithInheritanceController");		
	}
	
	public void testOverwriteAction() throws Exception {
		assertActions(listActions);
		execute("Families.new");
		assertActions(detailActions);		
		assertValue("number", "99");
		assertValue("description", "NOVA FAMILIA");
	}
	
	public void testHideShowActions() throws Exception {
		assertActions(listActions);
		execute("Families.new");
		assertActions(detailActions);		
		execute("HideShowCRUDActions.hideDelete");
		assertActions(actionsWithoutDelete);
		execute("HideShowCRUDActions.showDelete");
		assertActions(detailActions);	
		execute("HideShowCRUDActions.hideSaveDelete");
		assertActions(actionsWithoutSaveDelete);
		execute("HideShowCRUDActions.showSaveDelete");
		assertActions(detailActions);
	}
					
}
