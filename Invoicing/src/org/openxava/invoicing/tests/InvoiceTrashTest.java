package org.openxava.invoicing.tests;

public class InvoiceTrashTest extends CommercialDocumentTest { // tmp quitar
	
	public InvoiceTrashTest(String testName) { 
		super(testName, "InvoiceTrash"); 				
	}
	
	public void testAddOrders() throws Exception {
		assertListNotEmpty();
		assertTrue("Must be less than 8 rows to run this test", 
			getListRowCount() < 8);
		execute("Mode.detailAndFirst");
		
	}
}
