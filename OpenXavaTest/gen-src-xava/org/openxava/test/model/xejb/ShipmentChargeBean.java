
// File generated by OpenXava: Tue Jun 20 11:48:12 CEST 2006
// Archivo generado por OpenXava: Tue Jun 20 11:48:12 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: ShipmentCharge		Entity/Entidad

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
 * @ejb:bean name="ShipmentCharge" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/ShipmentCharge" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IShipmentCharge"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="ShipmentCharge" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByShipment(java.lang.String type, int mode, int number)" query="SELECT OBJECT(o) FROM ShipmentCharge o WHERE o._Shipment_type = ?1 AND o._Mode = ?2 AND o._Shipment_number = ?3 ORDER BY o.oid" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByShipment(java.lang.String type, int mode, int number)" query="SELECT OBJECT(o) FROM ShipmentCharge o WHERE o._Shipment_type = ?1 AND o._Mode = ?2 AND o._Shipment_number = ?3 ORDER BY o.oid" 	
 * @ejb:finder signature="ShipmentCharge findByOid(java.lang.String oid)" query="SELECT OBJECT(o) FROM ShipmentCharge o WHERE o.oid = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="ShipmentCharge findByOid(java.lang.String oid)" query="SELECT OBJECT(o) FROM ShipmentCharge o WHERE o.oid = ?1" 
 * 
 * @jboss:table-name "XAVATEST_SHIPMENTCHARGE"
 *
 * @author Javier Paniza
 */
