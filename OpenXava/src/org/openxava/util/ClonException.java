package org.openxava.util;

/**
 * Problema al clonar. <p>
 *
 * No es como <tt>CloneNotSupportedException</tt> la cual indica
 * que el método <tt>clone</tt> no se soporta. Esta excepción se
 * lanza cuando hay algún problema ejecutando el proceso de clonación,
 * aunque el método de clonación sí que se soporte.<br>
 * 
 * @author Javier Paniza
 */
public class ClonException extends Exception {
/**
 * Excepción con un mensaje por defecto.
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
