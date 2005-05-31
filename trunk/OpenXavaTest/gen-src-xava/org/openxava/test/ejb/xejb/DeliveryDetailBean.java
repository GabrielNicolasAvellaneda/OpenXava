
// File generated by OpenXava: Tue May 31 12:49:12 CEST 2005
// Archivo generado por OpenXava: Tue May 31 12:49:12 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Delivery		Aggregate/Agregado: DeliveryDetail

package org.openxava.test.ejb.xejb;

import java.util.*;
import java.math.*;
import javax.ejb.*;
import javax.rmi.PortableRemoteObject;

import org.openxava.ejbx.*;
import org.openxava.util.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.validators.ValidationException;

import org.openxava.test.ejb.*;


/**
 * @ejb:bean name="DeliveryDetail" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/DeliveryDetail" reentrant="true" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IDeliveryDetail"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="DeliveryDetail" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByDelivery(int number, int type_number, int invoice_year, int invoice_number)" query="SELECT OBJECT(o) FROM DeliveryDetail o WHERE o._Delivery_number = ?1 AND o._Delivery_type_number = ?2 AND o._Delivery_invoice_year = ?3 AND o._Delivery_invoice_number = ?4 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByDelivery(int number, int type_number, int invoice_year, int invoice_number)" query="SELECT OBJECT(o) FROM DeliveryDetail o WHERE o._Delivery_number = ?1 AND o._Delivery_type_number = ?2 AND o._Delivery_invoice_year = ?3 AND o._Delivery_invoice_number = ?4 " 
 * 
 * @jboss:table-name "XAVATEST@separator@DELIVERYDETAIL"
 *
 * @author Javier Paniza
 */
