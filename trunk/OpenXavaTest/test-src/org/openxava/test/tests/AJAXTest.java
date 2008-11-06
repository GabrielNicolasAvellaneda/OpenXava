package org.openxava.test.tests;

import java.util.*;

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
	
	public void testCollectionsInsidReferences() throws Exception {
		changeModule("Office");
		execute("CRUD.new");
		assertCollectionRowCount("defaultCarrier.fellowCarriers", 0);
		assertCollectionRowCount("defaultCarrier.fellowCarriersCalculated", 0);
		setValue("defaultCarrier.number", "1");
		assertCollectionRowCount("defaultCarrier.fellowCarriers", 3);
		assertCollectionRowCount("defaultCarrier.fellowCarriersCalculated", 3);		
		assertLoadedParts("xava_collection_xava.Office.defaultCarrier.fellowCarriers.," +
				"xava_collection_xava.Office.defaultCarrier.fellowCarriersCalculated.," +
				"xava_descriptions_list_xava.Office.defaultCarrier.drivingLicence," +
				"xava_editor_xava.Office.defaultCarrier.calculated," +
				"xava_editor_xava.Office.defaultCarrier.name," +
				"xava_editor_xava.Office.defaultCarrier.warehouse.name," +
				"xava_editor_xava.Office.defaultCarrier.warehouse.number," +
				"xava_editor_xava.Office.defaultCarrier.warehouse.zoneNumber," +
				"xava_editor_xava.Office.officeManager.arrivalTime," + // it's formatted
				"xava_editor_xava.Office.officeManager.endingTime," + // it's formatted
				"xava_editor_xava.Office.receptionist," + // it's formatted
				"xava_editor_xava.Office.zoneNumber," + // it's formatted
				"xava_errors, xava_focus_property_id, xava_messages,");		
	}
	
	public void testRefreshViewProperties() throws Exception { 
		changeModule("Customer");
		execute("CRUD.new");
		assertValue("address.viewProperty", "");
		execute("Address.fillViewProperty", "xava.keyProperty=xava.Customer.address.viewProperty");
		assertValue("address.viewProperty", "THIS IS AN ADDRESS");
		assertLoadedParts("xava_errors, " +
				"xava_editor_xava.Customer.address.viewProperty," +
				"xava_editor_xava.Customer.address.zipCode," + // Because it's formatted
				"xava_messages, xava_focus_property_id");		
	}
	
	public void testOnlyLoadModifiedParts() throws Exception {
		changeModule("Customer");
		assertLoadedParts("xava_core, ");
		execute("List.filter");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
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
				"xava_editor_xava.Customer.photo, xava_messages, " +
				"xava_focus_property_id");
		setValue("seller.number", "2");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.seller.name, " +
				"xava_focus_property_id, xava_messages,");
		setValue("seller.number", "1");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.seller.name, " +
			"xava_focus_property_id, xava_messages,");
		execute("Customer.changeNameLabel");
		assertLoadedParts("xava_label_xava.Customer.name, xava_errors, xava_messages, " +
				"xava_focus_property_id");
		execute("CRUD.new");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.seller.name, " +
				"xava_editor_xava.Customer.number, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.alternateSeller.name, " +
				"xava_focus_property_id, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_collection_xava.Customer.deliveryPlaces., xava_messages,");
		setValue("number", "4");
		execute("CRUD.search");
		assertLoadedParts("xava_errors, xava_editor_xava.Customer.number, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.relationWithSeller, " +				
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.photo, xava_messages, " +
				"xava_focus_property_id");		
		// Collections
		execute("List.orderBy", "property=name,collection=deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_messages, xava_focus_property_id");		
		execute("List.orderBy", "property=name,collection=deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_messages, xava_focus_property_id");
		execute("List.filter", "collection=deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_messages, xava_focus_property_id");
		execute("Collection.new", "viewObject=xava_view_deliveryPlaces");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_messages, xava_focus_property_id");
		execute("Collection.edit", "row=0,viewObject=xava_view_deliveryPlaces");		
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_messages, xava_focus_property_id");
		execute("List.orderBy", "property=name,collection=deliveryPlaces.receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., " +
				"xava_messages, xava_focus_property_id");
		execute("List.filter", "collection=deliveryPlaces.receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., " +
				"xava_messages, xava_focus_property_id");
		execute("Collection.new", "viewObject=xava_view_deliveryPlaces_receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., " +
				"xava_messages, xava_focus_property_id");
		execute("Collection.edit", "row=0,viewObject=xava_view_deliveryPlaces_receptionists");
		assertLoadedParts("xava_errors, xava_collection_xava.Customer.deliveryPlaces.receptionists., " +
				"xava_messages, xava_focus_property_id");
		
		// Hide/show members
		execute("Customer.hideSeller");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
		execute("Customer.showSeller");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
		
		// Actions of properties are hidden when editable state changes
		assertAction("Customer.changeNameLabel"); 		
		execute("EditableOnOff.setOff");
		assertNoAction("Customer.changeNameLabel"); 
		assertLoadedParts("xava_editor_xava.Customer.type, " +
				"xava_editor_xava.Customer.__ACTION__Customer_changeNameLabel, " +
				"xava_editor_xava.Customer.number, xava_errors, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_property_actions_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_property_actions_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.address.viewProperty, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.website, " +
				"xava_property_actions_xava.Customer.address.viewProperty, " +
				"xava_property_actions_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.remarks, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_editor_xava.Customer.email, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.photo, xava_messages, " +
				"xava_focus_property_id");		
		
		execute("EditableOnOff.setOn");
		assertAction("Customer.changeNameLabel"); 
		assertLoadedParts("xava_editor_xava.Customer.type, " +
				"xava_editor_xava.Customer.__ACTION__Customer_changeNameLabel, " +
				"xava_editor_xava.Customer.number, xava_errors, " +
				"xava_editor_xava.Customer.alternateSeller.number, " +
				"xava_property_actions_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.address.city, " +
				"xava_property_actions_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.address.zipCode, " +
				"xava_editor_xava.Customer.name, " +
				"xava_editor_xava.Customer.address.street, " +
				"xava_editor_xava.Customer.address.viewProperty, " +
				"xava_editor_xava.Customer.city, " +
				"xava_editor_xava.Customer.website, " +
				"xava_property_actions_xava.Customer.address.viewProperty, " +
				"xava_property_actions_xava.Customer.alternateSeller.number, " +
				"xava_editor_xava.Customer.remarks, " +
				"xava_editor_xava.Customer.relationWithSeller, " +
				"xava_editor_xava.Customer.email, " +
				"xava_descriptions_list_xava.Customer.address.state, " +
				"xava_editor_xava.Customer.seller.number, " +
				"xava_editor_xava.Customer.telephone, " +
				"xava_collection_xava.Customer.deliveryPlaces., " +
				"xava_editor_xava.Customer.photo, xava_messages, " +
				"xava_focus_property_id");
		
		// Change view programatically
		execute("Customer.changeToSimpleView");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
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
			
	public void testDependentDescriptionsList_resetDescriptionsCache_setEditable() throws Exception { 
		changeModule("Product2"); 
		// Dependent descriptions list
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		setValue("family.number", "1");
		setValue("family.number", "2");
		assertLoadedParts("xava_errors, xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_messages, xava_focus_property_id");		
		setValue("family.number", "1");
		assertLoadedParts("xava_errors, xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_messages, xava_focus_property_id");
		
		// Reset descriptions cache
		execute("Product2.changeLimitZone");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
				"xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_descriptions_list_xava.Product2.warehouse, " +
				"xava_focus_property_id, xava_messages,");
		
		// setEditable
		execute("Product2.deactivateType");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
				"xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_focus_property_id, xava_messages,");
	}
		
	public void	testShowingHiddingPartsReloadsFullView() throws Exception {
		changeModule("Product2"); 
		execute("Mode.detailAndFirst");
		assertNotExists("zoneOne");
		assertLoadedParts("xava_core, ");
		execute("Navigation.next");
		assertExists("zoneOne");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
		execute("Navigation.next");
		assertExists("zoneOne");
		assertLoadedParts("xava_descriptions_list_xava.Product2.family, xava_errors, " +
				"xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_editor_xava.Product2.photos, " +
				"xava_editor_xava.Product2.unitPriceInPesetas, " +
				"xava_descriptions_list_xava.Product2.warehouse, " +
				"xava_editor_xava.Product2.description, " +
				"xava_editor_xava.Product2.number, " +
				"xava_messages, xava_editor_xava.Product2.unitPrice, " +
				"xava_focus_property_id");
		execute("Navigation.next");
		assertNotExists("zoneOne");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
	}
	
	public void testFirstDetailActionExecutingLoadAllButOnlyFirstTime() throws Exception {
		changeModule("Carrier"); 		
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		assertLoadedParts("xava_errors, xava_messages, xava_focus_property_id");
		execute("Mode.list");
		execute("CRUD.new");
		assertLoadedParts("xava_core, ");
		execute("CRUD.new");
		assertLoadedParts("xava_errors, xava_messages, xava_focus_property_id"); // Perfect: From now on only the needed parts are reloaded
	}
		
	public void testCollections() throws Exception {
		changeModule("Carrier"); 
		execute("Mode.detailAndFirst");
		assertCollectionRowCount("fellowCarriers", 3);
		setValue("warehouse.number", "2");
		assertCollectionRowCount("fellowCarriers", 0);
		setValue("warehouse.number", "1");
		assertCollectionRowCount("fellowCarriers", 3);
		assertLoadedParts("xava_errors, xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_messages, xava_editor_xava.Carrier.warehouse.name, " +
				"xava_focus_property_id");		
		execute("Collection.edit", "row=0,viewObject=xava_view_fellowCarriersCalculated");
		assertLoadedParts("xava_errors, xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_messages, xava_focus_property_id");		
		
		execute("Navigation.next");
		assertLoadedParts("xava_errors, xava_editor_xava.Carrier.number, " +
				"xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_editor_xava.Carrier.name, xava_messages, " +
				"xava_focus_property_id");		
		
		execute("Carrier.translateAll"); // The first time more changes because null in some db columns
		execute("Carrier.translateAll");
		assertLoadedParts("xava_errors, xava_collection_xava.Carrier.fellowCarriers., " +
				"xava_collection_xava.Carrier.fellowCarriersCalculated., " +
				"xava_messages, xava_focus_property_id");
	}
	
	public void testSections() throws Exception {
		changeModule("InvoiceNestedSections");
		execute("CRUD.new");
		execute("Sections.change", "activeSection=2");
		execute("Sections.change", "activeSection=1");
		assertLoadedParts("xava_errors, xava_sections_xava_view, " +
				"xava_messages, xava_focus_property_id");
		execute("Sections.change", "activeSection=1,viewObject=xava_view_section1");
		assertLoadedParts("xava_errors, xava_sections_xava_view_section1, " +
				"xava_messages, xava_focus_property_id");
		execute("Sections.change", "activeSection=1,viewObject=xava_view_section1_section1");
		assertLoadedParts("xava_errors, xava_sections_xava_view_section1_section1, " +
				"xava_messages, xava_focus_property_id");		
	}
	
	public void testSectionsInsideSubview() throws Exception {
		changeModule("TransportCharge");
		execute("CRUD.new");
		execute("Sections.change", "activeSection=4,viewObject=xava_view_delivery");
		assertLoadedParts("xava_errors, xava_sections_xava_view_delivery, " +
				"xava_focus_property_id, xava_messages, ");
	}
	
	public void testDetailCollection() throws Exception {
		changeModule("Invoice");
		execute("CRUD.new");
		execute("Sections.change", "activeSection=1");
		execute("Collection.new", "viewObject=xava_view_section1_details");
		// First time that the detail is used all collection is reloaded
		assertLoadedParts("xava_errors, xava_collection_xava.Invoice.details., " +
				"xava_messages, xava_focus_property_id");

		setValue("details.product.number", "1");
		assertLoadedParts("xava_errors, " +
				"xava_editor_xava.Invoice.details.product.warehouseKey, " +
				"xava_editor_xava.Invoice.details.product.familyNumber, " +
				"xava_editor_xava.Invoice.details.product.photos, " +
				"xava_editor_xava.Invoice.details.product.unitPrice, " +
				"xava_editor_xava.Invoice.details.product.subfamilyNumber, " +
				"xava_editor_xava.Invoice.details.product.unitPriceInPesetas, " +
				"xava_editor_xava.Invoice.details.product.description, " +
				"xava_editor_xava.Invoice.details.quantity, " +
				"xava_messages, xava_focus_property_id");		
		setValue("details.product.number", "2");
		assertLoadedParts("xava_errors, " +
				"xava_editor_xava.Invoice.details.product.warehouseKey, " +
				"xava_editor_xava.Invoice.details.product.familyNumber, " +
				"xava_editor_xava.Invoice.details.product.photos, " +
				"xava_editor_xava.Invoice.details.product.subfamilyNumber, " +
				"xava_editor_xava.Invoice.details.product.unitPrice, " +
				"xava_editor_xava.Invoice.details.product.unitPriceInPesetas, " +
				"xava_editor_xava.Invoice.details.product.description, " +
				"xava_messages, xava_focus_property_id");		
	}	
	
	public void testErrorImagesForPropertiesAndDescriptionsLists() throws Exception {
		changeModule("Product2");
		execute("CRUD.new");
		execute("CRUD.save");
		assertErrorsCount(4);
		assertLoadedParts("xava_errors, xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_error_image_Product2.description, " +
				"xava_error_image_Product2.subfamily, " +
				"xava_error_image_Product2.unitPrice, " +
				"xava_error_image_Product2.number, " +
				"xava_messages, xava_focus_property_id");
		HtmlPage page = (HtmlPage) getWebClient().getCurrentWindow().getEnclosedPage();
		HtmlSpan description = (HtmlSpan) page.getElementById("xava_error_image_Product2.description");
		assertTrue("description has no image error", description.asXml().indexOf("/xava/images/error.gif") >= 0);
		
		setValue("description", "z");
		execute("CRUD.save");
		assertErrorsCount(3);		
		assertLoadedParts("xava_errors, xava_descriptions_list_xava.Product2.subfamily, " +
				"xava_error_image_Product2.description, " +				
				"xava_error_image_Product2.subfamily, " +
				"xava_error_image_Product2.unitPrice, " +
				"xava_error_image_Product2.number, " +
				"xava_editor_xava.Product2.description, " + // Needed to refresh it because a converter transforms it to uppercase
				"xava_messages, xava_focus_property_id");		
		assertTrue("description has image error", description.asXml().indexOf("/xava/images/error.gif") < 0);
		
		HtmlSpan number = (HtmlSpan) page.getElementById("xava_error_image_Product2.number");
		assertTrue("number has no image error", number.asXml().indexOf("/xava/images/error.gif") >= 0);
		execute("CRUD.new");
		assertTrue("number has image error", number.asXml().indexOf("/xava/images/error.gif") < 0);
		
		// To test that Number:999 is not used as member name
		setValue("number", "999");
		execute("CRUD.search");
		assertError("Object of type Product does not exists with key Number:999");
	}
	
	public void testChangingViewAndController() throws Exception {
		changeModule("Carrier");
		execute("CRUD.new");
		execute("Reference.createNew", "model=Warehouse,keyProperty=xava.Carrier.warehouse.number");
		assertLoadedParts("xava_errors, xava_view, xava_bottom_buttons, " +
				"xava_button_bar, xava_messages, xava_focus_property_id");
		execute("NewCreation.saveNew");
		assertLoadedParts("xava_errors, xava_error_image_Warehouse.zoneNumber, " +
				"xava_messages, xava_error_image_Warehouse.number, " +
				"xava_focus_property_id");		
	}
	
	public void testCustomView_uploadFile() throws Exception {
		changeModule("Product2");
		execute("Mode.detailAndFirst");
		execute("Gallery.edit", "galleryProperty=photos");
		assertLoadedParts("xava_errors, xava_view, xava_bottom_buttons, " +
				"xava_button_bar, xava_messages, xava_focus_property_id");
		execute("Gallery.addImage");
		assertLoadedParts("xava_core, ");
		String imageUrl = System.getProperty("user.dir") + "/test-images/foto_javi.jpg";
		setFileValue("newImage", imageUrl);
		execute("LoadImageIntoGallery.loadImage");
		assertNoErrors();
		assertMessage("Image added to the gallery");
		assertLoadedParts("xava_core, ");
		
		String imageOid = getForm().getInputByName("xava.GALLERY.images").getValueAttribute();
		execute("Gallery.removeImage", "oid="+imageOid);
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
		execute("Gallery.return");
		assertLoadedParts("xava_errors, xava_view, xava_bottom_buttons, " +
				"xava_button_bar, xava_messages, xava_focus_property_id");
	}
	
	public void testHandmadeWebView() throws Exception {
		changeModule("SellerJSP");
		execute("Mode.detailAndFirst");
		assertLoadedParts("xava_core, ");
		setValue("level.id", "B");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
		execute("Navigation.next");
		assertLoadedParts("xava_errors, xava_view, xava_messages, xava_focus_property_id");
	}
	
	public void testChangingModelOfView() throws Exception {
		changeModule("Human");
		execute("Mode.detailAndFirst");		
		assertLoadedParts("xava_core,");
		
		execute("Navigation.next");		
		assertLoadedParts("xava_editor_xava.Human.sex, xava_errors, " +
				"xava_editor_xava.Human.name, " +
				"xava_focus_property_id, xava_messages,");
		
		// Now the model changes...
		execute("Navigation.next"); 
		assertLoadedParts("xava_errors, xava_view, " + // ...so we reload the full view
				"xava_focus_property_id, xava_messages,");		
	}
	

	private void assertLoadedParts(String expected) throws Exception {
		assertEquals("Loaded parts are not the expected ones", order(expected), order(getLoadedParts()));
	}
	
	private String order(String parts) {
		StringTokenizer st = new StringTokenizer(parts, ",");
		SortedSet ordered = new TreeSet();
		while (st.hasMoreTokens()) {
			String part = st.nextToken().trim();
			if (!"".equals(part)) ordered.add(part);			
		}
		StringBuffer result = new StringBuffer();
		for (Iterator it=ordered.iterator(); it.hasNext(); ) {
			result.append(it.next());
			result.append(",\n");
		}
		return result.toString();
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
