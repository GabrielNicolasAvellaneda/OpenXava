package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellersJSPTest extends ModuleTestBase {
	
	public SellersJSPTest(String testName) {
		super(testName, "SellersJSP");		
	}
	
	public void testHandmadeWebView() throws Exception {
		execute("CRUD.new");
		assertValue("number", "");
		assertValue("name", "");
		assertValue("level.id", "");
		assertValue("level.description", "");
		assertEditable("number");
		assertEditable("name");		
		assertEditable("level.id");
		assertNoEditable("level.description");
		setValue("number", "66");
		setValue("name", "JUNIT");
		assertValue("level.description", "");
		setValue("level.id", "A");
		assertValue("level.description", "MANAGER");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("number", "");
		assertValue("name", "");
		assertValue("level.id", "");
		assertValue("level.description", "");
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("name", "JUNIT");
		assertValue("level.id", "A");
		assertValue("level.description", "MANAGER");		
		assertNoEditable("number");
		assertEditable("name");
		assertEditable("level.id");
		assertNoEditable("level.description");
				
		execute("CRUD.delete");
		assertMessage("Seller deleted successfully");
	}
			
}
