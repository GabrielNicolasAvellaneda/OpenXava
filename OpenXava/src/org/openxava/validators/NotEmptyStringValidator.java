package org.openxava.validators;

import org.openxava.util.*;


/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotEmptyStringValidator implements IPropertyValidator {

	public void validate(
		Messages errores,
		Object objeto,
		String nombrePropiedad,
		String nombreModelo) {
		try {
			if (Is.emptyString((String) objeto)) {
				errores.add("required", nombrePropiedad, nombreModelo);
			}
		}
		catch (ClassCastException ex) {
			errores.add("expected_type", nombrePropiedad, nombreModelo, "string");
		}
	}

}
