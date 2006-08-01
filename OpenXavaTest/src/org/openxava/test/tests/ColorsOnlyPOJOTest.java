package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;



/**
 * Tests issue that only works with POJOs (EJB3 and Hibernate)
 * but not EJB2. <p> 
 * 
 * @author Javier Paniza
 */

public class ColorsOnlyPOJOTest extends ModuleTestBase {
	
	public ColorsOnlyPOJOTest(String testName) {
		super(testName, "OpenXavaTest", "Colors");		
	}
		
	public void testIdentityCalculator() throws Exception {
		execute("List.orderBy", "property=number");
		execute("List.orderBy", "property=number");
		String last = getValueInList(0, "number");	
		execute("CRUD.new");
		setValue("number", "-1"); // needed in this case because 0 is an existing key
		setValue("name", "JUNIT COLOR " + (int) (Math.random() * 200));
		execute("TypicalNotResetOnSave.save");
		assertNoErrors();						
		String next = String.valueOf(Integer.parseInt(last) + 1);
		assertValue("number", next);		
	}
		
}
