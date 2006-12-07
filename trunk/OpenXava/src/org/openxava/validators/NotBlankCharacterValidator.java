package org.openxava.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;


/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotBlankCharacterValidator implements IPropertyValidator {

	private static Log log = LogFactory.getLog(NotBlankCharacterValidator.class);
	
	public void validate(
		Messages errors,
		Object object,
		String propertyName,
		String modelName) {
		try {
			if (Character.isWhitespace(((Character) object).charValue())) {
				errors.add("required", propertyName, modelName);
			}
		}
		catch (ClassCastException ex) {
			errors.add("expected_type", propertyName, modelName, "caracter");
		}
	}
}
