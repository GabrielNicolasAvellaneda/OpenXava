package org.openxava.util;
/**
 * Utilidades globales referentes al sistema. <p>
 * 
 * @author Javier Paniza
 */
public class XSystem {

	private static boolean enServidor = false;
/**
 * Hace que {@link #enServidor} devuelva <tt>true</tt>.
 *
 * Se dever�a llamar desde un bloque est�tico en la clase base com�n de ejb, o
 * en todos los ejbs si no hay clase base.<br>
 */
public static void _setOnServer() {
	enServidor = true;
}
/**
 * Si estamos en un cliente: aplicaci�n java, applet, servlet, jsp, etc. <p>
 */
public static boolean onClient() {
	return !onServer();
}
/**
 * Si estamos en un servidor EJB. <p>
 */
public static boolean onServer() {
	return enServidor;
}
}
