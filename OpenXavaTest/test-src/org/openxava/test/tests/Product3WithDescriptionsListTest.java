package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Product3WithDescriptionsListTest extends ModuleTestBase {
	
	public Product3WithDescriptionsListTest(String testName) {
		super(testName, "Product3WithDescriptionsList");		
	}

	public void testSetToNullADescriptionsListWithHiddenKey() throws Exception {
		// Creating new one
		execute("CRUD.new");		
		setValue("number", "66");
		setValue("description", "JUNIT PRODUCT");
		setValue("family.oid", "1037101896763");		
		execute("CRUD.save");
		assertNoErrors();
		assertValue("description", "");
				
		// Search it
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT PRODUCT");
		assertValue("family.oid", "1037101896763");
		assertDescriptionValue("family.oid", "HARDWARE");
		
		// Reset the family reference
		setValue("family.oid", "");
		execute("CRUD.save");
		assertNoErrors();
		
		// Verifying that change is done
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT PRODUCT");
		assertValue("family.oid", "");
		assertDescriptionValue("family.oid", "");
		
		// Deleting
		execute("CRUD.delete");			
		assertMessage("Product deleted successfully");		
	}
						
}
