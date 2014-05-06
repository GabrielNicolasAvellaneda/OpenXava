package org.openxava.test.tests;

import java.util.*;

import org.apache.commons.lang.*;
import org.openxava.tests.*;
import org.openxava.util.*;
import org.openxava.application.meta.*;

import com.gargoylesoftware.htmlunit.html.*;

/**
 * @author Javier Paniza
 */

class ApplicantTest extends ModuleTestBase {
	
	private boolean modulesLimit = true;
	
	ApplicantTest(String testName) {
		super(testName, "Applicant")		
	}
	
	void testModulesMenu() {
		modulesLimit = false
		resetModule()
		
		assertModulesCount 15

		HtmlElement searchBox = htmlPage.getHtmlElementById("search_modules_text")
		searchBox.type "ca"
		assertEquals "ca", searchBox.getAttribute("value")
		webClient.waitForBackgroundJavaScriptStartingBefore 10000
		assertModulesCount 15
		
		HtmlAnchor loadMoreModules = htmlPage.getHtmlElementById("more_modules").getParentNode()
		loadMoreModules.click();
		webClient.waitForBackgroundJavaScriptStartingBefore 10000
		assertModulesCount 16 // We have to adjust this when we add new modules that content "ca"
		
		searchBox.type " \b"
		assertEquals "", searchBox.getAttribute("value")
		webClient.waitForBackgroundJavaScriptStartingBefore 10000
		assertModulesCount 15
		
		loadMoreModules = htmlPage.getHtmlElementById("more_modules").getParentNode()
		loadMoreModules.click()
		webClient.waitForBackgroundJavaScriptStartingBefore 10000
		assertModulesCount MetaApplications.getMetaApplication("OpenXavaTest").getMetaModules().size() + 2 // The +2 is because a bug that shows the abstract mapped superclasses too
	}

	
	void testPolymorphicReferenceFromBaseClass() {
		execute "Mode.detailAndFirst"
		assertNoErrors()
		assertValue "name", "JUANILLO"
		assertValue "skill.description", "PROGRAMMING"
		assertValue "skill.language", "JAVA"
		assertValue "platform", "MULTIPLATFORM"		 
	}
	
	void testHtmlHeadNotDuplicated() {
		String html = getHtmlPage().getWebResponse().getContentAsString()
		assertEquals 1, StringUtils.countMatches(html, "<head>")
	}
	
	@Override
	protected String getModuleURL() throws XavaException { 
		return modulesLimit?super.getModuleURL():"http://" + getHost() + ":" + getPort() + "/OpenXavaTest/modules/Applicant";
	}
	
	private void assertModulesCount(int expectedCount) {
		int count = htmlPage.getElementById("modules_list_popup").getElementsByAttribute("div", "class", "module-name").size()
		assertEquals expectedCount, count
	}
			
}
