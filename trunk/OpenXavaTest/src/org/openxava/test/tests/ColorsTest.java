package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class ColorsTest extends ModuleTestBase {
	
	public ColorsTest(String testName) {
		super(testName, "OpenXavaTest", "Colors");		
	}

	
	public void testKeysWithZeroValue() throws Exception {
		assertValueInList(0, "number", "0");
		assertValueInList(0, "name", "ROJO");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("number", "0");
		assertValue("name", "ROJO");
	}
	
}
