package org.openxava.util;

/**
 * Representa una alineación de un texto u otro elemento. <p>
 *
 * En numero de objeto de esta clase es finito y accesible únicamente
 * mediante las variables final o el método {@link #get}.<br>
 * 
 * @author: Javier Paniza
 */
 
public class Align implements java.io.Serializable {
	private int code;
	private String description;
	private final static int CODIGO_DEFECTO = 0;
	private final static int CODIGO_IZQUIERDA = 1;
	private final static int CODIGO_CENTRO = 2;
	private final static int CODIGO_DERECHA = 3;
	
	public final static Align DEFAULT = new Align(CODIGO_DEFECTO, "Por defecto");
	public final static Align LEFT = new Align(CODIGO_IZQUIERDA, "Izquierda");
	public final static Align CENTER = new Align(CODIGO_CENTRO, "Centro");
	public final static Align RIGHT = new Align(CODIGO_DERECHA, "Derecha");

	private final static Align [] all = {
		DEFAULT, LEFT, CENTER, RIGHT			
	};
/**
 * Comentario de constructor Alineacion.
 *
 * @param codigo Identificador único.
 * @param descripcion  No ha de ser nulo.
 */
protected Align(int code, String description) {
	this.code = code;
	this.description = description;
}
/**
 */
public boolean equals(Object objeto) {
	if (!(objeto instanceof Align)) {
		return false;
	}
	return code == ((Align) objeto).code;	
}
/**
 * Obtiene la alineación asociada al código indicado. <p>
 * 
 * @param codigo int
 * @exception IllegalStateException  Si el código indicado no corresponde a ninguna alineación existente.
 */
public static Align get(int codigo) {
	switch (codigo) {
		case CODIGO_DEFECTO:
			return DEFAULT;
		case CODIGO_IZQUIERDA:
			return LEFT;
		case CODIGO_CENTRO:
			return CENTER;
		case CODIGO_DERECHA:
			return RIGHT;
		default:
			throw new IllegalArgumentException(XavaResources.getString("align_invalid_code", new Integer(codigo)));
	}
}
/**
 */
public int getCode() {
	return code;
}
/**
 * @return Nunca será nulo.
 */
public java.lang.String getDescription() {
	return description;
}
/**
 * Todas la alineaciones disponibles. <p>
 *
 * @return Nunca será nulo.
 */
public static Align [] getAll() {
	return all;
}
/**
 * Para facilitar preguntar que tipo de alineación es <tt>this</tt>.<p>
 *
 * @return <tt>true si this.equals(Alineacion.CENTRO)</tt>
 */
public boolean isCenter() {
	return code == CODIGO_CENTRO;
}
/**
 * Para facilitar preguntar que tipo de alineación es <tt>this</tt>.<p>
 *
 * @return <tt>true si this.equals(Alineacion.DEFECTO)</tt>
 */
public boolean isDefault() {
	return code == CODIGO_DEFECTO;
}
/**
 * Para facilitar preguntar que tipo de alineación es <tt>this</tt>.<p>
 *
 * @return <tt>true si this.equals(Alineacion.DERECHA)</tt>
 */
public boolean isRight() {
	return code == CODIGO_DERECHA;
}
/**
 * Para facilitar preguntar que tipo de alineación es <tt>this</tt>.<p>
 *
 * @return <tt>true si this.equals(Alineacion.IZQUIERDA)</tt>
 */
public boolean isIzquierda() {
	return code == CODIGO_IZQUIERDA;
}
/**
 */
public String toString() {
	return getDescription();
}
}
