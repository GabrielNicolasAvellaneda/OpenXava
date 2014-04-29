package org.openxava.test.tests;

import java.net.*;

import org.openxava.tests.*;
import org.openxava.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

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
	
	public void testClickOnFileInListMode() throws Exception {
		assertListRowCount(1);
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertEquals("Result not is an video/webm", "video/webm", response.getContentType());
	}
	
	public void testClickOnFileInDetailMode() throws Exception {
		assertListRowCount(1);
		execute("Mode.detailAndFirst");
		assertValue("title", "FORREST GUMP");
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertEquals("Result not is an video/webm", "video/webm", response.getContentType());
	}
	
	public void testAddFile() throws Exception {
		addFile();
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertEquals("Result not is text/html", "text/html", response.getContentType());		
	}
	
	public void testChangeFile() throws Exception {
		addFile();
		execute("AttachedFiles.change", "newFileProperty=trailer");
		String filepath = System.getProperty("user.dir") + "/reports/Film.jrxml";
		setFileValue("newFile", filepath);
		execute("UploadFile.uploadFile");
		assertNoErrors();
		WebResponse response = getWebClient().getPage(getUrlToFile()).getWebResponse();
		assertEquals("Result not is application/docbook+xml", "application/docbook+xml", response.getContentType());
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
		HtmlPage page = (HtmlPage) getWebClient().getCurrentWindow().getEnclosedPage();
		String href = null;
		for(HtmlAnchor anchor : page.getAnchors()) {
			if(anchor.getHrefAttribute().indexOf("/xava/xfile?application=") >= 0) {
				href = anchor.getHrefAttribute(); break;
			}
		}		
		if(href == null) return null;
		URL url = page.getWebResponse().getWebRequest().getUrl();
		String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		return urlPrefix + href;
	}
}
