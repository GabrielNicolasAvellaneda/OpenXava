package org.openxava.util.xmlparse;


import java.net.*;
import java.util.*;

import javax.xml.parsers.*;

import org.openxava.util.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
abstract public class ParserBase extends XmlElementsNames {

	protected final static int ENGLISH = 0;
	protected final static int ESPANOL = 1;
	protected int lang;
	 			
	private Element root;
	private static DocumentBuilder documentBuilder;	
	private String xmlFileURL;
	
	public ParserBase(String xmlFileURL) {
		// assert(xmlFileURL)
		this.xmlFileURL = xmlFileURL;
	}
	
	public ParserBase(String xmlFileURL, int language) {
		// assert(xmlFileURL)
		this.xmlFileURL = xmlFileURL;
		this.lang = language;
	}
	
	abstract protected void createObjects() throws XavaException;
	
	protected boolean getBoolean(Element el, String etiqueta) {
		return ParserUtil.getBoolean(el, etiqueta);
	}
	
	protected boolean getAttributeBoolean(Element el, String etiqueta) {
		return ParserUtil.getAttributeBoolean(el, etiqueta);
	}
	
	protected Element getElement(Element el, String etiqueta) {
		return ParserUtil.getElement(el, etiqueta);
	}
	
	protected int getInt(Element el, String etiqueta) throws XavaException {
		return ParserUtil.getInt(el, etiqueta);
	}
	
	protected int getAttributeInt(Element el, String etiqueta) throws XavaException {
		return ParserUtil.getAttributeInt(el, etiqueta);
	}
	
	protected org.w3c.dom.Element getRoot() {
		return root;
	}

	protected String getString(Element el, String etiqueta) {
		return ParserUtil.getString(el, etiqueta);
	}
	
	public void parse() throws XavaException {
		String urlArchivoXmlCompleta = null;
		try {						
			Enumeration resources = getClass().getClassLoader().getResources(xmlFileURL);
			while (resources.hasMoreElements()) {
				URL resource = (URL) resources.nextElement();
				urlArchivoXmlCompleta = resource.toExternalForm();				
				_parse(urlArchivoXmlCompleta);				
			}			
		} catch (XavaException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible cargar configuración del modelo a partir de "
					+ urlArchivoXmlCompleta
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		if (documentBuilder == null) {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();			
		}
		return documentBuilder;
	}
	
	private void _parse(String urlArchivoXmlCompleta) throws XavaException {
		try {						
			Document doc = getDocumentBuilder().parse(urlArchivoXmlCompleta);
			root = (Element) doc.getDocumentElement();
			createObjects();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible cargar configuración del modelo a partir de "
					+ urlArchivoXmlCompleta
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}		
	}
	
}