package org.openxava.invoicing.tests;

import java.math.*;

import org.openxava.invoicing.model.*;
import org.openxava.tests.*;
import static org.openxava.jpa.XPersistence.*;

public class ProductTest extends ModuleTestBase {
	
	private Author author;
	private Category category;
	private Product product1;
	private Product product2;
	
	public ProductTest(String testName) {
		super(testName, "Invoicing", "Product");				
	}
	
	protected void setUp() throws Exception {		
		createProducts();		
		super.setUp();
	}
		
	protected void tearDown() throws Exception {		
		super.tearDown();		
		removeProducts();
	}
	
	public void testRemoveFromList() throws Exception {
		
		setConditionValues(new String[] { "", "JUNIT" });
		
		setConditionComparators(
			new String[] { "=", "contains_comparator" });

		execute("List.filter");
		assertListRowCount(2);
		checkRow(1);
		// tmp execute("CRUD.deleteSelected");		
		execute("Invoicing.deleteSelected"); // tmp
		assertListRowCount(1);
	}
	
	public void testChangePrice() throws Exception {
		// Searching the product1
		execute("CRUD.new");
		setValue("number", Integer.toString(product1.getNumber()));
		execute("CRUD.search");
		assertValue("price", "10.00");
		
		// Changing the price
		setValue("price", "12.00");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("price", "");

		// Verifying
		setValue("number", Integer.toString(product1.getNumber()));
		execute("CRUD.search");
		assertValue("price", "12.00");
	}
	
	public void testISBNValidator() throws Exception {
		// Searching the product1
		execute("CRUD.new");
		setValue("number", Integer.toString(product1.getNumber()));
		execute("CRUD.search");		
		assertValue("description", "JUNIT Product 1");
		assertValue("isbn", "");
		
		// With incorrect ISBN format
		setValue("isbn", "1111");
		execute("CRUD.save");
		assertError("1111 is not a valid value for Isbn of " +
				"Product: I s b n does not exist");
		
		// ISBN does not exist though it has correct format
		setValue("isbn", "1234367890");
		execute("CRUD.save");
		assertError("1234367890 is not a valid value for Isbn of " +
				"Product: I s b n does not exist");
		
		// ISBN exists
		setValue("isbn", "0932633439");
		execute("CRUD.save");
		assertNoErrors();
	}
			
	private void createProducts() {
		// Creating the Java object
		author = new Author();
		author.setName("JUNIT Author");
		category = new Category();
		category.setDescription("JUNIT Category");
		
		product1 = new Product();
		product1.setNumber(900000001);
		product1.setDescription("JUNIT Product 1");
		product1.setAuthor(author);
		product1.setCategory(category);
		product1.setPrice(new BigDecimal("10"));
		
		product2 = new Product();
		product2.setNumber(900000002);
		product2.setDescription("JUNIT Product 2");
		product2.setAuthor(author);
		product2.setCategory(category);
		product2.setPrice(new BigDecimal("20"));
		
		// Marking as persistent objects
		getManager().persist(author);
		getManager().persist(category);
		getManager().persist(product1);
		getManager().persist(product2);
		
		// Commit changes to database
		commit();
	}
		
	private void removeProducts() {
		remove(product1, product2, author, category);
		commit();		
	}
	
	private void remove(Object ... entities) {
		for (Object entity: entities) {
			getManager().remove(getManager().merge(entity));
		}
	}		
	
}
