package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class ReallocationTest extends ModuleTestBase {
	
	public ReallocationTest(String testName) {
		super(testName, "Reallocation");		
	}	
	
	public void testDefaultValueCalculatorForReferencesInElementCollections() throws Exception {
		execute("CRUD.new");
		assertNoErrors();
		assertCollectionRowCount("details", 0);
		setValueInCollection("details", 0, "place", "THE PLACE");
		assertValueInCollection("details", 0, "product.number", "1");
		assertValueInCollection("details", 0, "product.description", "MULTAS DE TRAFICO");
		assertValueInCollection("details", 0, "product.unitPrice", "11.00");
	}
	
}
