package org.openxava.generators;

import org.openxava.util.*;

/**
 * Método de utilidad general para generar código.
 * 
 * @author Javier Paniza
 */
public class Generators {

	public static String generateCast(String tipo, String sentencia) throws XavaException {
		if (tipo == null) {
			throw new XavaException("cast_type_required", sentencia); 
		}
		tipo = tipo.trim();
		if (tipo.equals("boolean")) {
			return "((Boolean) " + sentencia + ").booleanValue()";
		}
		else if (tipo.equals("byte")) {
			return "((Byte) " + sentencia + ").byteValue()";
		}
		else if (tipo.equals("char")) {
			return "((Character) " + sentencia + ").charValue()";
		}
		else if (tipo.equals("short")) {
			return "((Short) " + sentencia + ").shortValue()";
		}
		else if (tipo.equals("int")) {
			return "((Integer) " + sentencia + ").intValue()";
		}
		else if (tipo.equals("long")) {
			return "((Long) " + sentencia + ").longValue()";
		}
		else if (tipo.equals("float")) {
			return "((Float) " + sentencia + ").floatValue()";
		}
		else if (tipo.equals("double")) {
			return "((Double) " + sentencia + ").doubleValue()";
		}
		else {
			return "(" + tipo + ") " + sentencia;
		}
	}
	
	public static String generatePrimitiveWrapper(String tipo, String sentencia) throws XavaException {
		if (tipo == null) {
			throw new XavaException("Imposible determinar a que tipo moldea la sentencia " + sentencia); 
		}
		tipo = tipo.trim();
		if (tipo.equals("boolean")) {
			return "new Boolean(" + sentencia + ")";
		}
		else if (tipo.equals("byte")) {
			return "new Byte(" + sentencia + ")";
		}
		else if (tipo.equals("char")) {
			return "new Character(" + sentencia + ")";
		}
		else if (tipo.equals("short")) {
			return "new Short(" + sentencia + ")";
		}
		else if (tipo.equals("int")) {
			return "new Integer(" + sentencia + ")";
		}
		else if (tipo.equals("long")) {
			return "new Long(" + sentencia + ")";
		}
		else if (tipo.equals("float")) {
			return "new Float(" + sentencia + ")";
		}
		else if (tipo.equals("double")) {
			return "new Double(" + sentencia + ")";
		}
		else {
			return sentencia;
		}
	}
	
	public static String generateCastFromString(String tipo, String sentencia) throws XavaException {
		if (tipo == null) {
			throw new XavaException("to_string_type_required", sentencia); 
		}
		tipo = tipo.trim();
		if (tipo.equals("boolean")) {			
			return "Boolean.valueOf(" + sentencia + ").booleanValue()";
		}
		else if (tipo.equals("byte")) {			
			return "Byte.parseByte(" + sentencia + ")";
		}
		else if (tipo.equals("char")) {
			return sentencia + ".length() > 0?" + sentencia + ".charAt(0):' '";
		}
		else if (tipo.equals("short")) {						
			return "Short.parseShort(" + sentencia + ")";
		}
		else if (tipo.equals("int")) {
			return "Integer.parseInt(" + sentencia + ")";			
		}
		else if (tipo.equals("long")) {
			return "Long.parseLong(" + sentencia + ")";			
		}
		else if (tipo.equals("float")) {			
			return "Float.parseFloat(" + sentencia + ")";
		}
		else if (tipo.equals("double")) {
			return "Double.parseDouble(" + sentencia + ")";			
		}
		else if (tipo.equals("java.math.BigDecimal") || tipo.equals("BigDecimal")) {
			return "new BigDecimal(" + sentencia + ")";
		}
		else {
			return sentencia;
		}
	}
	
	
	
}
