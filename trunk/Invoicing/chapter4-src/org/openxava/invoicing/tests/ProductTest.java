package org.openxava.invoicing.tests;

import org.openxava.tests.*;

public class ProductTest extends ModuleTestBase {
	
	public ProductTest(String testName) {
		super(testName, "Invoicing", "Product");				
	}
	
	public void testCreateReadUpdateDelete() throws Exception {
		// Create
		execute("CRUD.new");                    
		setValue("number", "77");                     
		setValue("description", "JUNIT Product");
		execute("CRUD.save");
		assertNoErrors();                            
		assertValue("number", "");                   
		assertValue("description", "");
		 
		// Read
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("number", "77");
		assertValue("description", "JUNIT Product");
		 
		// Update
		setValue("name", "JUNIT Product MODIFIED");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("number", "");
		assertValue("description", "");
		 
		// Verify if modified
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("number", "77");
		assertValue("description", "JUNIT Product MODIFIED");
		 
		// Delete
		execute("CRUD.delete");
		assertMessage("Product deleted successfully");
	}
		
}
