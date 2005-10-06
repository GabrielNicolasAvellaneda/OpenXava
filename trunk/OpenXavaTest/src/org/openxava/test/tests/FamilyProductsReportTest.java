package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamilyProductsReportTest extends ModuleTestBase {
	
		
	public FamilyProductsReportTest(String nombreTest) {
		super(nombreTest, "OpenXavaTest", "FamilyProductsReport");		
	}
	
	public void testReferenceWithNoSearch() throws Exception {
		assertNoAction("Reference.search");
		assertAction("Reference.createNew");
	}
	
}
