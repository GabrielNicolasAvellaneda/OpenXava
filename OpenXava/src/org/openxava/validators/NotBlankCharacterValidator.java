package org.openxava.validators;

import org.openxava.util.*;


/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotBlankCharacterValidator implements IPropertyValidator {

	public void validate(
		Messages errores,
		Object objeto,
		String nombrePropiedad,
		String nombreModelo) {
		try {
			if (Character.isWhitespace(((Character) objeto).charValue())) {
				errores.add("required", nombrePropiedad, nombreModelo);
			}
		}
		catch (ClassCastException ex) {
			errores.add("expected_type", nombrePropiedad, nombreModelo, "caracter");
		}
	}
}
