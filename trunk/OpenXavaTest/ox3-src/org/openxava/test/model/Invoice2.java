package org.openxava.test.model;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@Table(name="INVOICE")
@IdClass(InvoiceKey.class) // We reuse the key class for Invoice
@View( members =
	"year, number, date, vatPercentage;" +
	"customer;" +
	"details;"
)
public class Invoice2 {
	
	@Id @Column(length=4) @Required
	@DefaultValueCalculator(CurrentYearCalculator.class)
	private int year;
	
	@Id @Column(length=6) @Required
	private int number;
		
	@Required
	@DefaultValueCalculator(CurrentDateCalculator.class)
	private java.util.Date date;
	
	@Digits(integerDigits=2, fractionalDigits=1) 
	@Required
	private BigDecimal vatPercentage;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@ReferenceView("Simplest")
	private Customer customer;
	
	@OneToMany (mappedBy="invoice2", cascade=CascadeType.REMOVE)
	@org.hibernate.validator.Size(min=1)
	private Collection<InvoiceDetail2> details;
	
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

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Collection<InvoiceDetail2> getDetails() {
		return details;
	}

	public void setDetails(Collection<InvoiceDetail2> details) {
		this.details = details;
	}


}
