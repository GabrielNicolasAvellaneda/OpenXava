package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class InvoiceDetailsWithTotalsTest extends ModuleTestBase {
	
	public InvoiceDetailsWithTotalsTest(String testName) {
		super(testName, "InvoiceDetailsWithTotals");		
	}
	
	public void testTotalsInCollection() throws Exception { 		
		execute("Mode.detailAndFirst");		
		
		assertTotalsInCollection("details", "", "");
		
		// The .00 is because unitPrice is not formatted in the same way in persistent and
		// calculated collection. Possibly it is a bug, but until we'll fix it we send the .00 
		assertTotalsInCollection("calculatedDetails", ".00", "Amounts sum"); 		
		
		assertNoAction("List.removeColumnSum");		
		
		execute("Navigation.next");

		assertValueInCollection("details", 0, "deliveryDate", "7/26/04");
		assertTotalInCollection("details", "deliveryDate", "7/26/04"); 		

		assertValueInCollection("details", 0, "product.unitPrice", "11");
		assertTotalInCollection("details", "product.unitPrice", "11");
		
		assertValueInCollection("details", 0, "amount", "11.00");
		assertTotalInCollection("details",    "amount", "11.00");
		assertTotalInCollection("details", 1, "amount",  "4.62");
		assertTotalInCollection("details", 2, "amount", "15.62");
		
		execute("Navigation.previous");
		
		execute("List.customize", "collection=details");
		execute("List.removeColumn", "columnIndex=2,collection=details");
		
		assertTotalInCollection("details", 1, 3,   "400.00");
		assertTotalInCollection("details", 2, 3, "2,900.00");		
		
		execute("Print.generatePdf", "viewObject=xava_view_details");
		assertContentTypeForPopup("application/pdf");
		
		execute("List.removeColumn", "columnIndex=3,collection=details");
		assertTotalInCollection("details", "deliveryDate", "12/15/10");			
	}
	
	private void assertTotalsInCollection(String collection, String unitPriceSuffix, String amountsSumLabel) throws Exception {
		assertCollectionRowCount(collection, 2);
		
		assertValueInCollection(collection, 0, "deliveryDate", "11/11/11");
		assertValueInCollection(collection, 1, "deliveryDate", "12/15/10");
		assertTotalInCollection(collection, "deliveryDate", "12/15/10"); 		

		assertValueInCollection(collection, 0, "product.unitPrice", "20" + unitPriceSuffix);
		assertValueInCollection(collection, 1, "product.unitPrice", "0" + unitPriceSuffix);
		assertTotalInCollection(collection, "product.unitPrice", "20" + unitPriceSuffix);
		
		assertValueInCollection(collection, 0, "amount", "2,000.00");
		assertValueInCollection(collection, 1, "amount",   "500.00");
		assertTotalInCollection(collection,    "amount", "2,500.00");
		assertTotalInCollection(collection, 1, "amount",   "400.00");
		assertTotalInCollection(collection, 2, "amount", "2,900.00");
		
		// Labels
		assertTotalInCollection(collection, "product.description", "Product unit price sum");
		assertTotalInCollection(collection, "quantity", amountsSumLabel);
		assertTotalInCollection(collection, 1, "quantity", "V.A.T.");
		assertTotalInCollection(collection, 2, "quantity", "Total");
	}
	
}
