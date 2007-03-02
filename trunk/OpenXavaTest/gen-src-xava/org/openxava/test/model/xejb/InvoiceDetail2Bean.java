
// File generated by OpenXava: Thu Mar 01 13:37:26 CET 2007
// Archivo generado por OpenXava: Thu Mar 01 13:37:26 CET 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Invoice2		Aggregate/Agregado: InvoiceDetail2

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
 * @ejb:bean name="InvoiceDetail2" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/InvoiceDetail2" reentrant="true" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IInvoiceDetail2"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="InvoiceDetail2" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByProduct(long number)" query="SELECT OBJECT(o) FROM InvoiceDetail2 o WHERE o._Product_number = ?1 ORDER BY o.oid" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByProduct(long number)" query="SELECT OBJECT(o) FROM InvoiceDetail2 o WHERE o._Product_number = ?1 ORDER BY o.oid" 	
 * @ejb:finder signature="Collection findByInvoice2(int year, int number)" query="SELECT OBJECT(o) FROM InvoiceDetail2 o WHERE o._Invoice2_year = ?1 AND o._Invoice2_number = ?2 ORDER BY o.oid" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByInvoice2(int year, int number)" query="SELECT OBJECT(o) FROM InvoiceDetail2 o WHERE o._Invoice2_year = ?1 AND o._Invoice2_number = ?2 ORDER BY o.oid" 
 * 
 * @jboss:table-name "XAVATEST_INVOICEDETAIL"
 *
 * @author Javier Paniza
 */
