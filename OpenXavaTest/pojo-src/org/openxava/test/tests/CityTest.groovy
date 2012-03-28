package org.openxava.test.tests

import org.openxava.tests.*;

/**
 * Create on 27/02/2012 (10:56:25)
 * @author Ana Andres
 */
class CityTest extends ModuleTestBase{

	CityTest(String testName) {
		super(testName, "City")
	}
	
	void testStateFullNameWithFormulaFromAReference(){
		assertValueInList(0, 0, "1")
		assertValueInList(0, 1, "PHOENIX")
		assertLabelInList(2, "Full name with formula of State")
		assertValueInList(0, 2, "AZARIZONA")
	}
}
