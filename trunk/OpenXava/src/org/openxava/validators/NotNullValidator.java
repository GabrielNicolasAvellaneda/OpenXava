package org.openxava.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;


/**
 * 
 * 
 * @author Javier Paniza
 */
public class NotNullValidator implements IPropertyValidator {

	private static Log log = LogFactory.getLog(NotNullValidator.class);
	
	public void validate(
		Messages errors,
		Object object,
		String propertyName,
		String modelName) {
		if (object == null) {
			errors.add("required", propertyName, modelName);
		}
	}
	
}
