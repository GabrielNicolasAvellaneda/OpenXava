package org.openxava.test.tests;

import java.text.*;
import java.util.*;

import org.openxava.hibernate.*;
import org.openxava.model.meta.*;
import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class WarehousesTest extends ModuleTestBase {
	
		
	public WarehousesTest(String testName) {
		super(testName, "OpenXavaTest", "Warehouses");		
	}
	
	public void testPage7InList() throws Exception {
		execute("List.goPage", "page=6");
		execute("List.goPage", "page=7");
		assertListRowCount(3);
		execute("CRUD.new");
		execute("Mode.list");
		assertListRowCount(3);
	}
	
	public void testChangePageRowCountInTab() throws Exception {
		assertListRowCount(10);
		execute("Warehouses.changePageRowCount");
		assertListRowCount(20);
	}
	
	/**
	 * Needs the project AccessTracking deployed in the application server. <p>
	 * 
	 * In addition of AccessTracking and CRUD also it test:
	 * <ul>
	 * <li> Aspects to defining calculators.
	 * <li> postload-calculator
	 * <li> preremove-calculator
	 * </ul>
	 * @throws Exception
	 */	
	public void testAccessTracking_createReadUpdateDelete() throws Exception {		
		XHibernate.getSession().createQuery("delete from Access").executeUpdate();		
		XHibernate.commit();
		
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
				
		// Delete
		execute("CRUD.delete");
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
		
		// Date, time and table
		
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String time = timeFormat.format(new Date());
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String date = dateFormat.format(new Date());
		
		String table = MetaModel.get("Warehouse").getMapping().getTable();
				
		// Verifying the entries in access tracking		
		changeModule("AccessTracking", "Accesses");
		assertListRowCount(8);
		
		assertValueInList(0, "application", "test");
		assertValueInList(0, "model", "Warehouse");
		assertValueInList(0, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(0, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(0, "date", date);
		assertValueInList(0, "time", time);
		assertValueInList(0, "type", "Create");
		assertValueInList(0, "authorized", "Yes");
		assertValueInList(0, "recordId", "{zoneNumber=66, number=666}");
		
		assertValueInList(1, "application", "test");
		assertValueInList(1, "model", "Warehouse");
		assertValueInList(1, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(1, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(1, "date", date);
		assertValueInList(1, "time", time);
		assertValueInList(1, "type", "Read");
		assertValueInList(1, "authorized", "Yes");
		assertValueInList(1, "recordId", "{zoneNumber=66, number=666}");
		
		assertValueInList(2, "application", "test");
		assertValueInList(2, "model", "Warehouse");
		assertValueInList(2, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(2, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(2, "date", date);
		assertValueInList(2, "time", time);
		assertValueInList(2, "type", "Read");
		assertValueInList(2, "authorized", "Yes");
		assertValueInList(2, "recordId", "{zoneNumber=66, number=666}");		
		
		assertValueInList(3, "application", "test");
		assertValueInList(3, "model", "Warehouse");
		assertValueInList(3, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(3, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(3, "date", date);
		assertValueInList(3, "time", time);
		assertValueInList(3, "type", "Update");
		assertValueInList(3, "authorized", "Yes");
		assertValueInList(3, "recordId", "{zoneNumber=66, number=666}");
		
		assertValueInList(4, "application", "test");
		assertValueInList(4, "model", "Warehouse");
		assertValueInList(4, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(4, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(4, "date", date);
		assertValueInList(4, "time", time);
		assertValueInList(4, "type", "Read");
		assertValueInList(4, "authorized", "Yes");
		assertValueInList(4, "recordId", "{zoneNumber=66, number=666}");

		assertValueInList(5, "application", "test");
		assertValueInList(5, "model", "Warehouse");
		assertValueInList(5, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(5, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(5, "date", date);
		assertValueInList(5, "time", time);
		assertValueInList(5, "type", "Read");
		assertValueInList(5, "authorized", "Yes");
		assertValueInList(5, "recordId", "{zoneNumber=66, number=666}");
		
		assertValueInList(6, "application", "test");
		assertValueInList(6, "model", "Warehouse");
		assertValueInList(6, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(6, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(6, "date", date);
		assertValueInList(6, "time", time);
		assertValueInList(6, "type", "Delete");
		assertValueInList(6, "authorized", "Yes");
		assertValueInList(6, "recordId", "{zoneNumber=66, number=666}");
		
		assertValueInList(7, "application", "test");
		assertValueInList(7, "model", "Warehouse");
		assertValueInList(7, "table", table);
		assertTrue("User must to have value", !Is.emptyString(getValueInList(7, "user"))); // Usually 'nobody' or 'UNAUTHENTICATED'
		assertValueInList(7, "date", date);
		assertValueInList(7, "time", time);
		assertValueInList(7, "type", "Read");
		assertValueInList(7, "authorized", "Yes");
		
		assertTrue("The key of displayed data must be not empty", !Is.emptyString(getValueInList(7, "recordId")));
		assertTrue("The key of displayed data must be different", !getValueInList(7, "recordId").equals("{zoneNumber=66, number=666}"));
	}
		
	public void testNavigateInListWithALotOfObjects() throws Exception { 
		assertListRowCount(10);
		execute("List.goPage", "page=6");
		assertListRowCount(10);
		execute("List.goNextPage");
		assertListRowCount(3); // It sssumed 63 objects
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
		assertListRowCount(5); 
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
