package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class InvoicesNoListTest extends ModuleTestBase {
	

	public InvoicesNoListTest(String testName) {
		super(testName, "InvoicesNoList");		
	}

	public void testSearchReferenceWithListInANoListModule() throws Exception {
		// execute("CRUD.new"); // Does not call to new in order to test a bug that arise in this case
		execute("Sections.change", "activeSection=1");
		execute("Collection.new", "viewObject=xava_view_section1_details");
		assertValue("details.product.description", "");
		execute("Reference.search", "keyProperty=xava.Invoice.details.product.number");
		int lastIndex = getListRowCount() - 1;		
		String description = getValueInList(lastIndex, 1);		
		execute("ReferenceSearch.choose", "row=" + lastIndex);
		assertNoErrors();
		assertValue("details.product.description", description);
	}
								
}
