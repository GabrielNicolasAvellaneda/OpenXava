package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class InvoiceCustomerAsAggregateTest extends ModuleTestBase {
	
	public InvoiceCustomerAsAggregateTest(String testName) {
		super(testName, "InvoiceCustomerAsAggregate");		
	}
	
	public void testCollectionsInsideReferenceAsEmbedded() throws Exception {
		execute("Mode.detailAndFirst");
		assertCollectionRowCount("customer.deliveryPlaces", 0);
		execute("Collection.new", "viewObject=xava_view_customer_deliveryPlaces");
		setValue("customer.deliveryPlaces.name", "JUNIT DELIVERY PLACE NAME");
		setValue("customer.deliveryPlaces.address", "JUNIT DELIVERY PLACE ADDRESS");
		execute("Collection.save", "viewObject=xava_view_customer_deliveryPlaces");
		assertNoErrors();
		assertCollectionRowCount("customer.deliveryPlaces", 1);
		execute("Collection.hideDetail", "viewObject=xava_view_customer_deliveryPlaces");
		checkRowCollection("customer.deliveryPlaces", 0);
		execute("Collection.removeSelected", "viewObject=xava_view_customer_deliveryPlaces");
		assertNoErrors();
		assertCollectionRowCount("customer.deliveryPlaces", 0);		
	}
									
}
