package org.openxava.model.meta;

import java.util.*;

import org.openxava.model.meta.xmlparse.*;
import org.openxava.util.*;

/**
 * Clase de utilidad para acceder a la longitud por defecto de un tipo o esterotipo. <p>
 * 
 * @author Javier Paniza
 */
public class DefaultSize {
		
	private static Map estereotipos;
	private static Map tipos;
	
	public static void _addForStereotype(String nombre, int longitud) throws XavaException {
		if (estereotipos == null) {
			throw new XavaException("only_from_parse", "DefaultSize._addForStereotype");
		}		
		estereotipos.put(nombre, new Integer(longitud));
	}
	
	public static void _addForType(String nombreClase, int longitud) throws XavaException {
		if (tipos == null) {
			throw new XavaException("only_from_parse", "DefaultSize._addForType");
		}			
		tipos.put(nombreClase, new Integer(longitud));
	}
	
	
	public static int forStereotype(String nombre) throws ElementNotFoundException, XavaException {
		if (estereotipos == null) {
			configurar();
		}
		Integer result = (Integer) estereotipos.get(nombre);
		if (result == null) {
			throw new ElementNotFoundException("default_size_for_stereotype_not_found", nombre);
		}
		return result.intValue();
	}
	
	public static int forType(Class clase) throws ElementNotFoundException, XavaException {
		if (tipos == null) {
			configurar();
		}
		Integer result = (Integer) tipos.get(clase.getName());
		if (result == null) {
			throw new ElementNotFoundException("default_size_for_type_not_found", clase.getName());
		}
		return result.intValue();		
	}
	
	private static void configurar() throws XavaException {
		estereotipos = new HashMap();
		tipos = new HashMap();
		DefaultSizeParser.configurarLongitudDefecto();
	}
	
	
}
