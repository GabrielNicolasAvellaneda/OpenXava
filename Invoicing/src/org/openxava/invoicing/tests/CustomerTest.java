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
		setValue("address.street", "JUNIT Street");
		setValue("address.zipCode", "77555");
		setValue("address.city", "The JUNIT city");
		setValue("address.state", "The JUNIT state");
		execute("CRUD.save");
		assertNoErrors();                            
		assertValue("number", "");                   
		assertValue("name", "");
		assertValue("address.street", "");
		assertValue("address.zipCode", "");
		assertValue("address.city", "");
		assertValue("address.state", "");		
		 
		// Read
		setValue("number", "77");
		execute("CRUD.search");
		assertValue("number", "77");
		assertValue("name", "JUNIT Customer");
		assertValue("address.street", "JUNIT Street");
		assertValue("address.zipCode", "77555");
		assertValue("address.city", "The JUNIT city");
		assertValue("address.state", "The JUNIT state");
		
		 
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
		execute("Invoicing.delete"); 
		assertMessage("Customer deleted successfully");
	}
		
}
