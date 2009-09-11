package org.openxava.invoicing.tests;

public class OrderTest extends CommercialDocumentTest {
	
	public OrderTest(String testName) { 
		super(testName, "Order"); 				
	}
	
	public void testSetInvoice() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
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
	}
			
}
