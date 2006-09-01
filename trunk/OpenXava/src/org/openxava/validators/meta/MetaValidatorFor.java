package org.openxava.validators.meta;

import org.openxava.util.*;

/**
 * A validator associated to a type or stereotype. <p>
 * 
 * Used for validator for required and for default validators.<br>
 * 
 * @author Javier Paniza
 */
public class MetaValidatorFor {
	
	private java.lang.String forType;
	private String forStereotype;
	private java.lang.String validatorName;
	public java.lang.String validatorClass;
	
	public java.lang.String getValidatorClass() throws XavaException {
		if (validatorClass == null) {
			validatorClass =
				MetaValidators
					.getMetaValidator(getValidatorName())
					.getClassName();
		}
		return validatorClass;
	}

	public java.lang.String getValidatorName() {
		return validatorName;
	}

	public java.lang.String getForType() {
		return forType;
	}

	public void setValidatorClass(java.lang.String newValidatorClass) {
		validatorClass = newValidatorClass;
	}

	public void setValidatorName(java.lang.String newValidatorName) {
		validatorName = newValidatorName;
	}

	public void setForType(java.lang.String newForType) {
		forType = newForType;
	}

	public String getForStereotype() {
		return forStereotype;
	}

	public void setForStereotype(String newForStereotype) {
		this.forStereotype = newForStereotype;
	}

}
