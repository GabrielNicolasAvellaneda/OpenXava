package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 *  
 * @author Javier Paniza
 */

public class ProductExpensesTest extends ModuleTestBase {
	
	public ProductExpensesTest(String testName) {
		super(testName, "ProductExpenses");		
	}
		
	public void testSeachKeysEnumsAndDescriptionsListInElementCollection() throws Exception { 
		execute("CRUD.new");
		setValue("description", "JUNIT EXPENSES");
		
		String [][] invoiceValidValues = {
			{ "", "" },
			{ "[.1.2002.]", "2002 1" },
			{ "[.10.2004.]", "2004 10" },
			{ "[.11.2004.]", "2004 11" },
			{ "[.12.2004.]", "2004 12" },
			{ "[.2.2004.]", "2004 2" },
			{ "[.9.2004.]", "2004 9" },
			{ "[.14.2007.]", "2007 14" },
			{ "[.1.2009.]", "2009 1" },
			{ "[.1.2011.]", "2011 1" }			
		};		
		assertValidValues("expenses.0.invoice.KEY", invoiceValidValues);
		setValue("expenses.0.invoice.KEY", "[.1.2002.]");
				
		execute("CRUD.save");
		
		assertValue("description", "");
		assertValue("expenses.0.invoice.KEY", "");
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");

		assertValue("description", "JUNIT EXPENSES");
		assertValue("expenses.0.invoice.KEY", "[.1.2002.]");
		
		execute("CRUD.delete");
		assertNoErrors();
	}
			
}
