package org.openxava.invoicing.tests;

import org.openxava.invoicing.model.*;
import org.openxava.tests.*;
import static org.openxava.jpa.XPersistence.*;

public class ProductTest extends ModuleTestBase {
	
	public ProductTest(String testName) {
		super(testName, "Invoicing", "Product");				
	}
	
	protected void setUp() throws Exception {
		//assertNoProductsGreatherThan900000000();		
		createProducts();		
		super.setUp();
		//filterByProductsGreatherThan900000000();
	}
	
	private void assertNoProductsGreatherThan900000000() {		
		
	}

	public void testCreateReadUpdateDelete() throws Exception {
	}
	
	private void createProducts() {
		// Creating the Java object
		Author author = new Author();
		author.setName("JUNIT Author");
		Category category = new Category();
		category.setDescription("JUNIT Category");
		
		Product product1 = new Product();
		product1.setNumber(900000001);
		product1.setDescription("JUNIT Product 1");
		product1.setAuthor(author);
		product1.setCategory(category);
		
		Product product2 = new Product();
		product2.setNumber(900000002);
		product2.setDescription("JUNIT Product 2");
		product2.setAuthor(author);
		product2.setCategory(category);
		
		// Marking as persistent objects
		getManager().persist(author);
		getManager().persist(category);
		getManager().persist(product1);
		getManager().persist(product2);
		
		// Commit changes to database
		commit();
	}
		
}
