package org.openxava.test.tests;

import org.openxava.tests.*;

import com.gargoylesoftware.htmlunit.html.*;

/**
 * For testing that only the needed parts are reload by AJAX. <p>
 * 
 * Because this is very technology dependent we put all this test here
 * and not scattered for the module tests, in order to remove when 
 * we'll go with other presentation technology.<br> 
 * 
 * @author Javier Paniza
 */

public class AJAXTest extends ModuleTestBase {

	public AJAXTest(String nameTest) {
		super(nameTest, null);
	}
	
	public void testOnlyLoadModifiedParts() throws Exception {
		changeModule("Customer");
		assertLoadedParts("xava_core, ");
		execute("List.filter");
		assertLoadedParts("xava_errors, xava_view, xava_messages, ");
		execute("Mode.detailAndFirst");
		assertLoadedParts("xava_core, ");
		execute("Navigation.next");
		assertLoadedParts("xava_editor_xava.Customer.type, xava_errors, " +
				"xava_editor_xava.Customer.number, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.alternateSeller.name, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.website, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_editor_xava.Customer.email, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.photo, xava_messages, ");
		setValue("seller.number", "2");
		// First time email, telephone and website are reloaded because its value in the DB is
		// null, and the value obtained from view is empty string
		// viewProperty is a transient property, so the first time that its values is null
		assertLoadedParts("xava_editor_xava.Customer.website, xava_errors, " +
				"xava_editor_xava.Customer.seller.name, " +
				"xava_editor_xava.Customer.email, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.address.viewProperty, xava_messages, ");
		setValue("seller.number", "1");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.seller.name, " +
				"xava_editor_xava.Customer.seller.number, xava_messages, ");
		execute("CRUD.new");
		assertLoadedParts("xava_editor_xava.Customer.number, xava_errors, " +
				"xava_editor_xava.Customer.seller.name, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_editor_xava.Customer.alternateSeller.name, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.address.viewProperty, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.website, " +
				"xava_editor_xava.Customer.remarks, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_editor_xava.Customer.email, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_collection_xava.Customer.deliveryPlaces., xava_messages, ");
		setValue("number", "4");
		execute("CRUD.search");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.number, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_editor_xava.Customer.remarks, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.photo, xava_messages, ");
		
		// Collections
		execute("List.orderBy", "property=name,collection=deliveryPlaces");
		// First time seller, alternateSelle, email, telephone and website are 
		// reloaded because its value in the DB is null, and the value obtained 
		// from view is empty string
		// viewProperty is a transient property, so the first time that its values is null 
		assertLoadedParts("xava_editor_xava.Customer.website, xava_errors, " +
				"xava_editor_xava.Customer.seller.name, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.email, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.alternateSeller.name, " +
				"xava_editor_xava.Customer.address.viewProperty, xava_messages, ");
		execute("List.orderBy", "property=name,collection=deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., xava_messages, ");
		execute("List.filter", "collection=deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., xava_messages, ");
		execute("Collection.new", "viewObject=xava_view_deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., xava_messages, ");
		execute("Collection.edit", "row=0,viewObject=xava_view_deliveryPlaces");		
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., xava_messages, ");		
		execute("List.orderBy", "property=name,collection=deliveryPlaces.receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., xava_messages, ");
		execute("List.filter", "collection=deliveryPlaces.receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., xava_messages, ");
		execute("Collection.new", "viewObject=xava_view_deliveryPlaces_receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., xava_messages, ");
		execute("Collection.edit", "row=0,viewObject=xava_view_deliveryPlaces_receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., xava_messages, ");
		
