
// File generated by OpenXava: Fri Jun 22 18:13:53 CEST 2007
// Archivo generado por OpenXava: Fri Jun 22 18:13:53 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Invoice		Entity/Entidad

package org.openxava.test.model.xejb;

import java.util.*;
import java.math.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;

import org.openxava.ejbx.*;
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.validators.ValidationException;

import org.openxava.test.model.*;


/**
 * @ejb:bean name="Invoice" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Invoice" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IInvoice"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Invoice" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Customer_number = ?1 ORDER BY o.year, o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Customer_number = ?1 ORDER BY o.year, o.number" 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Invoice o WHERE 1 = 1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Invoice o WHERE 1 = 1" 	
 * @ejb:finder signature="Collection findPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid = 'S'" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid = 'S'" 	
 * @ejb:finder signature="Collection findNotPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid <> 'S'" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findNotPaidOnes()" query="SELECT OBJECT(o) FROM Invoice o WHERE o._Paid <> 'S'" 	
 * @ejb:finder signature="Invoice findByYearNumber(int year,int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o.year = ?1 and o.number = ?2" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Invoice findByYearNumber(int year,int number)" query="SELECT OBJECT(o) FROM Invoice o WHERE o.year = ?1 and o.number = ?2" 
 * 
 * @jboss:table-name "XAVATEST.INVOICE"
 *
 * @author Javier Paniza
 */
