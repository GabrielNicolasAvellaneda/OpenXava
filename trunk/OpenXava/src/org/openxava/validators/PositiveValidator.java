package org.openxava.validators;

import java.math.*;

import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza
 */
public class PositiveValidator implements IPropertyValidator {

	private static BigDecimal CERO_BIGDECIMAL = new BigDecimal("0");
	private static Double CERO_DOUBLE = new Double("0");
	private static Float CERO_FLOAT = new Float("0");	

	public void validate(
		Messages errores,
		Object objeto,		
		String nombrePropiedad,
		String nombreModelo) {
		if (objeto == null) {
			errores.add("positive_not_null", nombrePropiedad, nombreModelo);
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
			if (bd.compareTo(CERO_BIGDECIMAL) <= 0) {
				errores.add("positive", nombrePropiedad, nombreModelo);
			}
		}
		else if (n instanceof Double) {
			Double db = (Double) n;
			if (db.compareTo(CERO_DOUBLE) <= 0) {
				errores.add("positive", nombrePropiedad, nombreModelo);
			}
		}
		else if (n instanceof Float) {
			Float fl = (Float) n;
			if (fl.compareTo(CERO_FLOAT) <= 0) {
				errores.add("positive", nombrePropiedad, nombreModelo);
			}
		}		
		else if (n.intValue() <= 0) {
			errores.add("positive", nombrePropiedad, nombreModelo);
		}
	}
}
