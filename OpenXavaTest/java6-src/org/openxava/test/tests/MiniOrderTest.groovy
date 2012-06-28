package org.openxava.test.tests

import org.openxava.tests.*;

class MiniOrderTest extends ModuleTestBase {
	
	MiniOrderTest(String testName) {
		super(testName, "MiniOrder")		
	}
	
	void testAutomatedBusinessLogic() { 
		execute "CRUD.new"
		setValue "number", "66"		
		setValue "description", "ORDER JUNIT"
		setValue "productPrice", "2"
		setValue "qtyOrdered", "3"
		execute "CRUD.save"
		setValue "number", "66"
		execute "CRUD.refresh"
		assertValue "amount", "6"
	}
				
}
