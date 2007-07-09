package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class InvoicesDeliveriesTest extends ModuleTestBase {
	
	public InvoicesDeliveriesTest(String testName) {
		super(testName, "InvoicesDeliveries");		
	}
	
	public void testOverwriteViewActionInCollection() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Invoices.viewDelivery", "row=0,viewObject=xava_view_deliveries");		
		assertMessage("Delivery displayed");
	}
									
}
