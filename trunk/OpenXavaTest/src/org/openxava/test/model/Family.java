package org.openxava.test.model;

import java.io.*;

/**
 * @author Javier Paniza
 */
public class Family implements Serializable {

	private String oid;
	private int number;
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
