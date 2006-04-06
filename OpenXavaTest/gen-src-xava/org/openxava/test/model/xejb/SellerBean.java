
// File generated by OpenXava: Thu Apr 06 13:16:31 CEST 2006
// Archivo generado por OpenXava: Thu Apr 06 13:16:31 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Seller		Entity/Entidad

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
 * @ejb:bean name="Seller" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Seller" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ISeller"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Seller" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByLevel(java.lang.String id)" query="SELECT OBJECT(o) FROM Seller o WHERE o._Level_id = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByLevel(java.lang.String id)" query="SELECT OBJECT(o) FROM Seller o WHERE o._Level_id = ?1 " 	
 * @ejb:finder signature="Collection findByBoss(int number)" query="SELECT OBJECT(o) FROM Seller o WHERE o._Boss_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByBoss(int number)" query="SELECT OBJECT(o) FROM Seller o WHERE o._Boss_number = ?1 " 	
 * @ejb:finder signature="Collection findAll()" query="SELECT OBJECT(o) FROM Seller o WHERE 1 = 1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findAll()" query="SELECT OBJECT(o) FROM Seller o WHERE 1 = 1" 	
 * @ejb:finder signature="Seller findByNumber(int number)" query="SELECT OBJECT(o) FROM Seller o WHERE o.number = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Seller findByNumber(int number)" query="SELECT OBJECT(o) FROM Seller o WHERE o.number = ?1" 
 * 
 * @jboss:table-name "XAVATEST_SELLER"
 *
 * @author Javier Paniza
 */
