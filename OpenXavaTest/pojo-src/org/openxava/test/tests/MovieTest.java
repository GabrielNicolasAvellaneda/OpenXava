package org.openxava.test.tests;

import java.util.*;

import org.openxava.tests.*;
import org.openxava.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

/** 
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
		WebResponse response = getWebClient().getPage(getUrlToFile(0)).getWebResponse();
		assertTrue(response.getContentType().equals("video/webm") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testClickOnFileInDetailMode() throws Exception {
		assertListRowCount(1);
		execute("Mode.detailAndFirst");
		assertValue("title", "FORREST GUMP");
		WebResponse response = getWebClient().getPage(getUrlToFile(0)).getWebResponse();
		assertTrue(response.getContentType().equals("video/webm") || 
				   response.getContentType().equals(MIME_UNKNOWN));
	}
	
	public void testAddFile() throws Exception {
		addFile();
		WebResponse response = getWebClient().getPage(getUrlToFile(0)).getWebResponse();
		assertTrue(response.getContentType().equals("text/html") || 
				   response.getContentType().equals(MIME_UNKNOWN));
		changeModule("Movie");
		execute("AttachedFile.delete", "newFileProperty=trailer");
	}
	
	public void testChangeFile() throws Exception {
		addFile();
		execute("AttachedFile.choose", "newFileProperty=trailer");
		String filepath = System.getProperty("user.dir") + "/reports/Film.jrxml";
		setFileValue("newFile", filepath);
		execute("UploadFile.uploadFile");
		assertNoErrors();
		WebResponse response = getWebClient().getPage(getUrlToFile(0)).getWebResponse();
		assertTrue(response.getContentType().equals("application/docbook+xml") || 
				   response.getContentType().equals(MIME_UNKNOWN));
		changeModule("Movie");
		execute("AttachedFile.delete", "newFileProperty=trailer");
	}
	
	public void testDeleteFile() throws Exception {
		addFile();
		assertTrue("Trailer has no value", !Is.emptyString(getValue("trailer")));
		assertAction("AttachedFile.delete");
		execute("AttachedFile.delete", "newFileProperty=trailer");
		assertNoErrors();
		assertTrue("Trailer has value", Is.emptyString(getValue("trailer")));
	}
	
	public void testFilesLibrary() throws Exception {
		assertListRowCount(1);
		execute("Mode.detailAndFirst");
		assertTrue("At least 4 files", countFiles() == 4);	
		
		//Adding one file
		execute("Library.add", "newLibraryProperty=scripts");
		assertDialogTitle("Add files");
		String filepath  = System.getProperty("user.dir") + "/reports/Corporation.html";
		setFileValue("newFile", filepath);
		execute("UploadFileIntoLibrary.uploadFile");
		assertMessage("File added to the library");
		assertTrue("At least 5 files", countFiles() == 5);
		
		//Display file
		String url = getUrlToFile(1); 
		WebResponse response = getWebClient().getPage(url).getWebResponse();
		assertTrue(response.getContentType().equals("text/html") || 
				   response.getContentType().equals(MIME_UNKNOWN));
		changeModule("Movie");
		
		//Removing the file
		assertAction("Library.remove");
		execute("Library.remove", url.split("&")[2]);
		assertNoErrors();
		assertTrue("At least 4 files", countFiles() == 4);		
	}
	
	private void addFile() throws Exception {
		execute("CRUD.new");
		assertAction("AttachedFile.choose");
		execute("AttachedFile.choose", "newFileProperty=trailer");
		assertNoErrors();
		assertAction("UploadFile.uploadFile");
		String filepath = System.getProperty("user.dir") + "/reports/Corporation.html";
		setFileValue("newFile", filepath);
		execute("UploadFile.uploadFile");
		assertNoErrors();
	}	
		
	private String getUrlToFile(int index) {
		String href = getFileAnchors().get(index).getHrefAttribute();
		return "http://" + getHost() + ":" + getPort() + href;
	}
	
	private int countFiles() {
		return getFileAnchors().size();
	}
	
	private List<HtmlAnchor> getFileAnchors() {
		List<HtmlAnchor> anchors = getHtmlPage().getAnchors();
		for(Iterator<HtmlAnchor> it = anchors.iterator(); it.hasNext(); ) {
			HtmlAnchor anchor = it.next();
			if(!(anchor.getHrefAttribute().indexOf("/xava/xfile?application=") >=0)) {
				it.remove();
			}			
		}
		return anchors;
	}
}
