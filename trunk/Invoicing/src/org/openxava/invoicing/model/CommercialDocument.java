package org.openxava.invoicing.model;

import java.math.*;
import java.util.*;
import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.invoicing.calculators.*;
import org.openxava.jpa.*;

@Entity
@View(members=
	"year, number, date;" +
	"data {" +
		"customer;" +
		"details;" +
		"amounts [ " +
		"	vatPercentage, baseAmount, vat, totalAmount" +
		"];" +  
		"remarks" +
	"}"	
)
abstract public class CommercialDocument extends Identifiable {
	
	@Column(length=4) 
	@DefaultValueCalculator(CurrentYearCalculator.class)
	private int year;
	
	
	@Column(length=6)
	/* tmp
	@DefaultValueCalculator(value=NextNumberForYearCalculator.class,
		properties=@PropertyValue(name="year") 
	)
	*/
	private int number;
	
	
	@Required
	@DefaultValueCalculator(CurrentDateCalculator.class)
	private Date date;	
	
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@ReferenceView("Simple")
	private Customer customer;
	
	
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)	
	@ListProperties("product.number, product.description, quantity, pricePerUnit, amount")
	private Collection<Detail> details;	
	
	
	@Stereotype("MEMO") 
	private String remarks;
	
	@Digits(integerDigits=2, fractionalDigits=0) 
	@Required
	private BigDecimal vatPercentage;	
	
	@Stereotype("MONEY")
	public BigDecimal getBaseAmount() {
		BigDecimal result = new BigDecimal("0.00");
		for (Detail detail: getDetails()) {
			result = result.add(detail.getAmount());
		}
		return result;
	}
	
	@Stereotype("MONEY")
	@Depends("vatPercentage")
	public BigDecimal getVat() {
		return getBaseAmount()
			.multiply(getVatPercentage())
			.divide(new BigDecimal("100"));		
	}
	
	@Stereotype("MONEY")
	@Depends("baseAmount, vat")
	public BigDecimal getTotalAmount() {
		return getBaseAmount().add(getVat());
	}
	
	@PrePersist
	public void calculateNumber() throws Exception { // tmp
		Query query = XPersistence.getManager()
			.createQuery("select max(i.number) from CommercialDocument i " +
					"where i.year = :year");
		query.setParameter("year", year);		
		Integer lastNumber = (Integer) query.getSingleResult();
		this.number = lastNumber == null?1:lastNumber + 1;
	}
	
	// Getters and setters
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Collection<Detail> getDetails() {
		return details;
	}

	public void setDetails(Collection<Detail> details) {
		this.details = details;
	}
		
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public BigDecimal getVatPercentage() {
		return vatPercentage==null?BigDecimal.ZERO:vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

}
