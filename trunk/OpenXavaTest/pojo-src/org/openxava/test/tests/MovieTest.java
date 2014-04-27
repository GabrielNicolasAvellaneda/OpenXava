package org.openxava.test.tests;

import org.openxava.tests.*;

/**  
 * @author Jeromy Altuna
 */
public class MovieTest extends ModuleTestBase {
	
	public MovieTest(String testName) {
		super(testName, "Movie");
	}
	
	public void testPdfConcatReport() throws Exception {
		assertListRowCount(1);
		execute("Mode.detailAndFirst");
		assertAction("Movie.printDatasheet");
		assertValue("title", "FORREST GUMP");
		execute("Movie.printDatasheet");
		assertNoErrors();
		assertContentTypeForPopup("application/pdf");
		assertTrue(getPopupPDFAsText().indexOf("FORREST GUMP")>=0);
	}	
}
