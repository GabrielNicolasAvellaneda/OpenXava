package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class InvoiceNoSectionsTest extends ModuleTestBase {
	
	public InvoiceNoSectionsTest(String testName) {
		super(testName, "InvoiceNoSections");		
	}
	
	public void testTotalInCollection() throws Exception { 
		execute("CRUD.new");
		execute("CRUD.search");
		setValue("year", "2004");
		setValue("number", "9");
		execute("Search.search");
		// Defined by developer
		assertCollectionRowCount("details", 2);
		assertValueInCollection("details", 0, "product.unitPrice", "11");
		assertValueInCollection("details", 1, "product.unitPrice", "20");
		assertTotalInCollection("details", "product.unitPrice", "31");
				
		// Defined by the user
		execute("List.removeColumnSum", "property=product.unitPrice,collection=details");
		assertTotalInCollection("details", "product.unitPrice", "");
		
		assertValueInCollection("details", 0, "quantity", "6");
		assertValueInCollection("details", 1, "quantity", "2");		
		assertTotalInCollection("details", "quantity", "");
		execute("List.sumColumn", "property=quantity,collection=details");
		assertTotalInCollection("details", "quantity", "8");
		
		// Stores preferences
		resetModule();
		execute("CRUD.new");
		execute("CRUD.search");
		setValue("year", "2004");
		setValue("number", "9");
		execute("Search.search");
		assertTotalInCollection("details", "product.unitPrice", "");
		assertTotalInCollection("details", "quantity", "8");
		
		// Restore initial values
		execute("List.customize", "collection=details");
		execute("List.addColumns", "collection=details");
		execute("AddColumns.restoreDefault");
		assertTotalInCollection("details", "product.unitPrice", "31");
		assertTotalInCollection("details", "quantity", "");
		
		resetModule();
		execute("CRUD.new");
		execute("CRUD.search");
		setValue("year", "2004");
		setValue("number", "9");
		execute("Search.search");
		assertTotalInCollection("details", "product.unitPrice", "31");
		assertTotalInCollection("details", "quantity", "");
	}
	
}
