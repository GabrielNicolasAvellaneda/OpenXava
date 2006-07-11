package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class ColorsTest extends ModuleTestBase {
	
	public ColorsTest(String testName) {
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
	
	public void testKeysWithZeroValue() throws Exception {
		assertValueInList(0, "number", "0");
		assertValueInList(0, "name", "ROJO");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("number", "0");
		assertValue("name", "ROJO");
	}		
	
}
