package org.openxava.validators;

import org.openxava.util.*;


/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotNullValidator implements IPropertyValidator {

	public void validate(
		Messages errores,
		Object objeto,
		String nombrePropiedad,
		String nombreModelo) {
		if (objeto == null) {
			errores.add("required", nombrePropiedad, nombreModelo);
		}
	}
}
