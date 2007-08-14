package org.openxava.school.tests;


import org.openxava.annotations.parse.*;
import org.openxava.component.*;
import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class TeachersTest extends ModuleTestBase {
	
	
	
	public TeachersTest(String testName) {
		super(testName, "MySchool", "Teachers");		
	}
	
	public void testTmp() throws Exception { // tmp
		System.out.println("[TeachersTest.testTmp] MetaComponent.getAll()="+MetaComponent.getAll()); //  tmp
		System.out.println("[TeachersTest.testTmp] AnnotatedClassParser.getManagedClassNames()="+AnnotatedClassParser.getManagedClassNames()); //  tmp
		
	}

	public void testCreateReadUpdateDelete() throws Exception {
		// Create
		execute("CRUD.new");
		setValue("id", "JU");
		setValue("name", "JUNIT Teacher");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("id", "");
		assertValue("name", "");
		
		// Read
		setValue("id", "JU");
		execute("CRUD.search");
		assertValue("id", "JU");
		assertValue("name", "JUNIT Teacher");
		
		// Modify
		setValue("name", "JUNIT Teacher MODIFIED");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("id", "");
		assertValue("name", "");
		
		// Verify if modified
		setValue("id", "JU");
		execute("CRUD.search");
		assertValue("id", "JU");
		assertValue("name", "JUNIT Teacher MODIFIED");
		
		// Delete it
		execute("CRUD.delete");		
		assertMessage("Teacher deleted successfully");				
	}
	
}
