package org.openxava.util;

/**
 * Utilidades para trabajar con datos primitivos de Java. <p>
 * 
 * Datos primitivos son <tt>boolean, byte, char, short, int, 
 * long, float</tt> and <tt>double</tt>.<br>
 * 
 * @author Javier Paniza
 */
public class Primitives {
	
	/**
	 * Clase envoltorio correspondiente a la clase de dato primitivo
	 * enviado. <p>
	 * 
	 * Es decir, si se recibe <tt>int.class</tt>, se devuelve 
	 * <tt>Integer.class</tt>. <p>
	 * 
	 * Si se recibe una clase de un dato no primitivo se devuelve
	 * esa misma clase.<br>
	 * Si se recibe nulo se devuelve nulo.<br>
	 */
	public static Class toWrapperClass(Class origen) {
		if (origen == null) return null;
		if (!origen.isPrimitive()) return origen;
		if (origen.equals(boolean.class)) {
			return Boolean.class;
		}
		else if (origen.equals(byte.class)) {
			return Byte.class;
		}
		else if (origen.equals(char.class)) {
			return Character.class;
		}
		else if (origen.equals(short.class)) {
			return Short.class;
		}
		else if (origen.equals(int.class)) {
			return Integer.class;
		}
		else if (origen.equals(long.class)) {
			return Long.class;
		}
		else if (origen.equals(float.class)) {
			return Float.class;
		}
		else if (origen.equals(double.class)) {
			return Double.class;
		}
		throw new IllegalArgumentException(XavaResources.getString("primitive_type_not_recognized" + origen));
	}
	
}

