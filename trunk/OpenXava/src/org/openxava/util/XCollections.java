package org.openxava.util;

import java.util.*;

/**
 * Utilidades para trabajar con colecciones, enumeraciones e iteradores. <p>
 * 
 * @author Javier Paniza
 */
public class XCollections {


/**
 * Añade los elementos de la enumeración a la colección indicada. <p>
 *
 * @param coleccion En donde se añaden los elementos. No puede ser nulo.
 * @param aAñadir   Elementos a añadir. Si es nulo no se añade nada
 */
public static void add(Collection coleccion, Enumeration aAñadir) {
	Assert.arg(coleccion);	
	if (aAñadir == null) return;
	while (aAñadir.hasMoreElements()) {
		coleccion.add(aAñadir.nextElement());
	}	
}
/**
 * Envia a salida estandar los elementos de la colección. <p>
 * 
 * Util para depurar.<br>
 * @param c  Puede ser nulo,
 */
public static void println(Collection c) {
	if (c == null) return;
	println(c.iterator());
}
/**
 * Envía a salida estándar los elementos por los que se itera. <p>
 * 
 * @param it Puede ser nulo.
 */
public static void println(Iterator it) {
	if (it == null) return;
	while (it.hasNext()) {
		System.out.println(" - " + it.next());
	}
}
/**
 * Devuelve una colleción a partir de una enumeración. <p>
 *
 * @return Nunca será nulo
 * @param e Si se envía nulo se devolverá una colección vacía
 */
public Collection toCollection(Enumeration e) {
	Vector result = new Vector();
	if (e == null) return result;
	while (e.hasMoreElements()) {
		result.add(e.nextElement());
	}	
	return result;
}
}
