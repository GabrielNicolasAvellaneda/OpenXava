package org.openxava.test.model

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.model.*

/**
 * To test @UniqueConstraint in @SecondaryTable. These
 * unique constraints that are to be placed on the table.
 * The name element of UniqueConstrainst is only indicative, 
 * because Hibernate does not create the constraint in the 
 * table, with the indicated name.We have mapped the 
 * constraint name manually in Table.
 * 
 * It also allows testing UniqueConstraint on a single column.   
 * 
 * tmp ¿Poner en wiki? 
 *
 * @author Jeromy Altuna
 */
@Entity
@SecondaryTable(
	name="APPLICATIONUSER_INFO",
	uniqueConstraints=[
		@UniqueConstraint(name="not_repeat_user_info",
			columnNames=["name", "birthdate", "sex"])
	]
)
class ApplicationUser extends Identifiable {
	
	//For testing constraint on a single column.
	@Required
	@Column(length=8, unique=true) 
	String nic 
	
	@Column(length=40, table="APPLICATIONUSER_INFO")
	String name
	
	@Column(length=40, table="APPLICATIONUSER_INFO")
	Date birthdate
	
	@Column(table="APPLICATIONUSER_INFO")
	@Enumerated(EnumType.STRING)
	Sex sex
	public enum Sex { MALE, FEMALE }
	
	@OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
	Collection<Nickname> nicknames = new ArrayList<>()		
}