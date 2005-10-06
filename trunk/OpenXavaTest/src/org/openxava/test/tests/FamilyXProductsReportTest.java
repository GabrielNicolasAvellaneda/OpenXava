package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class FamilyXProductsReportTest extends ModuleTestBase {
	
		
	public FamilyXProductsReportTest(String nombreTest) {
		super(nombreTest, "OpenXavaTest", "Family1ProductsReport");		
	}
	
	public void testDescriptionsComboCacheNotShared() throws Exception {
		String [][] subfamiliesFamily1 = {
			{ "", ""},
			{ "1", "DESARROLLO"},
			{ "2", "GESTION"},						  
			{ "3", "SISTEMA"}						
		};		
		assertValidValues("subfamily.number", subfamiliesFamily1);
		changeModule("Family2ProductsReport");
		String [][] subfamiliesFamily2 = {
			{ "", ""},
			{ "12", "PC"},
			{ "13", "PERIFERICOS"},			
			{ "11", "SERVIDORES"}						
		};		
		assertValidValues("subfamily.number", subfamiliesFamily2);
	}
	
	public void testJasperReportBaseActionTest() throws Exception {
		execute("FamilyProductsReport.generate");
		// Next line: test that errors of a ValidationException thrown from a action are shown
		assertError("Value for Subfamily in FilterBySubfamily is required"); 
		setValue("subfamily.number", "1");
		execute("FamilyProductsReport.generate"); // takes-long is tested too (only testing that no crash)
		assertContentTypeForPopup("application/pdf");
	}

}
