package org.openxava.test.tests;

import java.util.*;

import org.openxava.jpa.XPersistence;
import org.openxava.test.model.Issue;
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
	
	public void testSearchReferenceWithDynamicChangeOfDefaultSchema() throws Exception {
		execute("CRUD.new");
		// Searching with list
		execute("Reference.search", "keyProperty=xava.Issue.worker.nickName");
		assertListRowCount(1);
		assertValueInList(0, 0, "JAVI");
		execute("ReferenceSearch.choose", "row=0");
		assertNoErrors();
		assertValue("worker.nickName", "JAVI");
		assertValue("worker.fullName", "FRANCISCO JAVIER PANIZA LUCAS");
		
		// Searching on change key
		setValue("worker.nickName", "");
		assertValue("worker.fullName", "");
		setValue("worker.nickName", "JAVI");
		assertValue("worker.nickName", "JAVI");
		assertValue("worker.fullName", "FRANCISCO JAVIER PANIZA LUCAS");		
	}
	
	public void testGenerateExcellWithTextThatContainsSemicolon() throws Exception {
		// create description whit semicolon
		execute("CRUD.new");
		setValue("id", "ABCDE");
		setValue("description", "UNO;\"DOS\";TRES");
		execute("CRUD.save");
		assertNoErrors();
		execute("Mode.list");
		
		// filter by the new and generate csv
		setConditionValues(new String[] { "ABCDE" });
		execute("List.filter");
		assertListRowCount(1);
		execute("Print.generateExcel");
		assertContentTypeForPopup("text/x-csv");

		String expectedLine = "\"ABCDE\";\"UNO;\"\"DOS\"\";TRES\"";
		StringTokenizer excel = new StringTokenizer(getPopupText(), "\n\r");
		String header = excel.nextToken();
		assertEquals("header", "Id;Description", header);		
		String line1 = excel.nextToken();
		assertEquals("line1", expectedLine, line1);
		assertTrue("Only one line must have generated", !excel.hasMoreTokens());
		
		// delete description with semicolon
		assertListRowCount(1);
		checkAll();
		execute("CRUD.deleteSelected");		
		assertNoErrors();
	}
	
	public void testRequiredOnReferenceToParent() throws Exception {
		execute("CRUD.new");
		setValue("id", "JUNIT");
		setValue("description", "JUNIT ISSUE");
		setValue("worker.nickName", "JAVI");
		assertValue("worker.fullName", "FRANCISCO JAVIER PANIZA LUCAS");
		assertCollectionRowCount("comments", 0);
		execute("Collection.new", "viewObject=xava_view_comments");
		setValue("comment", "Created from a JUNIT test");
		execute("Collection.save");
		assertNoErrors();
		assertCollectionRowCount("comments", 1);
		execute("CRUD.delete");
		assertNoErrors();
	}
		
}
