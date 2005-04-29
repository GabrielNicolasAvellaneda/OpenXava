
// File generated by OpenXava: Fri Apr 29 19:19:34 CEST 2005
// Archivo generado por OpenXava: Fri Apr 29 19:19:34 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Aggregate/Agregado: Receptionist

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
 * @ejb:bean name="Receptionist" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/Receptionist" reentrant="true" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IReceptionist"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Receptionist" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByDeliveryPlace(java.lang.String oid)" query="SELECT OBJECT(o) FROM Receptionist o WHERE o.deliveryPlace_oid = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByDeliveryPlace(java.lang.String oid)" query="SELECT OBJECT(o) FROM Receptionist o WHERE o.deliveryPlace_oid = ?1 " 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Receptionist o" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Receptionist o" 
 * 
 * @jboss:table-name "XAVATEST@separator@RECEPTIONIST"
 *
 * @author Javier Paniza
 */
abstract public class ReceptionistBean extends EJBReplicableBase 
			implements org.openxava.test.ejb.IReceptionist, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.ReceptionistKey ejbCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		org.openxava.test.ejb.DeliveryPlaceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryPlaceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "Receptionist"));
		} 
		setDeliveryPlace_oid(containerKey.oid); 
		try { 	
			org.openxava.calculators.NextIntegerCalculator oidCalculator = (org.openxava.calculators.NextIntegerCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().getCalculator(); 
			oidCalculator.setConnectionProvider(getPortableContext()); 
			setOid(((Integer) oidCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "Receptionist", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.ejb.ReceptionistKey ejbCreate(org.openxava.test.ejb.DeliveryPlaceKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		executeSets(values); 
		setDeliveryPlace_oid(containerKey.oid); 
		try { 	
			org.openxava.calculators.NextIntegerCalculator oidCalculator = (org.openxava.calculators.NextIntegerCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().getCalculator(); 
			oidCalculator.setConnectionProvider(getPortableContext()); 
			setOid(((Integer) oidCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "Receptionist", ex.getLocalizedMessage()));
		} 
			
		return null;
	}

	public void ejbPostCreate(org.openxava.test.ejb.DeliveryPlaceKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {			
	} 
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.ReceptionistKey ejbCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, org.openxava.test.ejb.ReceptionistData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		org.openxava.test.ejb.DeliveryPlaceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryPlaceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "Receptionist"));
		} 
		setDeliveryPlace_oid(containerKey.oid); 
		try { 	
			org.openxava.calculators.NextIntegerCalculator oidCalculator= (org.openxava.calculators.NextIntegerCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().getCalculator(); 
			oidCalculator.setConnectionProvider(getPortableContext()); 
			setOid(((Integer) oidCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "Receptionist", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, org.openxava.test.ejb.ReceptionistData data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.ejb.ReceptionistKey ejbCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, org.openxava.test.ejb.ReceptionistValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setReceptionistValue(value); 
		org.openxava.test.ejb.DeliveryPlaceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.ejb.DeliveryPlaceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "Receptionist"));
		} 
		setDeliveryPlace_oid(containerKey.oid); 
		try { 	
			org.openxava.calculators.NextIntegerCalculator oidCalculator = (org.openxava.calculators.NextIntegerCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().getCalculator(); 
			oidCalculator.setConnectionProvider(getPortableContext()); 
			setOid(((Integer) oidCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "Receptionist", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryPlace container, int counter, org.openxava.test.ejb.ReceptionistValue value) 
		throws
			CreateException,
			ValidationException {			
	}	 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.ejb.ReceptionistKey ejbCreate(org.openxava.test.ejb.DeliveryPlaceKey containerKey, int counter, org.openxava.test.ejb.ReceptionistValue value)
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		setReceptionistValue(value);
		setDeliveryPlace_oid(containerKey.oid); 
		try { 
			org.openxava.calculators.NextIntegerCalculator oidCalculator= (org.openxava.calculators.NextIntegerCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().getCalculator(); 
			oidCalculator.setConnectionProvider(getPortableContext()); 
			setOid(((Integer) oidCalculator.calculate()).intValue());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "Receptionist", ex.getLocalizedMessage()));
		} 
		return null;					

	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryPlaceKey containerKey, int counter, org.openxava.test.ejb.ReceptionistValue value)	
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
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "OID"
	 */
	public abstract int getOid();
	/**
	  * 
	  */
	public abstract void setOid(int newOid); 
	private org.openxava.converters.TrimStringConverter nameConverter;
	private org.openxava.converters.TrimStringConverter getNameConverter() {
		if (nameConverter == null) {
			try {
				nameConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("name");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "name"));
			}
			
		}	
		return nameConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "NAME"
	 */
	public abstract java.lang.String get_Name();
	public abstract void set_Name(java.lang.String newName); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String getName() {
		try {
			return (String) getNameConverter().toJava(get_Name());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Receptionist", "String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setName(String newName) {
		try { 
			this.modified = true; 
			set_Name((java.lang.String) getNameConverter().toDB(newName));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Receptionist", "String"));
		}		
	} 

	// Colecciones/Collections		

	// References/Referencias 

	// DeliveryPlace : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.DeliveryPlace getDeliveryPlace() {
		try {		
			return getDeliveryPlaceHome().findByPrimaryKey(getDeliveryPlaceKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "DeliveryPlace", "Receptionist"));
		}		
	}	
	/**
	 * @ejb:interface-method
	 */
	public void setDeliveryPlace(org.openxava.test.ejb.DeliveryPlace newDeliveryPlace) { 
		this.modified = true; 
		try {	
			if (newDeliveryPlace == null) setDeliveryPlaceKey(null);
			else setDeliveryPlaceKey((org.openxava.test.ejb.DeliveryPlaceKey) newDeliveryPlace.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "DeliveryPlace", "Receptionist"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.DeliveryPlaceKey getDeliveryPlaceKey() {				
		org.openxava.test.ejb.DeliveryPlaceKey key = new org.openxava.test.ejb.DeliveryPlaceKey(); 
		key.oid = getDeliveryPlace_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setDeliveryPlaceKey(org.openxava.test.ejb.DeliveryPlaceKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.DeliveryPlaceKey();
		} 
		setDeliveryPlace_oid(key.oid);		
		
	}
	/**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "DELIVERYPLACE"
	 */
	public abstract String getDeliveryPlace_oid();
	public abstract void setDeliveryPlace_oid(String newDeliveryPlace_oid); 

	private org.openxava.test.ejb.DeliveryPlaceHome deliveryPlaceHome;	
	private org.openxava.test.ejb.DeliveryPlaceHome getDeliveryPlaceHome() throws Exception{
		if (deliveryPlaceHome == null) {
			deliveryPlaceHome = (org.openxava.test.ejb.DeliveryPlaceHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.ejb/DeliveryPlace"),
			 		org.openxava.test.ejb.DeliveryPlaceHome.class);			 		
		}
		return deliveryPlaceHome;
	} 
	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Customer").getMetaAggregate("Receptionist"); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.ReceptionistData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.ejb.ReceptionistData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.ReceptionistValue getReceptionistValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setReceptionistValue(org.openxava.test.ejb.ReceptionistValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setOid(0); 
		setName(null); 
	}
		
}