abstract public class DeliveryDetailBean extends EJBReplicableBase 
			implements org.openxava.test.ejb.IDeliveryDetail, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.DeliveryDetailKey ejbCreate(org.openxava.test.ejb.Delivery container, int counter, Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		org.openxava.test.ejb.DeliveryKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Delivery", "DeliveryDetail"));
		} 
		setDelivery_number(containerKey.number); 
		setDelivery_type_number(containerKey._Type_number); 
		setDelivery_invoice_year(containerKey._Invoice_year); 
		setDelivery_invoice_number(containerKey._Invoice_number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.Delivery container, int counter, Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.ejb.DeliveryDetailKey ejbCreate(org.openxava.test.ejb.DeliveryKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		executeSets(values); 
		setDelivery_number(containerKey.number); 
		setDelivery_type_number(containerKey._Type_number); 
		setDelivery_invoice_year(containerKey._Invoice_year); 
		setDelivery_invoice_number(containerKey._Invoice_number); 
			
		return null;
	}

	public void ejbPostCreate(org.openxava.test.ejb.DeliveryKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {			
	} 
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.DeliveryDetailKey ejbCreate(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		org.openxava.test.ejb.DeliveryKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Delivery", "DeliveryDetail"));
		} 
		setDelivery_number(containerKey.number); 
		setDelivery_type_number(containerKey._Type_number); 
		setDelivery_invoice_year(containerKey._Invoice_year); 
		setDelivery_invoice_number(containerKey._Invoice_number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailData data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.DeliveryDetailKey ejbCreate(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setDeliveryDetailValue(value); 
		setNumber(value.getNumber()); 
		org.openxava.test.ejb.DeliveryKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Delivery", "DeliveryDetail"));
		} 
		setDelivery_number(containerKey.number); 
		setDelivery_type_number(containerKey._Type_number); 
		setDelivery_invoice_year(containerKey._Invoice_year); 
		setDelivery_invoice_number(containerKey._Invoice_number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.Delivery container, int counter, org.openxava.test.ejb.DeliveryDetailValue value) 
		throws
			CreateException,
			ValidationException {			
	}	 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.ejb.DeliveryDetailKey ejbCreate(org.openxava.test.ejb.DeliveryKey containerKey, int counter, org.openxava.test.ejb.DeliveryDetailValue value)
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		setDeliveryDetailValue(value); 
		setNumber(value.getNumber());
		setDelivery_number(containerKey.number);
		setDelivery_type_number(containerKey._Type_number);
		setDelivery_invoice_year(containerKey._Invoice_year);
		setDelivery_invoice_number(containerKey._Invoice_number); 
		return null;					

	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryKey containerKey, int counter, org.openxava.test.ejb.DeliveryDetailValue value)	
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
	
	// Properties/Propiedades 
	private org.openxava.converters.TrimStringConverter descriptionConverter;
	private org.openxava.converters.TrimStringConverter getDescriptionConverter() {
		if (descriptionConverter == null) {
			try {
				descriptionConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("description");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "description"));
			}
			
		}	
		return descriptionConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DESCRIPTION"
	 */
	public abstract java.lang.String get_Description();
	public abstract void set_Description(java.lang.String newDescription); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String getDescription() {
		try {
			return (String) getDescriptionConverter().toJava(get_Description());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryDetail", "String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setDescription(String newDescription) {
		try { 
			this.modified = true; 
			set_Description((java.lang.String) getDescriptionConverter().toDB(newDescription));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryDetail", "String"));
		}		
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

	// Colecciones/Collections		

	// References/Referencias 

	// Delivery : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Delivery getDelivery() {
		try {		
			return getDeliveryHome().findByPrimaryKey(getDeliveryKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Delivery", "DeliveryDetail"));
		}		
	}	
	/**
	 * @ejb:interface-method
	 */
	public void setDelivery(org.openxava.test.ejb.Delivery newDelivery) { 
		this.modified = true; 
		try {	
			if (newDelivery == null) setDeliveryKey(null);
			else setDeliveryKey((org.openxava.test.ejb.DeliveryKey) newDelivery.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Delivery", "DeliveryDetail"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.DeliveryKey getDeliveryKey() {				
		org.openxava.test.ejb.DeliveryKey key = new org.openxava.test.ejb.DeliveryKey(); 
		key.number = getDelivery_number(); 
		key._Type_number = getDelivery_type_number(); 
		key._Invoice_year = getDelivery_invoice_year(); 
		key._Invoice_number = getDelivery_invoice_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setDeliveryKey(org.openxava.test.ejb.DeliveryKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.DeliveryKey();
		} 
		setDelivery_number(key.number); 
		setDelivery_type_number(key._Type_number); 
		setDelivery_invoice_year(key._Invoice_year); 
		setDelivery_invoice_number(key._Invoice_number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DELIVERY_NUMBER"
	 */
	public abstract int get_Delivery_number();
	public abstract void set_Delivery_number(int newDelivery_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getDelivery_number() { 
		return get_Delivery_number(); 
	}
	public void setDelivery_number(int newDelivery_number) { 
		set_Delivery_number(newDelivery_number); 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DELIVERY_TYPE_NUMBER"
	 */
	public abstract int get_Delivery_type_number();
	public abstract void set_Delivery_type_number(int newDelivery_type_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getDelivery_type_number() { 
		return get_Delivery_type_number(); 
	}
	public void setDelivery_type_number(int newDelivery_type_number) { 
		set_Delivery_type_number(newDelivery_type_number); 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DELIVERY_INVOICE_YEAR"
	 */
	public abstract int get_Delivery_invoice_year();
	public abstract void set_Delivery_invoice_year(int newDelivery_invoice_year);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getDelivery_invoice_year() { 
		return get_Delivery_invoice_year(); 
	}
	public void setDelivery_invoice_year(int newDelivery_invoice_year) { 
		set_Delivery_invoice_year(newDelivery_invoice_year); 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DELIVERY_INVOICE_NUMBER"
	 */
	public abstract int get_Delivery_invoice_number();
	public abstract void set_Delivery_invoice_number(int newDelivery_invoice_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getDelivery_invoice_number() { 
		return get_Delivery_invoice_number(); 
	}
	public void setDelivery_invoice_number(int newDelivery_invoice_number) { 
		set_Delivery_invoice_number(newDelivery_invoice_number); 	
	} 

	private org.openxava.test.ejb.DeliveryHome deliveryHome;	
	private org.openxava.test.ejb.DeliveryHome getDeliveryHome() throws Exception{
		if (deliveryHome == null) {
			deliveryHome = (org.openxava.test.ejb.DeliveryHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.ejb/Delivery"),
			 		org.openxava.test.ejb.DeliveryHome.class);			 		
		}
		return deliveryHome;
	} 
	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Delivery").getMetaAggregate("DeliveryDetail"); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.DeliveryDetailData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.ejb.DeliveryDetailData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.DeliveryDetailValue getDeliveryDetailValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setDeliveryDetailValue(org.openxava.test.ejb.DeliveryDetailValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setNumber(0); 
		setDescription(null); 
		setDeliveryKey(null); 	
	}
		
}