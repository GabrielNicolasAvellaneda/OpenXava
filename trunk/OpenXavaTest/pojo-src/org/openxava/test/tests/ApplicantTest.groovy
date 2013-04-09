package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

class ApplicantTest extends ModuleTestBase {
	
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
			
}
