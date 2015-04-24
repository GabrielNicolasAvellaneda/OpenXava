package org.openxava.test.tests;

import org.apache.commons.lang.*;
import org.openxava.tests.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * @author Javier Paniza
 */

public class ApplicantTest extends ModuleTestBase {
	
	private boolean modulesLimit = true;
	
	public ApplicantTest(String testName) {
		super(testName, "Applicant");		
	}
	
	public void testGetEntityWithEmptyReferences_duplicateActionsNotAdded() throws Exception {  		
		assertListRowCount(1);
		execute("CRUD.new");
		assertNoAction("JPACRUD.create");
		execute("Applicant.showCreate");
		assertAction("JPACRUD.create");
		assertActionsCount("JPACRUD.create", 1);
		setValue("name", "JUNIT APPLICANT");
		execute("JPACRUD.create");
		assertNoErrors();
		execute("Mode.list");
		assertListRowCount(2);
		assertValueInList(1, 0, "JUNIT APPLICANT");
		execute("CRUD.deleteRow", "row=1");
		assertListRowCount(1);
	}
		
	private void assertActionsCount(String action, int expectedCount) { 
		String inputName = "ox_OpenXavaTest_Applicant__action___" + action.replace(".", "___"); 
		assertEquals(expectedCount, getHtmlPage().getElementsByName(inputName).size());		
	}
	
	public void assertHelp() throws Exception { 
		try {
			getHtmlPage().getAnchorByHref("http://openxava.wikispaces.com/help_en");
		}
		catch (ElementNotFoundException ex) {		
			fail("Help link is not correct"); 
		}
	}

	public void testModulesMenu_help() throws Exception { 
		modulesLimit = false;
		resetModule();
		
		assertModulesCount(15);
		assertFirstModuleInMenu("Abstract toto", "Abstract toto"); 

		HtmlElement searchBox = getHtmlPage().getHtmlElementById("search_modules_text");
		searchBox.type("CA");
		assertEquals("CA", searchBox.getAttribute("value"));
		getWebClient().waitForBackgroundJavaScriptStartingBefore(10000);
		assertModulesCount(15);
		assertFirstModuleInMenu("Academic years", "Academic years management");  
		
		HtmlAnchor loadMoreModules = (HtmlAnchor) getHtmlPage().getHtmlElementById("more_modules").getParentNode();
		loadMoreModules.click();
		getWebClient().waitForBackgroundJavaScriptStartingBefore(10000);
		assertModulesCount(19); // We have to adjust this when we add new modules that content "ca"
		
		searchBox.type(" \b");
		assertEquals("", searchBox.getAttribute("value"));
		getWebClient().waitForBackgroundJavaScriptStartingBefore(10000);
		assertModulesCount(15);
		
		loadMoreModules = (HtmlAnchor) getHtmlPage().getHtmlElementById("more_modules").getParentNode();
		loadMoreModules.click();
		getWebClient().waitForBackgroundJavaScriptStartingBefore(10000);
		assertTrue(getModulesCount() > 300);
		
		assertHelp();
	}

	private void assertFirstModuleInMenu(String expectedName, String expectedDescription) {
		HtmlElement module = getHtmlPage().getHtmlElementById("modules_list_popup").getElementsByAttribute("div", "class", "module-row ").get(0);
		HtmlElement moduleName = module.getElementsByAttribute("div", "class", "module-name").get(0);
		assertEquals(expectedName, moduleName.asText());
		HtmlElement moduleDescription = module.getElementsByAttribute("div", "class", "module-description").get(0);
		assertEquals(expectedDescription, moduleDescription.asText());
	}

	public void testPolymorphicReferenceFromBaseClass() throws Exception { 
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("name", "JUANILLO");
		assertValue("skill.description", "PROGRAMMING");
		assertValue("skill.language", "JAVA");
		assertValue("platform", "MULTIPLATFORM");		 
	}
	
	public void testHtmlHeadNotDuplicated() throws Exception {
		String html = getHtmlPage().getWebResponse().getContentAsString();
		assertEquals(1, StringUtils.countMatches(html, "<head>"));
	}
	
	protected String getModuleURL() { 
		return modulesLimit?super.getModuleURL():"http://" + getHost() + ":" + getPort() + "/OpenXavaTest/modules/Applicant";
	}
	
	private void assertModulesCount(int expectedCount) {
		assertEquals(expectedCount, getModulesCount());
	}
	
	private int getModulesCount() {
		return getHtmlPage().getHtmlElementById("modules_list_popup").getElementsByAttribute("div", "class", "module-name").size();
	}
			
}
