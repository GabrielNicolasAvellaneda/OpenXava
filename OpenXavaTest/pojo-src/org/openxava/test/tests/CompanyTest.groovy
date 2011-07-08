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
	}
		
	
}
