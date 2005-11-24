package org.openxava.test.tests;

import java.net.*;

import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;

import com.meterware.httpunit.*;

/**
 * @author Javier Paniza
 */

public class CustomersTest extends ModuleTestBase {
	
	private String section;		

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
		"Customers.hideSellerInList",
		"Customers.showSellerInList"
	};
	
	
	public CustomersTest(String testName) {
		super(testName, "OpenXavaTest", "Customers");
		section = "";				
	}
	
	public CustomersTest(String testName, String moduleName, boolean section) {
		super(testName, "OpenXavaTest", moduleName);		
		this.section = section?"_section0":"";		
	}
		
	public void testObtainAggregateValues() throws Exception {
		String city = getValueInList(0, "address.city");
		assertTrue("Value for city in first customer is required for run this test", !Is.emptyString(city));
		execute("Mode.detailAndFirst");
		assertValue("address.city", city);
	}
	
	public void testChangeLabelProgrammatic() throws Exception {
		execute("CRUD.new");
		assertLabel("name", "Name");
		execute("Customers.changeNameLabel");
		assertLabel("name", "Malnom");
	}
		
	public void testFilterByMemberOfAggregate() throws Exception {
		String [] totalCondition = { "", "", "", "V" };		
		setConditionValues(totalCondition);		
		execute("List.filter");
		assertNoErrors(); // At momment only testing if crash
	}
	
	public void testChangeView() throws Exception {
		execute("CRUD.new");
		assertExists("number");
		assertExists("remarks");
		execute("Customers.changeToSimpleView");
		assertExists("number");
		assertNotExists("remarks");
	}
	
	public void testOnChangePropertyOfReferenceWithMultipleKeyAsListDescriptionInAggregateOfCollection() throws Exception {
		execute("CRUD.new");		
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces");		
		assertValue("deliveryPlaces.remarks", "");
		WarehouseKey warehouseKey = new WarehouseKey();
		warehouseKey.set_Number(new Integer(1));
		warehouseKey.setZoneNumber(1); 
		setValue("deliveryPlaces.preferredWarehouse.KEY", warehouseKey.toString());
		assertValue("deliveryPlaces.remarks", "PREFERRED WAREHOUSE IS 1");
	}
		
	public void testViewGetValueInGroup() throws Exception {
		execute("CRUD.new");
		assertValue("remarks", "");
		setValue("relationWithSeller", "RELATION WITH SELLER JUNIT");
		execute("Customers.moveRelationWithSellerToRemarks");
		assertNoErrors();
		assertValue("remarks", "RELATION WITH SELLER JUNIT");
	}
	
	public void testFilterByValidValues() throws Exception {		
		int total = CustomerUtil.getHome().findAll().size();
		int normalOnes = CustomerUtil.getHome().findNormalOnes().size();
		int steadyOnes = CustomerUtil.getHome().findSteadyOnes().size();
		assertTrue("It is required customers for run this test", total > 0);
		assertTrue("It is required normal customers for run this test", normalOnes > 0);
		assertTrue("It is required steady customers for run this test", steadyOnes > 0);
		assertTrue("It is required at least 10 customers to run this test", total < 10);
		assertListRowCount(total);
						
		String [] normalCondition = { " ", "1", "", ""	};
		setConditionValues(normalCondition);
		execute("List.filter");
		assertNoErrors();
		
		assertListRowCount(normalOnes);
				
		String [] steadyCondition = { " ", "2", "", "" };
		setConditionValues(steadyCondition);		
		execute("List.filter");
		assertNoErrors();
		assertListRowCount(steadyOnes);		
		
		String [] totalCondition = { "", "", "", "" };		
		setConditionValues(totalCondition);		
		execute("List.filter");
		assertNoErrors();
		assertListRowCount(total);						
	}
	
		
	public void testImageEditor() throws Exception { 		
		execute("CRUD.new");
		execute("ImageEditor.changeImage", "newImageProperty=foto");
		assertNoErrors();
		assertAction("LoadImage.loadImage");		
		String imageUrl = System.getProperty("user.dir") + "/test-images/foto_javi.jpg";
		setFileValue("newImage", imageUrl);
		execute("LoadImage.loadImage");
		assertNoErrors();
		
		WebResponse response = getConversation().getCurrentPage();
		URL url = response.getURL();
		String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		
		WebImage image = response.getImageWithName("xava.Customer.photo");
		String imageURL = null;
		if (image.getSource().startsWith("/")) {
			imageURL = urlPrefix + image.getSource();
		}
		else {
			String urlBase = Strings.noLastToken(url.getPath(), "/");
			imageURL = urlPrefix + urlBase + image.getSource();
		}
		response = getConversation().getResponse(imageURL);
		assertEquals("Image not obtained", 0, response.getContentLength());		
	}
			
	public void testHideShowGroup() throws Exception {		
		execute("CRUD.new");
		assertExists("seller.number");
		assertExists("seller.name");
		assertExists("relationWithSeller");
		
		execute("Customers.hideSeller");
		assertNotExists("seller.number");
		assertNotExists("seller.name");
		assertNotExists("relationWithSeller");
		
		execute("Customers.showSeller");
		assertExists("seller.number");
		assertExists("seller.name");
		assertExists("relationWithSeller");
	}
	

	public void testSearchReferenceWithAListInAGroup() throws Exception {
		execute("CRUD.new");		
		assertValue("seller.name", "");
		execute("MyReference.search", "keyProperty=xava.Customer.seller.number");
		execute("MyReference.yes");
		String sellerName = getValueInList(0, 1);
		execute("ReferenceSearch.choose", "row=0");
		assertTrue("The name of first seller can't be empty string", sellerName.trim().length() > 0);		
		assertValue("seller.name", sellerName);
		assertNoAction("MyReference.yes"); // to sure that not returns to MyReference controller
	}

	public void testValidValues() throws Exception {   				
		execute("CRUD.new");
		String [][] validValues = {
			{ "0", "" },
			{ "1", "Normal" },
			{ "2", "Steady" },
			{ "3", "Special" }	
		};
		
		assertValue("type", "1");		
		assertValidValues("type", validValues);
	}
	
	public void testOnChangeAction() throws Exception {   				
		execute("CRUD.new");		
		assertValue("type", "1");		
		setValue("name", "PEPE");
		assertValue("type", "1");
		setValue("name", "JAVI");
		assertValue("type", "2");		
	}	
		
	
	// references to entities and aggregates
	public void testCreateModifyAndReadWithReferencesAndOverwriteSaveAction() throws Exception {				
		// Create one new
		execute("CRUD.new");
		assertNoErrors();
		setValue("number", "66");
		setValue("type", "2");
		setValue("name", "JUNIT CUSTOMER");
		setValue("address.street", "Junit Street");
		setValue("address.zipCode", "66666");
		setValue("address.city", "POBLE JUNIT PER A J");
		setValue("address.state.id", "NY");
		setValue("seller.number", "1");
		setValue("relationWithSeller", "A RELATION");
		setValue("alternateSeller.number", "2");		
		execute("Customers.save");  		
		assertNoErrors();

		assertNoEditable("number");
		assertEditable("type");		
		assertValue("number", "66");
		assertValue("type", "2");
		assertValue("name", "Junit Customer"); // Testing formatter with sets
		assertValue("address.street", "JUNIT STREET"); // Testing overwrite default formatter for applicacion. Use UpperCaseFormatter
		assertValue("address.zipCode", "66666");
		assertValue("address.city", "POBLE JUNIT PER A J");
		assertValue("address.state.id", "NY");
		assertValue("seller.number", "1");
		assertValue("relationWithSeller", "A RELATION");
		assertValue("alternateSeller.number", "2");						

		execute("CRUD.new"); 
		assertValue("number", "");
		assertValue("type", "1");
		assertValue("name", "");
		assertValue("address.street", "");
		assertValue("address.zipCode", "");
		assertValue("address.city", "");
		assertValue("address.state.id", "");		
		assertValue("seller.number", "");
		assertValue("seller.name", "");
		assertValue("relationWithSeller", "GOOD"); // default-value-calculator in a group
		assertValue("alternateSeller.number", "");
		assertValue("alternateSeller.name", "");		
		
		// Search just created
		setValue("number", "66");
		execute("CRUD.search");		
		assertValue("number", "66");
		assertValue("type", "2"); 
		assertValue("name", "Junit Customer"); // Testing formatter with sets
		assertValue("address.street", "JUNIT STREET"); // Testing overwrite default formatter for applicacion. Use UpperCaseFormatter
		assertValue("address.zipCode", "66666");
		assertValue("address.city", "POBLE JUNIT PER A J");
		assertValue("address.state.id", "NY");		
		assertValue("seller.number", "1");
		assertValue("seller.name", "MANUEL CHAVARRI");
		assertValue("relationWithSeller", "A RELATION");
		assertValue("alternateSeller.number", "2");
		assertValue("alternateSeller.name", "DON JUANVI LLAVADOR"); // the DON by a on-change
		
		// Modify
		setValue("seller.number", "2");
		execute("Customers.save");
		assertNoErrors();
		execute("CRUD.new");
		assertValue("number", "");
		assertValue("type", "1");
		assertValue("name", "");
		
		// Verifying modified
		execute("CRUD.new");
		setValue("number", "66");
		execute("CRUD.search");		
		assertValue("number", "66");
		assertValue("type", "2"); 
		assertValue("name", "Junit Customer");
		assertValue("seller.number", "2");
		assertValue("seller.name", "JUANVI LLAVADOR");
										
		// Delete it
		execute("CRUD.delete");													
		assertNoErrors();
		assertMessage("Customer deleted successfully");
	}
		
	public void testSearchReferenceOnChangeCodeAndOnChangeActionInSubview() throws Exception {
		execute("CRUD.new");
		setValue("seller.number", "1");
		assertValue("seller.number", "1");
		assertValue("seller.name", "MANUEL CHAVARRI");
		assertNoErrors();
		setValue("seller.number", "2");
		assertValue("seller.number", "2");
		assertValue("seller.name", "JUANVI LLAVADOR");
		assertNoErrors();		
		setValue("seller.number", "");
		assertValue("seller.number", "");
		assertValue("seller.name", "");
		assertNoErrors();				
		setValue("alternateSeller.number", "2");
		assertValue("alternateSeller.number", "2");
		assertValue("alternateSeller.name", "DON JUANVI LLAVADOR");
		assertNoErrors();						
	}
	
	public void testSearchReferenceWithListAndOnChangeActionInSubview() throws Exception {
		execute("CRUD.new");		
		assertValue("alternateSeller.number", "");
		assertValue("alternateSeller.name", "");
		// Choose using check boxes
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		checkRow(0);
		execute("ReferenceSearch.choose");
		assertValue("alternateSeller.number", "1");
		assertValue("alternateSeller.name", "DON MANUEL CHAVARRI");
				
		// Choose using link		
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		execute("ReferenceSearch.choose", "row=1");
		assertValue("alternateSeller.number", "2");
		assertValue("alternateSeller.name", "DON JUANVI LLAVADOR");
		
		// Canceling
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		// It is checked the 0 of other time
		execute("ReferenceSearch.cancel");
		assertValue("alternateSeller.number", "2");
		assertValue("alternateSeller.name", "DON JUANVI LLAVADOR");								
	}
	
	public void testCustomSearchReferenceAction() throws Exception {
		execute("CRUD.new");
		String html = getHtml();
		assertTrue("Search of 'seller' should be 'MyReference.search'", html.indexOf("'MyReference.search', 'keyProperty=xava.Customer.seller.number'") > 0);
		assertTrue("Search 'alternateSeller' should be 'Reference.search'", html.indexOf("'Reference.search', 'keyProperty=xava.Customer.alternateSeller.number'") > 0);
	}
	
	public void testReferencesIfBlankKeyOrNotExists() throws Exception {
		execute("CRUD.new");
		setValue("seller.number", "1");
		assertValue("seller.name", "MANUEL CHAVARRI");
		setValue("seller.number", "");
		assertValue("seller.name", "");
		setValue("seller.number", "1");
		assertValue("seller.name", "MANUEL CHAVARRI");
		setValue("seller.number", "907"); // We suposse that it not exists 
		assertValue("seller.name", "");									
	}
	
	public void testLeftJoinInListModeForReference() throws Exception {
		assertActions(listActions);
		int initialRows = getListRowCount();
		assertTrue("This test only run with less than 10 rows", initialRows < 10);
		
		// Create
		execute("CRUD.new");
		setValue("number", "66");
		setValue("type", "1");
		setValue("name", "JUNIT CUSTOMER");
		setValue("address.street", "JUNIT STREET");
		setValue("address.zipCode", "66666");
		setValue("address.city", "POBLE JUNIT PER A J");
		setValue("address.state.id", "NY");
		execute("Customers.save");
		assertNoErrors();
		
		// Verifying that it is in the list althought it has not seller
		execute("Mode.list");
		assertActions(listActions);
		assertListRowCount(initialRows + 1);		
		
		// Search just created
		execute("CRUD.new");
		setValue("number", "66");
		execute("CRUD.search");		
		assertValue("number", "66");
		assertValue("type", "1"); 
		assertValue("name", "Junit Customer");
		assertValue("address.street", "JUNIT STREET");
		assertValue("address.zipCode", "66666");
		assertValue("address.city", "POBLE JUNIT PER A J");
		assertValue("address.state.id", "NY");		
		assertValue("seller.number", "");
		assertValue("seller.name", "");
		assertValue("relationWithSeller", "GOOD"); // default-value-calculator in a group
		assertValue("alternateSeller.number", "");
		assertValue("alternateSeller.name", "");
		
		// Delete it
		execute("CRUD.delete");											
		assertNoErrors();
		assertMessage("Customer deleted successfully");
	}
	
	public void testIfKeyNotExistsInReferenceNotExecuteAction() throws Exception {
		execute("CRUD.new");
		setValue("relationWithSeller", "HOLA");
		setValueNotNotify("seller.number", "53"); // We suposse that not exists		
		click("CRUD.new");
		assertError("Seller with key {number=53} not found");
		assertValue("relationWithSeller", "HOLA"); // That implies that 'new' not was executed
	}
	
	public void testPropertiesOfEntityReferenceAndAggregateInList() throws Exception {
		setConditionValues(new String [] { "JAVI", "" });
		execute("List.filter");
		assertListRowCount(1);
		
		assertValueInList(0, 0, "Javi");
		assertValueInList(0, 2, "MANUEL CHAVARRI"); // property of reference to entity
		assertValueInList(0, 3, "EL PUIG"); // property of reference to aggregate
		assertValueInList(0, 4, "MANAGER"); // reference with 2 levels of indirection
		assertValueInList(0, 5, "NEW YORK"); // property of a reference inside an aggregate
	}
	
	public void testNestedAggregateCollections() throws Exception {				
		// Creating
		execute("CRUD.new");
		setValue("number", "66");
		setValue("type", "1");
		setValue("name", "JUNIT CUSTOMER");
		setValue("address.street", "JUNIT STREET");
		setValue("address.zipCode", "66666");
		setValue("address.city", "POBLE JUNIT PER A J");
		setValue("address.state.id", "NY");
				
		execute("Collection.new", "viewObject=xava_view"  + getSection() + "_deliveryPlaces");
		setValue("deliveryPlaces.name", "DELIVERY JUNIT 1");
		setValue("deliveryPlaces.address", "STREET JUNIT 1");		
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		setValue("deliveryPlaces.receptionists.name", "RECEPTIONISTS JUNIT 1 - 1");		
		execute("Collection.save", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		assertNoErrors();
		
		assertNoEditable("number"); // Header is saved hence it si not editable
		
		assertCollectionRowCount("deliveryPlaces", 1);
		assertValueInCollection("deliveryPlaces", 0, 0, "DELIVERY JUNIT 1");
		assertValueInCollection("deliveryPlaces", 0, 1, "STREET JUNIT 1");
		
		assertCollectionRowCount("deliveryPlaces.receptionists", 1);
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 1 - 1");
				
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		setValue("deliveryPlaces.receptionists.name", "RECEPTIONISTS JUNIT 1 - 2");		
		execute("Collection.save", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		
		assertCollectionRowCount("deliveryPlaces", 1);
		assertCollectionRowCount("deliveryPlaces.receptionists", 2);
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 1 - 1");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "RECEPTIONISTS JUNIT 1 - 2");
		
		execute("Collection.save", "viewObject=xava_view" + getSection() + "_deliveryPlaces");
		
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces");
		setValue("deliveryPlaces.name", "DELIVERY JUNIT 2");
		setValue("deliveryPlaces.address", "STREET JUNIT 2");		
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		setValue("deliveryPlaces.receptionists.name", "RECEPTIONISTS JUNIT 2 - 1");				
		execute("Collection.save", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		execute("Collection.new", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		setValue("deliveryPlaces.receptionists.name", "RECEPTIONISTS JUNIT 2 - 2");				
		execute("Collection.save", "viewObject=xava_view" + getSection() + "_deliveryPlaces_receptionists");
		
		assertCollectionRowCount("deliveryPlaces", 2);
		assertValueInCollection("deliveryPlaces", 0, 0, "DELIVERY JUNIT 1");
		assertValueInCollection("deliveryPlaces", 0, 1, "STREET JUNIT 1");
		assertValueInCollection("deliveryPlaces", 1, 0, "DELIVERY JUNIT 2");
		assertValueInCollection("deliveryPlaces", 1, 1, "STREET JUNIT 2");
				
		assertCollectionRowCount("deliveryPlaces.receptionists", 2);
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 2 - 1");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "RECEPTIONISTS JUNIT 2 - 2");
		
		execute("Collection.edit", "row=0,viewObject=xava_view" + getSection() + "_deliveryPlaces");
		
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 1 - 1");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "RECEPTIONISTS JUNIT 1 - 2");
				
		// Search
		execute("CRUD.new");
		assertCollectionRowCount("deliveryPlaces", 0);
		setValue("number", "66");
		execute("CRUD.search");
		
		assertCollectionRowCount("deliveryPlaces", 2);
		assertValueInCollection("deliveryPlaces", 0, 0, "DELIVERY JUNIT 1");
		assertValueInCollection("deliveryPlaces", 0, 1, "STREET JUNIT 1");
		assertValueInCollection("deliveryPlaces", 1, 0, "DELIVERY JUNIT 2");
		assertValueInCollection("deliveryPlaces", 1, 1, "STREET JUNIT 2");

		execute("Collection.edit", "row=0,viewObject=xava_view" + getSection() + "_deliveryPlaces");
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 1 - 1");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "RECEPTIONISTS JUNIT 1 - 2");
		
		execute("Collection.edit", "row=1,viewObject=xava_view" + getSection() + "_deliveryPlaces");
		assertValueInCollection("deliveryPlaces.receptionists", 0, 0, "RECEPTIONISTS JUNIT 2 - 1");
		assertValueInCollection("deliveryPlaces.receptionists", 1, 0, "RECEPTIONISTS JUNIT 2 - 2");
		
		// Delete
		execute("CRUD.delete");												
		assertNoErrors();
		assertMessage("Customer deleted successfully");
	}

	public void testSetEditableOfReferences() throws Exception {
		execute("Mode.detailAndFirst");
		assertEditable("address.street");
		assertEditable("seller.number");
		assertNoEditable("seller.name");
		assertAction("Reference.search");
		
		execute("EditableOnOff.setOff");
		assertNoEditable("address.street");
		assertNoEditable("seller.number");
		assertNoEditable("seller.name");
		assertNoAction("Reference.search");
		
		execute("EditableOnOff.setOn");
		assertEditable("address.street");
		assertEditable("seller.number");
		assertNoEditable("seller.name");
		assertAction("Reference.search");					
	}
	
	public void testFocus() throws Exception {
		// Focus in first active
		execute("Mode.detailAndFirst");
		assertFocusOn("type");
		execute("CRUD.new");
		assertFocusOn("number");
		
		// Focus not move when we execute actions
		execute("Customers.hideSeller");
		assertFocusOn("number");
		execute("Customers.showSeller");
		assertFocusOn("number");
		
		// Propety changed that produce submit do advance the focus
		setValue("seller.number", "1");
		assertFocusOn("relationWithSeller");
	}
	
	
	private String getSection() {
		return section;		
	}
	
}
