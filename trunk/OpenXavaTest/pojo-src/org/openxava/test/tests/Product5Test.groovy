package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class Product5Test extends ModuleTestBase {
	
	Product5Test(String testName) {
		super(testName, "Product5")		
	}

	void testDescriptionsListDependsOnEnum() throws Exception {
		
		execute("CRUD.new");
	
		// Verifying initial state
		String [][] familyValues = [
			[ "", "" ],
			[ "0", "NONE" ],
			[ "1", "SOFTWARE" ],
			[ "2", "HARDWARE" ],
			[ "3", "SERVICIOS" ]
		]
		
		assertValidValues("family", familyValues);
		setValue("family", "0");
		
		String [][] voidValues = [
			[ "", "" ]
		];
		
		assertValue("subfamily.number", "");
		assertValidValues("subfamily.number", voidValues);
		
		// Change value
		setValue("family", "2");
		String [][] hardwareValues = [
			[ "", "" ],
			[ "12", "PC" ],
			[ "13", "PERIFERICOS" ],
			[ "11", "SERVIDORES" ]
		];
		assertValue("subfamily.number", "");
		assertValidValues("subfamily.number", hardwareValues);
		
		// Changing the value again
		setValue("family", "1");
		String [][] softwareValues = [
			[ "", "" ],
			[ "1", "DESARROLLO" ],
			[ "2", "GESTION" ],
			[ "3", "SISTEMA" ]
		];
		assertValue("subfamily.number", "");
		assertValidValues("subfamily.number", softwareValues);
	}
	
	
	void testCollectionWithLongNameStoresPreferences() { 
		execute "CRUD.new"
		assertCollectionColumnCount "productDetailsSupplierContactDetails", 2
		execute "List.customize", "collection=productDetailsSupplierContactDetails"
		execute "List.removeColumn", "columnIndex=1,collection=productDetailsSupplierContactDetails"
		assertCollectionColumnCount "productDetailsSupplierContactDetails", 1
		
		resetModule()
		
		execute "CRUD.new"
		assertCollectionColumnCount "productDetailsSupplierContactDetails", 1
		execute "List.customize", "collection=productDetailsSupplierContactDetails"
		execute "List.addColumns", "collection=productDetailsSupplierContactDetails"
		execute "AddColumns.restoreDefault"
		assertCollectionColumnCount "productDetailsSupplierContactDetails", 2 
		
		resetModule()
		
		execute "CRUD.new"
		assertCollectionColumnCount "productDetailsSupplierContactDetails", 2
	}

	void testRememberActionsInList() throws Exception{
		String[] listActions = 
		[
			'CRUD.new', 'CRUD.deleteRow', 'CRUD.deleteSelected',
			'Print.generatePdf', 'Print.generateExcel',
			'List.filter', 'List.sumColumn', 'List.orderBy', 'List.hideRows',
			'List.viewDetail', 'List.customize',
			'Mode.detailAndFirst', 'Mode.split',
			'ExtendedPrint.myReports',
			'Product5.goB'
		]
		String[] detailActions =
		[
			"Navigation.previous", "Navigation.first", "Navigation.next",
			"CRUD.delete", "CRUD.new", "CRUD.refresh", "CRUD.save", "CRUD.search",
			"Mode.list", "Mode.split",
			"Gallery.edit", "List.filter", "Print.generatePdf", 
			"Collection.removeSelected", "List.orderBy", "Collection.new", 
			"Reference.createNew", "Reference.modify", "Print.generateExcel", "List.customize"
		]
		String[] galleryActions =
		[
			"Gallery.addImage", "Gallery.return", "Mode.list", "Mode.split"
		]
		
		// list -> detail -> list
		assertActions(listActions)
		execute("Product5.goB")
		assertAction("Product5.goA")
		assertNoAction("Product5.goB")
		
		execute("List.viewDetail", "row=0")
		assertActions(detailActions)
		
		execute("Mode.list")
		assertAction("Product5.goA")
		assertNoAction("Product5.goB")
		assertAction("CRUD.new")
		
		// list -> detail -> gallery editor -> list
		execute("List.viewDetail", "row=0")
		assertNoErrors();
		execute("Gallery.edit", "galleryProperty=photos")
		assertActions(galleryActions)
		
		execute("Mode.list")
		assertAction("Product5.goA")
		assertNoAction("Product5.goB")
		assertAction("CRUD.new")
	}
	
}
