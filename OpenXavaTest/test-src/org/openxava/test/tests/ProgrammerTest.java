package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class ProgrammerTest extends ModuleTestBase {
	
	public ProgrammerTest(String testName) {
		super(testName, "Programmer");		
	}
	
	public void testInheritedEntityCRUD() throws Exception { 
		execute("CRUD.new");
		setValue("name", "JUNIT PROGRAMMER");
		setValue("sex", "1");
		setValue("mainLanguage", "EIFFEL");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("name", "");
		assertValue("sex", "");
		assertValue("mainLanguage", "");
		setValue("name", "JUNIT PROGRAMMER");
		execute("CRUD.search");
		assertValue("name", "JUNIT PROGRAMMER");
		assertValue("sex", "1");
		assertValue("mainLanguage", "EIFFEL");
		execute("CRUD.delete");
		assertMessage("Programmer deleted successfully");
	}
	
	public void testInheritedEntityWithChildrenList() throws Exception {
		assertListColumnCount(3);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Sex");
		assertLabelInList(2, "Main language");
		assertListRowCount(3);
		assertValueInList(0, 0, "JAVI"); 
		assertValueInList(1, 0, "JUANJO"); 				
		assertValueInList(2, 0, "DANI");
	}
		
}
