package org.openxava.test.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Representing a first level embeddable class as detailed in bug 3047205 
 * @author Federico Alcantara
 *
 */
@Embeddable
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	private String personFirstName;
	private String personLastName;
	@Embedded
	private PhoneNumber phoneNumber;
	/**
	 * @return the personFirstName
	 */
	public String getPersonFirstName() {
		return personFirstName;
	}
	/**
	 * @param personFirstName the personFirstName to set
	 */
	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}
	/**
	 * @return the personLastName
	 */
	public String getPersonLastName() {
		return personLastName;
	}
	/**
	 * @param personLastName the personLastName to set
	 */
	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}
	/**
	 * @return the phoneNumber
	 */
	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
