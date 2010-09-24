package org.openxava.test.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import org.openxava.annotations.EntityValidator;
import org.openxava.annotations.PropertyValue;
import org.openxava.test.validators.ChildValidator;

@Entity
@IdClass(ChildId.class)
@EntityValidator(value = ChildValidator.class, properties = {
	@PropertyValue(name = "childId"),
	@PropertyValue(name = "description")
})
public class Child implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne(optional=true)
	private Parent parent; // To avoid using entity name
	
	@Id
	private String childId; 
	
	private String description;
	
	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
