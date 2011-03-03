package org.openxava.invoicing.tests;

import org.openxava.tests.*;

public class CategoryTest extends ModuleTestBase {
	
	public CategoryTest(String testName) {
		super(testName, "Invoicing", "Category");				
	}
		
	public void testCategoriesInList() throws Exception {
		assertValueInList(0, 0, "MUSIC");  
		assertValueInList(1, 0, "BOOKS");  
		assertValueInList(2, 0, "SOFTWARE"); 
	}
		
}
