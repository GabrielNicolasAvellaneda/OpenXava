package org.openxava.test.model;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza
 */
public class CustomerContactPersonKey implements java.io.Serializable {
	
	@Id 
	@ManyToOne(fetch=FetchType.LAZY) // Maybe a @OneToOne is better, but it throws NullPointerException
	private Customer customer;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return obj.toString().equals(this.toString());
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return "CustomerContactPersonKey::" + customer.getNumber();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer; 
	}

}
