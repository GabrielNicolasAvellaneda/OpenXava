package org.openxava.util;

/**
 * Excepci�n lanzada cuando se incumple una afirmaci�n. <p>
 *
 * El incumplimiento de una afirmaci�n implica un error
 * en la programaci�n. Normalmente no se lanzar� esta
 * excepci�n directamente, sino con la clase {@link org.openxava.util.Assert}.<br>
 * No siempre que ocurra un error de programaci�n se lanzar� esta excepcion,
 * es posible lanzar otra <tt>RuntimeException</tt> apropiadas al error que haya
 * ocurrido, como por ejemplo <tt>IndexOutOfBoundsException</tt>, <tt>IllegalArgumentException</tt> o
 * incluso la nefasta <tt>NullPointerException</tt>.
 * 
 * @author: Javier Paniza
 */
public class AssertException extends RuntimeException {
/**
 * Comentario de constructor AssertException.
 */
public AssertException() {
	super("Afirmaci�n ha fallado");
}
/**
 * Comentario de constructor AssertException.
 * @param s java.lang.String
 */
public AssertException(String s) {
	super(s);
}
}
