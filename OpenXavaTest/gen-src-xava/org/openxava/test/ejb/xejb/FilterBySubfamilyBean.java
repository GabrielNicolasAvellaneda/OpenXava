
// File generated by OpenXava: Mon Mar 07 10:45:47 CET 2005
// Archivo generado por OpenXava: Mon Mar 07 10:45:47 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: FilterBySubfamily		Entity/Entidad

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
 * @ejb:bean name="FilterBySubfamily" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/FilterBySubfamily" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IFilterBySubfamily"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="FilterBySubfamily" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findBySubfamily(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o.subfamily_number = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findBySubfamily(int number)" query="SELECT OBJECT(o) FROM FilterBySubfamily o WHERE o.subfamily_number = ?1 " 
 * 
 * @jboss:table-name "MOCKTABLE"
 *
 * @author Javier Paniza
 */
abstract public class FilterBySubfamilyBean extends EJBReplicableBase 
			implements org.openxava.test.ejb.IFilterBySubfamily, EntityBean {	
			
	private boolean creating = false;		

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.FilterBySubfamilyKey ejbCreate(Map values) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
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
	public org.openxava.test.ejb.FilterBySubfamilyKey ejbCreate(org.openxava.test.ejb.FilterBySubfamilyData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		setData(data); 
		setOid(data.getOid()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.FilterBySubfamilyData data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.FilterBySubfamilyKey ejbCreate(org.openxava.test.ejb.FilterBySubfamilyValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		setFilterBySubfamilyValue(value); 
		setOid(value.getOid()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.FilterBySubfamilyValue value) 
		throws
			CreateException,
			ValidationException {			
	}
	
	public void ejbLoad() {
		creating = false;
	}
	
	public void ejbStore() {
		if (creating) {
			creating = false;
			return;
		}			
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
	public abstract String getOid();
	/**
	  * 
	  */
	public abstract void setOid(String newOid); 

	// Colecciones/Collections		

	// References/Referencias 

	// Subfamily : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Subfamily2 getSubfamily() {
		try {		
			return getSubfamilyHome().findByPrimaryKey(getSubfamilyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily", "FilterBySubfamily"));
		}		
	}	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily(org.openxava.test.ejb.Subfamily2 newSubfamily) {
		try {	
			if (newSubfamily == null) setSubfamilyKey(null);
			else setSubfamilyKey((org.openxava.test.ejb.Subfamily2Key) newSubfamily.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily", "FilterBySubfamily"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Subfamily2Key getSubfamilyKey() {				
			org.openxava.test.ejb.Subfamily2Key key = new org.openxava.test.ejb.Subfamily2Key(); 
			key.number = getSubfamily_number();		
			return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamilyKey(org.openxava.test.ejb.Subfamily2Key key) {
		if (key == null) {
			key = new org.openxava.test.ejb.Subfamily2Key();
		} 
		setSubfamily_number(key.number);		
		
	}
	/**		
	 * @ejb:interface-method
	 * @ejb:persistent-field
	 * 
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @jboss:column-name "SUBFAMILY"
	 */
	public abstract int getSubfamily_number();
	public abstract void setSubfamily_number(int newSubfamily_number); 

	private org.openxava.test.ejb.Subfamily2Home subfamilyHome;	
	private org.openxava.test.ejb.Subfamily2Home getSubfamilyHome() throws Exception{
		if (subfamilyHome == null) {
			subfamilyHome = (org.openxava.test.ejb.Subfamily2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.ejb/Subfamily2"),
			 		org.openxava.test.ejb.Subfamily2Home.class);			 		
		}
		return subfamilyHome;
	} 
	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("FilterBySubfamily").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.FilterBySubfamilyData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.ejb.FilterBySubfamilyData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.FilterBySubfamilyValue getFilterBySubfamilyValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setFilterBySubfamilyValue(org.openxava.test.ejb.FilterBySubfamilyValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setOid(null); 
	}
		
}