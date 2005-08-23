package org.openxava.test.tests;

import java.math.*;
import java.text.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.openxava.test.calculators.*;
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
		super(testName, "OpenXavaTest", "Deliveries");		
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
		execute("Collection.new", "viewObject=xava_view_section2_details");
		setValue("details.number", "66");
		assertNoErrors();
	}
	
	public void testOnChangeActionOnlyOnce() throws Exception {
		execute("CRUD.new");
		assertValue("driverType", "X");
	}
	
	public void testReferenceAsDescriptionsListWithValidValuesInKey() throws Exception {
		execute("Mode.detailAndFirst");
		assertValue("shipment.KEY", "");
		ShipmentRemote shipment = (ShipmentRemote)
			PortableRemoteObject.narrow(
				ShipmentUtil.getHome().findAll().iterator().next(), ShipmentRemote.class);		
		setValue("shipment.KEY", shipment.getPrimaryKey().toString());
		execute("CRUD.save");
		assertNoErrors();
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertValue("shipment.KEY", shipment.getPrimaryKey().toString());
		assertDescriptionValue("shipment.KEY", shipment.getDescription());
		// Restoring		
		setValue("shipment.KEY", "");
		execute("CRUD.save");
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
		assertMessage("DeliveryType deleted successfully");
		changeModule("Deliveries");
		assertNoType("66");
	}
	

	public void testEntityValidatorWithKeyReference() throws Exception {		
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("CRUD.save");
		assertNoErrors();
	}
	
	public void testReadHiddenValuesFromServer() throws Exception { 				
		// Create one new
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");
		assertValue("invoice.date", "01/01/2002");						
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
		assertValue("invoice.date", "01/01/2002");		
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
			"Sections.change",
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
			"Sections.change",
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
	
	public void testPropertyAction() throws Exception {
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertNoAction("Deliveries.generateNumber");
		execute("CRUD.new");
		assertAction("Deliveries.generateNumber");
		assertValue("number", "");
		execute("Deliveries.generateNumber");
		assertValue("number", "77");
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
		assertValue("invoice.date", "01/01/2002");						
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
		assertValue("invoice.date", "01/01/2002");		
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
		setValue("distance", "2"); // National, in database 'N'
		execute("CRUD.save");
		assertNoErrors();
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");						
		assertValue("type.number", "");	
		assertValue("number", "");
		assertValue("description", "");
		assertValue("distance", "0");		
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
		assertValue("distance", "2");
		assertNoErrors();
		
		// Verifying database value
		DeliveryKey key = new DeliveryKey();
		key.set_Invoice_year(2002);
		key.set_Invoice_number(1);
		key.set_Type_number(1);		
		key.setNumber(66);
		String distanceDB = DeliveryUtil.getHome().findByPrimaryKey(key).getData().get_Distance();
		assertEquals("distance in database incorrect", "N", distanceDB);
																		
		// Delete
		execute("CRUD.delete");													
		assertNoErrors();
		assertMessage("Delivery deleted successfully");
	}
	
	
	public void testDeleteSelectedOnesAndOrderBy() throws Exception { 
		// Creating new
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "1");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		
		// To list mode and order
		execute("Mode.list");
		assertActions(listActions);
		execute("List.orderBy", "property=invoice.year");
		assertNoErrors();
		
		// Delete					
		assertValueInList(0, "invoice.year", "2002");
		assertValueInList(0, "invoice.number", "1");
		assertValueInList(0, "type.number", "1");
		assertValueInList(0, "number", "1");
		
		checkRow(0);
		
		execute("CRUD.deleteSelected");
		assertNoErrors();
		
		// Verifying that it is deleted
		DeliveryKey key = new DeliveryKey();
		key.set_Invoice_year(2002);
		key.set_Invoice_number(1);
		key.set_Type_number(1);		
		key.setNumber(1);
		try {
			DeliveryUtil.getHome().findByPrimaryKey(key);
			fail("Delivery " + key + " would be deleted and it is not the case");
		}
		catch (ObjectNotFoundException ex) {
			// everything well
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
		assertValue("deliveredBy", "0");
		assertNotExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", "1");
		assertExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", "2");
		assertNotExists("employee");		
		assertExists("carrier.number");		
		
		setValue("deliveredBy", "0");
		assertNotExists("employee");
		assertNotExists("carrier.number");
		
		setValue("deliveredBy", "2");
		assertNotExists("employee");
		assertExists("carrier.number");
				
		execute("CRUD.new");
		assertValue("deliveredBy", "0");
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
		setValue("deliveredBy", "1");
		setValue("employee", "JUNIT EMPLOYEE");		
		execute("CRUD.save");
		assertNoErrors();
		
		execute("CRUD.new");
		setValue("invoice.year", "2002");
		setValue("invoice.number", "1");						
		setValue("type.number", "1");
		setValue("number", "63");
		setValue("description", "JUNIT BY CARRIER");
		setValue("deliveredBy", "2");
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
		setValue("date", "22/02/1997");
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
		assertValue("date", "22/02/1997");
		assertValue("description", "JUNIT");		
		assertNoErrors();

		// Verifying if date property is well in list 
		// Only works if there are lest than 11 object (because see in first page)		
		execute("Mode.list");
		assertActions(listActions);
		assertNoErrors();
		int quantity = getListRowCount();
		boolean encontrado = false;
		int i = 0;
		for (i = 0; i < quantity; i++) {
			String numero = getValueInList(i, "number");						
			if ("66".equals(numero)) {				
				assertValueInList(i, "date", "22/02/1997");
				encontrado = true;
				break;
			}			
		}
		if (!encontrado) {
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
		assertValue("distance", "0");
		assertValue("vehicle", "");
		assertValue("transportMode", "");
		setValue("distance", "1"); // Local
		assertValue("distance", "1");
		assertValue("vehicle", "MOTORBIKE");
		assertValue("transportMode", "STREET/ROAD");
		assertValue("driverType", "ANY");
		setValue("distance", "2"); // National
		assertValue("distance", "2");
		assertValue("vehicle", "CAR");
		assertValue("transportMode", "HIGHWAY");
		assertValue("driverType", "DRIVER");
		setValue("distance", "0"); // Void
		assertValue("distance", "0");
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
		setValue("deliveredBy", "2");
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
			{"0", ""},
			{"1", "Lokal"},			
			{"2", "Nachional"}, 
			{"3", "Internachional"}
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
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(new java.util.Date());
	}
	
	private String getCurrentYear() {
		DateFormat df = new SimpleDateFormat("yyyy");
		return df.format(new java.util.Date());
	}

	private String getYearDiscount(String syear) throws Exception {
		int year = Integer.parseInt(syear);
		YearInvoiceDiscountCalculator calculator = new YearInvoiceDiscountCalculator();
		calculator.setYear(year);
		BigDecimal bd = (BigDecimal) calculator.calculate();
		return bd.setScale(0, BigDecimal.ROUND_DOWN).toString();
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

}
