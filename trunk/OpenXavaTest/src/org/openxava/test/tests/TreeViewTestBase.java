package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;
import org.openxava.util.XavaResources;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlInput;



/**
 * @author Federico Alcantara
 */

public abstract class TreeViewTestBase extends ModuleTestBase {
	
	public TreeViewTestBase(String testName, String application, String module) {
		super(testName, application, module);
	}
	
	public TreeViewTestBase(String testName, String module) {
		super(testName, module);		
	}

	
	protected DomElement getTreeViewElementInRow(String collection, int row) {
		HtmlDivision div = (HtmlDivision) getHtmlPage().getElementById("tree_" + collection);
		return div.getElementById("ygtvcontentel" + (row+1));
	}
	
	protected String getValueInTreeView(String collection, int row) {
		String returnValue = "";
		DomElement element = getTreeViewElementInRow(collection, row);
		String value = element.getElementsByTagName("span").item(0)
				.getTextContent().toString();

		for (byte charByte: value.getBytes()) {
			if (charByte > 0) {
				char charValue = (char) charByte;
				returnValue = returnValue + charValue;
			} else {
				returnValue = returnValue + " ";
			}
		}
		return returnValue;
	}
		
	protected int getTreeViewRowCount(String collection) throws Exception {
		HtmlDivision div = (HtmlDivision) getForm().getPage().getElementById("openxavaInput_" + collection);
		return div.getElementsByTagName("input").getLength();
	}
	
	protected int getTreeViewTabRow(String collection, int row) {
		HtmlDivision div = (HtmlDivision) getForm().getPage().getElementById("openxavaInput_" + collection);
		HtmlInput element = (HtmlInput)div.getElementsByTagName("input").item(row);
		String tabRow = element.getAttribute("value");
		tabRow = tabRow.substring(tabRow.indexOf(":") + 1);
		return Integer.parseInt(tabRow);
	}
	
	protected void executeOnTreeViewItem(String collection, String action, int row) throws Exception {
		int tabRow = getTreeViewTabRow(collection, row);
		execute(action, "row=" + tabRow + ",viewObject=xava_view_" + collection);
	}
	
	protected void checkRowTreeView(String collection, int row) throws Exception {
		getHtmlPage().executeJavaScript("function checkRowTreeView() {var node=tree_" + collection + ".tree.getNodeByIndex(" + (row + 1) + ");" +
				"tree_" + collection + ".tree.fireEvent('clickEvent',{node:node});}; checkRowTreeView();");
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
		//System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "debug");
		//System.getProperties().put("com.gargoylesoftware.htmlunit.javascript", "debug");
		//Logger.getLogger("com.gargoylesoftware.htmlunit.javascript").setLevel(Level.FINE);

		super.resetModule();
	}
	
}
