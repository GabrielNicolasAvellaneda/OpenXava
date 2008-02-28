package org.openxava.test.model;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * Number (the key) can be 0
 *  
 * @author Javier Paniza
 */

@Entity
public class Color {

	@Id @Column(length=5)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer number;
	
	@Column(length=20) @Required
	private String name;
	
	@Version 
	private int version;
	
	@Stereotype("IMAGE_LABEL")
	public String getSample() {
		if ("red".equalsIgnoreCase(name) ||
			"rojo".equalsIgnoreCase(name)) return "RED";
		if ("black".equalsIgnoreCase(name) ||
			"negro".equalsIgnoreCase(name)) return "BLACK";
		return "nocolor";		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