abstract public class SellerBean extends EJBReplicableBase 
			implements org.openxava.test.model.ISeller, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.SellerKey ejbCreate(Map values) 
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
	public org.openxava.test.model.SellerKey ejbCreate(org.openxava.test.model.SellerData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		setNumber(data.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.SellerData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.SellerKey ejbCreate(org.openxava.test.model.SellerValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setSellerValue(value); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.SellerValue value) 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Seller", "String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Name", "Seller", "String"));
		}		
	} 
	private static org.openxava.converters.IConverter regionsConverter;
	private org.openxava.converters.IConverter getRegionsConverter() {
		if (regionsConverter == null) {
			try {
				regionsConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("regions");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "regions"));
			}
			
		}	
		return regionsConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "REGIONS"
	 */
	public abstract java.lang.String get_Regions();
	public abstract void set_Regions(java.lang.String newRegions); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public String [] getRegions() {
		try {
			return (String []) getRegionsConverter().toJava(get_Regions());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Regions", "Seller", "String []"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setRegions(String [] newRegions) {
		try { 
			this.modified = true; 
			set_Regions((java.lang.String) getRegionsConverter().toDB(newRegions));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Regions", "Seller", "String []"));
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

	private org.openxava.test.model.CustomerHome customersHome;
	/**
	 * @ejb:interface-method
	 */
	public void addToCustomers(org.openxava.test.model.ICustomer newElement) {
		if (newElement != null) { 
			try {
				((org.openxava.test.model.CustomerRemote) newElement).setSellerKey((org.openxava.test.model.SellerKey) getEntityContext().getPrimaryKey());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("add_collection_element_error", "Customer", "Seller"));
			}
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void removeFromCustomers(org.openxava.test.model.ICustomer toRemove) {
		if (toRemove != null) {
			try {
				((org.openxava.test.model.CustomerRemote) toRemove).setSellerKey(null);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("remove_collection_element_error", "Customer", "Seller"));
			}
		}
	} 
	/**
	 * @ejb:interface-method
	 */
	public java.util.Collection getCustomers() {		
		try {
			return getCustomersHome().findBySeller(getNumber());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_collection_elements_error", "Customers", "Seller"));
		}
	}
		
	private org.openxava.test.model.CustomerHome getCustomersHome() throws Exception{
		if (customersHome == null) {
			customersHome = (org.openxava.test.model.CustomerHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Customer"),
			 		org.openxava.test.model.CustomerHome.class);			 		
		}
		return customersHome;
	}		

	// References/Referencias 

	// Level : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ISellerLevel getLevel() {
		try {		
			return getLevelHome().findByPrimaryKey(getLevelKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Level", "Seller"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.SellerLevelRemote getLevelRemote() {
		return (org.openxava.test.model.SellerLevelRemote) getLevel();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setLevel(org.openxava.test.model.ISellerLevel newLevel) { 
		this.modified = true; 
		try {	
			if (newLevel == null) setLevelKey(null);
			else {
				if (newLevel instanceof org.openxava.test.model.SellerLevel) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.SellerLevelRemote remote = (org.openxava.test.model.SellerLevelRemote) newLevel;
				setLevelKey((org.openxava.test.model.SellerLevelKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Level", "Seller"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.SellerLevelKey getLevelKey() {				
		org.openxava.test.model.SellerLevelKey key = new org.openxava.test.model.SellerLevelKey(); 
		key.id = getLevel_id();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setLevelKey(org.openxava.test.model.SellerLevelKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.SellerLevelKey(); 
			setLevel_id(key.id);					
		}
		else { 
			setLevel_id(key.id);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "LEVEL"
	 */
	public abstract String get_Level_id();
	public abstract void set_Level_id(String newLevel_id);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getLevel_id() { 
		return get_Level_id(); 
	}
	public void setLevel_id(String newLevel_id) { 
		set_Level_id(newLevel_id); 	
	} 

	private org.openxava.test.model.SellerLevelHome levelHome;	
	private org.openxava.test.model.SellerLevelHome getLevelHome() throws Exception{
		if (levelHome == null) {
			levelHome = (org.openxava.test.model.SellerLevelHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/SellerLevel"),
			 		org.openxava.test.model.SellerLevelHome.class);			 		
		}
		return levelHome;
	} 

	// Boss : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ISeller getBoss() {
		try {		
			return getBossHome().findByPrimaryKey(getBossKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Boss", "Seller"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.SellerRemote getBossRemote() {
		return (org.openxava.test.model.SellerRemote) getBoss();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setBoss(org.openxava.test.model.ISeller newBoss) { 
		this.modified = true; 
		try {	
			if (newBoss == null) setBossKey(null);
			else {
				if (newBoss instanceof org.openxava.test.model.Seller) {
					throw new IllegalArgumentException(XavaResources.getString("pojo_to_ejb_illegal"));
				}
				org.openxava.test.model.SellerRemote remote = (org.openxava.test.model.SellerRemote) newBoss;
				setBossKey((org.openxava.test.model.SellerKey) remote.getPrimaryKey());
			}	
		}
		catch (IllegalArgumentException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Boss", "Seller"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.SellerKey getBossKey() {				
		org.openxava.test.model.SellerKey key = new org.openxava.test.model.SellerKey(); 
		key.number = getBoss_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setBossKey(org.openxava.test.model.SellerKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.SellerKey(); 
			setBoss_number(key.number);					
		}
		else { 
			setBoss_number(key.number);		
		}
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "BOSS"
	 */
	public abstract int get_Boss_number();
	public abstract void set_Boss_number(int newBoss_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getBoss_number() { 
		return get_Boss_number(); 
	}
	public void setBoss_number(int newBoss_number) { 
		set_Boss_number(newBoss_number); 	
	} 

	private org.openxava.test.model.SellerHome bossHome;	
	private org.openxava.test.model.SellerHome getBossHome() throws Exception{
		if (bossHome == null) {
			bossHome = (org.openxava.test.model.SellerHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Seller"),
			 		org.openxava.test.model.SellerHome.class);			 		
		}
		return bossHome;
	} 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Seller").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.SellerData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.SellerData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.SellerValue getSellerValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setSellerValue(org.openxava.test.model.SellerValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setNumber(0); 
		setName(null); 
		setRegions(null); 
		setLevelKey(null); 
		setBossKey(null); 	
	} 		
}