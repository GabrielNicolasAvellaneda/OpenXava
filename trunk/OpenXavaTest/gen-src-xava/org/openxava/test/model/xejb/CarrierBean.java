
// File generated by OpenXava: Thu Apr 06 13:16:31 CEST 2006
// Archivo generado por OpenXava: Thu Apr 06 13:16:31 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Carrier		Entity/Entidad

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
 * @ejb:bean name="Carrier" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Carrier" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ICarrier"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Carrier" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByWarehouse(int zoneNumber, java.lang.Integer number)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._Warehouse_zoneNumber = ?1 AND o._Warehouse_number = ?2 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByWarehouse(int zoneNumber, java.lang.Integer number)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._Warehouse_zoneNumber = ?1 AND o._Warehouse_number = ?2 " 	
 * @ejb:finder signature="Collection findByDrivingLicence(java.lang.String type, int level)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._DrivingLicence_type = ?1 AND o._DrivingLicence_level = ?2 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByDrivingLicence(java.lang.String type, int level)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._DrivingLicence_type = ?1 AND o._DrivingLicence_level = ?2 " 	
 * @ejb:finder signature="Collection findFellowCarriersOfCarrier(int warehouse_zoneNumber, java.lang.Integer warehouse_number, java.lang.Integer _Number)" query="SELECT OBJECT(o) FROM Carrier o WHERE 
				o._Warehouse_zoneNumber = ?1 AND 
				o._Warehouse_number = ?2 AND 
				NOT (o._Number = ?3)
			" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findFellowCarriersOfCarrier(int warehouse_zoneNumber, java.lang.Integer warehouse_number, java.lang.Integer _Number)" query="SELECT OBJECT(o) FROM Carrier o WHERE 
				o._Warehouse_zoneNumber = ?1 AND 
				o._Warehouse_number = ?2 AND 
				NOT (o._Number = ?3)
			" 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Carrier o" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Carrier o" 	
 * @ejb:finder signature="Carrier findByNumber(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._Number = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Carrier findByNumber(java.lang.Integer number)" query="SELECT OBJECT(o) FROM Carrier o WHERE o._Number = ?1" 
 * 
 * @jboss:table-name "XAVATEST_CARRIER"
 *
 * @author Javier Paniza
 */
