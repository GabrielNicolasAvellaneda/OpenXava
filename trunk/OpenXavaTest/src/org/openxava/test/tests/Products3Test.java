package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Products3Test extends ModuleTestBase {
	
	public Products3Test(String testName) {
		super(testName, "OpenXavaTest", "Products3");		
	}
	
	public void testSearchingReferenceWithHiddenKeyTypingValue() throws Exception {
		execute("CRUD.new");
		assertEditable("family.number");
		assertValue("family.description", "");
		setValue("family.number", "1");
		assertValue("family.description", "SOFTWARE");
	}
	
	public void testReferenceWithHiddenKey_defaultValueCalculatorOnCreateWithJDBC() throws Exception {
		execute("CRUD.new");		
		assertValue("number", "78"); // to test default-value-calculator
		setValue("number", "66");
		setValue("description", "JUNIT PRODUCT");
		
		execute("Reference.search", "keyProperty=xava.Product3.family.number");
		String familyNumber = getValueInList(0, "number");		
		String familyDescription = getValueInList(0, "description");		
		execute("ReferenceSearch.choose", "row=0");
		assertValue("family.number", familyNumber);
		assertValue("family.description", familyDescription);
		
		execute("CRUD.save");
		assertNoErrors();
		assertValue("family.description", "");
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT PRODUCT");
		assertValue("family.number", familyNumber);
		assertValue("family.description", familyDescription);
		
		execute("CRUD.delete");			
		assertMessage("Product deleted successfully");
	}
	
	public void testSameAggregateTwiceWithDependentReferences() throws Exception {	
		String [][] familyValues = {
			{ "", "" },			
			{ "2", "HARDWARE" },
			{ "3", "SERVICIOS" },
			{ "1", "SOFTWARE" }
		};
		String [][] hardwareValues = {
			{ "", ""},
			{ "12", "PC"},
			{ "13", "PERIFERICOS"},			
			{ "11", "SERVIDORES"}						
		};
		String [][] softwareValues = {
			{ "", ""},
			{ "1", "DESARROLLO"},
			{ "2", "GESTION"},						  
			{ "3", "SISTEMA"}						
		};		
		String [][] voidValues = {
			{ "", "" },
		};
		
		execute("CRUD.new");		
		assertValidValues("subfamily1.family.number", familyValues);
		assertValidValues("subfamily1.subfamily.number", voidValues);
		assertValidValues("subfamily2.family.number", familyValues);
		assertValidValues("subfamily2.subfamily.number", voidValues);
		

		setValue("subfamily1.family.number", "2");
		assertValidValues("subfamily1.family.number", familyValues);
		assertValidValues("subfamily1.subfamily.number", hardwareValues);
		assertValidValues("subfamily2.family.number", familyValues);
		assertValidValues("subfamily2.subfamily.number", voidValues);
		
		setValue("subfamily2.family.number", "1");
		assertValidValues("subfamily1.family.number", familyValues);
		assertValidValues("subfamily1.subfamily.number", hardwareValues);
		assertValidValues("subfamily2.family.number", familyValues);
		assertValidValues("subfamily2.subfamily.number", softwareValues);		
	}
						
}
