
// File generated by OpenXava: Fri Oct 21 13:03:47 CEST 2005
// Archivo generado por OpenXava: Fri Oct 21 13:03:47 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Service		Aggregate/Agregado: AdditionalDetail

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
 * @ejb:bean name="AdditionalDetail" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/AdditionalDetail" reentrant="true" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IAdditionalDetail"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="AdditionalDetail" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByService(int number)" query="SELECT OBJECT(o) FROM AdditionalDetail o WHERE o._Service_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByService(int number)" query="SELECT OBJECT(o) FROM AdditionalDetail o WHERE o._Service_number = ?1 " 	
 * @ejb:finder signature="Collection findByType(int number)" query="SELECT OBJECT(o) FROM AdditionalDetail o WHERE o._Type_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByType(int number)" query="SELECT OBJECT(o) FROM AdditionalDetail o WHERE o._Type_number = ?1 " 
 * 
 * @jboss:table-name "XAVATEST_SERVICEDETAIL"
 *
 * @author Javier Paniza
 */
abstract public class AdditionalDetailBean extends EJBReplicableBase 
			implements org.openxava.test.model.IAdditionalDetail, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.AdditionalDetailKey ejbCreate(org.openxava.test.model.ServiceRemote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		org.openxava.test.model.ServiceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.ServiceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Service", "AdditionalDetail"));
		} 
		setService_number(containerKey.number); 
		try { 	
			org.openxava.calculators.CounterByPassOidCalculator counterCalculator = (org.openxava.calculators.CounterByPassOidCalculator)
				getMetaModel().getMetaProperty("counter").getMetaCalculatorDefaultValue().getCalculator(); 
			counterCalculator.setContainerKey(containerKey);
			counterCalculator.setCounter(counter); 
			setCounter(((Integer) counterCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "AdditionalDetail", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ServiceRemote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.AdditionalDetailKey ejbCreate(org.openxava.test.model.ServiceKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		executeSets(values); 
		setService_number(containerKey.number); 
		try { 	
			org.openxava.calculators.CounterByPassOidCalculator counterCalculator = (org.openxava.calculators.CounterByPassOidCalculator)
				getMetaModel().getMetaProperty("counter").getMetaCalculatorDefaultValue().getCalculator(); 
			counterCalculator.setContainerKey(containerKey);
			counterCalculator.setCounter(counter); 
			setCounter(((Integer) counterCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "AdditionalDetail", ex.getLocalizedMessage()));
		} 
			
		return null;
	}

	public void ejbPostCreate(org.openxava.test.model.ServiceKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {			
	} 
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.AdditionalDetailKey ejbCreate(org.openxava.test.model.ServiceRemote container, int counter, org.openxava.test.model.AdditionalDetailData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		org.openxava.test.model.ServiceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.ServiceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Service", "AdditionalDetail"));
		} 
		setService_number(containerKey.number); 
		try { 	
			org.openxava.calculators.CounterByPassOidCalculator counterCalculator= (org.openxava.calculators.CounterByPassOidCalculator)
				getMetaModel().getMetaProperty("counter").getMetaCalculatorDefaultValue().getCalculator(); 
			counterCalculator.setContainerKey(containerKey);
			counterCalculator.setCounter(counter); 
			setCounter(((Integer) counterCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "AdditionalDetail", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ServiceRemote container, int counter, org.openxava.test.model.AdditionalDetailData data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.AdditionalDetailKey ejbCreate(org.openxava.test.model.ServiceRemote container, int counter, org.openxava.test.model.AdditionalDetailValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setAdditionalDetailValue(value); 
		setCounter(value.getCounter()); 
		setService_number(value.getService_number()); 
		org.openxava.test.model.ServiceKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.ServiceKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Service", "AdditionalDetail"));
		} 
		setService_number(containerKey.number); 
		try { 	
			org.openxava.calculators.CounterByPassOidCalculator counterCalculator = (org.openxava.calculators.CounterByPassOidCalculator)
				getMetaModel().getMetaProperty("counter").getMetaCalculatorDefaultValue().getCalculator(); 
			counterCalculator.setContainerKey(containerKey);
			counterCalculator.setCounter(counter); 
			setCounter(((Integer) counterCalculator.calculate()).intValue()); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "AdditionalDetail", ex.getLocalizedMessage()));
		} 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.ServiceRemote container, int counter, org.openxava.test.model.AdditionalDetailValue value) 
		throws
			CreateException,
			ValidationException {			
	}	 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.AdditionalDetailKey ejbCreate(org.openxava.test.model.ServiceKey containerKey, int counter, org.openxava.test.model.AdditionalDetailValue value)
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		setAdditionalDetailValue(value); 
		setCounter(value.getCounter()); 
		setService_number(value.getService_number());
		setService_number(containerKey.number); 
		try { 
			org.openxava.calculators.CounterByPassOidCalculator counterCalculator= (org.openxava.calculators.CounterByPassOidCalculator)
				getMetaModel().getMetaProperty("counter").getMetaCalculatorDefaultValue().getCalculator(); 
			counterCalculator.setContainerKey(containerKey);
			counterCalculator.setCounter(counter); 
			setCounter(((Integer) counterCalculator.calculate()).intValue());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "AdditionalDetail", ex.getLocalizedMessage()));
		} 
		return null;					

	} 
	public void ejbPostCreate(org.openxava.test.model.ServiceKey containerKey, int counter, org.openxava.test.model.AdditionalDetailValue value)	
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
	 * @jboss:column-name "COUNTER"
	 */
	public abstract int getCounter();
	/**
	  * 
	  */
	public abstract void setCounter(int newCounter); 
	private org.openxava.converters.IntegerNumberConverter subfamilyConverter;
	private org.openxava.converters.IntegerNumberConverter getSubfamilyConverter() {
		if (subfamilyConverter == null) {
			try {
				subfamilyConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("subfamily");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "subfamily"));
			}
			
		}	
		return subfamilyConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SUBFAMILY"
	 */
	public abstract java.lang.Integer get_Subfamily();
	public abstract void set_Subfamily(java.lang.Integer newSubfamily); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getSubfamily() {
		try {
			return ((Integer) getSubfamilyConverter().toJava(get_Subfamily())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Subfamily", "AdditionalDetail", "int"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily(int newSubfamily) {
		try { 
			this.modified = true; 
			set_Subfamily((java.lang.Integer) getSubfamilyConverter().toDB(new Integer(newSubfamily)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Subfamily", "AdditionalDetail", "int"));
		}		
	} 

	// Colecciones/Collections		

	// References/Referencias 

	// Service : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IService getService() {
		try {		
			return getServiceHome().findByPrimaryKey(getServiceKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Service", "AdditionalDetail"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ServiceRemote getServiceRemote() {
		return (org.openxava.test.model.ServiceRemote) getService();
	}
	
	/**
	 * 
	 */
	public void setService(org.openxava.test.model.IService newService) { 
		this.modified = true; 
		try {	
			if (newService == null) setServiceKey(null);
			else {
				org.openxava.test.model.ServiceRemote remote = (org.openxava.test.model.ServiceRemote) newService;
				setServiceKey((org.openxava.test.model.ServiceKey) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Service", "AdditionalDetail"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ServiceKey getServiceKey() {				
		org.openxava.test.model.ServiceKey key = new org.openxava.test.model.ServiceKey(); 
		key.number = getService_number();		
		return key;
	}	
	
	/**
	 * 
	 */
	public void setServiceKey(org.openxava.test.model.ServiceKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.ServiceKey();
		} 
		setService_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "SERVICE"
	 */
	public abstract int get_Service_number();
	public abstract void set_Service_number(int newService_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getService_number() { 
		return get_Service_number(); 
	}
	public void setService_number(int newService_number) { 
		set_Service_number(newService_number); 	
	} 

	private org.openxava.test.model.ServiceHome serviceHome;	
	private org.openxava.test.model.ServiceHome getServiceHome() throws Exception{
		if (serviceHome == null) {
			serviceHome = (org.openxava.test.model.ServiceHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Service"),
			 		org.openxava.test.model.ServiceHome.class);			 		
		}
		return serviceHome;
	} 

	// Type : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IServiceType getType() {
		try {		
			return getTypeHome().findByPrimaryKey(getTypeKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Type", "AdditionalDetail"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ServiceTypeRemote getTypeRemote() {
		return (org.openxava.test.model.ServiceTypeRemote) getType();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setType(org.openxava.test.model.IServiceType newType) { 
		this.modified = true; 
		try {	
			if (newType == null) setTypeKey(null);
			else {
				org.openxava.test.model.ServiceTypeRemote remote = (org.openxava.test.model.ServiceTypeRemote) newType;
				setTypeKey((org.openxava.test.model.ServiceTypeKey) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Type", "AdditionalDetail"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ServiceTypeKey getTypeKey() {				
		org.openxava.test.model.ServiceTypeKey key = new org.openxava.test.model.ServiceTypeKey(); 
		key.number = getType_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setTypeKey(org.openxava.test.model.ServiceTypeKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.ServiceTypeKey();
		} 
		setType_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "TYPE"
	 */
	public abstract int get_Type_number();
	public abstract void set_Type_number(int newType_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getType_number() { 
		return get_Type_number(); 
	}
	public void setType_number(int newType_number) { 
		set_Type_number(newType_number); 	
	} 

	private org.openxava.test.model.ServiceTypeHome typeHome;	
	private org.openxava.test.model.ServiceTypeHome getTypeHome() throws Exception{
		if (typeHome == null) {
			typeHome = (org.openxava.test.model.ServiceTypeHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/ServiceType"),
			 		org.openxava.test.model.ServiceTypeHome.class);			 		
		}
		return typeHome;
	} 

	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Service").getMetaAggregate("AdditionalDetail"); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.AdditionalDetailData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.AdditionalDetailData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.AdditionalDetailValue getAdditionalDetailValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setAdditionalDetailValue(org.openxava.test.model.AdditionalDetailValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setCounter(0); 
		setSubfamily(0); 
		setServiceKey(null); 
		setTypeKey(null); 	
	}
		
}