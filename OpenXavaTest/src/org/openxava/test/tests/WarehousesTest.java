package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class WarehousesTest extends ModuleTestBase {
	
	public WarehousesTest(String testName) {
		super(testName, "OpenXavaTest", "Warehouses");		
	}	
		
	public void testNotLoseFilterOnChangeMode() throws Exception {
		assertListRowCount(10);
		setConditionValues(new String [] {"1"} );
		execute("List.filter");
		assertListRowCount(3);
		execute("Mode.detailAndFirst");
		execute("Mode.list");
		assertListRowCount(3);
	}
	
	public void testFilterFromNoFirstPage() throws Exception {
		execute("List.goPage", "page=2");
		String [] condition = {
				"", "2"
		};
		setConditionValues(condition);
		execute("List.filter");
		assertListRowCount(4); 
	}
	
	public void testRememberListPage() throws Exception {
		assertListRowCount(10);
		assertNoAction("List.goPreviousPage");
		execute("List.goPage", "page=2");
		assertListRowCount(10);
		assertAction("List.goPreviousPage");
		execute("Mode.detailAndFirst");
		execute("Mode.list");
		assertListRowCount(10);
		assertAction("List.goPreviousPage");
	}
	
	public void testCheckUncheckRows() throws Exception {
		checkRow(1);
		execute("List.goNextPage");
		assertNoErrors();
		checkRow(12);
		execute("List.goPreviousPage");
		assertNoErrors();
		assertRowChecked(1);
		uncheckRow(1);
		assertRowUnchecked(1);
		execute("List.goNextPage");
		assertNoErrors();
		assertRowChecked(12);
		execute("List.goPreviousPage");
		assertNoErrors();
		assertRowUnchecked(1);
	}

	public void testCreateReadUpdateDelete() throws Exception {
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
		
		// Create
		execute("CRUD.new");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		
		setValue("zoneNumber", "66");
		setValue("number", "666");
		setValue("name", "WAREHOUSE JUNIT");
		execute("CRUD.save");
		// Verifying from is clean
		assertValue("zoneNumber", "");
		assertValue("number", "");		
		assertValue("name", "");
		// Search
		setValue("zoneNumber", "66");
		setValue("number", "666");
		execute("CRUD.search");
		assertValue("zoneNumber", "66");
		assertValue("number", "666");		
		assertValue("name", "WAREHOUSE JUNIT");
		// Modify
		setValue("name", "WAREHOUSE JUNIT MODIFIED");
		execute("CRUD.save");
		// Verifying form is clean
		assertValue("zoneNumber", "");
		assertValue("number", "");		
		assertValue("name", "");
		// Verifying modified
		setValue("zoneNumber", "66");
		setValue("number", "666");
		execute("CRUD.search");
		assertValue("zoneNumber", "66");
		assertValue("number", "666");		
		assertValue("name", "WAREHOUSE JUNIT MODIFIED");
				
		// Go to page for delete
		execute("CRUD.delete");
		assertAction("ConfirmDelete.confirmDelete");
		assertAction("ConfirmDelete.cancel");		
		assertValue("zoneNumber", "66");
		assertValue("number", "666");		
		assertValue("name", "WAREHOUSE JUNIT MODIFIED");
		// Cancel
		execute("ConfirmDelete.cancel");		
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		
		// Return to page for delete
		execute("CRUD.delete");
		assertAction("ConfirmDelete.confirmDelete");
		assertAction("ConfirmDelete.cancel");
		assertValue("zoneNumber", "66");
		assertValue("number", "666");		
		assertValue("name", "WAREHOUSE JUNIT MODIFIED");
		// Delete
		execute("ConfirmDelete.confirmDelete");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		
		assertMessage("Warehouse deleted successfully");
		// Verifying is deleted
		execute("CRUD.new");
		setValue("zoneNumber", "66");
		setValue("number", "666");				
		execute("CRUD.search");		
		assertError("Object not found");
		assertErrorsCount(1);
	}
	
	public void testSaveExisting() throws Exception { 
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");

		
		// Create
		execute("CRUD.new");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		setValue("zoneNumber", "66");
		setValue("number", "666");
		setValue("name", "WAREHOUSE JUNIT");
		execute("CRUD.save");
		// Verifying form is clean
		assertValue("zoneNumber", "");
		assertValue("number", "");		
		assertValue("name", "");
		// Try to re-create
		execute("CRUD.new");
		setValue("zoneNumber", "66");
		setValue("number", "666");
		setValue("name", "WAREHOUSE JUNIT");
		execute("CRUD.save");
		
		assertError("Impossible to create: an object with that key already exists");
		
		// Delete
		setValue("zoneNumber", "66");
		setValue("number", "666");
		execute("CRUD.search");				
		execute("CRUD.delete");
		assertAction("ConfirmDelete.confirmDelete");
		assertAction("ConfirmDelete.cancel");
		assertValue("zoneNumber", "66");
		assertValue("number", "666");				
		execute("ConfirmDelete.confirmDelete");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");

		// Verifying is deleted
		execute("CRUD.new");
		setValue("zoneNumber", "66");
		setValue("number", "666");				
		execute("CRUD.search");		
		assertError("Object not found");							
	}
				
	public void testClickOneInListMode() throws Exception {
		// In list mode on start
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");

		String zoneNumber = getValueInList(3, "zoneNumber");
		String number = getValueInList(3, "number");
		String name = getValueInList(3, "name");
		execute("List.viewDetail", "row=3");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		assertValue("zoneNumber", zoneNumber);
		assertValue("number", number);
		assertValue("name", name);
	}
	
	public void testListNavigation_ChooseVarious_NavigateInChoosed() throws Exception {
		// In list mode on start
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
		String zoneNumber1 = getValueInList(0, "zoneNumber");
		String number1 = getValueInList(0, "number");
		String name1 = getValueInList(0, "name");
		checkRow(0);
		execute("List.goNextPage");
		String zoneNumber2 = getValueInList(0, "zoneNumber");
		String number2 = getValueInList(0, "number");
		String name2 = getValueInList(0, "name");
		checkRow(10);
		execute("List.goNextPage");
		String zoneNumber3 = getValueInList(1, "zoneNumber");
		String number3 = getValueInList(1, "number");
		String name3 = getValueInList(1, "name");
		checkRow(21);
		String zoneNumber4 = getValueInList(3, "zoneNumber");
		String number4 = getValueInList(3, "number");
		String name4 = getValueInList(3, "name");
		checkRow(23);
		execute("Mode.detailAndFirst");
		assertValue("zoneNumber", zoneNumber1);
		assertValue("number", number1);
		assertValue("name", name1);
		execute("Navigation.next");
		assertValue("zoneNumber", zoneNumber2);
		assertValue("number", number2);
		assertValue("name", name2);
		execute("Navigation.next");
		assertValue("zoneNumber", zoneNumber3);
		assertValue("number", number3);
		assertValue("name", name3);
		execute("Navigation.next");
		assertValue("zoneNumber", zoneNumber4);
		assertValue("number", number4);
		assertValue("name", name4);			
		execute("Navigation.next");
		assertError("No more elements in list");
		execute("Navigation.previous"); // In 3
		execute("Navigation.previous"); // In 2
		execute("Navigation.previous"); // In 1
		assertValue("zoneNumber", zoneNumber1);
		assertValue("number", number1);
		assertValue("name", name1);
		execute("Navigation.previous");
		assertError("We already are at the beginning of the list");
		execute("Navigation.next");
		assertValue("zoneNumber", zoneNumber2);
		assertValue("number", number2);
		assertValue("name", name2);
		execute("Navigation.first");
		assertValue("zoneNumber", zoneNumber1);
		assertValue("number", number1);
		assertValue("name", name1);							
	}
	
	public void testRememberSelected() throws Exception {
		// In list mode on start
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
		checkRow(0);
		execute("List.goNextPage");
		checkRow(10);
		checkRow(12);
		execute("List.goPreviousPage");
		assertRowChecked(0);
		execute("List.goNextPage");
		assertRowsChecked(10, 12);
	}
	
	public void testDefaulActionInListNotReturnToDetail() throws Exception {
		// In list mode on start
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
		executeDefaultAction(); // Execute search and not new 
		assertNoErrors();
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
	}
	
	public void testValidation() throws Exception {
		assertAction("Warehouses.toLowerCase");
		assertNoAction("Warehouses.changeZone");
		
		// Create
		execute("CRUD.new");
		assertNoAction("Warehouses.toLowerCase");
		assertAction("Warehouses.changeZone");
		setValue("zoneNumber", "66");
		setValue("number", "666");
		setValue("name", ""); // and the name is required
		execute("CRUD.save");
		
		assertError("Value for Name in Warehouse is required");		
	}
		
}
