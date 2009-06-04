package org.openxava.invoicing.model;

import javax.persistence.*;

import org.openxava.annotations.*;

@Entity
public class Product {
	
	@Id @Column(length=9)
	private int number;
	
	@Column(length=50) @Required
	private String description; 
	
	@ManyToOne(
		fetch=FetchType.LAZY,
		optional=false)
	@DescriptionsList
	private Category category;
	
	// Getters and setters

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
