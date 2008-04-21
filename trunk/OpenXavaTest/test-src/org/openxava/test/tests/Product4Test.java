package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Product4Test extends ModuleTestBase {
	
		
	public Product4Test(String testName) {
		super(testName, "Product4");		
	}
	
	public void testCalculatedPropertyWhenAnnotatedGetters_genericI18nForTabs() throws Exception {
		assertLabelInList(2, "Family");
		assertLabelInList(3, "Subfamily");
		
		execute("CRUD.new");
		assertEditable("unitPrice");
		assertNoEditable("unitPriceInPesetas");
		assertValue("unitPrice", "");
		assertValue("unitPriceInPesetas", "");
		setValue("unitPrice", "10");
		assertValue("unitPriceInPesetas", "1,664");		
	}
		
}
