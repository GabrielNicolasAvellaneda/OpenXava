package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Products3Test extends ModuleTestBase {
	
	public Products3Test(String testName) {
		super(testName, "OpenXavaTest", "Products3");		
	}
	
	public void testReferenceWithHiddenKey() throws Exception {
		execute("CRUD.new");		
		setValue("number", "66");
		setValue("description", "JUNIT PRODUCT");
		
		execute("Reference.search", "keyProperty=xava.Product3.family.number");
		String familyNumber = getValueInList(0, "number");		
		String familyDescription = getValueInList(0, "description");		
		execute("ReferenceSearch.choose", "row=0");
		assertValue("family.number", familyNumber);
		assertValue("family.description", familyDescription);
		
		execute("CRUD.save");
		assertNoErrors();
		assertValue("family.description", "");
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT PRODUCT");
		assertValue("family.number", familyNumber);
		assertValue("family.description", familyDescription);
		
		execute("CRUD.delete");			
		assertMessage("Product deleted successfully");
	}
						
}
