package org.openxava.test.model;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class ChildId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	private Parent parent; 
	
	@Id
	private String childId;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((childId == null) ? 0 : childId.hashCode());
		result = prime * result
				+ ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildId other = (ChildId) obj;
		if (childId == null) {
			if (other.childId != null)
				return false;
		} else if (!childId.equals(other.childId))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	} 
	

}
