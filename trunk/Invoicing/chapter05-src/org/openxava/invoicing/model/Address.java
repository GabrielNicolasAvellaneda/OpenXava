package org.openxava.invoicing.model;

import javax.persistence.*;
import org.openxava.annotations.*;

@Embeddable
public class Address {

	@Required @Column(length=30)	
	private String street;
	
	@Required @Column(length=5)
	private int zipCode;
	
	@Required @Column(length=20) 
	private String city;
	
	@Required @Column(length=30)
	private String state;
	
	// Getters and setters
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}	
	
}

