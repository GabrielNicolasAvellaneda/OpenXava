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
 * Se devería llamar desde un bloque estático en la clase base común de ejb, o
 * en todos los ejbs si no hay clase base.<br>
 */
public static void _setOnServer() {
	enServidor = true;
}
/**
 * Si estamos en un cliente: aplicación java, applet, servlet, jsp, etc. <p>
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
