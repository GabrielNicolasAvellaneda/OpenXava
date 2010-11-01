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
		//TODO: This seems to be not implemented in YUI2
		//assertValueInTreeView("treeItems", 0, "ROOT ITEM 1");
		//assertValueInTreeView("treeItemTwos", 0, "2-ROOT ITEM 1");
	}
}
