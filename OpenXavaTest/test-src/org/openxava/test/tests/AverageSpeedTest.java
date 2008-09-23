package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class AverageSpeedTest extends ModuleTestBase {
	
	public AverageSpeedTest(String testName) {
		super(testName, "AverageSpeed");		
	}
	
	public void testSearchingInAReferenceByANonIdDoesNotUseLike() throws Exception {		
		execute("CRUD.new");
		setValue("vehicle.code", "VLV40");
		assertValue("vehicle.model", "S40 T5");
		setValue("vehicle.code", "");
		assertValue("vehicle.model", "");
		setValue("vehicle.code", "VLV");
		assertValue("vehicle.model", "");		
	}
	
}
