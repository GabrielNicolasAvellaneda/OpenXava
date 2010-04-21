package org.openxava.invoicing.model;

import java.util.*;

import javax.persistence.*;
import org.openxava.annotations.*;

@Entity
public class Author extends Identifiable {
	
	@Column(length=50) @Required
	private String name;
		
	@OneToMany(mappedBy="author") 
	@ListProperties("number, description, price")	
	private Collection<Product> products;
 
	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}