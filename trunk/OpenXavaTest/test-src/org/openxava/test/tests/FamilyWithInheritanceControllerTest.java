package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamilyWithInheritanceControllerTest extends ModuleTestBase {

	private String [] detailActions = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",
		"Family.new",
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
		"Family.new",
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
		"Family.new",
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
		"Family.new",
		"CRUD.search",
		"HideShowCRUDActions.hideDelete",
		"HideShowCRUDActions.showDelete",
		"HideShowCRUDActions.hideSaveDelete",
		"HideShowCRUDActions.showSaveDelete",
		"Mode.list"					
	};
	
	public FamilyWithInheritanceControllerTest(String testName) {
		super(testName, "FamilyWithInheritanceController");		
	}
	
	public void testOverwriteAction() throws Exception {
		assertActions(listActions);
		execute("Family.new");
		assertActions(detailActions);		
		assertValue("number", "99");
		assertValue("description", "NOVA FAMILIA");
	}
	
	public void testHideShowActions() throws Exception {
		assertActions(listActions);
		execute("Family.new");
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
