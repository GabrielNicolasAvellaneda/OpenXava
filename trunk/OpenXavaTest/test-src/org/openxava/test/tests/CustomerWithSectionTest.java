package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */

public class CustomerWithSectionTest extends CustomerTest {

	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.orderBy",
		"List.viewDetail",
		"List.customize",
		"List.hideRows",
		"Customer.hideSellerInList",
		"Customer.showSellerInList"
	};

	private String [] listCustomizeActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.orderBy",
		"List.viewDetail",
		"List.customize",
		"List.addColumns",
		"List.moveColumnToLeft",
		"List.moveColumnToRight",
		"List.removeColumn",
		"List.hideRows",
		"Customer.hideSellerInList",
		"Customer.showSellerInList"
	};
	

	public CustomerWithSectionTest(String testName) {
		super(testName, "CustomerWithSection", true);		
	}
		
	public void testTELEPHONE_EMAIL_WEBURLstereotypes() throws Exception {
		execute("Mode.detailAndFirst");
		setValue("telephone", "asf");
		setValue("email", "pepe");
		setValue("website", "openxava");
		execute("Customer.save");		
		assertError("Telephone must be a valid number");
		assertError("eMail must be a valid email address");
		assertError("Web site must be a valid url");
		setValue("telephone", "123");
		setValue("email", "pepe@mycompany");
		setValue("website", "www.openxava.org");
		execute("Customer.save");
		assertError("Telephone must be at least 8 Digits long");
		assertError("eMail must be a valid email address");
		assertError("Web site must be a valid url");
		assertValue("email", "pepe@mycompany"); // not converted to uppercase
		assertValue("website", "www.openxava.org"); // not converted to uppercase
		setValue("telephone", "961112233");
		setValue("email", "pepe@mycompany.com");
		setValue("website", "http://www.openxava.org");
		execute("Customer.save");
		assertNoErrors();
	}
	
	public void testOrderAndFilterInNestedCollection() throws Exception {
		execute("CRUD.new");
		setValue("number", "4");
		execute("CRUD.search");		
		assertValue("name", "Cuatrero");
		
		assertCollectionRowCount("deliveryPlaces", 1);
		execute("Collection.edit", "row=0,viewObject=xava_view_section0_deliveryPlaces");
		
		assertCollectionRowCount("deliveryPlaces.receptionists", 2);
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "JUAN");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "PEPE");
		
		execute("List.orderBy", "property=name,collection=deliveryPlaces.receptionists");
		execute("List.orderBy", "property=name,collection=deliveryPlaces.receptionists");
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "PEPE");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "JUAN");

		setConditionValues("deliveryPlaces.receptionists", new String[] { "J"} );
		execute("List.filter", "collection=deliveryPlaces.receptionists");
		assertCollectionRowCount("deliveryPlaces.receptionists", 1);
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "JUAN");				
	}
	
	public void testModifyFromReference() throws Exception {
		execute("CRUD.new");
		execute("Reference.modify", "model=Seller,keyProperty=xava.Customer.seller.number");
		assertError("Impossible to modify an empty reference");
		setValue("number", "1");
		execute("CRUD.search");		
		assertValue("name", "Javi");
		assertValue("seller.name", "MANUEL CHAVARRI");
		execute("Reference.modify", "model=Seller,keyProperty=xava.Customer.seller.number");		
		assertValue("Seller", "number", "1");
		assertValue("Seller", "name", "MANUEL CHAVARRI");
		execute("Modification.cancel");
		assertValue("seller.name", "MANUEL CHAVARRI");
		execute("Reference.modify", "model=Seller,keyProperty=xava.Customer.seller.number");
		assertValue("Seller", "number", "1");
		assertValue("Seller", "name", "MANUEL CHAVARRI");
		setValue("Seller", "name", "MANOLO");
		execute("Modification.update");
		assertValue("seller.name", "MANOLO");
		execute("Reference.modify", "model=Seller,keyProperty=xava.Customer.seller.number");
		setValue("Seller", "name", "MANUEL CHAVARRI");
		execute("Modification.update");
		assertValue("seller.name", "MANUEL CHAVARRI");
	}
	
	public void testChooseInReferenceWithoutSelecting() throws Exception {
		execute("CRUD.new");
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		execute("ReferenceSearch.choose");
		assertNoErrors();
		assertAction("ReferenceSearch.choose"); // Because no row is selected we keep in searching list
	}
	
	public void testCustomizeReferenceListDoesNotReturnToListModeOfModule() throws Exception {
		execute("CRUD.new");
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		assertListColumnCount(3);
		execute("List.customize");
		execute("List.addColumns");
		execute("AddColumns.restoreDefault");
		assertListColumnCount(3); // To test that it's still is the tab of sellers, not the customer's one
	}
	
	public void testDefaultValidator() throws Exception {
		execute("CRUD.new");
		setValue("name", "x");
		execute("Customer.save");
		assertNoError("Person name MAKARIO is not allowed for Name in Customer");
		setValue("name", "MAKARIO");
		execute("Customer.save");
		assertError("Person name MAKARIO is not allowed for Name in Customer");
	}
	
	public void testCreatedFromReferenceIsChosenAndThrowsOnChange() throws Exception {
		execute("CRUD.new");
		execute("Reference.createNew", "model=Seller,keyProperty=xava.Customer.alternateSeller.number");
		setValue("Seller", "number", "66");
		setValue("Seller", "name", "SELLER JUNIT 66");
		execute("NewCreation.saveNew");
		assertNoErrors();
		assertValue("alternateSeller.number", "66");
		assertValue("alternateSeller.name", "DON SELLER JUNIT 66"); // The 'DON' is added by an on-change action
		deleteSeller(66);
	}	
	
	private void deleteSeller(int number) throws Exception {
		XPersistence.getManager().remove(XPersistence.getManager().find(Seller.class, number));				
	}

	public void testPropertyAction() throws Exception { 
		execute("CRUD.new");
		setValue("address.street", "DOCTOR PESSET");
		assertValue("address.street", "DOCTOR PESSET");
		execute("Customer.prefixStreet", "xava.keyProperty=xava.Customer.address.street");
		assertValue("address.street", "C/ DOCTOR PESSET");
	}
	
	public void testManyToManyCollection() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("states", 0);
		
		if (isOX3()) {
			// In OX3 ManyToMany is supported, then we have a collection of entities
			execute("Collection.add", "viewObject=xava_view_section1_states");
			assertValueInList(0, 0, "AK");
			assertValueInList(4, 0, "CA");
			checkRow(0);
			checkRow(4);
			execute("AddToCollection.add");
		}
		else {
			// In OX2 many to many is not supported, we simulate it using a collection of aggregates,
			// therefore the User Interface it's not the same (because it's a collection of aggragates)
			execute("Collection.new", "viewObject=xava_view_section1_states");
			setValue("states.state.id", "AK");
			assertValue("states.state.name", "ALASKA");
			execute("Collection.save", "viewObject=xava_view_section1_states");						
			setValue("states.state.id", "CA");
			assertValue("states.state.name", "CALIFORNIA");
			execute("Collection.save", "viewObject=xava_view_section1_states");			
		}
		
		assertCollectionRowCount("states", 2);
		assertValueInCollection("states", 0, 0, "AK");
		assertValueInCollection("states", 0, 1, "ALASKA");		
		assertValueInCollection("states", 1, 0, "CA");
		assertValueInCollection("states", 1, 1, "CALIFORNIA");
		// Using Edit + Remove
		execute("Collection.edit", "row=0,viewObject=xava_view_section1_states");
		execute("Collection.remove", "viewObject=xava_view_section1_states");
		assertCollectionRowCount("states", 1);
		// Using Check row + Remove selected
		checkRowCollection("states", 0);
		execute("Collection.removeSelected", "viewObject=xava_view_section1_states");
		assertNoErrors();
		assertCollectionRowCount("states", 0);
		
		// Verifying if that other part is not removed
		changeModule("StateHibernate");
		assertValueInList(0, 0, "AK");
		assertValueInList(4, 0, "CA");		
	}
	
	public void testChangeReferenceLabel() throws Exception {
		execute("CRUD.new");
		assertLabel("alternateSeller", "Alternate seller");
		execute("Customer.changeAlternateSellerLabel");
		assertLabel("alternateSeller", "Secondary seller");
	}
	
	public void testCustomizeList() throws Exception { 
		doTestCustomizeList_moveAndRemove();
		tearDown();	setUp();
		doTestCustomizeList_generatePDF();
		tearDown();	setUp();
		doTestRestoreColumns_addRemoveTabColumnsDynamically();
	}
	
	private void doTestCustomizeList_moveAndRemove() throws Exception {
		assertActions(listActions);
		execute("List.customize");		
		assertActions(listCustomizeActions);

		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City of Address");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address");
		assertTrue("It is needed customers for execute this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);
		String state = getValueInList(0, 5); 

		// move 2 to 3
		execute("List.moveColumnToRight", "columnIndex=2");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City of Address");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// try to move 5, it is the last, do nothing
		execute("List.moveColumnToRight", "columnIndex=5");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City of Address");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// move 3 to 2
		execute("List.moveColumnToLeft", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City of Address");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// try to move 0 to left, do nothing
		execute("List.moveColumnToLeft", "columnIndex=0");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City of Address");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 

		// remove column 3
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");		
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "Seller level");
		assertLabelInList(4, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, sellerLevel);
		assertValueInList(0, 4, state); 
						
		execute("List.customize");
		assertActions(listActions);
	}
	
	private void doTestCustomizeList_generatePDF() throws Exception {
		// Trusts in that testCustomizeList_moveAndRemove is executed before
		execute("List.customize");
		assertListColumnCount(5);
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(4);		
		execute("Print.generatePdf"); 
		assertContentTypeForPopup("application/pdf");
		
	}
		
	private void doTestRestoreColumns_addRemoveTabColumnsDynamically() throws Exception { 
		// Restoring initial tab setup
		execute("List.customize");
		execute("List.addColumns");							
		execute("AddColumns.restoreDefault");		
		// End restoring
		
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City of Address");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertTrue("Must to have customers for run this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);
		String state = getValueInList(0, 5); 
		
		execute("Customer.hideSellerInList");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City of Address");
		assertLabelInList(3, "Seller level");
		assertLabelInList(4, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, sellerLevel);
		assertValueInList(0, 4, state); 
		
		execute("Customer.showSellerInList");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");		
		assertLabelInList(3, "City of Address");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State of Address"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
	}
	
	public void testCustomizeList_addAndResetModule() throws Exception { 
		assertListColumnCount(6);
		String value = getValueInList(0, 0);
		execute("List.customize");
		execute("List.addColumns");		
		checkRow("selectedProperties", "number"); 		
		execute("AddColumns.addColumns");
		assertListColumnCount(7);
		assertValueInList(0, 0, value);
				
		resetModule();
		assertListColumnCount(7);
		assertValueInList(0, 0, value);
		
		execute("List.customize");
		execute("List.removeColumn", "columnIndex=6");
		assertListColumnCount(6);
	}
	
	public void testRowStyle() throws Exception {
		int c = getListRowCount();
		boolean found = false;
		
		for (int i=0; i<c; i++) {
			String type = getValueInList(i, "type");
			if ("Steady".equals(type)) {				
				assertRowStyleInList(i, "highlight");				
				found = true;
			}
			else {
				assertNoRowStyleInList(i);				
			}						
		}
		if (!found) {
			fail("It is required at least one Customer of 'Steady' type for run this test");
		}
	}
	
	
	
}
