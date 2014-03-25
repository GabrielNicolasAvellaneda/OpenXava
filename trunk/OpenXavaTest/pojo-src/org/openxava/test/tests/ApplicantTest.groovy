package org.openxava.test.tests;

import org.apache.commons.lang.*;
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
	
	void testHtmlHeadNotDuplicated() {
		String html = getHtmlPage().getWebResponse().getContentAsString()
		assertEquals 1, StringUtils.countMatches(html, "<head>")
	}
	
	@Override
	protected String getModuleURL() throws XavaException { 
		return urlParameters==null?super.getModuleURL():super.getModuleURL() + "?" + urlParameters;
	}
			
}