		// Hide/show members
		execute("Customer.hideSeller");
		assertLoadedParts("xava_errors, xava_view, xava_messages, ");
		execute("Customer.showSeller");
		assertLoadedParts("xava_errors, xava_view, xava_messages, ");		
	}
	
	public void testDescriptionsList() throws Exception {
		changeModule("Customer");
		execute("Mode.detailAndFirst");
		assertValue("number", "1");
		assertDescriptionValue("address.state.id", "New York");
		
		execute("Navigation.next");
		assertValue("number", "2");
		assertDescriptionValue("address.state.id", "Colorado");
		assertLoadedPart("xava_descriptions_list_xava.Customer.address.state");
		
		execute("Navigation.next");
		assertValue("number", "3");
		assertDescriptionValue("address.state.id", "New York");
		assertLoadedPart("xava_descriptions_list_xava.Customer.address.state");
		
		execute("Navigation.next");
		assertValue("number", "4");
		assertDescriptionValue("address.state.id", "New York");
		assertNotLoadedPart("xava_descriptions_list_xava.Customer.address.state");
		
		execute("Navigation.next");
		assertValue("number", "43");
		assertDescriptionValue("address.state.id", "Kansas");
		assertLoadedPart("xava_descriptions_list_xava.Customer.address.state");				
	}
			
	public void testDependentDescriptionsList() throws Exception { 
		changeModule("Product2"); 
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		setValue("family.number", "1");
		setValue("family.number", "2");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
				"xava_descriptions_list_xava.Product2.subfamily, xava_messages, ");
		setValue("family.number", "1");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
			"xava_descriptions_list_xava.Product2.subfamily, xava_messages, ");
	}
		
	public void	testShowingHiddingPartsReloadsFullView() throws Exception {
		changeModule("Product2"); 
		execute("Mode.detailAndFirst");
		assertNotExists("zoneOne");
		assertLoadedParts("xava_core, ");
		execute("Navigation.next");
		assertExists("zoneOne");
		assertLoadedParts("xava_errors, xava_view, xava_messages, ");
		execute("Navigation.next");
		assertExists("zoneOne");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
				"xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_editor_xava.Product2.photos, " +
				"xava_editor_xava.Product2.unitPriceInPesetas, " +
				"xava_descriptions_list_xava.Product2.warehouse, " +
				"xava_editor_xava.Product2.description, " +
				"xava_editor_xava.Product2.number, " +
				"xava_messages, xava_editor_xava.Product2.unitPrice, ");
		execute("Navigation.next");
		assertNotExists("zoneOne");
		assertLoadedParts("xava_errors, xava_view, xava_messages, ");
	}
	
	public void testFirstDetailActionExecutingLoadAllButOnlyFirstTime() throws Exception {
		changeModule("Carrier"); 		
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		// Very first time all is reloaded. It would nice optimized it.
		assertLoadedParts("xava_editor_xava.Carrier.warehouse.number, xava_errors, " +
				"xava_editor_xava.Carrier.number, " +
				"xava_editor_xava.Carrier.warehouse.zoneNumber, " +
				"xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_editor_xava.Carrier.name, " +
				"xava_descriptions_list_xava.Carrier.drivingLicence, " +
				"xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_editor_xava.Carrier.fellowCarriersSelected, " +
				"xava_messages, xava_editor_xava.Carrier.calculated, " +
				"xava_editor_xava.Carrier.remarks, " +
				"xava_editor_xava.Carrier.warehouse.name, ");
		execute("Mode.list");
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		assertLoadedParts("xava_errors, xava_messages, "); // Perfect: From now on only the needed parts are reloaded
	}
		
	public void testCollections() throws Exception {
		changeModule("Carrier"); 
		execute("Mode.detailAndFirst");
		assertCollectionRowCount("fellowCarriers", 3);
		setValue("warehouse.number", "2");
		assertCollectionRowCount("fellowCarriers", 0);
		setValue("warehouse.number", "1");
		assertCollectionRowCount("fellowCarriers", 3);
		assertLoadedParts("xava_editor_xava.Carrier.warehouse.number, " +
				"xava_errors, xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_messages, xava_editor_xava.Carrier.warehouse.name, ");
		
		execute("Collection.edit", "row=0,viewObject=xava_view_fellowCarriersCalculated");
		assertLoadedParts("xava_errors, xava_collection_xava.Carrier.fellowCarriersCalculated., xava_messages, ");		
		
		execute("Navigation.next");
		assertLoadedParts("xava_errors, xava_editor_xava.Carrier.number, " +
				"xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_editor_xava.Carrier.fellowCarriersSelected, " +
				"xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_editor_xava.Carrier.name, xava_messages, " +
				"xava_editor_xava.Carrier.remarks, ");
		
		execute("Carrier.translateAll"); // The first time more changes because null in some db columns
		execute("Carrier.translateAll");
		assertLoadedParts("xava_errors, xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_messages, ");
	}

	private void assertLoadedParts(String expected) throws Exception {
		assertEquals("Loaded parts are not the expected ones", expected, getLoadedParts());
	}
	
	private void assertLoadedPart(String expected) throws Exception {
		assertTrue("Loaded part not found", getLoadedParts().indexOf(expected + ",") >= 0);
	}
	
	private void assertNotLoadedPart(String expected) throws Exception {
		assertTrue("Loaded part found", getLoadedParts().indexOf(expected + ",") < 0);
	}
		
	private String getLoadedParts() {
		HtmlPage page = (HtmlPage) getWebClient().getCurrentWindow().getEnclosedPage();
		HtmlInput input = (HtmlInput) page.getHtmlElementById("xava_loaded_parts");
		return input.getValueAttribute();
	}

}
