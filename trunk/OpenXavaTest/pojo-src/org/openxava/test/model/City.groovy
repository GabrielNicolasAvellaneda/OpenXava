package org.openxava.test.model

import javax.persistence.*

import org.openxava.annotations.*
import org.openxava.tab.impl.*

/**
 * Create on 16/01/2012 (09:54:11)
 * @author Ana Andres
 */
@IdClass(CityKey.class)
@Entity
class City {
	@Id 
	@ManyToOne(fetch=FetchType.LAZY) 
	@DescriptionsList
	@JoinColumn(name="STATE", referencedColumnName="ID")
	State state
	
	@Id
	int code
	
	String name
}
