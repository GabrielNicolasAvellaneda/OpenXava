package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class InvoicesNestedSectionsTest extends ModuleTestBase {
	

	public InvoicesNestedSectionsTest(String testName) {
		super(testName, "InvoicesNestedSections");		
	}
	
	
	public void testCalculatedPropertiesDependingFromPropertiesInOtherSections() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=1");		
		execute("Sections.change", "activeSection=1,viewObject=xava_view_section1");
		assertValue("vatPercentage", "16"); // We rely on first invoice has this value 
		assertValue("vat", "400"); // We rely on first invoice has this value
		setValue("vatPercentage", "17");
		assertValue("vat", "425");
	}
		
	public void testNestedSections() throws Exception {
		// Level 0: Section 0
		execute("CRUD.new");
		assertExists("customer.type");
		assertNoAction("Collections.new");
				
		// Level 0: Section 1, Level 1: Section 0
		execute("Sections.change", "activeSection=1");
		assertNotExists("customer.type");
		assertAction("Collection.new");
		assertNotExists("vat");
		
		// Level 1: Section 1
		execute("Sections.change", "activeSection=1,viewObject=xava_view_section1");
		assertNotExists("customer.type");
		assertNoAction("Collection.new");
		assertExists("vat");
		assertNotExists("amountsSum");
		
		// Level 2: Section 1
		execute("Sections.change", "activeSection=1,viewObject=xava_view_section1_section1");
		assertNotExists("customer.type");
		assertNoAction("Collection.new");
		assertNotExists("vat");
		assertExists("amountsSum");
	}
	
	public void testReferenceActionsInNestedSections() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=1");
		execute("Collection.new", "viewObject=xava_view_section1_section0_details");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "MULTAS DE TRAFICO");
		execute("Reference.search", "keyProperty=xava.Invoice.details.product.number");
		assertNoErrors();
		execute("ReferenceSearch.cancel");
		execute("Reference.createNew", "keyProperty=xava.Invoice.details.product.number");
		assertNoErrors();
	}	
									
}
