package org.openxava.model.meta;

import java.util.*;

import org.openxava.model.meta.xmlparse.*;
import org.openxava.util.*;

/**
 * Clase de utilidad para acceder al tipo por defecto de un esterotipo. <p>
 * 
 * @author Javier Paniza
 */
public class TypeStereotypeDefault {
		
	private static Map stereotypes;
	
	
	public static void _addForStereotype(String nombre, String tipo) throws XavaException {
		if (stereotypes == null) {
			throw new XavaException("only_from_parse", "TypeStereotypeDefault._addForStereotype");
		}				
		stereotypes.put(nombre, tipo);
	}
	
	
	
	
	public static String forStereotype(String nombre) throws ElementNotFoundException, XavaException {
		if (stereotypes == null) {
			configurar();
		}		 
		String result = (String) stereotypes.get(nombre);		
		if (result == null) {
			throw new ElementNotFoundException("default_type_for_stereotype_not_found", nombre);
		}
		
		return result;
	}
			
	private static void configurar() throws XavaException {
		stereotypes = new HashMap();		
		StereotypeTypeDefaultParser.configurarTipoEstereotipoDefecto();
	}
	
	
}
