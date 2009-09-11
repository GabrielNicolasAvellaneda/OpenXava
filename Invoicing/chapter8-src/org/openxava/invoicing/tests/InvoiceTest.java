package org.openxava.invoicing.tests;

public class InvoiceTest extends CommercialDocumentTest {
	
	public InvoiceTest(String testName) { 
		super(testName, "Invoice"); 				
	}
	
	public void testAddOrders() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("orders", 0);
		execute("Collection.add", 
			"viewObject=xava_view_section1_orders");
		execute("AddToCollection.add", "row=0");
		assertCollectionRowCount("orders", 1);
		checkRowCollection("orders", 0);
		execute("Collection.removeSelected", 
			"viewObject=xava_view_section1_orders");
		assertCollectionRowCount("orders", 0);
	}
				
}
