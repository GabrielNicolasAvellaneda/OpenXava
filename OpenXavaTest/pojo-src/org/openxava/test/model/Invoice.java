package org.openxava.test.model;

import java.math.*;
import java.sql.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.hibernate.annotations.Type;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.calculators.*;
import org.openxava.test.filters.*;
import org.openxava.jpa.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

@Entity
@IdClass(InvoiceKey.class)
@Views({
	@View(members=
		"year, number, date, paid;" +		
		"discounts [" +
		"	customerDiscount, customerTypeDiscount, yearDiscount;" +
		"];" +
		"comment;" +			
		"customer { customer }" +		
		"details { details }" +			
		"amounts { amountsSum; vatPercentage; vat }" +
		"deliveries { deliveries }"		
	),
	@View(name="NoSections", members=
		"year, number, date;" +		
		"customer;" +		
		"details;" +			
		"amountsSum;" +
		"vatPercentage, vat;" +
		"total"	
	),	
	@View(name="Simple", members="year, number, date, yearDiscount;"),
	@View(name="NestedSections", members=
		"year, number, date;" +
		"customer { customer }" +
		"data {" +				 
		"	details { details }" +
		"	amounts {" +
		"		vat { vatPercentage; vat }" +				
		"		amountsSum { amountsSum }" +
		"	}" +				
		"}" +						
		"deliveries { deliveries }"		
	),
	@View(name="NestedSections2", members=
		"year, number, date;" +
		"data {" +				 
		"	amountsSum { amountsSum }" +
		"	details { details }" +						
		"}" 		
	),
	@View(name="OnlyReadDetails", members=
		"year, number, date, paid;" +
		"details;"
	),
	@View(name="OnlyEditDetails", members=
		"year, number, date, paid;" +
		"details;"
	),
	@View(name="NotAllActionsInDetails", members=
		"year, number, date, paid;" +
		"details;"
	),	
	@View(name="Deliveries", members=
		"year, number, date;" +
		"deliveries;"
	),
	@View(name="Amounts", members=
		"year, number;" +
		"amounts [#" + 		
			"customerDiscount, customerTypeDiscount, yearDiscount;" +
			"amountsSum, vatPercentage, vat;" +
		"]"			
	),
	@View(name="CustomerAsAggregateWithDeliveryPlaces", members=
		"year, number, date, paid;" +
		"customer"
	)	
})

@Tabs({
	@Tab(properties="year, number, date, amountsSum, vat, detailsCount, paid, importance"), 	
	@Tab(name="Level4Reference", properties="year, number, customer.seller.level.description"),
	@Tab(name="Simple", properties="year, number, date", 
		defaultOrder="${year} desc, ${number} desc"
	),
	@Tab(name="Current", 
		filter=CurrentYearFilter.class, 
		properties="year, number, amountsSum, vat, detailsCount, paid, customer.name",			
		baseCondition="${year} = ?"
	),
	@Tab(name="CurrentSelect", 
		filter=CurrentYearFilter.class,
		properties="year, number, amountsSum, paid, customer.name",
		baseCondition=
			"select" + 
			"	${year}, ${number}, ${amountsSum}, ${paid}, ${customer.name} " +
			"from " +
			"	XAVATEST@separator@FACTURA, XAVATEST@separator@CLIENTE " +
			"where " +
			"	XAVATEST@separator@FACTURA.CLIENTE_CODIGO = XAVATEST@separator@CLIENTE.CODIGO AND " +
			"	${year} = ?"							
	),
	@Tab(name="DefaultYear", 
		filter=DefaultYearFilter.class,
		properties="year, number, customer.number, customer.name, amountsSum, vat, detailsCount, paid, importance",
		baseCondition="${year} = ?"
	),
	@Tab(name="DefaultYearEnv", 
		filter=DefaultYearEnvFilter.class,
		properties="year, number, customer.number, customer.name, amountsSum, vat, detailsCount, paid, importance",
		baseCondition="${year} = ?"
	),
	@Tab(name="ActiveYear",
		filter=ActiveYearFilter.class,
		properties="year, number, customer.number, customer.name, amountsSum, vat, detailsCount, paid, importance",
		baseCondition="${year} = ?"
	),
	@Tab(name="Invoice20020001",
		filter=Invoice20020001Filter.class,
		properties="year, number, amountsSum, vat",
		baseCondition="${year} = ? and ${number} = ?"
	),
	@Tab(name="YearParameter", 
		filter=YearParameterFilter.class,
		properties="year, number, customer.number, customer.name, amountsSum, vat, detailsCount, paid, importance",
		baseCondition="${year} = ?"
	)
})

public class Invoice {
	
	private static Log log = LogFactory.getLog(Invoice.class);
	
	final private static BigDecimal DISCOUNT = new BigDecimal("20.00");
	final private static BigDecimal HUNDRED = new BigDecimal("100");
	
	@Id @Column(length=4) @Max(9999) @Required
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
	
	@Column(length=50)
	private String comment;
	
	@Type(type="org.openxava.types.SiNoType")
	private boolean paid;
		
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@ReferenceView("Simple")
	@ReferenceViews(
		@ReferenceView(forViews="CustomerAsAggregateWithDeliveryPlaces", value="SimpleWithDeliveryPlaces")	
	)
	@AsEmbedded(forViews="CustomerAsAggregateWithDeliveryPlaces")
	private Customer customer;
	
