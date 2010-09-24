package org.openxava.test.tests;

import org.openxava.jpa.XPersistence;
import org.openxava.tests.ModuleTestBase;

public class ValidateEmbeddedCollectionTest extends ModuleTestBase {
	public ValidateEmbeddedCollectionTest(String nameTest) {
		super (nameTest, "Parent");
	}
	

	@Override
	public void setUp() throws Exception {
		removeEntities();
		super.setUp();
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeEntities();
	}

	/**
	 * Tests for normal CRUD operation at parent level.
	 * @throws Exception
	 */
	public void testParentCRUD() throws Exception {
		execute("CRUD.new");
		setValue("parentId","FATHER");
		setValue("description", "THE FATHER");
		execute("CRUD.save");
		assertNoErrors();
		execute("CRUD.new");
		setValue("parentId","FATHER");
		execute("CRUD.refresh");
		assertValue("parentId","FATHER");
		assertValue("description", "THE FATHER");
		// now the delete
		execute("CRUD.delete");
		assertNoErrors();
		execute("CRUD.new");
		setValue("parentId","FATHER");
		execute("CRUD.refresh");
		assertValue("description", "");
	}
	
	/**
	 * Tests for normal CRUD operation at child level.
	 * @throws Exception
	 */
	public void testChildrenCRUD() throws Exception {
		execute("CRUD.new");
		setValue("parentId","FATHER");
		setValue("description", "THE FATHER");
		execute("CRUD.save");
		assertNoErrors();
		execute("CRUD.new");
		setValue("parentId","FATHER");
		execute("CRUD.refresh");
		execute("Collection.new", "viewObject=xava_view_children");
		setValue("childId", "JOHN");
		setValue("description", "THIS IS JOHN");
		execute("Collection.save");
		assertNoErrors();
		assertCollectionRowCount("children", 1);
		execute("Collection.removeSelected", "row=0,viewObject=xava_view_children");
	}
	
	/**
	 * Tests if you can add children directly when having a new
	 * parent record.
	 * @throws Exception
	 */
	public void testChildrenDirectCRUD() throws Exception {
		execute("CRUD.new");
		setValue("parentId","FATHER");
		setValue("description", "THE FATHER");
		execute("Collection.new", "viewObject=xava_view_children");
		setValue("childId", "JOHN");
		setValue("description", "THIS IS JOHN");
		execute("Collection.save");
		assertNoErrors();
		assertCollectionRowCount("children", 1);
		execute("Collection.removeSelected", "row=0,viewObject=xava_view_children");
	}
	
	private void removeEntities() throws Exception {
		XPersistence.getManager().createQuery("delete from Child").executeUpdate();
		XPersistence.getManager().createQuery("delete from Parent").executeUpdate();
		XPersistence.commit();
	}
	
}
