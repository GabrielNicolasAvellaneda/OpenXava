package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase; 

import com.gargoylesoftware.htmlunit.html.*;

/**
 * 
 * @author Javier Paniza
 */
abstract public class CustomizeListTestBase extends ModuleTestBase {
	
	private String module;
	
	public CustomizeListTestBase(String testName, String module) {
		super(testName, module);
		this.module = module;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void removeColumn(int index) throws Exception {
		removeColumn("list", "xava_tab", index);
	}
	
	protected void removeColumn(String collection, int index) throws Exception {
		removeColumn(collection, "xava_collectionTab_" + collection, index);
	}
	
	private void removeColumn(String collection, String tabObject, int index) throws Exception {
		getWebClient().setCssEnabled(true);
		HtmlElement removeLink = getHtmlPage().getAnchorByHref(
			"javascript:openxava.removeColumn('OpenXavaTest', '" + module + "', 'ox_OpenXavaTest_" + module + "__" + collection +"_col" + index + "', '" + tabObject + "')");
		removeLink.click();
		Thread.sleep(500);		
	}

}
