package org.openxava.test.model;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.jpa.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@View(name="Simple", members="number, name")
@Tab(properties="calculated, number, name")
public class Carrier {
	
	@Id @Column(length=5)
	private int number;
	
	@Required @Column(length=40) @Stereotype("NO_FORMATING_STRING")
	private String name;
	
	// We apply conversion (null into an empty String) to DRIVINGLICENCE_TYPE column
	// In order to do it, we create the drivingLicence_level, drivingLicence_type
	// we do JoinColumns not insertable nor updatable, and modify the setDrivingLincence method
	// and we create a drivingLicenceConversion() method.
	@DescriptionsList
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({ 
		@JoinColumn(name="DRIVINGLICENCE_LEVEL", referencedColumnName="LEVEL", insertable=false, updatable=false),  
		@JoinColumn(name="DRIVINGLICENCE_TYPE", referencedColumnName="TYPE", insertable=false, updatable=false) 
	})	
	private DrivingLicence drivingLicence;	
	private Integer drivingLicence_level; 
	private String drivingLicence_type; 
	
	@ManyToOne(optional=false)
	private Warehouse warehouse;

	@Stereotype("MEMO")
	@DefaultValueCalculator(
		calculator=org.openxava.test.calculators.CarrierRemarksCalculator.class,
		properties={
			@PropertyValue(name="drivingLicenceType", from="drivingLicence.type") 
		}		
	)
	private String remarks;
	
	public static Collection<Carrier> findAll() {
		Query query = XPersistence.getManager().createQuery("from Carrier as o"); 
 		return query.getResultList();  				
	}	

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length=10) @Action(name="Carriers.translateName")
	public String getCalculated() {
		return "TR";
	}

	public DrivingLicence getDrivingLicence() {
		// In this way because the column for type of driving lincence does not admit null
		try {
			if (drivingLicence != null) drivingLicence.toString(); // to force load
			return drivingLicence;
		}
		catch (EntityNotFoundException ex) {			
			return null;  
		}
	}

	public void setDrivingLicence(DrivingLicence licence) {
		// In this way because the column for type of driving lincence does not admit null
		this.drivingLicence = licence;
		this.drivingLicence_level = licence==null?null:licence.getLevel();		
		this.drivingLicence_type = licence==null?null:licence.getType(); 			
	}

	@PrePersist @PreUpdate
	private void drivingLicenceConversion() {
		if (this.drivingLicence_type == null) this.drivingLicence_type = "";
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@CollectionView(name="Simple") @EditOnly
	@ListActions({
			@ListAction(name="Carriers.translateName"),
			@ListAction(name="Carriers.allToEnglish")
	})
	@Condition(
		"${warehouse.zoneNumber} = ${this.warehouse.zoneNumber} AND " + 
		"${warehouse.number} = ${this.warehouse.number} AND " +
		"NOT (${number} = ${this.number})"
	)
	public Collection<Carrier> getFellowCarriers() { 
		// At the moment you must write a code that returns the same result
		// of the @Condition. In OX3.1 this will not be necessary
		Query query = XPersistence.getManager().createQuery("from Carrier c where " +
			"c.warehouse.zoneNumber = :zone AND " + 
			"c.warehouse.number = :warehouseNumber AND " + 
			"NOT (c.number = :number) ");
		query.setParameter("zone", getWarehouse().getZoneNumber());
		query.setParameter("warehouseNumber", getWarehouse().getNumber());
		query.setParameter("number",  getNumber());
		return query.getResultList();
	}
	
	@CollectionView(name="Simple")
	public Collection<Carrier> getFellowCarriersCalculated() {
		// This method exists for compliance with OpenXavaTest
		return getFellowCarriers();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public void translate() {
		if (!translateToSpanish()) translateToEnglish();		
	}
	
	
	public boolean translateToEnglish() {
		// A naive implementation
		if ("UNO".equals(name)) {
			name = "ONE";
			return true;
		}
		if ("DOS".equals(name)) {
			name ="TWO";
			return true;
		}
		if ("TRES".equals(name)) {
			name ="THREE";
			return true;
		}
		if ("CUATRO".equals(name)) {
			name ="FOUR";
			return true;
		}		
		if ("CINCO".equals(name)) {
			name ="FIVE";
			return true;
		}
		return false;
	}
		
	public boolean translateToSpanish() {
		// A naive implementation
		if ("ONE".equals(name)) {
			name = "UNO";
			return true;
		}
		if ("TWO".equals(name)) {
			name ="DOS";
			return true;
		}
		if ("THREE".equals(name)) {
			name ="TRES";
			return true;
		}
		if ("FOUR".equals(name)) {
			name ="CUATRO";
			return true;
		}
		if ("FIVE".equals(name)) {
			name ="CINCO";
			return true;
		}
		return false;
	}	
	
}
