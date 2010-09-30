package org.openxava.test.validators;

import org.openxava.util.Is;
import org.openxava.util.Messages;
import org.openxava.validators.IValidator;

/**
 * 
 * @author Federico Alcántara
 */
public class ChildValidator implements IValidator {
	private static final long serialVersionUID = 1L;
	private String description;
	private String childId;

	public void validate(Messages errors) throws Exception {
		if (Is.emptyString(getDescription())) {
			errors.add("blank_description");
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getChildId() {
		return childId;
	}

}
