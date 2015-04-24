package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class ProjectTest extends ModuleTestBase {
	
	public ProjectTest(String testName) {
		super(testName, "Project");		
	}
	
	public void testCreateNewElementInListWithOrderColumn() throws Exception { 
		execute("CRUD.new");
		setValue("name", "JUNIT PROJECT");
		assertCollectionRowCount("tasks", 0);	
		execute("Collection.new", "viewObject=xava_view_tasks");
		setValue("description", "THE JUNIT TASK");
		setValue("priority", "2");
		setValue("dueDate", "3/20/15");
		execute("Collection.save");
		assertNoErrors();
		assertCollectionRowCount("tasks", 1);
		assertValueInCollection("tasks", 0, 0, "THE JUNIT TASK");
		assertValueInCollection("tasks", 0, 1, "HIGH");
		assertValueInCollection("tasks", 0, 2, "3/20/15");
		execute("CRUD.delete");
		assertNoErrors();
	}
		
}
