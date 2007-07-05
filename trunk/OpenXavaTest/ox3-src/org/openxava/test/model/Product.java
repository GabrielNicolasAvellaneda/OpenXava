package org.openxava.test.model;

import java.math.*;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;

/**
 * This example uses stereotypes for family an subfamily.
 * It is better to use reference (like in Product2), but
 * therefore it's show some features more advances of stereotypes.
 * 
 * @author Javier Paniza
 */

@Entity
@EntityValidators({
	@EntityValidator(validator=org.openxava.test.validators.CheapProductValidator.class, properties= {
		@PropertyValue(name="limit", value="100"),
		@PropertyValue(name="description"),
		@PropertyValue(name="unitPrice")
	}),
	@EntityValidator(validator=org.openxava.test.validators.ExpensiveProductValidator.class, properties= {
		@PropertyValue(name="limit", value="1000"),
		@PropertyValue(name="description"),
		@PropertyValue(name="unitPrice")
	}),
	@EntityValidator(validator=org.openxava.test.validators.ForbiddenPriceValidator.class, 
		properties= {
			@PropertyValue(name="forbiddenPrice", value="555"),
			@PropertyValue(name="unitPrice")
		},
		onlyOnCreate=true
	)	
})

@Views({
	@View( name = "WithSection" , members = 
		"number;" +
		"data {" +
		"	description;" +
		"	photos;" +
		"	familyNumber;" +
		"	subfamilyNumber;" +
		"	warehouseKey;" +
		"	price [" +
		"		unitPrice;" +
		"		unitPriceInPesetas;" +
		"	]" +
		"	remarks" + 
		"}"		
	),
	@View( name="Simple", members = "number, description, unitPrice" ),
	@View( name="EditPrice", members = "number, description, unitPrice")
})

@Tab(properties = "number, description, unitPrice, unitPriceInPesetas")

public class Product {
	
	@Id @Column(length=10) 
	private long number;
	
	@Column(length=40) @Required
	@PropertyValidators ({
		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=
			@PropertyValue(name="string", value="MOTO")
		),
		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=
			@PropertyValue(name="string", value="COCHE")
		),		
		@PropertyValidator(validator=org.openxava.test.validators.ExcludeStringValidator.class, properties=			
			@PropertyValue(name="string", value="CUATRE"),
			onlyOnCreate=true
		)		
	})
	@ReadOnly(forViews="EditPrice")
	private String description;

	@Stereotype("IMAGES_GALLERY")
	private String photos;
	
	@Stereotype("FAMILY") @Required @Column(name="FAMILY")
	private int familyNumber;
	
	@Stereotype("SUBFAMILY") @Required @Column(name="SUBFAMILY")
	private int subfamilyNumber;
	
	@Column(name="ZONE")
	private Integer warehouseZoneNumber;
	
	@Column(name="WAREHOUSE")
	private Integer warehouseNumber;
		
	@Stereotype("MONEY") @Required
	@DefaultValueCalculator(calculator=org.openxava.test.calculators.DefaultProductPriceCalculator.class, properties=
		@PropertyValue(name="familyNumber")
	)
	@PropertyValidator(validator=org.openxava.test.validators.UnitPriceValidator.class)
	@OnChange( forViews="DEFAULT, WithSection",
		action=org.openxava.test.actions.OnChangeProductUnitPriceAction.class
	)
	private BigDecimal unitPrice;
	
	@Stereotype("MEMO")
	private String remarks;

	@Depends(properties="unitPrice") 
	@Max(9999999999L) 	
	public BigDecimal getUnitPriceInPesetas() {
		if (unitPrice == null) return null;
		return unitPrice.multiply(new BigDecimal("166.386")).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	public void increasePrice() {
		setUnitPrice(getUnitPrice().multiply(new BigDecimal("1.02")).setScale(2));
	}
	
	public BigDecimal getPrice(String country, BigDecimal tariff) throws ProductException, PriceException {
		if ("España".equals(country) || "Guatemala".equals(country)) {
			return getUnitPrice().add(tariff);   
		}
		else {
			throw new PriceException("Country not register");
		}				
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFamilyNumber() {
		return familyNumber;
	}

	public void setFamilyNumber(int familyNumber) {
		this.familyNumber = familyNumber;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getSubfamilyNumber() {
		return subfamilyNumber;
	}

	public void setSubfamilyNumber(int subfamilyNumber) {
		this.subfamilyNumber = subfamilyNumber;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Stereotype("WAREHOUSE") @Required
	@OnChange(forViews="WithSection",
		action=org.openxava.test.actions.OnChangeVoidAction.class
	)
	public WarehouseKey getWarehouseKey() {
		WarehouseKey key = new WarehouseKey();
		key.setNumber(this.warehouseNumber);
		key.setZoneNumber(this.warehouseZoneNumber);
		return key;
	}

	public void setWarehouseKey(WarehouseKey warehouseKey) {	
		if (warehouseKey == null) {
			this.warehouseZoneNumber = null;
			this.warehouseNumber = null;						
		}
		else {
			this.warehouseZoneNumber = warehouseKey.getZoneNumber();
			this.warehouseNumber = warehouseKey.getNumber();			
		}
	}
	
}
