package org.openxava.util;

import java.util.*;

/**
 * Utilidades para trabajar con colecciones, enumeraciones e iteradores. <p>
 * 
 * @author Javier Paniza
 */
public class XCollections {


/**
 * A�ade los elementos de la enumeraci�n a la colecci�n indicada. <p>
 *
 * @param coleccion En donde se a�aden los elementos. No puede ser nulo.
 * @param aA�adir   Elementos a a�adir. Si es nulo no se a�ade nada
 */
public static void add(Collection coleccion, Enumeration aA�adir) {
	Assert.arg(coleccion);	
	if (aA�adir == null) return;
	while (aA�adir.hasMoreElements()) {
		coleccion.add(aA�adir.nextElement());
	}	
}
/**
 * Envia a salida estandar los elementos de la colecci�n. <p>
 * 
 * Util para depurar.<br>
 * @param c  Puede ser nulo,
 */
public static void println(Collection c) {
	if (c == null) return;
	println(c.iterator());
}
/**
 * Env�a a salida est�ndar los elementos por los que se itera. <p>
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
 * Devuelve una colleci�n a partir de una enumeraci�n. <p>
 *
 * @return Nunca ser� nulo
 * @param e Si se env�a nulo se devolver� una colecci�n vac�a
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