abstract public class InvoiceDetail2Bean extends EJBReplicableBase 
			implements org.openxava.test.model.IInvoiceDetail2, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.InvoiceDetail2Key ejbCreate(org.openxava.test.model.Invoice2Remote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		org.openxava.test.model.Invoice2Key containerKey = null;
		try {
			containerKey = (org.openxava.test.model.Invoice2Key) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Invoice2", "InvoiceDetail2"));
		} 
		setInvoice2_year(containerKey.year); 
		setInvoice2_number(containerKey.number); 
		try { 	
			org.openxava.test.calculators.InvoiceDetail2OidCalculator oidCalculator = (org.openxava.test.calculators.InvoiceDetail2OidCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 	
			oidCalculator.setInvoiceYear(getInvoice2_year()); 	
			oidCalculator.setInvoiceNumber(getInvoice2_number()); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.Invoice2Remote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.InvoiceDetail2Key ejbCreate(org.openxava.test.model.Invoice2Key containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		executeSets(values); 
		setInvoice2_year(containerKey.year); 
		setInvoice2_number(containerKey.number); 
		try { 	
			org.openxava.test.calculators.InvoiceDetail2OidCalculator oidCalculator = (org.openxava.test.calculators.InvoiceDetail2OidCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 	
			oidCalculator.setInvoiceYear(getInvoice2_year()); 	
			oidCalculator.setInvoiceNumber(getInvoice2_number()); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
			
		return null;
	}

	public void ejbPostCreate(org.openxava.test.model.Invoice2Key containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException { 
	} 
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.InvoiceDetail2Key ejbCreate(org.openxava.test.model.Invoice2Remote container, int counter, org.openxava.test.model.InvoiceDetail2Data data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		org.openxava.test.model.Invoice2Key containerKey = null;
		try {
			containerKey = (org.openxava.test.model.Invoice2Key) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Invoice2", "InvoiceDetail2"));
		} 
		setInvoice2_year(containerKey.year); 
		setInvoice2_number(containerKey.number); 
		try { 	
			org.openxava.test.calculators.InvoiceDetail2OidCalculator oidCalculator= (org.openxava.test.calculators.InvoiceDetail2OidCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 	
			oidCalculator.setInvoiceYear(getInvoice2_year()); 	
			oidCalculator.setInvoiceNumber(getInvoice2_number()); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.Invoice2Remote container, int counter, org.openxava.test.model.InvoiceDetail2Data data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.InvoiceDetail2Key ejbCreate(org.openxava.test.model.Invoice2Remote container, int counter, org.openxava.test.model.InvoiceDetail2Value value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setInvoiceDetail2Value(value); 
		setOid(value.getOid()); 
		org.openxava.test.model.Invoice2Key containerKey = null;
		try {
			containerKey = (org.openxava.test.model.Invoice2Key) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Invoice2", "InvoiceDetail2"));
		} 
		setInvoice2_year(containerKey.year); 
		setInvoice2_number(containerKey.number); 
		try { 	
			org.openxava.test.calculators.InvoiceDetail2OidCalculator oidCalculator = (org.openxava.test.calculators.InvoiceDetail2OidCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 	
			oidCalculator.setInvoiceYear(getInvoice2_year()); 	
			oidCalculator.setInvoiceNumber(getInvoice2_number()); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.Invoice2Remote container, int counter, org.openxava.test.model.InvoiceDetail2Value value) 
		throws
			CreateException,
			ValidationException { 			
	}	 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.InvoiceDetail2Key ejbCreate(org.openxava.test.model.Invoice2Key containerKey, int counter, org.openxava.test.model.InvoiceDetail2Value value)
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		setInvoiceDetail2Value(value); 
		setOid(value.getOid());
		setInvoice2_year(containerKey.year);
		setInvoice2_number(containerKey.number); 
		try { 
			org.openxava.test.calculators.InvoiceDetail2OidCalculator oidCalculator= (org.openxava.test.calculators.InvoiceDetail2OidCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 
			oidCalculator.setInvoiceYear(getInvoice2_year()); 
			oidCalculator.setInvoiceNumber(getInvoice2_number()); 
			setOid((String) oidCalculator.calculate());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
		return null;					

	} 
	public void ejbPostCreate(org.openxava.test.model.Invoice2Key containerKey, int counter, org.openxava.test.model.InvoiceDetail2Value value)	
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
	public java.math.BigDecimal getAmount() { 
		boolean cmtActivated = false;
		if (!org.openxava.hibernate.XHibernate.isCmt()) {
			org.openxava.hibernate.XHibernate.setCmt(true);
			cmtActivated = true;
		} 		
		try {			
			org.openxava.test.calculators.DetailAmountCalculator amountCalculator= (org.openxava.test.calculators.DetailAmountCalculator)
				getMetaModel().getMetaProperty("amount").getMetaCalculator().createCalculator();  	
			
			amountCalculator.setUnitPrice(getUnitPrice());  	
			
			amountCalculator.setQuantity(getQuantity()); 
			return (java.math.BigDecimal) amountCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Amount", "InvoiceDetail2", ex.getLocalizedMessage()));
		} 
		finally {
			if (cmtActivated) {
				org.openxava.hibernate.XHibernate.setCmt(false);
			}
		} 		
	}
	public void setAmount(java.math.BigDecimal newAmount) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	private static org.openxava.converters.IConverter unitPriceConverter;
	private org.openxava.converters.IConverter getUnitPriceConverter() {
		if (unitPriceConverter == null) {
			try {
				unitPriceConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("unitPrice");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "unitPrice"));
			}
			
		}	
		return unitPriceConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "UNITPRICE"
	 */
	public abstract java.math.BigDecimal get_UnitPrice();
	public abstract void set_UnitPrice(java.math.BigDecimal newUnitPrice); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getUnitPrice() {
		try {
			return (java.math.BigDecimal) getUnitPriceConverter().toJava(get_UnitPrice());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "UnitPrice", "InvoiceDetail2", "java.math.BigDecimal"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setUnitPrice(java.math.BigDecimal newUnitPrice) {
		try { 
			this.modified = true; 
			set_UnitPrice((java.math.BigDecimal) getUnitPriceConverter().toDB(newUnitPrice));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "UnitPrice", "InvoiceDetail2", "java.math.BigDecimal"));
		}		
	} 
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "OID"
	 */
	public abstract String getOid();
	/**
	  * 
	  */
	public abstract void setOid(String newOid); 
	private static org.openxava.converters.IConverter quantityConverter;
	private org.openxava.converters.IConverter getQuantityConverter() {
		if (quantityConverter == null) {
			try {
				quantityConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("quantity");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "quantity"));
			}
			
		}	
		return quantityConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "QUANTITY"
	 */
	public abstract java.lang.Integer get_Quantity();
	public abstract void set_Quantity(java.lang.Integer newQuantity); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getQuantity() {
		try {
			return ((Integer) getQuantityConverter().toJava(get_Quantity())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Quantity", "InvoiceDetail2", "int"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setQuantity(int newQuantity) {
		try { 
			this.modified = true; 
			set_Quantity((java.lang.Integer) getQuantityConverter().toDB(new Integer(newQuantity)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Quantity", "InvoiceDetail2", "int"));
		}		
	} 

	// Colecciones/Collections		

	// References/Referencias 

	// Product : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IProduct getProduct() {
		try {		
			return getProductHome().findByPrimaryKey(getProductKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Product", "InvoiceDetail2"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ProductRemote getProductRemote() {
		return (org.openxava.test.model.ProductRemote) getProduct();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setProduct(org.openxava.test.model.IProduct newProduct) { 
		this.modified = true; 
		try {	
			if (newProduct == null) setProductKey(null);
			else {
				if (newProduct instanceof org.openxava.test.model.Product) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.ProductRemote remote = (org.openxava.test.model.ProductRemote) newProduct;
				setProductKey((org.openxava.test.model.ProductKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Product", "InvoiceDetail2"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ProductKey getProductKey() {				
		org.openxava.test.model.ProductKey key = new org.openxava.test.model.ProductKey(); 
		key.number = getProduct_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setProductKey(org.openxava.test.model.ProductKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.ProductKey(); 
			setProduct_number(key.number);					
		}
		else { 
			setProduct_number(key.number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "PRODUCT_NUMBER"
	 */
	public abstract long get_Product_number();
	public abstract void set_Product_number(long newProduct_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public long getProduct_number() { 
		return get_Product_number(); 
	}
	public void setProduct_number(long newProduct_number) { 
		set_Product_number(newProduct_number); 	
	} 

	private org.openxava.test.model.ProductHome productHome;	
	private org.openxava.test.model.ProductHome getProductHome() throws Exception{
		if (productHome == null) {
			productHome = (org.openxava.test.model.ProductHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Product"),
			 		org.openxava.test.model.ProductHome.class);			 		
		}
		return productHome;
	} 

	// Invoice2 : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IInvoice2 getInvoice2() {
		try {		
			return getInvoice2Home().findByPrimaryKey(getInvoice2Key());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Invoice2", "InvoiceDetail2"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Invoice2Remote getInvoice2Remote() {
		return (org.openxava.test.model.Invoice2Remote) getInvoice2();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setInvoice2(org.openxava.test.model.IInvoice2 newInvoice2) { 
		this.modified = true; 
		try {	
			if (newInvoice2 == null) setInvoice2Key(null);
			else {
				if (newInvoice2 instanceof org.openxava.test.model.Invoice2) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.Invoice2Remote remote = (org.openxava.test.model.Invoice2Remote) newInvoice2;
				setInvoice2Key((org.openxava.test.model.Invoice2Key) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Invoice2", "InvoiceDetail2"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Invoice2Key getInvoice2Key() {				
		org.openxava.test.model.Invoice2Key key = new org.openxava.test.model.Invoice2Key(); 
		key.year = getInvoice2_year(); 
		key.number = getInvoice2_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setInvoice2Key(org.openxava.test.model.Invoice2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Invoice2Key(); 
			setInvoice2_year(key.year); 
			setInvoice2_number(key.number);					
		}
		else { 
			setInvoice2_year(key.year); 
			setInvoice2_number(key.number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "INVOICE_YEAR"
	 */
	public abstract int get_Invoice2_year();
	public abstract void set_Invoice2_year(int newInvoice2_year);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getInvoice2_year() { 
		return get_Invoice2_year(); 
	}
	public void setInvoice2_year(int newInvoice2_year) { 
		set_Invoice2_year(newInvoice2_year); 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "INVOICE_NUMBER"
	 */
	public abstract int get_Invoice2_number();
	public abstract void set_Invoice2_number(int newInvoice2_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getInvoice2_number() { 
		return get_Invoice2_number(); 
	}
	public void setInvoice2_number(int newInvoice2_number) { 
		set_Invoice2_number(newInvoice2_number); 	
	} 

	private org.openxava.test.model.Invoice2Home invoice2Home;	
	private org.openxava.test.model.Invoice2Home getInvoice2Home() throws Exception{
		if (invoice2Home == null) {
			invoice2Home = (org.openxava.test.model.Invoice2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Invoice2"),
			 		org.openxava.test.model.Invoice2Home.class);			 		
		}
		return invoice2Home;
	} 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Invoice2").getMetaAggregate("InvoiceDetail2"); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.InvoiceDetail2Data getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.InvoiceDetail2Data data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.InvoiceDetail2Value getInvoiceDetail2Value();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setInvoiceDetail2Value(org.openxava.test.model.InvoiceDetail2Value value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setOid(null); 
		setQuantity(0); 
		setUnitPrice(null); 
		setProductKey(null); 
		setInvoice2Key(null); 	
	} 		
}