package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;

/**
 * Create on 21/01/2009 (17:18:32)
 * @autor Ana Andrés
 */
public class ReturnPreviousModuleTest extends ModuleTestBase {
	
	public ReturnPreviousModuleTest(String testName) {
		super(testName, "ColorM1");		
	}
	
	/*
	 * - module1
	 * 	. module2
	 * 		' module2Sub1
	 * 		' module2Sub2
	 */
	public void testReturnPreviousModule() throws Exception {
		assertExists("property1");
		execute("ColorM1.changeModule2");
		assertExists("property2");
		execute("ReturnPreviousModule.return");
		assertExists("property1");
		execute("ColorM1.changeModule2");
		assertExists("property2");
		execute("ColorM2.changeModule2Sub1");
		assertExists("property2Sub1");
		execute("ReturnPreviousModule.return");
		assertExists("property2");
		execute("ReturnPreviousModule.return");
		assertExists("property1");
	}
	
}
