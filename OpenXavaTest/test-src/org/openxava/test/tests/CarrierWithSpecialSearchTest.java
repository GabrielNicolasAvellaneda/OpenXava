package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;
import org.openxava.jpa.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */

public class CarrierWithSpecialSearchTest extends CarrierTestBase {
	
	public CarrierWithSpecialSearchTest(String testName) {
		super(testName, "CarrierWithSpecialSearch");		
	}
		
	public void testSearchCarrierWithDefaultValue() throws Exception {
		execute("CRUD.new");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "TRES");
		assertValue("number", "3");
		assertNoErrors();
	}

}
