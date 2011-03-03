package org.openxava.invoicing.model;

import javax.persistence.*;

@Embeddable  
public class Address {

	@Column(length=30)  
	private String street;
	
	@Column(length=5)
	private int zipCode;
	
	@Column(length=20) 
	private String city;
	
	@Column(length=30)
	private String state;
	
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