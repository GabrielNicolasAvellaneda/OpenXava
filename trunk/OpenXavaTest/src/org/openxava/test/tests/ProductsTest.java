package org.openxava.test.tests;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class ProductsTest extends ModuleTestBase {
	
	private String [] detailActions = {
		"Navigation.previous",
		"Navigation.first",
		"Navigation.next",
		"CRUD.new",
		"CRUD.save",
		"CRUD.delete",
		"CRUD.search",		
		"Mode.list"		
	};
	
	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail"				
	};
	
	private String [] deleteActions = {
		"ConfirmDelete.confirmDelete",
		"ConfirmDelete.cancel",
		"Mode.list"		
	};
	
	public ProductsTest(String testName) {
		super(testName, "OpenXavaTest", "Products");		
	}
	
	public ProductsTest(String testName, String module) {
		super(testName, "OpenXavaTest", module);		
	}
	
	public void testCustomizeList_sortProperties() throws Exception {
		execute("List.customize");
		execute("List.addColumns");
		
		assertCollectionRowCount("xavaPropertiesList", 4);
		assertValueInCollection("xavaPropertiesList",  0, 1, "familyNumber");
		assertValueInCollection("xavaPropertiesList",  1, 1, "subfamilyNumber");
		assertValueInCollection("xavaPropertiesList",  2, 1, "warehouseKey");
		assertValueInCollection("xavaPropertiesList",  3, 1, "remarks");
		 
		execute("AddColumns.sort");						
		assertValueInCollection("xavaPropertiesList",  0, 1, "familyNumber");
		assertValueInCollection("xavaPropertiesList",  1, 1, "remarks");
		assertValueInCollection("xavaPropertiesList",  2, 1, "subfamilyNumber");
		assertValueInCollection("xavaPropertiesList",  3, 1, "warehouseKey");
		
		execute("AddColumns.sort"); // A second time, unsort it
		assertValueInCollection("xavaPropertiesList",  0, 1, "familyNumber");
		assertValueInCollection("xavaPropertiesList",  1, 1, "subfamilyNumber");
		assertValueInCollection("xavaPropertiesList",  2, 1, "warehouseKey");
		assertValueInCollection("xavaPropertiesList",  3, 1, "remarks");		
	}
		
	public void testFiltersInDescriptionsEditor() throws Exception {
		execute("CRUD.new");
		WarehouseKey key1 = new WarehouseKey();
		key1.setZoneNumber(1);
		key1.set_Number(new Integer(1));
		WarehouseKey key2 = new WarehouseKey();
		key2.setZoneNumber(1);
		key2.set_Number(new Integer(2));
		WarehouseKey key3 = new WarehouseKey();
		key3.setZoneNumber(1);
		key3.set_Number(new Integer(3));		
		
		String [][] zone1WarehouseValues = new String [][] { // It is assumed that 1 is default zone
			{ "", "" },
			{ key1.toString(), "CENTRAL VALENCIA" },
			{ key3.toString(), "VALENCIA NORTE" },
			{ key2.toString(), "VALENCIA SURETE" }
		};
		
		assertValidValues("warehouseKey", zone1WarehouseValues);
	}

	public void testDepedentsStereotypesAndDescriptionsEditor_someDescriptions_and_formatters() throws Exception {
		assertActions(listActions);
		
		execute("CRUD.new");
		assertActions(detailActions);

		// Verifying initial status		
		String [][] familyValues = {
			{ "", "" },
			{ "1", "SOFTWARE" },
			{ "2", "HARDWARE" },
			{ "3", "SERVICIOS" }	
		};
		
		assertValue("familyNumber", "");		
		assertValidValues("familyNumber", familyValues);
		
		String [][] emptyValues = {
			{ "", "" }
		};
		
		assertValue("subfamilyNumber", "");		
		assertValidValues("subfamilyNumber", emptyValues);
		
		// Change value
		setValue("familyNumber", "2");
		String [][] hardwareValues = {
			{ "", ""},
			{ "11", "011 SERVIDORES"},								
			{ "12", "012 PC"},
			{ "13", "013 PERIFERICOS"}							
		};
		assertValue("subfamilyNumber", "");
		assertValidValues("subfamilyNumber", hardwareValues);
		
		// We change the value again
		setValue("familyNumber", "1");
		String [][] softwareValues = {
			{ "", ""},
			{ "1", "01 DESARROLLO"},
			{ "2", "02 GESTION"},						  
			{ "3", "03 SISTEMA"}						
		};
		assertValue("subfamilyNumber", "");
		assertValidValues("subfamilyNumber", softwareValues);										
	}
	
	public void testDescriptionsFormatterWhenReadOnly() throws Exception {
		execute("Mode.detailAndFirst");
		String subfamily = getDescriptionValue("subfamilyNumber");
		assertTrue("Subfamily must not to be empty", !Is.emptyString(subfamily));
		execute("CRUD.delete");
		assertNoErrors();
		assertDescriptionValue("subfamilyNumber", subfamily);		
	}
	
	public void testNavigationWithDepedentsStereotypes() throws Exception {
		assertActions(listActions);
		execute("Mode.detailAndFirst");
		assertValue("number", "1");
		assertValue("familyNumber", "1");
		assertValue("subfamilyNumber", "2");		
		execute("Navigation.next");		
		assertValue("number", "2");
		assertValue("familyNumber", "2");
		assertValue("subfamilyNumber", "11");		
		execute("Navigation.next");
		assertValue("number", "3");
		assertValue("familyNumber", "1");
		assertValue("subfamilyNumber", "1");						
	}
	
	public void testCreateWithDescriptionsEditorsAndFormatters() throws Exception {
		assertActions(listActions);
		
		// Create
		execute("CRUD.new");
		assertActions(detailActions);
		setValue("number", "66");
		setValue("description", "TEST PRODUCT");
		setValue("familyNumber", "1");
		setValue("subfamilyNumber", "1");
		setValue("warehouseKey", "[.1.1.]");
		setValue("unitPrice", "125.66");
		assertNoEditable("unitPriceInPesetas");
		execute("CRUD.save");				
		assertNoErrors();
				
		// Searching for verify
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("number", "66");
		assertValue("description", "TEST PRODUCT");
		assertValue("familyNumber", "1");
		assertValue("subfamilyNumber", "1");
		assertValue("warehouseKey", "[.1.1.]");
		assertValue("unitPrice", "125.66");
				
		// Go to page for delete
		execute("CRUD.delete");
		assertAction("ConfirmDelete.confirmDelete");
		assertAction("ConfirmDelete.cancel");		
		assertValue("number", "66");		
		assertValue("description", "TEST PRODUCT");		
		execute("ConfirmDelete.confirmDelete");
		assertActions(detailActions);
		assertMessage("Product deleted successfully");		
	}
	
	public void testValueCalculatedDependent() throws Exception {
		assertActions(listActions);
		
		execute("CRUD.new");
		assertActions(detailActions);
		
		// Change value
		setValue("unitPrice", "100");
		assertValue("unitPriceInPesetas", "16,639");
	}
	
	public void testCalculatedDefaultValueDependent() throws Exception {
		assertActions(listActions);				
		execute("CRUD.new");
		assertActions(detailActions);	
		assertValue("familyNumber", "");
		assertValue("unitPrice", "");
		assertValue("unitPriceInPesetas", "");	
		
		setValue("familyNumber", "2");
		assertValue("unitPrice", "20");
		assertValue("unitPriceInPesetas", "3,328");
		
		// It is not change because unitPrice already has value
		setValue("familyNumber", "1");
		assertValue("unitPrice", "20");
		assertValue("unitPriceInPesetas", "3,328");
				
		// Test again		
		setValue("familyNumber", "2");		
		setValue("unitPrice", "");
		
		// No it is changed because unitPrice has not value, hence
		// default value is calculated
		setValue("familyNumber", "1");
		assertValue("unitPrice", "10");
		assertValue("unitPriceInPesetas", "1,664");		
	}
	
	public void testConsultWithCalculatedValuesAndByDefault() throws Exception {
		assertActions(listActions);
		
		execute("CRUD.new");
		assertActions(detailActions);
		setValue("number", "1");
		execute("CRUD.search");
						
		assertValue("familyNumber", "1");
		assertValue("unitPrice", "11");
		assertValue("unitPriceInPesetas", "1,830");		
	}
	
	public void testPropertyValidator_SomesAndCustomized() throws Exception {
		assertActions(listActions);		
		execute("CRUD.new");
		assertActions(detailActions);
		
		setValue("number", "66");
		setValue("description", "UNA MOTO RAPIDA");
		setValue("familyNumber", "1");
		setValue("subfamilyNumber", "1");
		setValue("warehouseKey", "[.1.1.]");
		setValue("unitPrice", "100");
		assertNoEditable("unitPriceInPesetas");
		execute("CRUD.save");				
		assertError("Product can not contains MOTO in Description");
		
		setValue("description", "UN COCHE COMODO");
		execute("CRUD.save");
		assertError("Product can not contains COCHE in Description");				
	}
	
	public void testEntityValidator() throws Exception {
		assertActions(listActions);		
		execute("CRUD.new");
		assertActions(detailActions);
		
		setValue("number", "66");
		setValue("description", "UN PRODUCTO CARO");
		setValue("familyNumber", "1");
		setValue("subfamilyNumber", "1");
		setValue("warehouseKey", "[.1.1.]");
		setValue("unitPrice", "100");
		assertNoEditable("unitPriceInPesetas");
		execute("CRUD.save");				
		assertError("The products EXPENSIVE must to have price greater than 1,000");
		
		setValue("description", "UN PRODUCTO BARATO");
		setValue("unitPrice", "1000");
		execute("CRUD.save");
		assertError("The products CHEAP can not be of price greater than 100");				
	}

	public void testCalculatedInListMode() throws Exception {		
		assertActions(listActions);
		assertValueInList(1, "number", "2");
		assertValueInList(1, "unitPrice", "20");
		assertValueInList(1, "unitPriceInPesetas", "3,328");
	}
	
	public void testValidationWithValidatorsChanged() throws Exception {
		assertActions(listActions);				
		execute("CRUD.new");
		assertActions(detailActions);
		execute("CRUD.save");		
		// Since validator for FAMILY and SUBFAMILY has set to NO_NULL
		// it not use of integer (NO_ZERO) and hence it do not validate although required is true
		assertNoError("Value for Family in Product is required");
		assertNoError("Value for Subfamily in Product is required");
	}

	public void testGoFromListToDetailAlwaysSetDefaultController_editableWellOnSearch() throws Exception {
		assertActions(listActions);
		execute("Mode.detailAndFirst");
		assertNoEditable("number");
		assertEditable("description");		
		assertActions(detailActions);
		execute("CRUD.delete");		
		assertActions(deleteActions);	
		execute("Mode.list");
		assertActions(listActions);
		execute("Mode.detailAndFirst");
		assertActions(detailActions);
		assertNoEditable("number");
		assertEditable("description");
	}
								
	public void testOnChangeDependentsOfPropertyWithDefaultValue() throws Exception {
		execute("CRUD.new");
		assertValue("unitPrice","");
		assertValue("remarks", "");		
		setValue("familyNumber", "1");		
		assertValue("unitPrice","10");
		assertValue("remarks", "The price is 10");				
	}
	
}
