package org.openxava.test.tests;

import org.openxava.tests.*;
import org.openxava.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * 
 * @author Jeromy Altuna
 */

public class MovieTest extends ModuleTestBase {
	
	private static final String MIME_UNKNOWN = "application/octet-stream";

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
		assertTrue(getPopupPDFPageCount() == 3);
	}
	
	public void testClickOnFileInListMode() throws Exception {
		assertListRowCount(1);
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertTrue(response.getContentType().equals("video/webm") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testClickOnFileInDetailMode() throws Exception {
		assertListRowCount(1);
		execute("Mode.detailAndFirst");
		assertValue("title", "FORREST GUMP");
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertTrue(response.getContentType().equals("video/webm") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testAddFile() throws Exception {
		addFile();
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertTrue(response.getContentType().equals("text/html") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testChangeFile() throws Exception {
		addFile();
		execute("AttachedFiles.change", "newFileProperty=trailer");
		String filepath = System.getProperty("user.dir") + "/reports/Film.jrxml";
		setFileValue("newFile", filepath);
		execute("UploadFile.uploadFile");
		assertNoErrors();
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertTrue(response.getContentType().equals("application/docbook+xml") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testDeleteFile() throws Exception {
		addFile();
		assertTrue("Trailer has no value", !Is.emptyString(getValue("trailer")));
		assertAction("AttachedFiles.delete");
		execute("AttachedFiles.delete", "newFileProperty=trailer");
		assertNoErrors();
		assertTrue("Trailer has value", Is.emptyString(getValue("trailer")));
	}
	
	private void addFile() throws Exception {
		execute("CRUD.new");
		assertAction("AttachedFiles.change");
		execute("AttachedFiles.change", "newFileProperty=trailer");
		assertNoErrors();
		assertAction("UploadFile.uploadFile");
		String filepath = System.getProperty("user.dir") + "/reports/Corporation.html";
		setFileValue("newFile", filepath);
		execute("UploadFile.uploadFile");
		assertNoErrors();
	}	
		
	private String getUrlToFile() {
		String href = null;
		for(HtmlAnchor anchor : getHtmlPage().getAnchors()) {
			if(anchor.getHrefAttribute().indexOf("/xava/xfile?application=") >= 0) {
				href = anchor.getHrefAttribute(); break;
			}
		}		
		return "http://" + getHost() + ":" + getPort() + href;
	}	
}