abstract public class InvoiceBean extends EJBReplicableBase 
			implements org.openxava.test.model.IInvoice, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.InvoiceKey ejbCreate(Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
			
		return null;
	} 
	public void ejbPostCreate(Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.InvoiceKey ejbCreate(org.openxava.test.model.InvoiceData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		setYear(data.getYear()); 
		setNumber(data.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.InvoiceData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.InvoiceKey ejbCreate(org.openxava.test.model.InvoiceValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setInvoiceValue(value); 
		setYear(value.getYear()); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.InvoiceValue value) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	public void ejbLoad() {
		creating = false;
		modified = false; 
	}
	
	public void ejbStore() {
		if (creating) {
			creating = false;
			return;
		}
		if (!modified) return; 
		
		modified = false;
	} 	
	

	public void ejbRemove() throws RemoveException { 						
	} 	
	
	// Properties/Propiedades 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getTotal() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.TotalInvoiceCalculator totalCalculator= (org.openxava.test.calculators.TotalInvoiceCalculator)
				getMetaModel().getMetaProperty("total").getMetaCalculator().createCalculator();  	
			
			totalCalculator.setVat(getVat());  	
			
			totalCalculator.setAmountsSum(getAmountsSum()); 
			return (java.math.BigDecimal) totalCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Total", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setTotal(java.math.BigDecimal newTotal) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	private static org.openxava.converters.IConverter vatPercentageConverter;
	private org.openxava.converters.IConverter getVatPercentageConverter() {
		if (vatPercentageConverter == null) {
			try {
				vatPercentageConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("vatPercentage");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "vatPercentage"));
			}
			
		}	
		return vatPercentageConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "VATPERCENTAGE"
	 */
	public abstract java.math.BigDecimal get_VatPercentage();
	public abstract void set_VatPercentage(java.math.BigDecimal newVatPercentage); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getVatPercentage() {
		try {
			return (java.math.BigDecimal) getVatPercentageConverter().toJava(get_VatPercentage());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "VatPercentage", "Invoice", "java.math.BigDecimal"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setVatPercentage(java.math.BigDecimal newVatPercentage) {
		try { 
			this.modified = true; 
			set_VatPercentage((java.math.BigDecimal) getVatPercentageConverter().toDB(newVatPercentage));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "VatPercentage", "Invoice", "java.math.BigDecimal"));
		}		
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public boolean isConsiderable() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.ConsiderableCalculator considerableCalculator= (org.openxava.test.calculators.ConsiderableCalculator)
				getMetaModel().getMetaProperty("considerable").getMetaCalculator().createCalculator();  	
			
			considerableCalculator.setAmount(getAmountsSum()); 
			return ((Boolean) considerableCalculator.calculate()).booleanValue();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references
			return false; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Considerable", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setConsiderable(boolean newConsiderable) {
		// for it is in value object
		// para que aparezca en los value objects
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getYearDiscount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.YearInvoiceDiscountCalculator yearDiscountCalculator= (org.openxava.test.calculators.YearInvoiceDiscountCalculator)
				getMetaModel().getMetaProperty("yearDiscount").getMetaCalculator().createCalculator();  	
			
			yearDiscountCalculator.setYear(getYear()); 
			return (java.math.BigDecimal) yearDiscountCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "YearDiscount", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setYearDiscount(java.math.BigDecimal newYearDiscount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getDetailsCount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.DetailsCountCalculator detailsCountCalculator= (org.openxava.test.calculators.DetailsCountCalculator)
				getMetaModel().getMetaProperty("detailsCount").getMetaCalculator().createCalculator();  	
			
			detailsCountCalculator.setYear(getYear());  	
			
			detailsCountCalculator.setNumber(getNumber()); 
				detailsCountCalculator.setConnectionProvider(getPortableContext()); 
			return ((Integer) detailsCountCalculator.calculate()).intValue();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return 0; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "DetailsCount", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setDetailsCount(int newDetailsCount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getVat() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.PercentageCalculator vatCalculator= (org.openxava.test.calculators.PercentageCalculator)
				getMetaModel().getMetaProperty("vat").getMetaCalculator().createCalculator();  	
			
			vatCalculator.setPercentage(getVatPercentage());  	
			
			vatCalculator.setValue(getAmountsSum()); 
			return (java.math.BigDecimal) vatCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Vat", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setVat(java.math.BigDecimal newVat) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "NUMBER"
	 */
	public abstract int getNumber();
	/**
	  * 
	  */
	public abstract void setNumber(int newNumber); 
	private static org.openxava.converters.IConverter dateConverter;
	private org.openxava.converters.IConverter getDateConverter() {
		if (dateConverter == null) {
			try {
				dateConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("date");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "date"));
			}
			
		}	
		return dateConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DATE"
	 */
	public abstract java.sql.Date get_Date();
	public abstract void set_Date(java.sql.Date newDate); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.util.Date getDate() {
		try {
			return (java.util.Date) getDateConverter().toJava(get_Date());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Date", "Invoice", "java.util.Date"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setDate(java.util.Date newDate) {
		try { 
			this.modified = true; 
			set_Date((java.sql.Date) getDateConverter().toDB(newDate));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Date", "Invoice", "java.util.Date"));
		}		
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getAmountsSum() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.AmountsSumCalculator amountsSumCalculator= (org.openxava.test.calculators.AmountsSumCalculator)
				getMetaModel().getMetaProperty("amountsSum").getMetaCalculator().createCalculator(); 
				amountsSumCalculator.setModel(this); 
			return (java.math.BigDecimal) amountsSumCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "AmountsSum", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setAmountsSum(java.math.BigDecimal newAmountsSum) {
		// for it is in value object
		// para que aparezca en los value objects
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getCustomerDiscount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.CustomerInvoiceDiscountCalculator customerDiscountCalculator= (org.openxava.test.calculators.CustomerInvoiceDiscountCalculator)
				getMetaModel().getMetaProperty("customerDiscount").getMetaCalculator().createCalculator();  	
			
			customerDiscountCalculator.setNumber(getCustomer_number());  	
			
			customerDiscountCalculator.setPaid(isPaid()); 
			return (java.math.BigDecimal) customerDiscountCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "CustomerDiscount", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setCustomerDiscount(java.math.BigDecimal newCustomerDiscount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	private static org.openxava.converters.IConverter paidConverter;
	private org.openxava.converters.IConverter getPaidConverter() {
		if (paidConverter == null) {
			try {
				paidConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("paid");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "paid"));
			}
			
		}	
		return paidConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "PAID"
	 */
	public abstract java.lang.String get_Paid();
	public abstract void set_Paid(java.lang.String newPaid); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public boolean isPaid() {
		try {
			return ((Boolean) getPaidConverter().toJava(get_Paid())).booleanValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Paid", "Invoice", "boolean"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setPaid(boolean newPaid) {
		try { 
			this.modified = true; 
			set_Paid((java.lang.String) getPaidConverter().toDB(new Boolean(newPaid)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Paid", "Invoice", "boolean"));
		}		
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String getImportance() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.ImportanceCalculator importanceCalculator= (org.openxava.test.calculators.ImportanceCalculator)
				getMetaModel().getMetaProperty("importance").getMetaCalculator().createCalculator();  	
			
			importanceCalculator.setAmount(getAmountsSum()); 
			return (String) importanceCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Importance", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setImportance(String newImportance) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "YEAR"
	 */
	public abstract int getYear();
	/**
	  * 
	  */
	public abstract void setYear(int newYear); 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getCustomerTypeDiscount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.CustomerTypeInvoiceDiscountCalculator customerTypeDiscountCalculator= (org.openxava.test.calculators.CustomerTypeInvoiceDiscountCalculator)
				getMetaModel().getMetaProperty("customerTypeDiscount").getMetaCalculator().createCalculator();  	
			
			customerTypeDiscountCalculator.setType(getCustomer().getType()); 
			return (java.math.BigDecimal) customerTypeDiscountCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "CustomerTypeDiscount", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setCustomerTypeDiscount(java.math.BigDecimal newCustomerTypeDiscount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	private static org.openxava.converters.IConverter commentConverter;
	private org.openxava.converters.IConverter getCommentConverter() {
		if (commentConverter == null) {
			try {
				commentConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("comment");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "comment"));
			}
			
		}	
		return commentConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "COMMENT"
	 */
	public abstract java.lang.String get_Comment();
	public abstract void set_Comment(java.lang.String newComment); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String getComment() {
		try {
			return (String) getCommentConverter().toJava(get_Comment());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Comment", "Invoice", "String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setComment(String newComment) {
		try { 
			this.modified = true; 
			set_Comment((java.lang.String) getCommentConverter().toDB(newComment));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Comment", "Invoice", "String"));
		}		
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getSellerDiscount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.SellerInvoiceDiscountCalculator sellerDiscountCalculator= (org.openxava.test.calculators.SellerInvoiceDiscountCalculator)
				getMetaModel().getMetaProperty("sellerDiscount").getMetaCalculator().createCalculator();  	
			
			sellerDiscountCalculator.setSellerNumber(getCustomer().getSeller().getNumber()); 
			return (java.math.BigDecimal) sellerDiscountCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "SellerDiscount", "Invoice", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setSellerDiscount(java.math.BigDecimal newSellerDiscount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 

	// Colecciones/Collections	

	private org.openxava.test.model.InvoiceDetailHome detailsHome; 
	/**
	 * @ejb:interface-method
	 */
	public java.util.Collection getDetails() {		
		try {
			return getDetailsHome().findByInvoice(getYear(), getNumber());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_collection_elements_error", "Details", "Invoice"));
		}
	}
		
	private org.openxava.test.model.InvoiceDetailHome getDetailsHome() throws Exception{
		if (detailsHome == null) {
			detailsHome = (org.openxava.test.model.InvoiceDetailHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/InvoiceDetail"),
			 		org.openxava.test.model.InvoiceDetailHome.class);			 		
		}
		return detailsHome;
	}	

	private org.openxava.test.model.DeliveryHome deliveriesHome; 
	/**
	 * @ejb:interface-method
	 */
	public java.util.Collection getDeliveries() {		
		try {
			return getDeliveriesHome().findByInvoice(getYear(), getNumber());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_collection_elements_error", "Deliveries", "Invoice"));
		}
	}
		
	private org.openxava.test.model.DeliveryHome getDeliveriesHome() throws Exception{
		if (deliveriesHome == null) {
			deliveriesHome = (org.openxava.test.model.DeliveryHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Delivery"),
			 		org.openxava.test.model.DeliveryHome.class);			 		
		}
		return deliveriesHome;
	}		

	// References/Referencias 

	// Customer : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ICustomer getCustomer() {
		try {		
			return getCustomerHome().findByPrimaryKey(getCustomerKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Customer", "Invoice"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.CustomerRemote getCustomerRemote() {
		return (org.openxava.test.model.CustomerRemote) getCustomer();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setCustomer(org.openxava.test.model.ICustomer newCustomer) { 
		this.modified = true; 
		try {	
			if (newCustomer == null) setCustomerKey(null);
			else {
				if (newCustomer instanceof org.openxava.test.model.Customer) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.CustomerRemote remote = (org.openxava.test.model.CustomerRemote) newCustomer;
				setCustomerKey((org.openxava.test.model.CustomerKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Customer", "Invoice"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.CustomerKey getCustomerKey() {				
		org.openxava.test.model.CustomerKey key = new org.openxava.test.model.CustomerKey(); 
		key.number = getCustomer_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setCustomerKey(org.openxava.test.model.CustomerKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.CustomerKey(); 
			setCustomer_number(key.number);					
		}
		else { 
			setCustomer_number(key.number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "CUSTOMER_NUMBER"
	 */
	public abstract int get_Customer_number();
	public abstract void set_Customer_number(int newCustomer_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getCustomer_number() { 
		return get_Customer_number(); 
	}
	public void setCustomer_number(int newCustomer_number) { 
		set_Customer_number(newCustomer_number); 	
	} 

	private org.openxava.test.model.CustomerHome customerHome;	
	private org.openxava.test.model.CustomerHome getCustomerHome() throws Exception{
		if (customerHome == null) {
			customerHome = (org.openxava.test.model.CustomerHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Customer"),
			 		org.openxava.test.model.CustomerHome.class);			 		
		}
		return customerHome;
	} 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Invoice").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.InvoiceData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.InvoiceData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.InvoiceValue getInvoiceValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setInvoiceValue(org.openxava.test.model.InvoiceValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setYear(0); 
		setNumber(0); 
		setDate(null); 
		setVatPercentage(null); 
		setComment(null); 
		setPaid(false); 
		setCustomerKey(null); 	
	} 		
}