package org.openxava.test.tests;

import org.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class CarriersTest extends ModuleTestBase {
	
	public CarriersTest(String testName) {
		super(testName, "OpenXavaTest", "Carriers");		
	}
	
	protected void setUp() throws Exception {
		deleteCarriers();
		createCarriers();  		
		super.setUp();
	}
	
	private void createCarriers() throws Exception {
		Warehouse wh = new Warehouse();
		wh.setNumber(1);
		wh.setZoneNumber(1);
		Carrier c1 = new Carrier();
		c1.setWarehouse(wh);
		// driving licence is not set to test converters in references
		c1.setNumber(1);
		c1.setName("UNO");
		getSession().save(c1);
		
		Carrier c2 = new Carrier();
		c2.setWarehouse(wh);
		c2.setNumber(2);
		c2.setName("DOS");
		getSession().save(c2);				

		Carrier c3 = new Carrier();
		c3.setWarehouse(wh);
		c3.setNumber(3);
		c3.setName("TRES");
		getSession().save(c3);
	
		Carrier c4 = new Carrier();
		c4.setWarehouse(wh);
		c4.setNumber(4);
		c4.setName("CUATRO");
		getSession().save(c4);
		
		Warehouse wh2 = new Warehouse();
		wh2.setNumber(1);
		wh2.setZoneNumber(2);
		
		Carrier c5 = new Carrier();
		c5.setWarehouse(wh2);
		c5.setNumber(5);
		c5.setName("Cinco");
		getSession().save(c5);
		
		closeSession();
	}
	
	private void deleteCarriers()
		throws Exception {
		getSession().createQuery("delete from Carrier").executeUpdate();
		closeSession();
	}
	
	public void testHideShowRows() throws Exception {
		assertListRowCount(5);
		assertAction("List.hideRows");
		assertNoAction("List.showRows");
		
		execute("List.hideRows");		
		assertListRowCount(1);
		assertNoAction("List.hideRows");
		assertAction("List.showRows");
		
		execute("List.filter");
		assertListRowCount(5);
		assertAction("List.hideRows");
		assertNoAction("List.showRows");

		resetModule();
		assertListRowCount(1);
		assertNoAction("List.hideRows");
		assertAction("List.showRows");

		execute("List.showRows");
		assertListRowCount(5);
		assertAction("List.hideRows");
		assertNoAction("List.showRows");
		
		resetModule();
		assertListRowCount(5);
		assertAction("List.hideRows");
		assertNoAction("List.showRows");		
	}
	
	public void testJDBCAction() throws Exception {
		assertListRowCount(5); 		
		execute("Carriers.deleteAll");
		assertNoErrors();
		assertListRowCount(1);
	}
	
	public void testResetSelectedOnReturnToList() throws Exception {
		checkRow(3);
		assertRowChecked(3);
		execute("CRUD.new");
		execute("Mode.list");
		assertRowUnchecked(3);
	}
	
	public void testActionOfCalculatedPropertyAlwaysPresent() throws Exception {
		execute("CRUD.new");
		assertAction("Carriers.translateName");
		assertExists("calculated");
		assertNoEditable("calculated");
	}
	
	public void testFilterIgnoringCase() throws Exception {
		assertListRowCount(5);
		String [] condition = { "", "cinco" };
		setConditionValues(condition);
		execute("List.filter");		
		assertListRowCount(1);
		assertValueInList(0, "number", "5");
		assertValueInList(0, "name", "Cinco");
	}
	
	public void testPropertyDependsDescriptionsListReference_multipleKeyWithSpaces() throws Exception {
		execute("CRUD.new");		
		DrivingLicenceKey key = new DrivingLicenceKey();
		key.setType("C ");
		key.setLevel(1);
		assertValue("remarks","");			
		setValue("drivingLicence.KEY", key.toString());		
		assertNoErrors();
		assertValue("drivingLicence.KEY", key.toString());
		assertValue("remarks", "He can drive trucks");
	}
	
	public void testCreateFromReferenceWithOwnController() throws Exception {
		execute("CRUD.new");		
		execute("Reference.createNew", "model=Warehouse,keyProperty=xava.Carrier.warehouse.number");		
		assertNoErrors();
		assertAction("NewCreation.saveNew");
		assertAction("NewCreation.cancel");
		assertValue("Warehouse", "name", "NEW WAREHOUSE");
	}
	
	public void testDeleteUsingBeforeReferenceSearch() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		execute("Reference.search", "keyProperty=xava.Carrier.warehouse.zoneNumber");
		execute("ReferenceSearch.cancel");
		execute("CRUD.delete");		
		assertNoErrors();
		assertMessage("Carrier deleted successfully");		
	}
	
	public void testGoListModeWithoutRecords() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoErrors();		
		
		deleteCarriers();
		
		execute("Mode.list");				
		execute("Mode.detailAndFirst");
		assertError("Impossible go to detail mode, there are no elements in list");		
	}

	
	public void testDeleteWithoutSelected() throws Exception {
		assertCarriersCount(5);		
		execute("List.viewDetail", "row=2");		
		assertValue("number", "3");
		assertValue("name", "TRES");
		execute("CRUD.delete");
		assertMessage("Carrier deleted successfully");
		assertNoEditable("number");
		assertEditable("name");				
		assertValue("number", "4");
		assertValue("name", "CUATRO");
		assertCarriersCount(4);
		execute("Navigation.previous");
		assertValue("number", "2");
		assertValue("name", "DOS");
		assertNoErrors();		
		execute("Navigation.previous");
		assertValue("number", "1");
		assertValue("name", "UNO");
		assertNoErrors();		
		execute("CRUD.delete");
		assertMessage("Carrier deleted successfully");
		assertValue("number", "2");
		assertValue("name", "DOS");
		execute("Navigation.next");
		assertValue("number", "4");
		assertValue("name", "CUATRO");
		assertNoErrors();
		execute("Navigation.next");
		assertValue("number", "5");
		assertValue("name", "Cinco");
		assertNoErrors();				
		execute("CRUD.delete");
		assertMessage("Carrier deleted successfully");
		assertValue("number", "4");
		assertValue("name", "CUATRO");
		execute("CRUD.delete");		
		assertMessage("Carrier deleted successfully");
		assertValue("number", "2");
		assertValue("name", "DOS");
		assertCarriersCount(1);
		execute("CRUD.delete");		
		assertMessage("Carrier deleted successfully");
		assertValue("number", "");
		assertValue("name", "");
		// The last ramain without edit
		assertNoEditable("number");
		assertNoEditable("name");						
		assertCarriersCount(0);
		execute("CRUD.new");
		assertEditable("number");
		assertEditable("name");
	}
	
	
	public void testDeleteWithSelected() throws Exception {
		assertCarriersCount(5);
		checkRow(1); // 2, DOS
		checkRow(2); // 3, TRES
		checkRow(4); // 5, CINCO		
		execute("Mode.detailAndFirst");		
		assertValue("number", "2");
		assertValue("name", "DOS");
		execute("Navigation.next");
		assertValue("number", "3");
		assertValue("name", "TRES");
		assertNoErrors();		
		execute("CRUD.delete");		
		assertMessage("Carrier deleted successfully");
		assertCarriersCount(4);
		assertValue("number", "5");
		assertValue("name", "Cinco");
		assertNoErrors();
		execute("CRUD.delete");		
		assertMessage("Carrier deleted successfully");
		assertCarriersCount(3);
		assertValue("number", "2");
		assertValue("name", "DOS");
		execute("CRUD.delete");		
		assertMessage("Carrier deleted successfully");
		assertValue("number", "");
		assertValue("name", "");
		assertCarriersCount(2);
	}
		
	public void testFilterWithCalculatedValues() throws Exception {
		setConditionValues(new String [] { "3" });
		execute("List.filter");
		assertListRowCount(1);
		assertValueInList(0, "number", "3");
		assertValueInList(0, "name", "TRES");
		setConditionValues(new String [] { "4", "CUA" }); // With 2 arguments
		execute("List.filter");
		assertListRowCount(1);
		assertValueInList(0, "number", "4");
		assertValueInList(0, "name", "CUATRO");		
	}
	
	public void testCollectionWithCondition() throws Exception {
		execute("CRUD.new");
		setValue("number", "1");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "UNO");
		assertCollectionRowCount("fellowCarriers", 3);
		assertValueInCollection("fellowCarriers", 0, "number", "2");
		assertValueInCollection("fellowCarriers", 1, "number", "3");
		assertValueInCollection("fellowCarriers", 2, "number", "4");
	}
	
	public void testCalculatedCollection() throws Exception {
		execute("CRUD.new");
		setValue("number", "1");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "UNO");
		assertCollectionRowCount("fellowCarriersCalculated", 3);
		assertValueInCollection("fellowCarriersCalculated", 0, "number", "2");
		assertValueInCollection("fellowCarriersCalculated", 1, "number", "3");
		assertValueInCollection("fellowCarriersCalculated", 2, "number", "4");
	}
	
	
	public void testListActionInCollection() throws Exception {
		execute("CRUD.new");
		setValue("number", "1");
		execute("CRUD.search");
		assertNoErrors();

		assertValueInCollection("fellowCarriers", 0, "name", "DOS");
		assertValueInCollection("fellowCarriers", 1, "name", "TRES");
		assertValueInCollection("fellowCarriers", 2, "name", "CUATRO");
		
		execute("Carriers.translateName", "viewObject=xava_view_fellowCarriers");
		assertNoErrors();
		assertValueInCollection("fellowCarriers", 0, "name", "DOS");
		assertValueInCollection("fellowCarriers", 1, "name", "TRES");
		assertValueInCollection("fellowCarriers", 2, "name", "CUATRO");
				
		checkRowCollection("fellowCarriers", 1);
		checkRowCollection("fellowCarriers", 2);
		execute("Carriers.translateName", "viewObject=xava_view_fellowCarriers");
		assertNoErrors();
		assertValueInCollection("fellowCarriers", 0, "name", "DOS");
		assertValueInCollection("fellowCarriers", 1, "name", "THREE");
		assertValueInCollection("fellowCarriers", 2, "name", "FOUR");		
	}
	
	private void assertCarriersCount(int c) throws Exception {
		Query query = getSession().createQuery("select count(*) from Carrier" );	
		int carrierCount = ((Integer) query.uniqueResult()).intValue();
		assertEquals("Carriers count",c,carrierCount);
	}
	
	
}
