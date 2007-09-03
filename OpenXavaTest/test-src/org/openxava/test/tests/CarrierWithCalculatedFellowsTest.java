package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class CarrierWithCalculatedFellowsTest extends ModuleTestBase {
	
	public CarrierWithCalculatedFellowsTest(String testName) {
		super(testName, "CarrierWithCalculatedFellows");		
	}
	
	public void testDefaultListActionsForCollectionsDoesNotApplyToCalculatedCollections_emptyCollectionActionIsNotShown() throws Exception {
		execute("CRUD.new");
		assertNoErrors();
		assertExists("number");
		assertExists("name");
		assertNoAction("Print.generatePdf");
		assertNoAction("Print.generateExcel");
		assertNoAction("Collection.removeSelected"); 		
	}
	
}
