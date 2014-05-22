package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

/**
 * 
 * @author Javier Paniza
 */

@Embeddable
public class TrainingSession {

	@Column(length=30) @Required
	private String description;
	
	private int kms;
	
	private Date date;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getKms() {
		return kms;
	}

	public void setKms(int kms) {
		this.kms = kms;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
