package org.openxava.test.model

import org.openxava.annotations.*;

import javax.persistence.*;

/**
 * 
 * @author Javier Paniza 
 */
@Entity
@Tab( properties= "name, address.street, address.zipCode, address.city" )
class Building extends Nameable {
	
	@AttributeOverrides([
		@AttributeOverride(name="street",
			column=@Column(name="BSTREET")),
		@AttributeOverride(name="zipCode",
			column=@Column(name="BZIPCODE"))
	])
	Address address
	
}
