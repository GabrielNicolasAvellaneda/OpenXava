package org.openxava.test.tests;

/**
 * 
 * 
 * @author Federico Alcántara 
 */

public class TreeViewDoubleEditorsTest extends TreeViewTestBase {
	public TreeViewDoubleEditorsTest(String testName) {
		super(testName, "TreeContainer");		
	}

	public void testForDoubleEditors() throws Exception {
		execute("Mode.detailAndFirst");
		assertValueInTreeView("treeItems", 0, "ROOT ITEM 1");
		assertValueInTreeView("treeItemTwos", 0, "ROOT ITEM 1");
	}
}
