package org.openxava.test.model;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;
import org.openxava.tracking.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@IdClass(WarehouseKey.class)
@EntityListeners(AccessTrackingListener.class)
@Tab( 
	filter=org.openxava.test.filters.LimitZoneFilter.class,
	baseCondition="${zoneNumber} <= ?"
)

public class Warehouse {
	
	@Id 
	// Column is also specified in WarehouseKey because a bug in Hibernate, see
	// http://opensource.atlassian.com/projects/hibernate/browse/ANN-361	
	@Column(length=3, name="ZONE")	
	private int zoneNumber;	
	
	@Id @Column(length=3)
	private int number;
	
	@Column(length=40) @Required
	private String name;
	
 	public static Warehouse findByZoneNumberNumber(int zoneNumber, int number) throws NoResultException { 	 			
 		Query query = XPersistence.getManager().createQuery("from Warehouse as o where o.zoneNumber = :zoneNumber and number = :number"); 
		query.setParameter("zoneNumber", zoneNumber); 
		query.setParameter("number", number); 
 		return (Warehouse) query.getSingleResult();
	} 


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(int zoneNumber) {
		this.zoneNumber = zoneNumber;
	}
		
}
