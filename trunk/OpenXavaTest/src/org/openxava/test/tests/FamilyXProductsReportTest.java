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

}
