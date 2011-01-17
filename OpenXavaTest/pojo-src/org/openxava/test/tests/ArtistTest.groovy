package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class ArtistTest extends ModuleTestBase {
	
	ArtistTest(String testName) {
		super(testName, "Artist")		
	}
	
	void testBeanValidationJSR303() throws Exception { // tmp
		execute "Mode.detailAndFirst"
		setValue "age", "120"		
		execute "CRUD.save"
		assertError "120 is not a valid value for age of Artist: must be less than or equal to 99"
	}
	
}
