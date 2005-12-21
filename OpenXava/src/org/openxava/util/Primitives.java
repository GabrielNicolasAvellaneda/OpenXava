package org.openxava.util;

/**
 * Utilities to work with Java primitive data. <p> 
 * 
 * Primitive types are <code>boolean, byte, char, short, int, 
 * long, float</code> and <code>double</code>.<br>
 * 
 * @author Javier Paniza
 */
public class Primitives {
	
	/**
	 * Wrapper class corresponding to class of primitive sent. <p>
	 *
	 * That is, if it receive a <code>int.class</code> then
	 * returns a <code>Integer.class</code>. <p>  
	 *
	 * If receives a class of a no primitive class then returns
	 * the same class. <br> 
	 * If receives null returns null.<br>
	 */
	public static Class toWrapperClass(Class origin) {
		if (origin == null) return null;
		if (!origin.isPrimitive()) return origin;
		if (origin.equals(boolean.class)) {
			return Boolean.class;
		}
		else if (origin.equals(byte.class)) {
			return Byte.class;
		}
		else if (origin.equals(char.class)) {
			return Character.class;
		}
		else if (origin.equals(short.class)) {
			return Short.class;
		}
		else if (origin.equals(int.class)) {
			return Integer.class;
		}
		else if (origin.equals(long.class)) {
			return Long.class;
		}
		else if (origin.equals(float.class)) {
			return Float.class;
		}
		else if (origin.equals(double.class)) {
			return Double.class;
		}
		throw new IllegalArgumentException(XavaResources.getString("primitive_type_not_recognized" + origin));
	}
	
	/**
	 * Primitive class corresponding to class of wrapper class sent. <p>
	 *
	 * That is, if it receive a <code>Integer.class</code> then
	 * returns a <code>int.class</code>. <p>  
	 *
	 * If receives a class of a no primitive wrapper class then returns
	 * the same class. <br> 
	 * If receives null returns null.<br>
	 */
	public static Class toPrimitiveClass(Class origin) {
		if (origin == null) return null;		
		if (origin.equals(Boolean.class)) {
			return boolean.class;
		}
		else if (origin.equals(Byte.class)) {
			return byte.class;
		}
		else if (origin.equals(Character.class)) {
			return char.class;
		}
		else if (origin.equals(Short.class)) {
			return short.class;
		}
		else if (origin.equals(Integer.class)) {
			return int.class;
		}
		else if (origin.equals(Long.class)) {
			return long.class;
		}
		else if (origin.equals(Float.class)) {
			return float.class;
		}
		else if (origin.equals(Double.class)) {
			return double.class;
		}
		return origin;
	}	
	
}
