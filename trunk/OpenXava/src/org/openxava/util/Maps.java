package org.openxava.util;

import java.util.*;

/**
 * Utilidades para trabajar con mapas. <p>
 * 
 * @author Javier Paniza
 */
public class Maps {

	/**
	 * Devuelve un clon. <p>
	 *
	 * Intenta hace un clon usando el método <tt>clon()</tt> del origen si éste
	 * es <tt>Cloneable</tt>. En caso contrario hace el clon manual del mapa, sin
	 * clonar los elementos (es decir, <b>no</b> hace un clon <b>PROFUNDO</b>).<br>
	 * 
	 * @return Puede ser nulo (si la original es nulo), y será del mismo tipo que la original.
	 * @param m Mapa original a clonar. Ha de ser <tt>Cloneable</tt> o en su defecto,
	 *                                  tener un constructor por defecto e implementado el
	 *                                  método <tt>putAll</tt>. 
	 * @exception IllegalArgumentException Si argumento no cumple sus precondiciones.
	 */
	public static Map clone(Map m) {
		if (m == null)
			return null;
		try {
			return (Map) Objects.clone(m);
		}
		catch (CloneNotSupportedException ex) {
			return manualClone(m);
		}
	}
	
	/**
	 * Obtain a value in a map with nested maps from a qualified name.
	 * 
	 * For example:
	 * <code>(((Map) mymap.get("a")).get("b")).get("c")</code>
	 * is equal to:
	 * <code>Maps.getValueFromQualifiedName(mymap, "a.b.c")</code> 
	 * 
	 * @param tree Map with map in some values, hence in tree-form.
	 * @param qualifiedName Name in form a.b.c.  
	 */
	public static Object getValueFromQualifiedName(Map tree, String qualifiedName) {
		int idx = qualifiedName.indexOf('.'); 
		if (idx < 0) return tree.get(qualifiedName);
		Map subtree = (Map) tree.get(qualifiedName.substring(0, idx));
		if (subtree == null) return null;
		return  getValueFromQualifiedName(subtree, qualifiedName.substring(idx + 1));		
	}
	
	/**
	 * Hace un clon del mapa a mano.
	 * @return <tt>instanceof  origen.getClass()</tt>
	 * @param origen No puede ser nulo. Ha de tener un constructor por defecto. 
	 */
	private static Map manualClone(Map origen) {
		try {
			Map result = (Map) origen.getClass().newInstance();
			result.putAll(origen);
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(XavaResources.getString("clone_map_require_default_constructor"));
		}
	}
	
	/**
	 * Hace un clon recursivo del mapa. <p>
	 * 
	 * Un clon recursivo significa que si algún valor es a su vez un mapa se aplica
	 * este mismo método.
	 * 
	 * @return <tt>instanceof  origen.getClass()</tt>
	 * @param origen No puede ser nulo. Ha de tener un constructor por defecto. 
	 */
	public static Map recursiveClone(Map origen) {
		try {
			Map result = (Map) origen.getClass().newInstance();
			Iterator it = origen.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				Object value = en.getValue();
				if (value instanceof Map) {
					result.put(en.getKey(), recursiveClone((Map)value));
				}
				else {
					result.put(en.getKey(), value);
				}
			}
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(XavaResources.getString("clone_map_require_default_constructor"));
		}
	}
	
	
	/**
	 * Se considera vacío si es nulo, sin elementos, con elementos nulos o
	 * elementos con valor neutro (ceros, cadenas vacias).
	 * 
	 * @param valores Puede ser nulo
	 * @return Si está o no vacía.
	 */
	public static boolean isEmpty(Map valores) {
		if (valores == null) return true;
		if (valores.size() == 0) return true;
		Iterator it = valores.values().iterator();
		while (it.hasNext()) {
			Object valor = it.next();					
			if (valor instanceof String) {
				 if (!((String) valor).trim().equals("")) return false;
			}
			else if (valor instanceof Number) {
				 if (((Number) valor).intValue() != 0) return false;
			}
			else if (valor instanceof Map) {				
				if (!isEmpty((Map) valor)) return false;				
			}
			else if (valor instanceof Collection) {
				 if (((Collection) valor).size() > 0) return false;			
			}
			else if (valor != null) return false;			 			
		}		
		return true;
	}
	
}
