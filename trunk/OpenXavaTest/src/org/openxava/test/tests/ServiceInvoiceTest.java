package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class ServiceInvoiceTest extends ModuleTestBase {
	
	public ServiceInvoiceTest(String testName) {
		super(testName, "ServiceInvoice");		
	}
	
	public void testDefaultSchemaInHibernateCfg() throws Exception {
		assertListNotEmpty();
	}
	
	public void testSearchKeyMustBeEditable() throws Exception {
		execute("Mode.detailAndFirst");
		assertEditable("year");
		assertEditable("number");
	}
		
}
