package org.openxava.validators;

import java.math.*;

import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotNegativeValidator implements IPropertyValidator {

	private static BigDecimal ZERO_BIGDECIMAL = new BigDecimal("0");
	private static Double ZERO_DOUBLE = new Double("0");
	private static Float ZERO_FLOAT = new Float("0");	

	public void validate(
		Messages errores,
		Object objeto,		
		String nombrePropiedad,
		String nombreModelo) {
		if (objeto == null) {
			errores.add("not_negative_not_null", nombrePropiedad, nombreModelo);
		}
		Number n = null;
		if (objeto instanceof Number) {
			n = (Number) objeto;
		}
		else if (objeto instanceof String) {
			try {
				n = new BigDecimal((String) objeto);
			}
			catch (NumberFormatException ex) {
				errores.add("numeric", nombrePropiedad, nombreModelo);
				return;
			}
		}
		else {
			errores.add("numeric", nombrePropiedad, nombreModelo);
			return;
		}
		if (n instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) n;
			if (bd.compareTo(ZERO_BIGDECIMAL) < 0) {
				errores.add("not_negative", nombrePropiedad, nombreModelo);
			}
		}
		else if (n instanceof Double) {
			Double db = (Double) n;
			if (db.compareTo(ZERO_DOUBLE) < 0) {
				errores.add("not_negative", nombrePropiedad, nombreModelo);
			}
		}
		else if (n instanceof Float) {
			Float fl = (Float) n;
			if (fl.compareTo(ZERO_FLOAT) < 0) {
				errores.add("not_negative", nombrePropiedad, nombreModelo);
			}
		}		
		else if (n.intValue() < 0) {
			errores.add("not_negative", nombrePropiedad, nombreModelo);
		}
	}
}
