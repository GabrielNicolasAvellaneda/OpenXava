package org.openxava.test.tests;

import java.math.*;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class InvoicesFromDeliveriesTest extends ModuleTestBase {
	
	public InvoicesFromDeliveriesTest(String testName) {
		super(testName, "OpenXavaTest", "InvoicesFromDeliveries");		
	}
	
	public void testEditEntityDifferentFromOneFromTheModule() throws Exception {
		// Verifying the objects in list are deliveries, and there is one
		assertTrue("It must to have at least 1 delivery for run this test", getListRowCount() > 0);		
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Number");
		assertLabelInList(3, "Description");
		assertLabelInList(4, "Number");
		assertLabelInList(5, "Date");
		assertLabelInList(6, "Description");
		
		// Memorize the invoice of first delivery
		String invoiceYear = getValueInList(0,0);
		String invoiceNumber = getValueInList(0,1);
								
		// Verifying that edit that invoice						
		execute("Mode.detailAndFirst");		
		assertNoErrors();
		assertValue("Invoice", "year", invoiceYear);
		assertValue("Invoice", "number", invoiceNumber);
		
		execute("Sections.change", "activeSection=2");
		
		// Change the vat percentage
		setModel("Invoice");
		String vatPercentage = getValue("vatPercentage");		
		
		String newVatPercentage = new BigDecimal(vatPercentage).add(new BigDecimal("1")).toString();
				
		setValue("vatPercentage", newVatPercentage);
		
		execute("CRUD.save");
		assertNoErrors();
		
		// Verifying that it is changed going to list mode and return (consulting the first)
		execute("Mode.list");
		assertNoErrors();
		
		execute("Mode.detailAndFirst");
		assertValue("year", invoiceYear);
		assertValue("number", invoiceNumber);

		assertValue("vatPercentage", newVatPercentage);		
		
		// Restoring the original vat
		setValue("vatPercentage", vatPercentage);		
		execute("CRUD.save");
		assertNoErrors();		
	}
							
}
