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
		checkFirstOrderWithDeliveredEquals("Yes");
		checkFirstOrderWithDeliveredEquals("No");
		execute("AddToCollection.add"); 
		assertError("ERROR! 1 element(s) NOT added to Orders of Invoice");
		assertMessage("1 element(s) added to Orders of Invoice");
		assertCollectionRowCount("orders", 1);
		checkRowCollection("orders", 0);
		execute("Collection.removeSelected", 
			"viewObject=xava_view_section1_orders");
		assertCollectionRowCount("orders", 0);
	}
	
	private void checkFirstOrderWithDeliveredEquals(String value) throws Exception {
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			if (value.equals(getValueInList(i, 10))) { 
				checkRow(i);
				return;
			}
		}
		fail("Must be at least one row with delivered=" + value);
	}
	
					
}
