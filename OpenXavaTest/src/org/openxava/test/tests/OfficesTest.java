package org.openxava.test.tests;


import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class OfficesTest extends ModuleTestBase {
			
	private String clerkName;

	private String warehouseName;

	public OfficesTest(String testName) {
		super(testName, "Offices");		
	}
	
	public void testDescriptionEditorInStereotypeWithAggregateAsModel() throws Exception {
		execute("CRUD.new");
		assertExists("receptionist");			
		int count = Receptionist.findAll().size();
		assertTrue("At least 2 receptionists are required for run this test", count >= 2);
		assertValidValuesCount("receptionist", count + 1);		
	}
	
	public void testMoreThanOneReferenceWithoutValue() throws Exception {
		// for test sql inner join behaviour
		deleteAllOffices();
		// Create offices for test
		execute("CRUD.new");
		setValue("zoneNumber", "1"); // not key for detecting a bug that arose when this is not key 
		setValue("number", "1");
		setValue("name", "OFFICE JUNIT 1");
		execute("CRUD.save");
		assertNoErrors();
		
		execute("CRUD.new");
		setValue("zoneNumber", "1");
		setValue("number", "2");
		setValue("name", "OFFICE JUNIT 2");
		execute("CRUD.save");
		assertNoErrors();
		
		execute("Mode.list");
		assertListRowCount(2);
		
		deleteAllOffices();
	}
	
	private void deleteAllOffices() throws Exception {
		int rc = getListRowCount();
		if (rc < 2) return;
		for (int i = 0; i < rc; i++) {
			checkRow(i);
		}
		execute("CRUD.deleteSelected");
		assertNoErrors();		
	}

	public void testTableColumnSharedByPropertyAndReference() throws Exception {
		deleteOffice22();
						
		execute("CRUD.new");
		assertExists("zoneNumber"); // not key for detecting a bug that arose when this is not key
		assertExists("number");
		assertNotExists("mainWarehouse.zoneNumber");
		assertExists("mainWarehouse.number");
		assertNotExists("officeManager.zoneNumber");
		assertNotExists("officeManager.officeNumber");
		assertExists("officeManager.number");
		
		// Warehouse				
		setValue("zoneNumber", "1");
		execute("Reference.search", "keyProperty=xava.Office.mainWarehouse.number");
		assertWarehouseList("1");								
		execute("ReferenceSearch.cancel");
		
		setValue("zoneNumber", "2");
		execute("Reference.search", "keyProperty=xava.Office.mainWarehouse.number");
		assertWarehouseList("2");
		execute("ReferenceSearch.cancel");
				
		setValue("mainWarehouse.number", "1");
		assertValue("mainWarehouse.name", getWarehouseName());
		
		// Office manager
		setValue("number", "1");
		execute("Reference.search", "keyProperty=xava.Office.officeManager.number");		
		assertClerksList("2", "1");								
		execute("ReferenceSearch.cancel");
		
		setValue("number", "2");
		execute("Reference.search", "keyProperty=xava.Office.officeManager.number");
		assertClerksList("2", "2");
		execute("ReferenceSearch.cancel");
				
		setValue("officeManager.number", "1");
		assertValue("officeManager.name", getClerkName());
				
		
		
		//
		setValue("name", "OFFICE JUNIT 2");
		execute("CRUD.save");
		assertNoErrors();
		
		execute("CRUD.new");
		setValue("zoneNumber", "2");
		setValue("number", "2");
		execute("CRUD.search");
		
		assertValue("zoneNumber", "2");
		assertValue("number", "2");
		assertValue("name", "OFFICE JUNIT 2");
		assertValue("mainWarehouse.number", "1");
		assertValue("mainWarehouse.name", getWarehouseName());
		assertValue("officeManager.number", "1");
		assertValue("officeManager.name", getClerkName());
				
		deleteOffice22();
	}
	
	public void testReadReferenceOverlappedAndNotOverlappedAndFilter() throws Exception {
		// One overlapped
		execute("CRUD.new");
		setValue("zoneNumber", "1"); // not key for detecting a bug that arose when this is not key
		execute("Reference.search", "keyProperty=xava.Office.mainWarehouse.number");
		assertWarehouseList("1");
		setConditionValues(new String [] {"1", "2"} );
		execute("List.filter");
		assertNoErrors();		
		assertWarehouseList("1", "2");
		execute("ReferenceSearch.cancel");
		
		// One not overlapped
		execute("Reference.search", "keyProperty=xava.Office.defaultCarrier.number");
		setConditionValues(new String [] {"1"} );
		execute("List.filter");
		assertNoErrors();
		assertCarriersList("1");				
	}

	private void deleteOffice22() {
		try {
			Office office = new Office();
			office.setNumber(2);
			XHibernate.getSession().delete(office);
			XHibernate.commit();
		}
		catch (Exception ex) {
		}				
	}

	private String getWarehouseName() throws Exception {
		if (warehouseName == null) {
			Warehouse key = new Warehouse();
			key.setZoneNumber(2);
			key.setNumber(1);			
			IWarehouse warehouse = (IWarehouse) XHibernate.getSession().get(Warehouse.class, key);
			warehouseName = warehouse.getName();
		}
		return warehouseName;
	}
	
	private String getClerkName() throws Exception {
		if (clerkName == null) {
				Clerk key = new Clerk();
				key.setZoneNumber(2);
				key.setOfficeNumber(2);
				key.setNumber(1);
				IClerk clerk = (IClerk) XHibernate.getSession().get(Clerk.class, key);
				clerkName = clerk.getName();
		}
		return clerkName;
	}
	

	private void assertWarehouseList(String zone) throws Exception {
		int c = getListRowCount();
		assertTrue("It must to have at least one warehouse in zone " + zone + " for run this test", c > 0);
		assertLabelInList(0, "Zone");
		for (int i = 0; i < c; i++) {
			assertValueInList(i, 0, zone);
		}		
	}
	
	private void assertWarehouseList(String zone, String number) throws Exception {
		int c = getListRowCount();
		assertTrue("It must to have at least one warehouse in zone " + zone + " and number " + number + " for run test", c > 0);
		assertLabelInList(0, "Zone");
		assertLabelInList(1, "Warehouse number");
		for (int i = 0; i < c; i++) {
			assertValueInList(i, 0, zone);
			assertValueInList(i, 1, number);
		}		
	}
	
	
	private void assertCarriersList(String number) throws Exception {
		int c = getListRowCount();
		assertTrue("It must to have at least one carrier with number " + number + " for run this test", c > 0);
		assertLabelInList(1, "Number");
		for (int i = 0; i < c; i++) {
			assertValueInList(i, 1, number);
		}		
	}
	
	
	private void assertClerksList(String zone, String office) throws Exception {
		int c = getListRowCount();
		assertTrue("It must to have at least one clerk in zone " + zone + " and office " + office + " for run this test", c > 0);
		assertLabelInList(0, "Zone");
		assertLabelInList(1, "Office");
		for (int i = 0; i < c; i++) {
			assertValueInList(i, 0, zone);
			assertValueInList(i, 1, office);			
		}		
	}	

}
