package org.openxava.invoicing.tests;

import org.openxava.tests.*;

public class CustomerTest extends ModuleTestBase {
	
	public CustomerTest(String testName) {
		super(testName, "Invoicing", "Customer");				
	}
	
	public void testCreateReadUpdateDelete() throws Exception {
		// Create
		execute("CRUD.new");                    
		setValue("number", "77");                     
		setValue("name", "JUNIT Customer");
		execute("CRUD.save");
		assertNoErrors();                            
		assertValue("number", "");                   
		assertValue("name", "");
		 
		// Read
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("number", "77");
		assertValue("name", "JUNIT Customer");
		 
		// Update
		setValue("name", "JUNIT Customer MODIFIED");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("number", "");
		assertValue("name", "");
		 
		// Verify if modified
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("number", "77");
		assertValue("name", "JUNIT Customer MODIFIED");
		 
		// Delete
		execute("CRUD.delete");
		assertMessage("Customer deleted successfully");
	}
		
}
