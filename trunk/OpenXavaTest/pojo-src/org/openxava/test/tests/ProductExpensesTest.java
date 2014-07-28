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
		
	public void testDescriptionsListInElementCollection() throws Exception { 
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
		String [][] productValidValues = {
			{ "", "" },
			{ "4", "CUATRE" },
			{ "2", "IBM ESERVER ISERIES 270" },
			{ "1", "MULTAS DE TRAFICO" },
			{ "5", "PROVAX" },
			{ "6", "SEIS" },
			{ "7", "SIETE" },
			{ "3", "XAVA" }
		};		
		assertValidValues("expenses.0.product.number", productValidValues);

		String [][] carrierValidValues = {
			{ "", "" },
			{ "4", "CUATRO" },
			{ "5", "Cinco" },
			{ "2", "DOS" },
			{ "3", "TRES" },
			{ "1", "UNO" }
		};		
		assertValidValues("expenses.0.carrier.number", carrierValidValues);		
		
		setValue("expenses.0.invoice.KEY", "[.1.2002.]");
		setValue("expenses.0.product.number", "4"); 
		setValue("expenses.0.carrier.number", "3"); 
				
		execute("CRUD.save");
		
		assertValue("description", "");
		assertValue("expenses.0.invoice.KEY", "");
		assertValue("expenses.0.product.number", ""); 
		assertValue("expenses.0.carrier.number", ""); 		
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");

		assertValue("description", "JUNIT EXPENSES");
		assertValue("expenses.0.invoice.KEY", "[.1.2002.]");
		assertValue("expenses.0.product.number", "4"); 
		assertValue("expenses.0.carrier.number", "3"); 		
		
		execute("CRUD.delete");
		assertNoErrors();
	}
			
}
