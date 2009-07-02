package org.openxava.invoicing.model;

import javax.persistence.*;
import org.openxava.annotations.*;

@Entity
public class Author extends Identifiable {
	
	@Column(length=50) @Required
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
