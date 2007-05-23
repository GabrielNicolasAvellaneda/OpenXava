package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */
public class FamilyRangeProductsReportTest extends ModuleTestBase {
	
	
	public FamilyRangeProductsReportTest(String testName) {
		super(testName, "FamilyRangeProductsReport");		
	}

	public void testDifferentLabelsForReferenceMembers() throws Exception {
		assertLabel("subfamily.number", "Number");
		assertLabel("subfamilyTo.number", "Number to");		
	}
	
}
