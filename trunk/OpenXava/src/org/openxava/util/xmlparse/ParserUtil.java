package org.openxava.util.xmlparse;

import org.openxava.util.*;
import org.w3c.dom.*;




public class ParserUtil {
	
	public static Element getElement(Element el, String etiqueta) {
		NodeList l = el.getElementsByTagName(etiqueta);
		if (l.getLength() < 1)
			return null;
		return (Element) l.item(0);
	}
		
	public static boolean getAttributeBoolean(Element el, String etiqueta) {
		String s = el.getAttribute(etiqueta);				
		return Boolean.valueOf(s).booleanValue();
	}
	
	public static boolean getBoolean(Element el, String etiqueta) {
		String s = getString(el, etiqueta);
		return Boolean.valueOf(s).booleanValue();
	}
	
		
	public static int getInt(Element el, String etiqueta) throws XavaException {
		String s = getString(el, etiqueta);
		if (s == null)
			return 0;
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible convertir el valor de " + etiqueta + " en un entero");
		}
	}
	
	public static int getAttributeInt(Element el, String etiqueta) throws XavaException {
		String s = el.getAttribute(etiqueta);
		if (Is.emptyString(s))
			return 0;
		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible convertir el valor de " + etiqueta + " [" + s + "] en un entero");
		}
	}
	
	public static String getString(Element el, String etiqueta) {
		NodeList l = el.getElementsByTagName(etiqueta);
		if (l.getLength() < 1)
			return null;
		Node n = l.item(0).getFirstChild();
		return n == null ? "" : n.getNodeValue();
	}

}

