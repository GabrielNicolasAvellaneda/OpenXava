package org.openxava.test.model;

/**
 * Used for testing AspectProperty. <p>
 * 
 * Properties without getters and setters. <br>
 * 
 * @author Javier Paniza
 */

public class Person {
	
	public String name;
	public String surnames;
	public int age;
	protected void setAge(int age) {		
		if (age > 200) {
			throw new IllegalArgumentException("Too old");
		}
		this.age = age;
	}	
		
	public String fullName;
	protected String getFullName() {
		return name + " " + surnames;
	}
	
	public String address;
	protected String getAddress() {
		return address.toUpperCase();
	}
	
	public String fullNameAndAddress;
	protected String getFullNameAndAddress() {
		return getFullName() + " " + getAddress(); 
	}
		
}
