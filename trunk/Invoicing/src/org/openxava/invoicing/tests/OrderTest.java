package org.openxava.invoicing.tests;

import sun.reflect.ReflectionFactory.*;

public class OrderTest extends CommercialDocumentTest {
	
	public OrderTest(String testName) { 
		super(testName, "Order"); 				
	}
	
	public void testCreateInvoiceFromOrder() throws Exception { // tmp
		viewDetailWhere("delivered", "Yes");
		assertValue("delivered", "true");		
		int orderDetailCount = getCollectionRowCount("details");
		
	}
	
	private void viewDetailWhere(String property, String value) throws Exception { // tmp
		int rowCount = getListRowCount();		
		for (int row=0; row<rowCount; row++) {			
			if (value.equals(getValueInList(row, property))) {
				execute("List.viewDetail", "row=" + row);
				return;
			}
		}		
		fail("No encontrado " + property + "=" + value); // tmp i18n		
	}

	public void testSetInvoice() throws Exception {
		assertListNotEmpty();
		execute("List.orderBy", "property=number"); 
		execute("Mode.detailAndFirst");
		assertValue("delivered", "false"); 
		execute("Sections.change", "activeSection=1");
		assertValue("invoice.number", "");
		assertValue("invoice.year", "");
		execute("Reference.search", 
			"keyProperty=invoice.year");
		String year = getValueInList(0, "year");
		String number = getValueInList(0, "number");		
		execute("ReferenceSearch.choose", "row=0");
		assertValue("invoice.year", year);
		assertValue("invoice.number", number);

		// Not delivered order cannot have invoice 
		execute("CRUD.save"); 
		assertErrorsCount(1); 
		setValue("delivered", "true");
		execute("CRUD.save");
		assertNoErrors();
		
		// Order with invoice cannot be deleted
		execute("Mode.list");
		execute("Mode.detailAndFirst");		
		execute("Invoicing.delete"); 
		assertErrorsCount(1);
		
		// Restore values
		setValue("delivered", "false");
		setValue("invoice.year", "");
		execute("CRUD.save");
		assertNoErrors();
	}
			
}
