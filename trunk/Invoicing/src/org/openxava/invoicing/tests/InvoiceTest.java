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
	
	public void testTrash() throws Exception { // tmp
		assertListNotEmpty();
		int initialRowCount = getListRowCount(); 
		assertTrue("Must be less than 10 rows to run this test", 
			initialRowCount < 10);		
		String year1 = getValueInList(0, 0);
		String number1 = getValueInList(0, 1);
		execute("Mode.detailAndFirst");
		execute("Invoicing.delete");
		execute("Mode.list");
		
		assertListRowCount(initialRowCount - 1);
		assertDocumentNotInList(year1, number1);
		
		String year2 = getValueInList(0, 0);
		String number2 = getValueInList(0, 1);
		checkRow(0);
		execute("Invoicing.deleteSelected");
		assertListRowCount(initialRowCount - 2);
		assertDocumentNotInList(year2, number2);		
		
		changeModule("InvoiceTrash");
		assertListNotEmpty();
		int initialTrashRowCount = getListRowCount(); 
		assertTrue("Must be less than 10 rows to run this test", 
			initialRowCount < 10);
		assertDocumentInList(year1, number1);
		assertDocumentInList(year2, number2);		
 
		int row1 = getDocumentRowInList(year1, number1);
		execute("Trash.restore", "row=" + row1);
		assertListRowCount(initialTrashRowCount - 1);
		assertDocumentNotInList(year1, number1);
		
		int row2 = getDocumentRowInList(year2, number2);
		checkRow(row2);
		execute("Trash.restore");
		assertListRowCount(initialTrashRowCount - 2);
		assertDocumentNotInList(year2, number2);
		
		changeModule("Invoice");
		assertListRowCount(initialRowCount);
		assertDocumentInList(year1, number1);
		assertDocumentInList(year2, number2);		
	}
	
	private void assertDocumentNotInList(String year, String number) throws Exception {
		assertTrue("Document " + year + "/" + number + " must not be in list",
				getDocumentRowInList(year, number) < 0);
	}
		
	private void assertDocumentInList(String year, String number) throws Exception {
		assertTrue("Document " + year + "/" + number + " must be in list",
			getDocumentRowInList(year, number) >= 0);
	}
	
	private int getDocumentRowInList(String year, String number) throws Exception {
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			if (year.equals(getValueInList(i, 0)) &&
				number.equals(getValueInList(i, 1))) 
			{
				return i;				
			}
		}		
		return -1;
	}
	

	private void checkFirstOrderWithDeliveredEquals(String value) throws Exception {
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			if (value.equals(getValueInList(i, 2))) {
				checkRow(i);
				return;
			}
		}
		fail("Must be at least one row with delivered=" + value);
	}
	
					
}
