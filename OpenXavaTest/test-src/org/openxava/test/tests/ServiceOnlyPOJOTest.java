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
		
		execute("Reference.search", "keyProperty=xava.Service.invoice.number");
		assertValueInList(0, 0, "2007");
		assertValueInList(0, 1, "1");
		assertValueInList(0, 2, "Primer servicio");
		execute("ReferenceSearch.choose", "row=0");
		assertValue("invoice.year", "2007");
		assertValue("invoice.number", "1");	
		assertValue("invoice.amount", "790.00");
		assertValue("invoice.description", "Primer servicio");	
		
	}
						
}
