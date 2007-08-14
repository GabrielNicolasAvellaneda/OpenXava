package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class IssueTest extends ModuleTestBase {
	
	public IssueTest(String testName) {
		super(testName, "Issue");		
	}
	
	public void testDynamicChangeOfDefaultSchema() throws Exception {
		// We start on schema 'COMPANYA'
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
		execute("Mode.detailAndFirst");
		assertValue("id", "A0001");
		assertValue("description", "COMPANY A ISSUE 1");
		assertValueInCollection("comments", 0, "comment", "Comment on A0001");
		execute("Mode.list");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");

		// We change to 'COMPANYB' schema
		execute("Issue.changeToCompanyB");
		assertListRowCount(3);
		assertValueInList(0, "id", "B0001");
		assertValueInList(1, "id", "B0002");
		assertValueInList(2, "id", "B0003");
		execute("Mode.detailAndFirst");
		assertValue("id", "B0001");
		assertValue("description", "COMPANY B ISSUE 1");
		assertValueInCollection("comments", 0, "comment", "Comment on B0001");
		execute("Mode.list");
		assertListRowCount(3);
		assertValueInList(0, "id", "B0001");
		assertValueInList(1, "id", "B0002");
		assertValueInList(2, "id", "B0003");
		
		// At the end we return to 'COMPANYA' schema
		execute("Issue.changeToCompanyA");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
		execute("Mode.detailAndFirst");
		assertValue("id", "A0001");
		assertValue("description", "COMPANY A ISSUE 1");
		assertValueInCollection("comments", 0, "comment", "Comment on A0001");
		execute("Mode.list");
		assertListRowCount(2);
		assertValueInList(0, "id", "A0001");
		assertValueInList(1, "id", "A0002");
	}	
	
}
