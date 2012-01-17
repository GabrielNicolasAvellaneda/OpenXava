package org.openxava.test.model

import javax.persistence.*
import javax.persistence.ManyToOne

import org.hibernate.mapping.*
import org.openxava.annotations.*

/**
 * Create on 16/01/2012 (09:55:27)
 * @author Ana Andres
 */
class CityKey implements Serializable{
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATE", referencedColumnName="ID")
	@DescriptionsList
	State state
	
	int code
}