abstract public class CarrierBean extends EJBReplicableBase 
			implements org.openxava.test.model.ICarrier, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.CarrierKey ejbCreate(Map values) 
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
	public org.openxava.test.model.CarrierKey ejbCreate(org.openxava.test.model.CarrierData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data);  
		set_Number(data.get_Number()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.CarrierData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.CarrierKey ejbCreate(org.openxava.test.model.CarrierValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setCarrierValue(value); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.CarrierValue value) 
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
	private static org.openxava.converters.IConverter remarksConverter;
	private org.openxava.converters.IConverter getRemarksConverter() {
		if (remarksConverter == null) {
			try {
				remarksConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("remarks");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "remarks"));
			}
			
		}	
		return remarksConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "REMARKS"
	 */
	public abstract java.lang.String get_Remarks();
	public abstract void set_Remarks(java.lang.String newRemarks); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.lang.String getRemarks() {
		try {
			return (java.lang.String) getRemarksConverter().toJava(get_Remarks());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Carrier", "java.lang.String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setRemarks(java.lang.String newRemarks) {
		try { 
			this.modified = true; 
			set_Remarks((java.lang.String) getRemarksConverter().toDB(newRemarks));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Carrier", "java.lang.String"));
		}		
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String getCalculated() {	
		try { 
			org.openxava.hibernate.XHibernate.setCmt(true); 		
			org.openxava.calculators.StringCalculator calculatedCalculator= (org.openxava.calculators.StringCalculator)
				getMetaModel().getMetaProperty("calculated").getMetaCalculator().createCalculator(); 
			return (String) calculatedCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "Calculated", "Carrier", ex.getLocalizedMessage()));
		} 
		finally {
			org.openxava.hibernate.XHibernate.setCmt(false);
		} 		
	}
	public void setCalculated(String newCalculated) {
		// for it is in value object
		// para que aparezca en los value objects
	} 
	private static org.openxava.converters.IConverter nameConverter;
	private org.openxava.converters.IConverter getNameConverter() {
		if (nameConverter == null) {
			try {
				nameConverter = (org.openxava.converters.IConverter) 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Carrier", "String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Carrier", "String"));
		}		
	} 
	private static org.openxava.converters.IConverter numberConverter;
	private org.openxava.converters.IConverter getNumberConverter() {
		if (numberConverter == null) {
			try {
				numberConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("number");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "number"));
			}
			
		}	
		return numberConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "NUMBER"
	 */
	public abstract java.lang.Integer get_Number();
	public abstract void set_Number(java.lang.Integer newNumber); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getNumber() {
		try {
			return ((Integer) getNumberConverter().toJava(get_Number())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Number", "Carrier", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setNumber(int newNumber) {
		try { 
			this.modified = true; 
			set_Number((java.lang.Integer) getNumberConverter().toDB(new Integer(newNumber)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Number", "Carrier", "int"));
		}		
	} 

	// Colecciones/Collections	

	private org.openxava.test.model.CarrierHome fellowCarriersCalculatedHome; 
	/**
	 * @ejb:interface-method
	 */ 
	public java.util.Collection getFellowCarriersCalculated() {		
		try { 		
			org.openxava.test.calculators.FellowCarriersCalculator fellowCarriersCalculatedCalculator= (org.openxava.test.calculators.FellowCarriersCalculator)
				getMetaModel().getMetaCollection("fellowCarriersCalculated").getMetaCalculator().createCalculator(); 
				fellowCarriersCalculatedCalculator.setEntity(this); 
			return (java.util.Collection) fellowCarriersCalculatedCalculator.calculate();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.calculate_value_error", "fellowCarriersCalculated", "Carrier", ex.getLocalizedMessage()));
		}
	}	

	private org.openxava.test.model.CarrierHome fellowCarriersHome; 
	/**
	 * @ejb:interface-method
	 */
	public java.util.Collection getFellowCarriers() {		
		try {
			return getFellowCarriersHome().findFellowCarriersOfCarrier(getWarehouse_zoneNumber(), getWarehouse_number(), get_Number());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_collection_elements_error", "FellowCarriers", "Carrier"));
		}
	}
		
	private org.openxava.test.model.CarrierHome getFellowCarriersHome() throws Exception{
		if (fellowCarriersHome == null) {
			fellowCarriersHome = (org.openxava.test.model.CarrierHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Carrier"),
			 		org.openxava.test.model.CarrierHome.class);			 		
		}
		return fellowCarriersHome;
	}		

	// References/Referencias 

	// Warehouse : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IWarehouse getWarehouse() {
		try {		
			return getWarehouseHome().findByPrimaryKey(getWarehouseKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Warehouse", "Carrier"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.WarehouseRemote getWarehouseRemote() {
		return (org.openxava.test.model.WarehouseRemote) getWarehouse();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setWarehouse(org.openxava.test.model.IWarehouse newWarehouse) { 
		this.modified = true; 
		try {	
			if (newWarehouse == null) setWarehouseKey(null);
			else {
				if (newWarehouse instanceof org.openxava.test.model.Warehouse) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.WarehouseRemote remote = (org.openxava.test.model.WarehouseRemote) newWarehouse;
				setWarehouseKey((org.openxava.test.model.WarehouseKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Warehouse", "Carrier"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.WarehouseKey getWarehouseKey() {				
		org.openxava.test.model.WarehouseKey key = new org.openxava.test.model.WarehouseKey(); 
		key.zoneNumber = getWarehouse_zoneNumber(); 
		key._Number = get_Warehouse_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setWarehouseKey(org.openxava.test.model.WarehouseKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.WarehouseKey(); 
			setWarehouse_zoneNumber(key.zoneNumber); 
			setWarehouse_number(key._Number);					
		}
		else { 
			setWarehouse_zoneNumber(key.zoneNumber); 
			set_Warehouse_number(key._Number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "WAREHOUSE_ZONE"
	 */
	public abstract int get_Warehouse_zoneNumber();
	public abstract void set_Warehouse_zoneNumber(int newWarehouse_zoneNumber);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getWarehouse_zoneNumber() { 
		return get_Warehouse_zoneNumber(); 
	}
	public void setWarehouse_zoneNumber(int newWarehouse_zoneNumber) { 
		set_Warehouse_zoneNumber(newWarehouse_zoneNumber); 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "WAREHOUSE_NUMBER"
	 */
	public abstract java.lang.Integer get_Warehouse_number();
	public abstract void set_Warehouse_number(java.lang.Integer newWarehouse_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public java.lang.Integer getWarehouse_number() { 
		return get_Warehouse_number(); 
	}
	public void setWarehouse_number(java.lang.Integer newWarehouse_number) { 
		set_Warehouse_number(newWarehouse_number); 	
	} 

	private org.openxava.test.model.WarehouseHome warehouseHome;	
	private org.openxava.test.model.WarehouseHome getWarehouseHome() throws Exception{
		if (warehouseHome == null) {
			warehouseHome = (org.openxava.test.model.WarehouseHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Warehouse"),
			 		org.openxava.test.model.WarehouseHome.class);			 		
		}
		return warehouseHome;
	} 

	// DrivingLicence : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IDrivingLicence getDrivingLicence() {
		try {		
			return getDrivingLicenceHome().findByPrimaryKey(getDrivingLicenceKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "DrivingLicence", "Carrier"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.DrivingLicenceRemote getDrivingLicenceRemote() {
		return (org.openxava.test.model.DrivingLicenceRemote) getDrivingLicence();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setDrivingLicence(org.openxava.test.model.IDrivingLicence newDrivingLicence) { 
		this.modified = true; 
		try {	
			if (newDrivingLicence == null) setDrivingLicenceKey(null);
			else {
				if (newDrivingLicence instanceof org.openxava.test.model.DrivingLicence) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.DrivingLicenceRemote remote = (org.openxava.test.model.DrivingLicenceRemote) newDrivingLicence;
				setDrivingLicenceKey((org.openxava.test.model.DrivingLicenceKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "DrivingLicence", "Carrier"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.DrivingLicenceKey getDrivingLicenceKey() {				
		org.openxava.test.model.DrivingLicenceKey key = new org.openxava.test.model.DrivingLicenceKey(); 
		key.type = getDrivingLicence_type(); 
		key.level = getDrivingLicence_level();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setDrivingLicenceKey(org.openxava.test.model.DrivingLicenceKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.DrivingLicenceKey(); 
			setDrivingLicence_type(key.type); 
			setDrivingLicence_level(key.level);					
		}
		else { 
			setDrivingLicence_type(key.type); 
			setDrivingLicence_level(key.level);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DRIVINGLICENCE_TYPE"
	 */
	public abstract String get_DrivingLicence_type();
	public abstract void set_DrivingLicence_type(String newDrivingLicence_type);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getDrivingLicence_type() { 
		try {
			return (String) drivingLicence_typeConverter.toJava(get_DrivingLicence_type());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "drivingLicence.type", "Carrier", "String"));
		} 
	}
	public void setDrivingLicence_type(String newDrivingLicence_type) { 
		try {
			set_DrivingLicence_type((String) drivingLicence_typeConverter.toDB(newDrivingLicence_type));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "drivingLicence.type", "Carrier", "String"));
		} 	
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "DRIVINGLICENCE_LEVEL"
	 */
	public abstract int get_DrivingLicence_level();
	public abstract void set_DrivingLicence_level(int newDrivingLicence_level);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getDrivingLicence_level() { 
		return get_DrivingLicence_level(); 
	}
	public void setDrivingLicence_level(int newDrivingLicence_level) { 
		set_DrivingLicence_level(newDrivingLicence_level); 	
	} 

	private org.openxava.test.model.DrivingLicenceHome drivingLicenceHome;	
	private org.openxava.test.model.DrivingLicenceHome getDrivingLicenceHome() throws Exception{
		if (drivingLicenceHome == null) {
			drivingLicenceHome = (org.openxava.test.model.DrivingLicenceHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/DrivingLicence"),
			 		org.openxava.test.model.DrivingLicenceHome.class);			 		
		}
		return drivingLicenceHome;
	} 
	
	private static final org.openxava.converters.NotNullStringConverter 
		drivingLicence_typeConverter =
			new org.openxava.converters.NotNullStringConverter(); 

	// Methods/Metodos 
	/**
	 * @ejb:interface-method
	 */
	public void translate()  {
		try { 
			org.openxava.hibernate.XHibernate.setCmt(true); 		
			org.openxava.test.calculators.TranslateCarrierNameCalculator translateCalculator = (org.openxava.test.calculators.TranslateCarrierNameCalculator)
				getMetaModel().getMetaMethod("translate").getMetaCalculator().createCalculator(); 
				translateCalculator.setEntity(this); 
			translateCalculator.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("method_execution_error", "translate", "Carrier"));
		} 
		finally {
			org.openxava.hibernate.XHibernate.setCmt(false);
		} 
		
	} 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Carrier").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.CarrierData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.CarrierData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.CarrierValue getCarrierValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setCarrierValue(org.openxava.test.model.CarrierValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setNumber(0); 
		setName(null); 
		setRemarks(null); 
		setWarehouseKey(null); 
		setDrivingLicenceKey(null); 	
	} 		
}