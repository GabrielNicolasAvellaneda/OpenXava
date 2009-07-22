package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class CarrierWithCalculatedFellowsTest extends ModuleTestBase {
	
	public CarrierWithCalculatedFellowsTest(String testName) {
		super(testName, "CarrierWithCalculatedFellows");		
	}
	
	public void testMemoFormatterInListOfCalculatedCollection() throws Exception {
		execute("List.viewDetail", "row=0");
		assertValueInCollection("fellowCarriersCalculated", 0, 2, "compañero de uno, de tres y de cuatro. Pero, por a...");
		assertValueInCollection("fellowCarriersCalculated", 1, 2, "no es muy amigable");
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
	
	public void testOnSelectElementActionInCalculatedCollections() throws Exception {
		assertListNotEmpty();
		execute("List.viewDetail", "row=0");		
		assertValue("fellowCarriersCalculatedSize", "");
		checkRowCollection("fellowCarriersCalculated", 0);
		assertValue("fellowCarriersCalculatedSize", "1");
		uncheckRowCollection("fellowCarriersCalculated", 0);
		assertValue("fellowCarriersCalculatedSize", "0");
	}
	
	public void testAutomaticNaturalLabel() throws Exception {
		setLocale("es");
		execute("CRUD.new");
		assertLabel("fellowCarriersCalculatedSize", "Fellow carriers calculated size");
	}
	
}
