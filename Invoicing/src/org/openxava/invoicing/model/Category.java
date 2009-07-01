package org.openxava.invoicing.model;

import javax.persistence.*;

@Entity
public class Category extends Identifiable {
	
	@Column(length=50)
	private String description;
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}	

}
