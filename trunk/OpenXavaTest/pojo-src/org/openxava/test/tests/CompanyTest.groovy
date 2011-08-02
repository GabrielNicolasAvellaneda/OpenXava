package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class CompanyTest extends ModuleTestBase {
	
	CompanyTest(String testName) {
		super(testName, "Company")		
	}
	
	void testReferenceAndCascadeRemoveCollectionToSameEntity() { 
		execute "Mode.detailAndFirst"
		assertNoErrors();
		assertValue "name", "MY COMPANY"
		assertValue "mainBuilding.name", "MY OFFICE"
		assertCollectionRowCount "buildings", 1
		assertValueInCollection "buildings", 0, 0, "BUILDING A"
		execute "Collection.edit", "row=0,viewObject=xava_view_buildings"
		assertNoErrors()
		assertValue "name", "BUILDING A"
		execute "Collection.save"
		assertNoErrors()
	}
	
	void testCollectionElementInsideAGroup() {
		execute "CRUD.new"
		execute "Collection.new", "viewObject=xava_view_buildings"
		assertNoErrors() // For verifying that really works
		assertMessagesCount 1
		setValue "function", "Factory" // For verifying that onchange is thrown only once
		assertMessagesCount 1		
	}	
	
}
