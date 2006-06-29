package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class SizesTest extends ModuleTestBase {
	
	public SizesTest(String testName) {
		super(testName, "OpenXavaTest", "Sizes");		
	}
	
	public void testSequence() throws Exception {
		execute("List.orderBy", "property=id");
		execute("List.orderBy", "property=id");
		String last = getValueInList(0, "id");	
		execute("CRUD.new");
		setValue("name", "JUNIT SIZE " + (int) (Math.random() * 200));
		execute("CRUD.save");
		assertNoErrors();		
		execute("Mode.list");
		String next = String.valueOf(Integer.parseInt(last) + 1);
		assertValueInList(0, "id", next);		
	}
	
}
