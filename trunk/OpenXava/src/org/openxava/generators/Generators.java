package org.openxava.generators;

import org.openxava.util.*;

/**
 * Utility methods used in code generation.
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
	
	public static String getWebsphereSQLType(String javaTypeName, boolean href) throws XavaException {
		if ("String".equals(javaTypeName) || "java.lang.String".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLCharacterStringType_2":"RDBSchema:SQLCharacterStringType";
		}
		if ("int".equals(javaTypeName) || "Integer".equals(javaTypeName) || "java.lang.Integer".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLExactNumeric_1":"RDBSchema:SQLExactNumeric";
		}
		if ("java.math.BigDecimal".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLNumeric_1":"RDBSchema:SQLNumeric";
		}
		if ("java.util.Date".equals(javaTypeName) || "java.sql.Date".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLDate_1":"RDBSchema:SQLDate";
		}
		if ("double".equals(javaTypeName) || "Double".equals(javaTypeName) || "java.math.Double".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLApproximateNumeric_2":"RDBSchema:SQLApproximateNumeric";
		}
		if ("float".equals(javaTypeName) || "Float".equals(javaTypeName) || "java.lang.Float".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLFloat_1":"RDBSchema:SQLFloat";
		}
		if ("short".equals(javaTypeName) || "Short".equals(javaTypeName) || "java.lang.Short".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLExactNumeric_2":"RDBSchema:SQLExactNumeric";
		}
		if ("java.sql.Time".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLTime_1":"RDBSchema:SQLTime";
		}
		if ("java.sql.Timestamp".equals(javaTypeName)) {
			return href?"SQL92_Primitives.xmi#SQLTimestamp_1":"RDBSchema:SQLTimestamp";
		}
		throw new XavaException("websphere_type_not_supported", javaTypeName);
	}
	
}
