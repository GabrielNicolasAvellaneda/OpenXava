package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class InvoicesNestedSectionsTest extends ModuleTestBase {
	

	public InvoicesNestedSectionsTest(String testName) {
		super(testName, "OpenXavaTest", "InvoicesNestedSections");		
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
									
}
