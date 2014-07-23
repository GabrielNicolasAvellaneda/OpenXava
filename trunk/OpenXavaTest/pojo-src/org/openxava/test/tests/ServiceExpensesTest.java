package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 *  
 * @author Javier Paniza
 */

public class ServiceExpensesTest extends ModuleTestBase {
	
	public ServiceExpensesTest(String testName) {
		super(testName, "ServiceExpenses");		
	}
		
	public void testSeachKeysEnumsAndDescriptionsListInElementCollection() throws Exception { 
		execute("CRUD.new");
		assertLabelInCollection("expenses", 4, "Receptionist");
		setValue("description", "JUNIT EXPENSES");
		setValue("expenses.0.invoice.year", "2007");
		assertValue("expenses.0.invoice.amount", "");
		setValue("expenses.0.invoice.number", "2");
		assertValue("expenses.0.invoice.amount", "1,730.00");
		
		String [][] statusValidValues = {
			{ "", "" },
			{ "0", "Paid" },
			{ "1", "Pending" },
			{ "2", "Rejected" }
		};		
		assertValidValues("expenses.0.status", statusValidValues);
		setValue("expenses.0.status", "1");
		
		String [][] receptionistValidValues = {
			{ "", "" },
			{ "2", "ANTONIOA" },
			{ "3", "JUAN" },
			{ "1", "PEPE" },
			{ "4", "PEPE" }
		};
		assertValidValues("expenses.0.receptionist.oid", receptionistValidValues);
		setValue("expenses.0.receptionist.oid", "3");		
		
		execute("CRUD.save");
		
		assertValue("description", "");
		assertValue("expenses.0.invoice.year", "");
		assertValue("expenses.0.invoice.number", "");
		assertValue("expenses.0.invoice.amount", "");
		assertValue("expenses.0.status", "");
		assertValue("expenses.0.receptionist.oid", ""); 
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");

		assertValue("description", "JUNIT EXPENSES");
		assertValue("expenses.0.invoice.year", "2007");
		assertValue("expenses.0.invoice.number", "2");
		assertValue("expenses.0.invoice.amount", "1,730.00");
		assertValue("expenses.0.status", "1");
		assertValue("expenses.0.receptionist.oid", "3"); 
		
		execute("CRUD.delete");
		assertNoErrors();
	}
			
}
