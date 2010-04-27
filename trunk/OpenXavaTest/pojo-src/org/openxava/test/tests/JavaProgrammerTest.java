package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * 
 * @author Javier Paniza
 */

public class JavaProgrammerTest extends ModuleTestBase {
	
	public JavaProgrammerTest(String testName) {
		super(testName, "JavaProgrammer");		
	}
		
	public void test2LevelsInheritedEntityCRUD() throws Exception { 
		execute("CRUD.new");
		setValue("name", "JUNIT JAVA PROGRAMMER");
		setValue("sex", "1");
		setValue("mainLanguage", "JAVA");
		setValue("favouriteFramework", "OPENXAVA");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("name", "");
		assertValue("sex", "");
		assertValue("mainLanguage", "");
		assertValue("favouriteFramework", "");
		setValue("name", "JUNIT JAVA PROGRAMMER");
		execute("CRUD.refresh");
		assertValue("name", "JUNIT JAVA PROGRAMMER");
		assertValue("sex", "1");
		assertValue("mainLanguage", "JAVA");
		assertValue("favouriteFramework", "OPENXAVA");
		execute("CRUD.delete");
		assertMessage("Java programmer deleted successfully");
	}
	
	public void test2LevelsInheritedEntityWithBaseConditionList() throws Exception {
		assertListColumnCount(4);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Sex");
		assertLabelInList(2, "Main language");
		assertLabelInList(3, "Favourite framework");
		assertListRowCount(1);
		assertValueInList(0, 0, "JAVI");  				
	}
		
}
