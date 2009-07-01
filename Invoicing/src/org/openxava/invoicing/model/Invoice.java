package org.openxava.invoicing.model;

import java.util.*;
import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.invoicing.calculators.*;

@Entity
@IdClass(InvoiceKey.class)
@View(members=
	"year, number, date;" +
	"customer;" +
	"details;" +
	"remarks"
)
public class Invoice {
	
	
	@Id @Column(length=4)
	@DefaultValueCalculator(CurrentYearCalculator.class)
	private int year;
	
	
	@Id @Column(length=6)
	@DefaultValueCalculator(value=NextNumberForYearCalculator.class,
		properties=@PropertyValue(name="year") 
	)
	private int number;
	
	
	@Required
	@DefaultValueCalculator(CurrentDateCalculator.class)
	private Date date;	
	
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@ReferenceView("Simple")
	private Customer customer;
	
	
	@OneToMany(mappedBy="invoice", cascade=CascadeType.ALL)
	@ListProperties("product.number, product.description, quantity")	
	private Collection<Detail> details;
	
	
	@Stereotype("MEMO")
	private String remarks;
	
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
	

}