	@OneToMany (mappedBy="invoice", cascade=CascadeType.REMOVE)
	@OrderBy("serviceType desc") @org.hibernate.validator.Size(min=1)
	@ListsProperties({
		@ListProperties(forViews="DEFAULT", value="serviceType, product.description, product.unitPriceInPesetas, quantity, unitPrice, amount, free"),
		@ListProperties(forViews="NoSections", value="product.description, product.unitPrice+, quantity, amount"),  
	})
	@EditAction(forViews="DEFAULT", value="Invoice.editDetail")
	@DetailAction(forViews="DEFAULT", value="Invoice.viewProduct")
	@ReadOnly(forViews="OnlyReadDetails")
	@EditOnly(forViews="OnlyEditDetails")
	@NewAction(forViews="NotAllActionsInDetails", value="")
	@RemoveAction(forViews="NotAllActionsInDetails", value="")
	@RemoveSelectedAction(forViews="NotAllActionsInDetails", value="")
	private Collection<InvoiceDetail> details;
				
	@OneToMany (mappedBy="invoice")
	@CollectionView(forViews="DEFAULT, Deliveries", value="InInvoice")
	@ReadOnly(forViews="DEFAULT, Deliveries")
	@ViewAction(forViews="Deliveries", value="Invoice.viewDelivery")
	private Collection<Delivery> deliveries;
	
	@Stereotype("MONEY")
	public BigDecimal getSellerDiscount() {
		Customer customer = getCustomer();
		if (customer == null) return null;
		Seller seller = customer.getSeller();
		if (seller == null) return BigDecimal.ZERO;
		return seller.getNumber() == 1?DISCOUNT:BigDecimal.ZERO;
	}

	@Stereotype("MONEY") @Depends("year")
	public BigDecimal getYearDiscount() {		
		if (year < 2002) return new BigDecimal("0.00");
		if (year < 2004) return new BigDecimal("200.00");
		return new BigDecimal("400.00");
	}
	
	@Stereotype("MONEY")
	public BigDecimal getAmountsSum() {		
		BigDecimal result = BigDecimal.ZERO;		
		for (InvoiceDetail detail: getDetails()) { 			
			result = result.add(detail.getAmount());
		}		
		return result;		
	}
	
	@Stereotype("MONEY") @Depends("vatPercentage, amountsSum")
	public BigDecimal getVat() {
		return getAmountsSum().multiply(getVatPercentage()).divide(HUNDRED, 2, BigDecimal.ROUND_HALF_UP);
	}
	
	@Max(999)
	public int getDetailsCount() {
		// An example of using JDBC
		Connection con = null;
		try {
			con = DataSourceConnectionProvider.getByComponent("Invoice").getConnection();				
			String table = MetaModel.get("InvoiceDetail").getMapping().getTable();
			PreparedStatement ps = con.prepareStatement("select count(*) from " + table + " where INVOICE_YEAR = ? and INVOICE_NUMBER = ?");						
			ps.setInt(1, getYear());
			ps.setInt(2, getNumber());
			ResultSet rs = ps.executeQuery();
			rs.next();
			Integer result = new Integer(rs.getInt(1)); 
			ps.close();
			return result;			
		}
		catch (Exception ex) {
			log.error("Problem calculating details count of an Invoice", ex);
			// You can throw any runtime exception here
			throw new SystemException(ex); 
		}
		finally {
			try {
				con.close();
			}
			catch (Exception ex) {				
			}
		}				
	}
	
	@Depends("amountsSum")
	public boolean isConsiderable() {		
		return getAmountsSum().compareTo(new BigDecimal(10000)) >= 0;
	}
	
	@Column(length=10)
	public String getImportance() {
		BigDecimal amounts = getAmountsSum();
		if (amounts.compareTo(new BigDecimal(100)) < 0) return "Trivial";
		if (amounts.compareTo(new BigDecimal(3000)) < 0) return "Normal";
		return "Succulent";		
	}
	
	@Stereotype("MONEY") @Depends("customer.number, paid")
	public BigDecimal getCustomerDiscount() {
		if (paid) return new BigDecimal("77");
		if (customer.getNumber() == 1) return new BigDecimal("11.50");
		if (customer.getNumber() == 2) return new BigDecimal("22.75");
		return new BigDecimal("0.25");		
	}
	
	@Stereotype("MONEY")
	public BigDecimal getCustomerTypeDiscount() {
		Customer.Type type = customer.getType(); 
		if (type == Customer.Type.STEADY) return new BigDecimal("20.00");
		if (type == Customer.Type.SPECIAL) return new BigDecimal("30.00");
		return new BigDecimal("00.00");		
	}
	
	@Stereotype("MONEY")
	public BigDecimal getTotal() {
		return getVat().add(getAmountsSum());
	}
	
 	public static Collection findAll()  { 		 			
 		Query query = XPersistence.getManager().createQuery("from Invoice"); 
 		return query.getResultList();  		 		
 	} 	
 	
 	public static Collection findPaidOnes()  { 		 			
 		Query query = XPersistence.getManager().createQuery("from Invoice as o where o.paid = true"); 
 		return query.getResultList();  		 		
 	}
 	
 	public static Collection findNotPaidOnes()  { 			
 		Query query = XPersistence.getManager().createQuery("from Invoice as o where o.paid = false"); 
 		return query.getResultList();  		
 	} 	
 	
 	public static Invoice findByYearNumber(int year,int number) throws NoResultException { 			
 		Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from Invoice as o where o.year = :year and number = :number"); 
		query.setParameter("year", new Integer(year)); 
		query.setParameter("number", new Integer(number)); 
		return (Invoice) query.getSingleResult();
 	} 

	
	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public BigDecimal getVatPercentage() {
		return vatPercentage==null?BigDecimal.ZERO:vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Collection<InvoiceDetail> getDetails() {
		return details; 
	}

	public void setDetails(Collection<InvoiceDetail> details) {
		this.details = details;
	}

	public Collection<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(Collection<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

}
