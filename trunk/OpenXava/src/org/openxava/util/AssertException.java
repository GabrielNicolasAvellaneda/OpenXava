package org.openxava.util;

/**
 * Excepción lanzada cuando se incumple una afirmación. <p>
 *
 * El incumplimiento de una afirmación implica un error
 * en la programación. Normalmente no se lanzará esta
 * excepción directamente, sino con la clase {@link org.openxava.util.Assert}.<br>
 * No siempre que ocurra un error de programación se lanzará esta excepcion,
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
	super("Afirmación ha fallado");
}
/**
 * Comentario de constructor AssertException.
 * @param s java.lang.String
 */
public AssertException(String s) {
	super(s);
}
}
