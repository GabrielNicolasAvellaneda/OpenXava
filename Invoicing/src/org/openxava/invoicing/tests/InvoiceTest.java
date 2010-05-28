package org.openxava.invoicing.tests;

public class InvoiceTest extends CommercialDocumentTest {
	
	public InvoiceTest(String testName) { 
		super(testName, "Invoice"); 				
	}
	
	public void testAddOrders() throws Exception { // tmp 
		assertListNotEmpty();
		execute("List.orderBy", "property=number"); 
		execute("Mode.detailAndFirst");
		String customerNumber = getValue("customer.number"); // tmp
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("orders", 0);
		execute("Invoice.addOrders", 
			"viewObject=xava_view_section1_orders");		
		assertCustomerInList(customerNumber); // tmp
		execute("AddOrdersToInvoice.add", "row=0"); 
		assertMessage("1 element(s) added to Orders of Invoice");
		assertCollectionRowCount("orders", 1);
		checkRowCollection("orders", 0);
		execute("Collection.removeSelected", 
			"viewObject=xava_view_section1_orders");
		assertCollectionRowCount("orders", 0);
	}
	
	public void testAddOrders2() throws Exception { // tmp Quitar 
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		// tmp String customerNumber = getValue("customer.number"); // tmp
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("orders", 0);
		// tmp execute("Collection.add",
		execute("Invoice.addOrders", // tmp
			"viewObject=xava_view_section1_orders");
		// tmp checkFirstOrderWithDeliveredEquals("Yes");
		// tmp checkFirstOrderWithDeliveredEquals("No");
		// tmp assertCustomerInList(customerNumber); // tmp
		// tmp assertValueForAllRows("delivered", "Yes"); // tmp
		
		// tmp execute("AddToCollection.add"); 
		execute("AddOrdersToInvoice.add", "row=0"); // tmp
		// tmp assertError("ERROR! 1 element(s) NOT added to Orders of Invoice");
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
