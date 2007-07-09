package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class IssuesTest extends ModuleTestBase {
	
	public IssuesTest(String testName) {
		super(testName, "Issues");		
	}
	
	public void testDynamicChangeOfDefaultSchema() throws Exception {
		// We start on schema 'COMPANYA'
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
		execute("Mode.detailAndFirst");
		assertValue("id", "A0001");
		assertValue("description", "COMPANY A ISSUE 1");
		execute("Mode.list");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");

		// We change to 'COMPANYB' schema
		execute("Issues.changeToCompanyB");
		assertListRowCount(3);
		assertValueInList(0, "id", "B0001");
		assertValueInList(1, "id", "B0002");
		assertValueInList(2, "id", "B0003");
		execute("Mode.detailAndFirst");
		assertValue("id", "B0001");
		assertValue("description", "COMPANY B ISSUE 1");
		execute("Mode.list");
		assertListRowCount(3);
		assertValueInList(0, "id", "B0001");
		assertValueInList(1, "id", "B0002");
		assertValueInList(2, "id", "B0003");
		
		// At the end we return to 'COMPANYA' schema
		execute("Issues.changeToCompanyA");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
		execute("Mode.detailAndFirst");
		assertValue("id", "A0001");
		assertValue("description", "COMPANY A ISSUE 1");
		execute("Mode.list");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
	}	
	
}
