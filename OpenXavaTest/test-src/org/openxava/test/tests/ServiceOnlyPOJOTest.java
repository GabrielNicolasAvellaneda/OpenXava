package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class ServiceOnlyPOJOTest extends ModuleTestBase {
			
	public ServiceOnlyPOJOTest(String nombreTest) {
		super(nombreTest, "Service");		
	}
	
	// This test does not work with EJB2, because
	// it uses a reference to ServiceInvoice that relies
	// in hibernate default schema.
	public void testSearchKey() throws Exception { 		
		execute("CRUD.new");
		execute("Sections.change", "activeSection=1");
		
		assertEditable("invoice.year");
		assertEditable("invoice.number");
		assertNoEditable("invoice.amount");
		assertNoEditable("invoice.description");
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");
		assertValue("invoice.amount", "");
		assertValue("invoice.description", "");
		
		setValue("invoice.year", "2007");
		assertValue("invoice.amount", "");
		assertValue("invoice.description", "");
		
		setValue("invoice.number", "2");
		assertValue("invoice.amount", "1,730.00");
		assertValue("invoice.description", "Second service");		
	}
						
}
