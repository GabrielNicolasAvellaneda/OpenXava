package org.openxava.test.tests;

import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

class ApplicantTest extends ModuleTestBase {
	
	private String urlParameters = null;
	
	ApplicantTest(String testName) {
		super(testName, "Applicant")		
	}
	
	void testPolymorphicReferenceFromBaseClass() {
		execute "Mode.detailAndFirst"
		assertNoErrors()
		assertValue "name", "JUANILLO"
		assertValue "skill.description", "PROGRAMMING"
		assertValue "skill.language", "JAVA"
		assertValue "platform", "MULTIPLATFORM"		 
	}
	
	void testWithNoHtmlHead() { 
		String html = getHtmlPage().getWebResponse().getContentAsString()
		assertTrue html.contains("html>")
		assertTrue html.contains("<head>")
		assertTrue html.contains("</head>")
		assertTrue html.contains("<body")
		assertTrue html.contains("</body>")
		assertTrue html.contains("</html>")
		urlParameters = "htmlHead=false"
		resetModule()
		html = getHtmlPage().getWebResponse().getContentAsString()
		assertFalse html.contains("html>")
		assertFalse html.contains("<head>")
		assertFalse html.contains("</head>")
		assertFalse html.contains("<body")
		assertFalse html.contains("</body>")
		assertFalse html.contains("</html>")
	}
	
	@Override
	protected String getModuleURL() throws XavaException { 
		return urlParameters==null?super.getModuleURL():super.getModuleURL() + "?" + urlParameters;
	}
			
}
