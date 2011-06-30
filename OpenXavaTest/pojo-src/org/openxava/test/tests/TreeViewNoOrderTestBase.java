package org.openxava.test.tests;

import java.util.Iterator;

import org.openxava.jpa.XPersistence;
import org.openxava.test.model.TreeContainerNoOrder;
import org.openxava.test.model.TreeItemNoOrder;
import org.openxava.tests.ModuleTestBase;
import org.openxava.util.XavaResources;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;

/**
 * 
 * @author Federico Alcantara
 */

public abstract class TreeViewNoOrderTestBase extends ModuleTestBase {
	
	public TreeViewNoOrderTestBase(String testName, String application, String module) {
		super(testName, application, module);
	}
	
	public TreeViewNoOrderTestBase(String testName, String module) {
		super(testName, module);		
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		populateTreeNoOrder();
	}
	
	@Override
	protected void tearDown() throws Exception {
		//populateTreeNoOrder();
		super.tearDown();
	}
	
	
	protected String getValueInTreeView(String collection, int row) {
		DomElement element = getTreeViewElementInRow(collection, row);
		String value = element.getElementsByTagName("span").item(0)
				.getTextContent().toString();
		return value.replace((char) 160, (char) 32); 
	}
		
	protected int getTreeViewRowCount(String collection) throws Exception {
		HtmlDivision div = (HtmlDivision) getForm().getPage().getElementById("openxavaInput_" + collection);
		return div.getElementsByTagName("input").getLength();
	}
	
	
	protected int getTreeViewTabRow(String collection, int row) {
		HtmlElement element = getTreeViewXavaElement(collection, row);
		String tabRow = element.getAttribute("value");
		tabRow = tabRow.substring(tabRow.indexOf(":") + 1);
		return Integer.parseInt(tabRow);
	}
	
	protected void executeOnTreeViewItem(String collection, String action, int row) throws Exception {
		int tabRow = getTreeViewTabRow(collection, row);
		execute(action, "row=" + tabRow + ",viewObject=xava_view_" + collection);
	}
	
	protected void checkRowTreeView(String collection, int row) throws Exception {
		HtmlInput input = getTreeViewXavaElement(collection, row);
		input.setChecked(true);
	}
	
	/** 
	 * Find the tree view element in a treeview collection
	 * @param collection name of the collection represented as tree
	 * @param row row to be found
	 * @return DomElement containing the tree item details
	 * @throws ElementNotFoundException
	 */
	protected DomElement getTreeViewElementInRow(String collection, int row) throws ElementNotFoundException {
		HtmlDivision div = (HtmlDivision) getHtmlPage().getElementById("tree_" + collection);
		Iterator<HtmlElement> it = div.getAllHtmlChildElements().iterator();
		int count = 0;
		while (it.hasNext()) {
			HtmlElement element = it.next();
			if (element.getId().startsWith("ygtvcontentel")) {
				if (count++ == row) {
					return element;
				}
			}
		}
		throw new ElementNotFoundException("tree_" + collection, "row", Integer.toString(row));
	}

	/**
	 * Finds the mapping of the treeview item to the IXTableModel 
	 * @param collection name of the collection
	 * @param row row to be look up for
	 * @return the HtmlInput element containg the details of the mapping
	 * @throws ElementNotFoundException
	 */
	protected HtmlInput getTreeViewXavaElement(String collection, int row) throws ElementNotFoundException {
		HtmlDivision div = (HtmlDivision) getForm().getPage().getElementById("openxavaInput_" + collection);
		Iterator<HtmlElement> elements = div.getAllHtmlChildElements().iterator();
		int count = 0;
		while (elements.hasNext()) {
			HtmlElement element = elements.next();
			if (element instanceof HtmlInput) {
				if (count++ == row ) {
					return (HtmlInput) element;
				}
			}
		}
		throw new ElementNotFoundException("tree_" + collection, "row", Integer.toString(row));
	}

	protected void assertTreeViewRowCount(String collection, int expectedCount) throws Exception {
		assertEquals(XavaResources.getString("collection_row_count", collection), expectedCount, getTreeViewRowCount(collection));
	}
	
	protected void assertValueInTreeView(String collection, int row, String value) throws Exception {
		String rowValue = getValueInTreeView(collection, row);
		assertEquals(XavaResources.getString("unexpected_value_in_collection", new Integer(0), new Integer(row), collection), value, rowValue);
	}
	protected void assertValueInTreeViewIgnoreCase(String collection, int row, String value) throws Exception {
		String rowValue = getValueInTreeView(collection, row);
		assertEquals(XavaResources.getString("unexpected_value_in_collection", new Integer(0), new Integer(row), collection), value.toUpperCase(), rowValue.toUpperCase());
	}

	@Override
	protected void resetModule() throws Exception {
		super.resetModule();
	}

	protected void populateTreeNoOrder() throws Exception {
		XPersistence.getManager().createQuery("delete from TreeItemNoOrder").executeUpdate();
		XPersistence.commit();
		TreeContainerNoOrder parent = XPersistence.getManager().find(TreeContainerNoOrder.class, 1);
		if (parent == null) {
			parent = new TreeContainerNoOrder();
			parent.setId(1);
			parent.setDescription("MY OWN TREE");
			parent = XPersistence.getManager().merge(parent);
		}
 		TreeItemNoOrder root = createTreeItem(parent, null, "ROOT ITEM 1");
		TreeItemNoOrder child1 = createTreeItem(parent, root, "CHILD ITEM 1");
		createTreeItem(parent, root, "CHILD ITEM 2");
		TreeItemNoOrder child3 = createTreeItem(parent, root, "CHILD ITEM 3");		
		createTreeItem(parent, child1, "SUBITEM 1 OF 1");
		createTreeItem(parent, child1, "SUBITEM 2 OF 1");
		createTreeItem(parent, child3, "SUBITEM 1 OF 3");		
		
		XPersistence.commit();
	}

	private TreeItemNoOrder createTreeItem(TreeContainerNoOrder container, TreeItemNoOrder parentTree, String description) throws Exception { 
		TreeItemNoOrder item = new TreeItemNoOrder();
		String path = "";
		if (parentTree != null) {
			path = parentTree.getPath() + "/" + parentTree.getId();
		}
		item.setPath(path);
		item.setDescription(description);
		item.setParentContainer(container);
		return XPersistence.getManager().merge(item);
	}		
}
