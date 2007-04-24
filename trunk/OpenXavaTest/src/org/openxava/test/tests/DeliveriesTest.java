package org.openxava.test.tests;

import java.text.*;
import java.util.*;

import org.hibernate.*;
import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class DeliveriesTest extends ModuleTestBase {
	
	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Remarks.hideRemarks",
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail",
		"List.hideRows"
	};
		
	public DeliveriesTest(String testName) {
		super(testName, "Deliveries");		
	}
	
	public void testMinimunInCollection_checkboxNotInCollectionWhenNotEditable_overrideCollectionActions() throws Exception {
		// minimunCollection
		execute("CRUD.new");
		setValue("invoice.year", "2004");
		setValue("invoice.number", "2");
		setValue("type.number", "1");
		setValue("number", "666");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("description", "DELIVERY JUNIT 666");
		
		execute("Sections.change", "activeSection=2");
		assertCollectionRowCount("details", 3);
		
		execute("DeliveryDetails.new", "viewObject=xava_view_section2_details_details");
		setValue("details.number", "14");
		setValue("details.description", "JUNIT DETAIL 14");
		execute("DeliveryDetails.save", "viewObject=xava_view_section2_details_details");
		
		assertError("More than 3 items in Details of Delivery are not allowed");
		assertCollectionRowCount("details", 3);
		
		execute("DeliveryDetails.hideDetail", "viewObject=xava_view_section2_details_details");
		checkRowCollection("details", 2);
		execute("DeliveryDetails.removeSelected", "viewObject=xava_view_section2_details_details");
		assertNoErrors();
		assertMessage("DeliveryDetail deleted from database");
		assertMessage("Delivery detail 13 deleted successfully"); // This message is by the override action for removeSelected		
		assertCollectionRowCount("details", 2);
		
		execute("DeliveryDetails.new", "viewObject=xava_view_section2_details_details");
		setValue("details.number", "13");
		setValue("details.description", "DETAIL 13");
		execute("DeliveryDetails.save", "viewObject=xava_view_section2_details_details");
		assertNoErrors();
		assertMessage("The action Save for delivery detail executed");
		assertCollectionRowCount("details", 3);
		
		execute("Collection.edit", "row=2,viewObject=xava_view_section2_details_details");
		execute("DeliveryDetails.save", "viewObject=xava_view_section2_details_details");
		assertNoErrors();
		
		// checkboxNotInCollectionWhenNotEditable, this test only work in a HTML UI
		assertTrue("Check box must be present", getHtml().indexOf("xava.Delivery.details.__SELECTED__") >= 0);
		execute("EditableOnOff.setOff");
		assertTrue("Check box must not be present", getHtml().indexOf("xava.Delivery.details.__SELECTED__") < 0);
	}
	
	public void testFocusWhenSectionsAndGroupsInHeader() throws Exception {
		execute("CRUD.new");
		setValue("shortcut", "DY");
		assertValue("remarks", "Delayed");
		assertFocusOn("remarks");
	}
	
	public void testNonExistentReferenceUsedAsKey() throws Exception {
		createDeliveryType(0, "JUNIT DELIVERY TYPE 0");
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");
		assertValue("invoice.date", "1/1/02");						
		setValue("type.number", "0");
		setValue("number", "66");
		setValue("description", "JUNIT");		
		execute("CRUD.save");
		assertNoErrors();
		
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");							
		setValue("type.number", "0");
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("description", "JUNIT");
		
		execute("CRUD.search");
		assertNoErrors();
		assertValue("description", "JUNIT");

		deleteDeliveryType(0);
		execute("CRUD.search");
		assertNoErrors();
		assertValue("description", "JUNIT");

		execute("CRUD.delete");
		assertMessage("Delivery deleted successfully");
	}

	private void createDeliveryType(int number, String description) {
		DeliveryType type = new DeliveryType();
		type.setNumber(number);
		type.setDescription(description);		
		XHibernate.getSession().save(type);
		XHibernate.commit();
	}
	
	private void deleteDeliveryType(int number) {
		DeliveryType type = new DeliveryType();
		type.setNumber(number);		
		XHibernate.getSession().delete(type);
		XHibernate.commit();
	}
	
	public void testSearchingByAnyProperty() throws Exception {
		// One result
		execute("CRUD.new");
		assertValue("number", "");
		assertValue("description", "");
		setValue("description", "%SEARCHING");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("number", "777");
		assertValue("description", "FOR TEST SEARCHING BY DESCRIPTION");
		
		// There are more than one match, returns the first
		execute("CRUD.new");
		assertValue("number", "");
		assertValue("description", "");
		setValue("driverType", "");
		setValue("description", "DEL");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("description", "DELIVERY JUNIT 666");				
	}	
		
	public void testDateCalendarEditor() throws Exception {
		execute("CRUD.new");
		assertExists("invoice.date");
		assertNotExists("invoice.date_CALENDAR_BUTTON_");
		assertExists("date_CALENDAR_BUTTON_");		
	}
	
	public void testAggregateInCollectionWithVisibleKeyDoesNotTryToSearchOnChangeKey() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=2");
		execute("DeliveryDetails.new", "viewObject=xava_view_section2_details_details");
		setValue("details.number", "66");
		assertNoErrors();
	}
	
	public void testOnChangeActionOnlyOnce() throws Exception {
		execute("CRUD.new");
		assertValue("driverType", "X");
	}
	
	public void testAggregateInCollectionWithNotHiddenKey() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=2");
		
		// The bucle is for choosing a delivery with less than 3 details
		while (getCollectionRowCount("details") >= 3) {
			execute("Navigation.next");
		}
		
		execute("DeliveryDetails.new", "viewObject=xava_view_section2_details_details");
		assertMessage("The action New for delivery detail executed");
		setValue("details.number", "66");
		setValue("details.description", "JUNIT DELIVERY DETAIL");
		execute("DeliveryDetails.save", "viewObject=xava_view_section2_details_details");
		assertMessage("The action Save for delivery detail executed");
		assertNoErrors();				
		
		execute("Collection.edit", "row=0,viewObject=xava_view_section2_details_details");
		assertValue("details.number", "66");
		execute("DeliveryDetails.hideDetail", "viewObject=xava_view_section2_details_details");
		assertMessage("The action Close for delivery detail executed");
		execute("Collection.edit", "row=0,viewObject=xava_view_section2_details_details");
		assertValue("details.number", "66");
		execute("DeliveryDetails.remove", "viewObject=xava_view_section2_details_details");
		assertMessage("The action Remove for delivery detail executed");
		assertNoErrors();
	}
	
	public void testReferenceAsDescriptionsListWithValidValuesInKey_validateViewPropertiesOnModify() throws Exception {
		execute("Mode.detailAndFirst");
		assertValue("shipment.KEY", "");
		IShipment shipment = (IShipment) Shipment.findAll().iterator().next();
		setValue("shipment.KEY", toKeyString(shipment));
		execute("CRUD.save");
		assertError("Value for Advice in Delivery is required");
		setValue("advice", "Modifying");
		execute("CRUD.save");
		assertNoErrors();
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertValue("shipment.KEY", toKeyString(shipment)); 
		assertDescriptionValue("shipment.KEY", shipment.getDescription());
		// Restoring		
		setValue("shipment.KEY", "");
		setValue("advice", "Restoring");
		execute("CRUD.save");
		assertNoErrors();
	}
	
	public void testWhenStereotypeWithoutFormatterUseTypeFormatter() throws Exception {
		// date: Without stereotype, use date formatter
		String date = getValueInList(0, "date");		
		// dataAsLabel: With stereotype, but it has no formatter,
		// hence it must to use date formatter		
		String dateAsLabel = getValueInList(0, "dateAsLabel");		
		assertEquals(date, dateAsLabel);
	}
	
	public void testSecondLevelCalculatedPropertyAndDependenOf3LevelPropertyInList() throws Exception {
		int c = getListRowCount();
		boolean withoutDiscount = false;
		boolean withDiscount = true;
		for (int i=0; i<c; i++) {
			String value = getValueInList(i, "invoice.sellerDiscount");
			if ("0".equals(value)) withoutDiscount = true;
			else if ("20".equals(value)) withDiscount = true;
			else fail("Only 0 or 20 are valid values for invoice.sellerDiscount");
		}
		assertTrue("It's required deliveries with invoices with and without seller discount", withDiscount && withoutDiscount);
	}
		
	public void testUseListWithOtherModelAndReturnToModuleList() throws Exception {
		execute("CRUD.new");
		execute("Deliveries.viewCurrentYearInvoices");
		assertNoErrors();
		execute("Return.return");
		assertNoErrors();
		execute("Mode.list");
	}
	
	public void testCreateObjectInvalidateDescriptionsCache() throws Exception {
		execute("CRUD.new");
		assertNoType("66");
		changeModule("DeliveryTypes");
		execute("CRUD.new");
		setValue("number", "66");
		setValue("description", "JUNIT TEST");
		execute("CRUD.save");
		assertNoErrors();
		changeModule("Deliveries");
		assertType("66");
		changeModule("DeliveryTypes");
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		execute("CRUD.delete");		
		assertMessage("Delivery type deleted successfully");
		changeModule("Deliveries");
		assertNoType("66");
	}
	

	public void testEntityValidatorWithKeyReference() throws Exception {		
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		assertNoErrors();
		setValue("advice", "Validating");
		execute("CRUD.save");
		assertNoErrors();
	}
	
	public void testReadHiddenValuesFromServer() throws Exception { 				
		// Create one new
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");
		assertValue("invoice.date", "1/1/02");						
		setValue("type.number", "1");
		setValue("number", "66");
		setValue("description", "JUNIT");
		setValue("remarks", "HIDDEN REMARK");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");						
		assertValue("type.number", "");	
		assertValue("number", "");
		assertValue("description", "");
		assertValue("remarks", "No remarks");

		// Hide remarks		
		execute("Remarks.hideRemarks");
		assertNotExists("remarks");
		
		// Search the just created
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");		
		setValue("number", "66");				
		execute("CRUD.search");
		assertNoErrors();
		assertValue("invoice.year", "2002");
		assertValue("invoice.number", "1");				
		assertValue("invoice.date", "1/1/02");		
		assertValue("type.number", "1");
		assertValue("number", "66");		
		assertValue("description", "JUNIT");				
		assertNotExists("remarks");
		
		// Show remarks
		execute("Remarks.showRemarks");
		assertExists("remarks");
		assertValue("remarks", "HIDDEN REMARK");
																
		// Delete it
		execute("CRUD.delete");													
		assertNoErrors();
		assertMessage("Delivery deleted successfully");		
	}
	
	
	public void testNavigationActionCanReturnPreviousController() throws Exception {
		String [] initialActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",						
			"Mode.list",
			"Reference.search",
			"Reference.createNew",
			"Reference.modify",
			"Sections.change",
			"Deliveries.setDefaultInvoice",
			"Deliveries.setDefaultType",
			"Deliveries.generateNumber",
			"Deliveries.activateDeactivateSection",
			"Deliveries.hideActions",
			"Deliveries.viewCurrentYearInvoices",
			"EditableOnOff.setOn",
			"EditableOnOff.setOff",
			"Remarks.hideRemarks",
			"Remarks.showRemarks",
			"Remarks.setRemarks"			
		};
		
		String [] minimumActions = {
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",						
			"Mode.list",
			"Reference.search",
			"Reference.createNew",
			"Reference.modify",
			"Sections.change",
			"Deliveries.setDefaultInvoice",
			"Deliveries.setDefaultType",			
			"Deliveries.generateNumber"
		};
		
		String [] creatingNewActions = {
			"NewCreation.saveNew",
			"NewCreation.cancel",
			"Mode.list"
		};
				
		execute("CRUD.new");
		assertActions(initialActions);
		
		execute("Deliveries.hideActions");
		assertActions(minimumActions);
		
		execute("Reference.createNew", "model=DeliveryType,keyProperty=xava.Delivery.type.number");
		assertActions(creatingNewActions);
		
		execute("NewCreation.cancel");
		assertActions(minimumActions);	
	}
	
	public void testPropertyAndReferenceActions() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertNoAction("Deliveries.generateNumber"); // of property
		assertNoAction("Deliveries.setDefaultType"); // of reference as descriptions-list
		assertNoAction("Deliveries.setDefaultInvoice"); // of reference 
		execute("CRUD.new");
		assertAction("Deliveries.generateNumber");
		assertAction("Deliveries.setDefaultType");
		assertAction("Deliveries.setDefaultInvoice");
		assertValue("number", "");
		assertValue("type.number", "");
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");
		execute("Deliveries.generateNumber");
		assertValue("number", "77");
		execute("Deliveries.setDefaultType");
		assertValue("type.number", "1");
		execute("Deliveries.setDefaultInvoice");
		assertValue("invoice.year", "2002");
		assertValue("invoice.number", "1");
		assertValue("invoice.date", "1/1/02");		
	}
				
	public void testActivateDeactivateSection() throws Exception {
		execute("CRUD.new");
		assertEditable("advice");
		assertEditable("remarks");
		execute("Deliveries.activateDeactivateSection");
		assertNoEditable("advice");
		assertNoEditable("remarks");
		execute("Deliveries.activateDeactivateSection");
		assertEditable("advice");
		assertEditable("remarks");		
	}
	
	public void testCreateAndReadWithKeyReferences() throws Exception { 				
		// Create new one 
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");
		assertValue("invoice.date", "1/1/02");						
		setValue("type.number", "1");
		setValue("number", "66");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");						
		assertValue("type.number", "");	
		assertValue("number", "");
		assertValue("description", "");		
		// Searching the just created
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");		
		setValue("number", "66");				
		execute("CRUD.search");
		assertNoErrors();
		assertValue("invoice.year", "2002");
		assertValue("invoice.number", "1");				
		assertValue("invoice.date", "1/1/02");		
		assertValue("type.number", "1");
		assertValue("number", "66");		
		assertValue("description", "JUNIT");				
		assertNoEditable("invoice.year");
		assertNoEditable("invoice.number");
		assertNoEditable("type");
		assertNoEditable("number");
		assertEditable("description");
																
		// Delete it
		execute("CRUD.delete");													
		assertNoErrors();
		assertMessage("Delivery deleted successfully");
	}
	
	public void testConverterWithMetaSets() throws Exception { 				
		// Creating new
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "66");
		setValue("description", "JUNIT");
		setValue("distance", isOX3()?"1":"2"); // National, in database 'N'
		execute("CRUD.save");
		assertNoErrors();
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");						
		assertValue("type.number", "");	
		assertValue("number", "");
		assertValue("description", "");
		assertValue("distance", isOX3()?"":"0");		
		// Search just created
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");		
		setValue("number", "66");				
		execute("CRUD.search");
		assertNoErrors();
		assertValue("invoice.year", "2002");
		assertValue("invoice.number", "1");						
		assertValue("type.number", "1");
		assertValue("number", "66");		
		assertValue("description", "JUNIT");		
		assertValue("distance", isOX3()?"1":"2");
		assertNoErrors();
		
		// Verifying database value
		Query query = XHibernate.getSession().createQuery("select d.distance from Delivery as d where "
				+ "invoice.year=2002 and invoice.number=1 and type.number=1 and number=66");		
		String distanceDB = (String) query.uniqueResult();
		assertEquals("distance in database incorrect", "N", distanceDB);
																		
		// Delete
		execute("CRUD.delete");													
		assertNoErrors();
		assertMessage("Delivery deleted successfully");
	}
	
	
	public void testDeleteSelectedOnesAndOrderBy() throws Exception { 
		// Creating new
		execute("CRUD.new");
		setValue("invoice.year", "2009");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "1");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		
		// To list mode and order
		execute("Mode.list");
		assertActions(listActions);
		execute("List.orderBy", "property=invoice.year"); // ascending
		execute("List.orderBy", "property=invoice.year"); // descending
		assertNoErrors();
		
		// Delete					
		assertValueInList(0, "invoice.year", "2009");
		assertValueInList(0, "invoice.number", "1");
		assertValueInList(0, "type.number", "1");
		assertValueInList(0, "number", "1");
		
		checkRow(0);
		
		execute("CRUD.deleteSelected");
		assertNoErrors();
		
		// Verifying that it is deleted
		Query query = XHibernate.getSession().createQuery("from Delivery where "
				+ "invoice.year=2009 and invoice.number=1 and type.number=1 and number=1");		
		if (!query.list().isEmpty()) {
			fail("Delivery would be deleted and it is not the case");
		}
	}
		
	public void testInEntityReferencesNoDefaultValues() throws Exception { 
		execute("CRUD.new");
		assertValue("invoice.year", "");
		assertValue("invoice.number","");
		assertValue("invoice.date", "");
		assertValue("invoice.yearDiscount", "");
		assertNoErrors();
	}
	
	public void testReferencesIfKeyNotExists() throws Exception { 
		execute("CRUD.new");		
		setValue("invoice.year", "2004"); // We supose that not exists
		assertValue("invoice.yearDiscount", "400"); 
		setValue("invoice.number", "907"); // We supose that not exists
				
		assertError("Invoice with key {year=2004, number=907} not found");
				
		// The reference datas are deleted in screen
		assertValue("invoice.year", "");
		assertValue("invoice.number","");
		assertValue("invoice.date", "");
		assertValue("invoice.yearDiscount", "");		
	}
	
	public void testViewPropertyAndHideMembers() throws Exception { 
		execute("CRUD.new");
		assertValue("deliveredBy", isOX3()?"":"0");
		assertNotExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", isOX3()?"0":"1");
		assertExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", isOX3()?"1":"2");
		assertNotExists("employee");		
		assertExists("carrier.number");		
		
		setValue("deliveredBy", isOX3()?"":"0");
		assertNotExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", isOX3()?"1":"2");
		assertNotExists("employee");
		assertExists("carrier.number");
				
		execute("CRUD.new");
		assertValue("deliveredBy", isOX3()?"":"0");
		assertNotExists("employee");
		assertNotExists("carrier.number");			
	}
	
	public void testEnvironmentVariablesModule() throws Exception { 
		// Verifying if works the action search special for this module 

		// Creating
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "61");
		setValue("description", "JUNIT WITHOUT DELIVEREDBY");
		execute("CRUD.save");
		assertNoErrors();
		
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "62");
		setValue("description", "JUNIT BY EMPLOYEE");
		setValue("deliveredBy", isOX3()?"0":"1");
		setValue("employee", "JUNIT EMPLOYEE");		
		execute("CRUD.save");
		assertNoErrors();
		
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "63");
		setValue("description", "JUNIT BY CARRIER");
		setValue("deliveredBy", isOX3()?"1":"2");
		setValue("carrier.number", "1");		
		execute("CRUD.save");
		assertNoErrors();
		
		// Reading and verifying
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "63");
		execute("CRUD.search");		
		assertValue("description", "JUNIT BY CARRIER");		
		assertExists("carrier.number");						    
		assertNotExists("employee");
		assertValue("carrier.number", "1");
		
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "62");
		execute("CRUD.search");		
		assertValue("description", "JUNIT BY EMPLOYEE");
		assertNotExists("carrier.number");
		assertExists("employee");
		assertValue("employee", "JUNIT EMPLOYEE");
		
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "61");
		execute("CRUD.search");		
		assertValue("description", "JUNIT WITHOUT DELIVEREDBY");		
		assertNotExists("carrier.number");
		assertNotExists("employee");
				
		// Delete
		execute("CRUD.delete");
		assertMessage("Delivery deleted successfully");


		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "62");
		execute("CRUD.search");				
		execute("CRUD.delete");
		assertMessage("Delivery deleted successfully");

		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "63");
		execute("CRUD.search");						
		execute("CRUD.delete");			
		assertMessage("Delivery deleted successfully");		
	}
	
	public void testMultipleMappingProperty() throws Exception { 				
		// Creating new
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "66");
		setValue("date", "2/22/97");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");						
		assertValue("type.number", "");	
		assertValue("number", "");
		assertValue("date", getCurrentDate());
		assertValue("description", "");		
		// Search just created
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");		
		setValue("number", "66");				
		execute("CRUD.search");
		assertNoErrors();
		assertValue("invoice.year", "2002");
		assertValue("invoice.number", "1");						
		assertValue("type.number", "1");
		assertValue("number", "66");			
		assertValue("date", "2/22/97");
		assertValue("description", "JUNIT");		
		assertNoErrors();

		// Verifying if date property is well in list 
		// Only works if there are lest than 11 object (because see in first page)		
		execute("Mode.list");
		assertActions(listActions);
		assertNoErrors();
		int quantity = getListRowCount();
		boolean found = false;
		int i = 0;
		for (i = 0; i < quantity; i++) {
			String number = getValueInList(i, "number");						
			if ("66".equals(number)) {				
				assertValueInList(i, "date", "2/22/97");
				found = true;
				break;
			}			
		}
		if (!found) {
			fail("It is necessary that exists delivery 66 in list and there are al least 11 deliveries");		
		}
				
		execute("List.viewDetail", "row=" + i);
																		
		// Delete
		execute("CRUD.delete");
		assertNoErrors();
		assertMessage("Delivery deleted successfully");
	}
	
	public void testCalculatedValueDependentOnChangePropertyOnChangeAndPropertyOnChangeDepedentOnPropertyOnChange() throws Exception { 
		execute("CRUD.new");
		assertValue("distance", isOX3()?"":"0");
		assertValue("vehicle", "");
		assertValue("transportMode", "");
		setValue("distance", isOX3()?"0":"1"); // Local
		assertValue("distance", isOX3()?"0":"1");
		assertValue("vehicle", "MOTORBIKE");
		assertValue("transportMode", "STREET/ROAD");
		assertValue("driverType", "ANY");
		setValue("distance", isOX3()?"1":"2"); // National
		assertValue("distance", isOX3()?"1":"2");
		assertValue("vehicle", "CAR");
		assertValue("transportMode", "HIGHWAY");
		assertValue("driverType", "DRIVER");
		setValue("distance", isOX3()?"":"0"); // Void
		assertValue("distance", isOX3()?"":"0");
		assertValue("vehicle", "");
		assertValue("transportMode", "");
		assertValue("driverType", "DRIVERX");	
	}
	
	public void testOnChangeWithQualifiedProperty() throws Exception { 
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		// Left from field
		assertValue("remarks", "No remarks");
		setValue("remarks", "");
		setValue("invoice.year", "2004");		
		setValue("invoice.number", "2"); 
		assertValue("remarks", "No remarks");
		setValue("remarks", "");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");
		assertValue("remarks", "First invoice of year");
		setValue("remarks", "");
		setValue("invoice.number", "2");
		assertValue("remarks", "No remarks");
		
		// Searching with reference search button
		setValue("remarks", "");
		searchInvoiceWithList("2004", "2");
		assertValue("invoice.year", "2004");		
		assertValue("invoice.number", "2"); 
		assertValue("remarks", "No remarks");
		setValue("remarks", "");
		searchInvoiceWithList("2002", "1");
		assertValue("invoice.year", "2002");		
		assertValue("invoice.number", "1"); 
		assertValue("remarks", "First invoice of year");
		setValue("remarks", "");
		searchInvoiceWithList("2004", "2");
		assertValue("invoice.year", "2004");		
		assertValue("invoice.number", "2"); 
		assertValue("remarks", "No remarks");							
	}
	
	public void testOnChangeDescriptionsListKey_messagesInChangeAction() throws Exception { 
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertValue("remarks", "No remarks");
		setValue("deliveredBy", isOX3()?"1":"2");
		assertNoMessages();
		setValue("carrier.number", "3");
		assertMessagesCount(1);
		assertMessage("Carrier changed");		
		assertValue("remarks", "The carrier is 3");				
		setValue("carrier.number", "2");		
		assertValue("remarks", "The carrier is 2");				
	}
	
	public void testHideInSection() throws Exception { 
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertExists("remarks");
		execute("Remarks.hideRemarks");
		assertNotExists("remarks");
		execute("Remarks.showRemarks");
		assertExists("remarks");
	}
	
	public void testI18nOfValidValues() throws Exception {
		execute("CRUD.new");
		String [][] distanceValues = {
			{isOX3()?"":"0", ""},
			{isOX3()?"0":"1", "Lokal"},			
			{isOX3()?"1":"2", "Nachional"}, 
			{isOX3()?"2":"3", "Internachional"}
		};
		assertValidValues("distance", distanceValues);
	}
	
	public void testViewPropertyInSectionDefaultCalcultarAndValidators() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertExists("advice");
		assertValue("advice", "IF YOU DRINK DO NOT DRIVE");
		setValue("advice", "");
		execute("CRUD.save");
		assertError("Value for Advice in Delivery is required");
	}
	
	public void testEditableAffectsSection() throws Exception {
		execute("Mode.detailAndFirst");
		assertEditable("description"); // out of section
		assertEditable("advice"); // in section
		execute("EditableOnOff.setOff");
		assertNoEditable("description"); // out of section
		assertNoEditable("advice"); // in section				
	}
	
	public void testValidValuesInList() throws Exception {
		int quantity = getListRowCount();
		assertTrue("For this test is needed at least one created delivery", quantity > 0);
		Collection values = new ArrayList();
		values.add("Lokal");
		values.add("Nachional");
		values.add("Internachional");
		boolean thereIsOne = false;
		for (int i=0; i<quantity; i++) {
			String value = getValueInList(i, "distance");			
			if (Is.emptyString(value)) continue;
			if (values.contains(value)) {
				thereIsOne = true;
				continue;
			}
			fail("Only the next values are valid: " + values);
		}
		assertTrue("For this test is need at least one delivery with value in 'distance' property", thereIsOne);
	}
	 
	public void testSetValueAgainstPropertiesOfSectionsHiddenAndShowed() throws Exception {
		execute("Remarks.hideRemarks");
		execute("CRUD.new");
		assertNotExists("remarks");
		execute("Remarks.showRemarks");
		assertExists("remarks");		
		execute("Remarks.setRemarks");
		assertValue("remarks", "Hell in your eyes");	
	}
	
	public void testGeneratePdf() throws Exception {
		execute("Print.generatePdf");		
		assertContentTypeForPopup("application/pdf");
	}
		
	private String getCurrentDate() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		return df.format(new java.util.Date());
	}
		
	private void searchInvoiceWithList(String year, String number) throws Exception {
		execute("Reference.search", "keyProperty=xava.Delivery.invoice.year");
		setConditionValues(
			new String [] { year, number, "", "true" }
		);
		execute("List.filter");
		execute("ReferenceSearch.choose", "row=0");							
	}
	
	private void assertNoType(String type) throws Exception {
		String [] types = getKeysValidValues("type.number");		
		assertTrue(type + " not expected", !Arrays.asList(types).contains(type));
	}

	private void assertType(String type) throws Exception {
		String [] types = getKeysValidValues("type.number");		
		assertTrue(type + " expected", Arrays.asList(types).contains(type));
	}
	
	private String toKeyString(IShipment shipment) throws Exception {
		if (isOX3()) {
			StringTokenizer st = new StringTokenizer(shipment.toString(), "[.]");
			StringBuffer sb = new StringBuffer("[.");
			String mode = (String) shipment.getMetaModel().getMetaProperty("mode").getValidValue(Integer.parseInt(st.nextToken())); 
			sb.append(mode.toUpperCase());
			sb.append('.');
			sb.append(st.nextToken());
			sb.append('.');
			String type = (String) shipment.getMetaModel().getMetaProperty("type").getValidValue(Integer.parseInt(st.nextToken()));
			sb.append(type.toUpperCase());
			sb.append(".]");				
			return sb.toString();
		}
		else {
			return shipment.toString();
		}
	}	

}
