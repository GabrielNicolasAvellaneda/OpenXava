package org.openxava.util;

/**
 * Problema al clonar. <p>
 *
 * No es como <tt>CloneNotSupportedException</tt> la cual indica
 * que el m�todo <tt>clone</tt> no se soporta. Esta excepci�n se
 * lanza cuando hay alg�n problema ejecutando el proceso de clonaci�n,
 * aunque el m�todo de clonaci�n s� que se soporte.<br>
 * 
 * @author Javier Paniza
 */
public class ClonException extends Exception {
/**
 * Excepci�n con un mensaje por defecto.
 */
public ClonException() {
	super("Imposible clonar");
}
/**
 * Con el mensaje especificado.
 */
public ClonException(String s) {
	super(s);
}
}