abstract public class ShipmentChargeBean extends EJBReplicableBase 
			implements org.openxava.test.model.IShipmentCharge, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.ShipmentChargeKey ejbCreate(Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		try { 	
			org.openxava.calculators.UUIDCalculator oidCalculator = (org.openxava.calculators.UUIDCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 
			oidCalculator.setModel(this); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "ShipmentCharge", ex.getLocalizedMessage()));
		} 
			
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
	public org.openxava.test.model.ShipmentChargeKey ejbCreate(org.openxava.test.model.ShipmentChargeData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		setOid(data.getOid()); 
		try { 	
			org.openxava.calculators.UUIDCalculator oidCalculator= (org.openxava.calculators.UUIDCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 
			oidCalculator.setModel(this); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "ShipmentCharge", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ShipmentChargeData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.ShipmentChargeKey ejbCreate(org.openxava.test.model.ShipmentChargeValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setShipmentChargeValue(value); 
		setOid(value.getOid()); 
		try { 	
			org.openxava.calculators.UUIDCalculator oidCalculator = (org.openxava.calculators.UUIDCalculator)
				getMetaModel().getMetaProperty("oid").getMetaCalculatorDefaultValue().createCalculator(); 
			oidCalculator.setModel(this); 
			setOid((String) oidCalculator.calculate()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "ShipmentCharge", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ShipmentChargeValue value) 
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
	private static org.openxava.converters.IConverter amountConverter;
	private org.openxava.converters.IConverter getAmountConverter() {
		if (amountConverter == null) {
			try {
				amountConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("amount");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "amount"));
			}
			
		}	
		return amountConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "AMOUNT"
	 */
	public abstract java.math.BigDecimal get_Amount();
	public abstract void set_Amount(java.math.BigDecimal newAmount); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.math.BigDecimal getAmount() {
		try {
			return (java.math.BigDecimal) getAmountConverter().toJava(get_Amount());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Amount", "ShipmentCharge", "java.math.BigDecimal"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setAmount(java.math.BigDecimal newAmount) {
		try { 
			this.modified = true; 
			set_Amount((java.math.BigDecimal) getAmountConverter().toDB(newAmount));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Amount", "ShipmentCharge", "java.math.BigDecimal"));
		}		
	} 
	/**
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 *
	 * @jboss:column-name "ID"
	 */
	public abstract String getOid();
	/**
	  * 
	  */
	public abstract void setOid(String newOid); 
	private static org.openxava.converters.IConverter modeConverter;
	private org.openxava.converters.IConverter getModeConverter() {
		if (modeConverter == null) {
			try {
				modeConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("mode");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "mode"));
			}
			
		}	
		return modeConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "MODE"
	 */
	public abstract java.lang.Integer get_Mode();
	public abstract void set_Mode(java.lang.Integer newMode); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getMode() {
		try {
			return ((Integer) getModeConverter().toJava(get_Mode())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Mode", "ShipmentCharge", "int"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setMode(int newMode) {
		try { 
			this.modified = true; 
			set_Mode((java.lang.Integer) getModeConverter().toDB(new Integer(newMode)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Mode", "ShipmentCharge", "int"));
		}		
	} 

	// Colecciones/Collections		

	// References/Referencias 

	// Shipment : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IShipment getShipment() {
		try {		
			return getShipmentHome().findByPrimaryKey(getShipmentKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Shipment", "ShipmentCharge"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ShipmentRemote getShipmentRemote() {
		return (org.openxava.test.model.ShipmentRemote) getShipment();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setShipment(org.openxava.test.model.IShipment newShipment) { 
		this.modified = true; 
		try {	
			if (newShipment == null) setShipmentKey(null);
			else {
				if (newShipment instanceof org.openxava.test.model.Shipment) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.ShipmentRemote remote = (org.openxava.test.model.ShipmentRemote) newShipment;
				setShipmentKey((org.openxava.test.model.ShipmentKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Shipment", "ShipmentCharge"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ShipmentKey getShipmentKey() {				
		org.openxava.test.model.ShipmentKey key = new org.openxava.test.model.ShipmentKey(); 
		key._Type = get_Shipment_type(); 
		key.mode = getShipment_mode(); 
		key.number = getShipment_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setShipmentKey(org.openxava.test.model.ShipmentKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.ShipmentKey(); 
			setShipment_type(key._Type); 
			setShipment_mode(key.mode); 
			setShipment_number(key.number);					
		}
		else { 
			set_Shipment_type(key._Type); 
			setShipment_mode(key.mode); 
			setShipment_number(key.number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SHIPMENT_TYPE"
	 */
	public abstract java.lang.String get_Shipment_type();
	public abstract void set_Shipment_type(java.lang.String newShipment_type);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public java.lang.String getShipment_type() { 
		try {
			return (java.lang.String) shipment_typeConverter.toJava(get_Shipment_type());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.type", "ShipmentCharge", "java.lang.String"));
		} 
	}
	public void setShipment_type(java.lang.String newShipment_type) { 
		try {
			set_Shipment_type((java.lang.String) shipment_typeConverter.toDB(newShipment_type));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "shipment.type", "ShipmentCharge", "java.lang.String"));
		} 	
	}
	/**		
	 * @ejb:interface-method
	 *
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getShipment_mode() {
		return getMode();
	}
	public void setShipment_mode(int Shipment_mode) {
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SHIPMENT_NUMBER"
	 */
	public abstract int get_Shipment_number();
	public abstract void set_Shipment_number(int newShipment_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getShipment_number() { 
		return get_Shipment_number(); 
	}
	public void setShipment_number(int newShipment_number) { 
		set_Shipment_number(newShipment_number); 	
	} 

	private org.openxava.test.model.ShipmentHome shipmentHome;	
	private org.openxava.test.model.ShipmentHome getShipmentHome() throws Exception{
		if (shipmentHome == null) {
			shipmentHome = (org.openxava.test.model.ShipmentHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Shipment"),
			 		org.openxava.test.model.ShipmentHome.class);			 		
		}
		return shipmentHome;
	} 
	
	private static final org.openxava.converters.ValidValuesLetterConverter 
		shipment_typeConverter =
			new org.openxava.converters.ValidValuesLetterConverter(); 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("ShipmentCharge").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.ShipmentChargeData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.ShipmentChargeData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.ShipmentChargeValue getShipmentChargeValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setShipmentChargeValue(org.openxava.test.model.ShipmentChargeValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setOid(null); 
		setMode(0); 
		setAmount(null); 
		setShipmentKey(null); 	
	} 		
}