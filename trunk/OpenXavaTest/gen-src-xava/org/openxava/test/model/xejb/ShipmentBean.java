
// File generated by OpenXava: Tue May 29 11:26:00 CEST 2007
// Archivo generado por OpenXava: Tue May 29 11:26:00 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Shipment		Entity/Entidad

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
 * @ejb:bean name="Shipment" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Shipment" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IShipment"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Shipment" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByCustomerContactPerson(int customer_number)" query="SELECT OBJECT(o) FROM Shipment o WHERE o._CustomerContactPerson_customer_number = ?1 ORDER BY o._Type, o.mode, o.number" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByCustomerContactPerson(int customer_number)" query="SELECT OBJECT(o) FROM Shipment o WHERE o._CustomerContactPerson_customer_number = ?1 ORDER BY o._Type, o.mode, o.number" 	
 * @ejb:finder signature="Collection findByMode(int mode)" query="SELECT OBJECT(o) FROM Shipment o WHERE o.mode = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByMode(int mode)" query="SELECT OBJECT(o) FROM Shipment o WHERE o.mode = ?1" 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Shipment o" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Shipment o" 	
 * @ejb:finder signature="Shipment findByTypeModeNumber(java.lang.String type,int mode,int number)" query="SELECT OBJECT(o) FROM Shipment o WHERE o._Type = ?1 and o.mode = ?2 and o.number = ?3" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Shipment findByTypeModeNumber(java.lang.String type,int mode,int number)" query="SELECT OBJECT(o) FROM Shipment o WHERE o._Type = ?1 and o.mode = ?2 and o.number = ?3" 
 * 
 * @jboss:table-name "XAVATEST.SHIPMENT"
 *
 * @author Javier Paniza
 */
abstract public class ShipmentBean extends EJBReplicableBase 
			implements org.openxava.test.model.IShipment, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.ShipmentKey ejbCreate(Map values) 
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
	public org.openxava.test.model.ShipmentKey ejbCreate(org.openxava.test.model.ShipmentData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data);  
		set_Type(data.get_Type()); 
		setMode(data.getMode()); 
		setNumber(data.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ShipmentData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.ShipmentKey ejbCreate(org.openxava.test.model.ShipmentValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setShipmentValue(value); 
		setType(value.getType()); 
		setMode(value.getMode()); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ShipmentValue value) 
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
	private static org.openxava.converters.IConverter timeConverter;
	private org.openxava.converters.IConverter getTimeConverter() {
		if (timeConverter == null) {
			try {
				timeConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("time");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "time"));
			}
			
		}	
		return timeConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "TIME"
	 */
	public abstract java.sql.Timestamp get_Time();
	public abstract void set_Time(java.sql.Timestamp newTime); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.sql.Timestamp getTime() {
		try {
			return (java.sql.Timestamp) getTimeConverter().toJava(get_Time());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Time", "Shipment", "java.sql.Timestamp"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setTime(java.sql.Timestamp newTime) {
		try { 
			this.modified = true; 
			set_Time((java.sql.Timestamp) getTimeConverter().toDB(newTime));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Time", "Shipment", "java.sql.Timestamp"));
		}		
	} 
	private static org.openxava.converters.IConverter descriptionConverter;
	private org.openxava.converters.IConverter getDescriptionConverter() {
		if (descriptionConverter == null) {
			try {
				descriptionConverter = (org.openxava.converters.IConverter) 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Shipment", "String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Shipment", "String"));
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
	private static org.openxava.converters.IConverter typeConverter;
	private org.openxava.converters.IConverter getTypeConverter() {
		if (typeConverter == null) {
			try {
				typeConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("type");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "type"));
			}
			
		}	
		return typeConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "TYPE"
	 */
	public abstract java.lang.String get_Type();
	public abstract void set_Type(java.lang.String newType); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getType() {
		try {
			return ((Integer) getTypeConverter().toJava(get_Type())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Type", "Shipment", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setType(int newType) {
		try { 
			this.modified = true; 
			set_Type((java.lang.String) getTypeConverter().toDB(new Integer(newType)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Type", "Shipment", "int"));
		}		
	} 
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "MODE"
	 */
	public abstract int getMode();
	/**
	  * 
	  */
	public abstract void setMode(int newMode); 

	// Colecciones/Collections		

	// References/Referencias 

	// CustomerContactPerson : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ICustomerContactPerson getCustomerContactPerson() {
		try {		
			return getCustomerContactPersonHome().findByPrimaryKey(getCustomerContactPersonKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "CustomerContactPerson", "Shipment"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.CustomerContactPersonRemote getCustomerContactPersonRemote() {
		return (org.openxava.test.model.CustomerContactPersonRemote) getCustomerContactPerson();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setCustomerContactPerson(org.openxava.test.model.ICustomerContactPerson newCustomerContactPerson) { 
		this.modified = true; 
		try {	
			if (newCustomerContactPerson == null) setCustomerContactPersonKey(null);
			else {
				if (newCustomerContactPerson instanceof org.openxava.test.model.CustomerContactPerson) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.CustomerContactPersonRemote remote = (org.openxava.test.model.CustomerContactPersonRemote) newCustomerContactPerson;
				setCustomerContactPersonKey((org.openxava.test.model.CustomerContactPersonKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "CustomerContactPerson", "Shipment"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.CustomerContactPersonKey getCustomerContactPersonKey() {				
		org.openxava.test.model.CustomerContactPersonKey key = new org.openxava.test.model.CustomerContactPersonKey(); 
		key._Customer_number = get_CustomerContactPerson_customer_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setCustomerContactPersonKey(org.openxava.test.model.CustomerContactPersonKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.CustomerContactPersonKey(); 
			setCustomerContactPerson_customer_number(key._Customer_number);					
		}
		else { 
			set_CustomerContactPerson_customer_number(key._Customer_number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "CUSTOMERCONTACT"
	 */
	public abstract int get_CustomerContactPerson_customer_number();
	public abstract void set_CustomerContactPerson_customer_number(int newCustomerContactPerson_customer_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getCustomerContactPerson_customer_number() { 
		return get_CustomerContactPerson_customer_number(); 
	}
	public void setCustomerContactPerson_customer_number(int newCustomerContactPerson_customer_number) { 
		set_CustomerContactPerson_customer_number(newCustomerContactPerson_customer_number); 	
	} 

	private org.openxava.test.model.CustomerContactPersonHome customerContactPersonHome;	
	private org.openxava.test.model.CustomerContactPersonHome getCustomerContactPersonHome() throws Exception{
		if (customerContactPersonHome == null) {
			customerContactPersonHome = (org.openxava.test.model.CustomerContactPersonHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/CustomerContactPerson"),
			 		org.openxava.test.model.CustomerContactPersonHome.class);			 		
		}
		return customerContactPersonHome;
	} 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Shipment").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.ShipmentData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.ShipmentData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.ShipmentValue getShipmentValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setShipmentValue(org.openxava.test.model.ShipmentValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setType(0); 
		setMode(0); 
		setNumber(0); 
		setDescription(null); 
		setTime(null); 
		setCustomerContactPersonKey(null); 	
	} 		
}