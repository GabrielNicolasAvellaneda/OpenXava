package org.openxava.test.tests;

import org.openxava.tests.*;

import com.gargoylesoftware.htmlunit.html.*;


/**
 * 
 * @author Javier Paniza
 */

public class ReallocationDetailsEditOnlyTest extends ModuleTestBase {
	
	public ReallocationDetailsEditOnlyTest(String testName) {
		super(testName, "ReallocationDetailsEditOnly");		
	}	
	
	public void testDefaultValueCalculatorForReferencesInElementCollections() throws Exception {
		execute("Mode.detailAndFirst");
		assertValue("description", "THE BIG REALLOCATION");
		assertNoErrors();
		
		assertCollectionRowCount("details", 3);

		assertValueInCollection("details", 0, "place", "VALENCIA OFFICE");
		assertValueInCollection("details", 0, "product.number", "1");
		assertValueInCollection("details", 0, "product.description", "MULTAS DE TRAFICO");
		assertValueInCollection("details", 0, "product.unitPrice", "11.00");
		
		assertValueInCollection("details", 2, "place", "CASA EN MICHIGAN");
		assertValueInCollection("details", 2, "product.number", "3");
		assertValueInCollection("details", 2, "product.description", "XAVA");
		assertValueInCollection("details", 2, "product.unitPrice", "0.00");
		
		assertEditableInCollection("details", 0, "place");
		assertEditableInCollection("details", 0, "product.number");
		assertNoEditableInCollection("details", 0, "product.description");
		assertNoEditableInCollection("details", 0, "product.unitPrice");
		 
		assertEditableInCollection("details", 2, "place");
		assertEditableInCollection("details", 2, "product.number");
		assertNoEditableInCollection("details", 2, "product.description");
		assertNoEditableInCollection("details", 2, "product.unitPrice");
		
		HtmlTable table = (HtmlTable) getHtmlPage().getElementById("ox_OpenXavaTest_ReallocationDetailsEditOnly__details");
		assertEquals(4, table.getRowCount());
		String html = getHtml(); 
		assertFalse(html.contains("elementCollectionEditor.removeRow("));
		assertTrue(html.contains("Reference.search"));
	}
	
}
