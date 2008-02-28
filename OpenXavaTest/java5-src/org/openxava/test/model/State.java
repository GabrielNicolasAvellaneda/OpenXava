package org.openxava.test.model;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */


@Entity @Table(schema="XAVATEST")
public class State {
	
	@Id @Column(length=2) @Required
	private String id;

	@Required @Column(length=20)
	private String name;

	public String getFullName() {
		return getId() + " " + getName();
	}	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
