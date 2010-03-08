package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class CommunityTest extends ModuleTestBase {
	
	public CommunityTest(String testName) {
		super(testName, "Community");		
	}
	
	public void testPolimorphingEditionOfCollectionElements() throws Exception { 		
		execute("Mode.detailAndFirst");
		assertValue("name", "PROGRAMMERS");
		assertCollectionRowCount("members", 3);
		execute("Collection.view", "row=0,viewObject=xava_view_members");
		assertValue("name", "JAVI");
		assertValue("favouriteFramework", "OPENXAVA");
		closeDialog();

		execute("Collection.view", "row=1,viewObject=xava_view_members");
		assertValue("name", "JUANJO");
		assertNotExists("favouriteFramework");
		assertValue("mainLanguage", "RPG");
	}
	
}
