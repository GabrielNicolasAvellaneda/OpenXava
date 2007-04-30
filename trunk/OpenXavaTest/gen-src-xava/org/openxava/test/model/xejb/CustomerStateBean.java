
// File generated by OpenXava: Mon Apr 30 18:14:56 CEST 2007
// Archivo generado por OpenXava: Mon Apr 30 18:14:56 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Aggregate/Agregado: CustomerState

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
 * @ejb:bean name="CustomerState" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/CustomerState" reentrant="true" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ICustomerState"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="CustomerState" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByState(java.lang.String id)" query="SELECT OBJECT(o) FROM CustomerState o WHERE o._State_id = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByState(java.lang.String id)" query="SELECT OBJECT(o) FROM CustomerState o WHERE o._State_id = ?1 " 	
 * @ejb:finder signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM CustomerState o WHERE o._Customer_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByCustomer(int number)" query="SELECT OBJECT(o) FROM CustomerState o WHERE o._Customer_number = ?1 " 
 * 
 * @jboss:table-name "XAVATEST.CUSTOMER_STATE"
 *
 * @author Javier Paniza
 */
abstract public class CustomerStateBean extends EJBReplicableBase 
			implements org.openxava.test.model.ICustomerState, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.CustomerStateKey ejbCreate(org.openxava.test.model.CustomerRemote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		executeSets(values); 
		org.openxava.test.model.CustomerKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.CustomerKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "CustomerState"));
		} 
		setCustomer_number(containerKey.number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.CustomerRemote container, int counter, Map values) 
		throws
			CreateException,
			ValidationException { 
	} 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.CustomerStateKey ejbCreate(org.openxava.test.model.CustomerKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		executeSets(values); 
		setCustomer_number(containerKey.number); 
			
		return null;
	}

	public void ejbPostCreate(org.openxava.test.model.CustomerKey containerKey, int counter, Map values)	
		throws
			CreateException,
			ValidationException { 
	} 
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.CustomerStateKey ejbCreate(org.openxava.test.model.CustomerRemote container, int counter, org.openxava.test.model.CustomerStateData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		org.openxava.test.model.CustomerKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.CustomerKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "CustomerState"));
		} 
		setCustomer_number(containerKey.number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.CustomerRemote container, int counter, org.openxava.test.model.CustomerStateData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */ 
	public org.openxava.test.model.CustomerStateKey ejbCreate(org.openxava.test.model.CustomerRemote container, int counter, org.openxava.test.model.CustomerStateValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setCustomerStateValue(value); 
		setCustomer_number(value.getCustomer_number()); 
		setState_id(value.getState_id()); 
		org.openxava.test.model.CustomerKey containerKey = null;
		try {
			containerKey = (org.openxava.test.model.CustomerKey) container.getPrimaryKey();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("obtain_primary_key_error_on_create", "Customer", "CustomerState"));
		} 
		setCustomer_number(containerKey.number); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.CustomerRemote container, int counter, org.openxava.test.model.CustomerStateValue value) 
		throws
			CreateException,
			ValidationException { 			
	}	 
	/**
	 * @ejb:create-method
	 */
	public org.openxava.test.model.CustomerStateKey ejbCreate(org.openxava.test.model.CustomerKey containerKey, int counter, org.openxava.test.model.CustomerStateValue value)
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;
		modified = false;
		setCustomerStateValue(value); 
		setCustomer_number(value.getCustomer_number()); 
		setState_id(value.getState_id());
		setCustomer_number(containerKey.number); 
		return null;					

	} 
	public void ejbPostCreate(org.openxava.test.model.CustomerKey containerKey, int counter, org.openxava.test.model.CustomerStateValue value)	
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

	// Colecciones/Collections		

	// References/Referencias 

	// State : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IState getState() {
		try {		
			return getStateHome().findByPrimaryKey(getStateKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "State", "CustomerState"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.StateRemote getStateRemote() {
		return (org.openxava.test.model.StateRemote) getState();
	}
	
	/**
	 * 
	 */
	public void setState(org.openxava.test.model.IState newState) { 
		this.modified = true; 
		try {	
			if (newState == null) setStateKey(null);
			else {
				if (newState instanceof org.openxava.test.model.State) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.StateRemote remote = (org.openxava.test.model.StateRemote) newState;
				setStateKey((org.openxava.test.model.StateKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "State", "CustomerState"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.StateKey getStateKey() {				
		org.openxava.test.model.StateKey key = new org.openxava.test.model.StateKey(); 
		key.id = getState_id();		
		return key;
	}	
	
	/**
	 * 
	 */
	public void setStateKey(org.openxava.test.model.StateKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.StateKey(); 
			setState_id(key.id);					
		}
		else { 
			setState_id(key.id);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * @ejb:pk-field
	 * @jboss:column-name "STATE"
	 */
	public abstract String get_State_id();
	public abstract void set_State_id(String newState_id);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getState_id() { 
		return get_State_id(); 
	}
	public void setState_id(String newState_id) { 
		set_State_id(newState_id); 	
	} 

	private org.openxava.test.model.StateHome stateHome;	
	private org.openxava.test.model.StateHome getStateHome() throws Exception{
		if (stateHome == null) {
			stateHome = (org.openxava.test.model.StateHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/State"),
			 		org.openxava.test.model.StateHome.class);			 		
		}
		return stateHome;
	} 

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
			throw new EJBException(XavaResources.getString("get_reference_error", "Customer", "CustomerState"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.CustomerRemote getCustomerRemote() {
		return (org.openxava.test.model.CustomerRemote) getCustomer();
	}
	
	/**
	 * 
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
			throw new EJBException(XavaResources.getString("set_reference_error", "Customer", "CustomerState"));
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
	 * 
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
	 * @ejb:pk-field
	 * @jboss:column-name "CUSTOMER"
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
			metaModel = MetaComponent.get("Customer").getMetaAggregate("CustomerState"); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.CustomerStateData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.CustomerStateData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.CustomerStateValue getCustomerStateValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setCustomerStateValue(org.openxava.test.model.CustomerStateValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setStateKey(null); 
		setCustomerKey(null); 	
	} 		
}