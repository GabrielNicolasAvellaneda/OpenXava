package org.openxava.test.model;

import javax.persistence.*;

import org.openxava.annotations.*;

/**
 * This is only for testing a single reference as key, 
 * but remember this is not an example of good OO modeling.<br>
 * A way to achieve to use a single reference as key is:
 * <ul> 
 * <li> Define the reference as @ManyToOne and @Id
 * <li> Define an @IdClass with this reference.
 * </ul> 
 * 
 * @OneToOne, alhough more natural, fails in Hibernate for this case.<br>
 * Although you have only a reference with only a field as primary key,
 * you have to define the key as composite.<br> 
 * 
 * @author Javier Paniza
 */

@Entity
@IdClass(CustomerContactPersonKey.class) 
@Views({
	@View(name="CustomerAsAggregate3Levels"), 
	@View(name="Simple", members="name; customer")
})
public class CustomerContactPerson {
		
	@ReferenceView(name="Simple")
	@ReferenceViews({
		@ReferenceView(forViews="Simple", name="Simplest"),
		@ReferenceView(forViews="CustomerAsAggregate3Levels", name="SellerAsAggregate2Levels")		
	})		
	@AsEmbedded(forViews="CustomerAsAggregate3Levels")
	@Id @ManyToOne(fetch=FetchType.LAZY) // Maybe a @OneToOne is better, but it throws NullPointerException	 
	private Customer customer;
	
	@Stereotype("PERSON_NAME") @Required
	private String name;
	
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
