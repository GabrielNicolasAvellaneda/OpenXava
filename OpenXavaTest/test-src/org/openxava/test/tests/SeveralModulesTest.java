package org.openxava.test.tests;

import java.io.*;
import java.net.*;

import org.openxava.tests.*;
import org.openxava.util.*;
import org.openxava.web.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * 
 * JUnit:
 * * Acciones
 * * Evento al-cambiar  
 * * Secciones (Formula)
 * * Seleccionar en una lista y colecci�n (Carrier)
 * * Foco
 * * Upload file (Customer)
 * * Dos portlets del mismo modelo
 * 
 * A mano:
 * * HTMLEditor (Formula)
 * * Imagen de proceso por lotes
 * * Mostra/ocultar filtros en lista/colecci�n
 * * Clear condition values
 * * Atajos de teclado
 * - Dentro del portal: 
 * 		- WPS6.1 (y revisar est�tica)
 * 		- Liferay 5.1 con suite completa 
 * - Probar en todos los navegadores
 * 
 * Otros:
 * - En migration:
 * 		// tmp execute("Delivery.generateNumber", "xava.keyProperty=xava.Delivery.number"); 
 *		execute("Delivery.generateNumber", "xava.keyProperty=number"); // tmp
 *
 * 		// tmp HtmlImage image = (HtmlImage) page.getHtmlElementsByName("xava.Formula.ingredients.image").get(0);		
		HtmlImage image = (HtmlImage) page.getHtmlElementsByName(decorateId("ingredients.image")).get(0); // tmp

 * 
 * @author Javier Paniza
 */

public class SeveralModulesTest extends ModuleTestBase {
	
	public SeveralModulesTest(String testName) {
		super(testName, "Carrier");		
	}
	
	public void testSeveralModulesInSamePage() throws Exception { 
		assertActions();
		assertOnChangeEvent();		
		assertFocusOn("relationWithSeller");		
		assertSections();
		assertCollections();
		assertUploadFiles();				
	}

	private void assertUploadFiles() throws Exception, IOException,
			MalformedURLException {
		selectModuleInPage("Customer");
		execute("ImageEditor.changeImage", "newImageProperty=photo");
		assertNoErrors();
		assertAction("LoadImage.loadImage");		
		String imageUrl = System.getProperty("user.dir") + "/test-images/foto_javi.jpg";
		setFileValue("newImage", imageUrl);
		execute("LoadImage.loadImage");
		assertNoErrors();
		
		HtmlPage page = (HtmlPage) getWebClient().getCurrentWindow().getEnclosedPage();		
		URL url = page.getWebResponse().getUrl();
		String urlPrefix = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		
		
		HtmlImage image = (HtmlImage) page.getHtmlElementsByName(Ids.decorate("OpenXavaTest", "Customer", "photo")).get(0);
		String imageURL = null;
		if (image.getSrcAttribute().startsWith("/")) {
			imageURL = urlPrefix + image.getSrcAttribute();
		}
		else {
			String urlBase = Strings.noLastToken(url.getPath(), "/");
			imageURL = urlPrefix + urlBase + image.getSrcAttribute();
		}		
		WebResponse response = getWebClient().getPage(imageURL).getWebResponse();		
		assertTrue("Image not obtained", response.getContentAsString().length() > 0);
		assertEquals("Result is not an image", "image", response.getContentType());
	}

	private void assertCollections() throws Exception {
		selectModuleInPage("Carrier");
		execute("CRUD.new");
		setValue("number", "1");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "UNO");
		assertCollectionRowCount("fellowCarriers", 3);
		assertValueInCollection("fellowCarriers", 0, "number", "2");
		assertValueInCollection("fellowCarriers", 0, "name", "DOS");
		assertValueInCollection("fellowCarriers", 1, "number", "3");
		assertValueInCollection("fellowCarriers", 1, "name", "TRES");
		assertValueInCollection("fellowCarriers", 2, "number", "4");
		assertValueInCollection("fellowCarriers", 2, "name", "CUATRO");
		
		checkRowCollection("fellowCarriers", 1);
		checkRowCollection("fellowCarriers", 2);
		execute("Carrier.translateName", "viewObject=xava_view_fellowCarriers");
		assertNoErrors();
		assertValueInCollection("fellowCarriers", 0, "name", "DOS");
		assertValueInCollection("fellowCarriers", 1, "name", "THREE");
		assertValueInCollection("fellowCarriers", 2, "name", "FOUR");
		
		checkRowCollection("fellowCarriersCalculated", 1);
		checkRowCollection("fellowCarriersCalculated", 2);
		execute("Carrier.translateName", "viewObject=xava_view_fellowCarriersCalculated");
		assertNoErrors();
		assertValueInCollection("fellowCarriersCalculated", 0, "name", "DOS");
		assertValueInCollection("fellowCarriersCalculated", 1, "name", "TRES");
		assertValueInCollection("fellowCarriersCalculated", 2, "name", "CUATRO");
	}

	private void assertSections() throws Exception {
		selectModuleInPage("CustomerWithSection");
		execute("Mode.detailAndFirst");		

		selectModuleInPage("Formula");
		assertValueInList(0, "name", "HTML TEST");
		execute("Mode.detailAndFirst");		
		assertValue("name", "HTML TEST");
		
		selectModuleInPage("CustomerWithSection");
		assertExists("seller.number");
		execute("Sections.change", "activeSection=1");
		assertNotExists("seller.number");
		
		selectModuleInPage("Formula");
		assertNotExists("recipe");
		execute("Sections.change", "activeSection=1");
		assertExists("recipe");
	}

	private void assertOnChangeEvent() throws Exception {
		assertValue("seller.number", "1");
		assertValue("seller.name", "MANUEL CHAVARRI");
		setValue("seller.number", "2");	
		assertValue("seller.name", "JUANVI LLAVADOR");
	}

	private void assertActions() throws Exception {
		assertValueInList(0, "name", "UNO");
		selectModuleInPage("Customer");
		assertValueInList(0, "name", "Javi");
		execute("List.viewDetail", "row=0");
		assertValue("name", "Javi");
		assertValue("website", "http://www.openxava.org");
	}
	
	
	protected String getModuleURL() throws XavaException { 
		if (isLiferayEnabled()) {
			return "http://" + getHost() + ":" + getPort() + "/" + getLiferayURL() + "/OpenXavaTest/SeveralModules";
		}
		else {
			return"http://" + getHost() + ":" + getPort() + "/OpenXavaTest/xava/severalModules.jsp";
		}
	}

}
