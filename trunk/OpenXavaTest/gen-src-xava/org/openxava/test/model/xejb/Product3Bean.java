
// File generated by OpenXava: Tue Dec 20 18:06:26 CET 2005
// Archivo generado por OpenXava: Tue Dec 20 18:06:26 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Product3		Entity/Entidad

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
 * @ejb:bean name="Product3" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Product3" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IProduct3"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Product3" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByFamily(java.lang.String oid)" query="SELECT OBJECT(o) FROM Product3 o WHERE o._Family_oid = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByFamily(java.lang.String oid)" query="SELECT OBJECT(o) FROM Product3 o WHERE o._Family_oid = ?1 " 
 * 
 * @jboss:table-name "XAVATEST_PRODUCT3"
 *
 * @author Javier Paniza
 */
abstract public class Product3Bean extends EJBReplicableBase 
			implements org.openxava.test.model.IProduct3, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.Product3Key ejbCreate(Map values) 
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
	public org.openxava.test.model.Product3Key ejbCreate(org.openxava.test.model.Product3Data data) 
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
	public void ejbPostCreate(org.openxava.test.model.Product3Data data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.Product3Key ejbCreate(org.openxava.test.model.Product3Value value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setProduct3Value(value); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.Product3Value value) 
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
	private org.openxava.converters.TrimStringConverter commentsConverter;
	private org.openxava.converters.TrimStringConverter getCommentsConverter() {
		if (commentsConverter == null) {
			try {
				commentsConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("comments");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "comments"));
			}
			
		}	
		return commentsConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "COMMENTS"
	 */
	public abstract java.lang.String get_Comments();
	public abstract void set_Comments(java.lang.String newComments); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public java.lang.String getComments() {
		try {
			return (java.lang.String) getCommentsConverter().toJava(get_Comments());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Comments", "Product3", "java.lang.String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setComments(java.lang.String newComments) {
		try { 
			this.modified = true; 
			set_Comments((java.lang.String) getCommentsConverter().toDB(newComments));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Comments", "Product3", "java.lang.String"));
		}		
	} 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Product3", "String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Product3", "String"));
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
	public abstract long getNumber();
	/**
	  * 
	  */
	public abstract void setNumber(long newNumber); 

	// Colecciones/Collections		

	// References/Referencias  	
	// Subfamily1 : Aggregate/Agregado 
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 
	public org.openxava.test.model.SubfamilyInfo getSubfamily1() {
		org.openxava.test.model.SubfamilyInfo r = new org.openxava.test.model.SubfamilyInfo(); 
		r.setFamily(getSubfamily1_family()); 
		r.setSubfamily(getSubfamily1_subfamily());		
		return r;
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 	 
	public void setSubfamily1(org.openxava.test.model.SubfamilyInfo newSubfamily1) { 
		this.modified = true; 	
		if (newSubfamily1 == null) newSubfamily1 = new org.openxava.test.model.SubfamilyInfo();		
		setSubfamily1_family(newSubfamily1.getFamily());		
		setSubfamily1_subfamily(newSubfamily1.getSubfamily());			
	} 

	// Subfamily1_family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IFamily2 getSubfamily1_family() {
		try {		
			return getSubfamily1_familyHome().findByPrimaryKey(getSubfamily1_familyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily1_family", "SubfamilyInfo"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Remote getSubfamily1_familyRemote() {
		return (org.openxava.test.model.Family2Remote) getSubfamily1_family();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_family(org.openxava.test.model.IFamily2 newSubfamily1_family) { 
		this.modified = true; 
		try {	
			if (newSubfamily1_family == null) setSubfamily1_familyKey(null);
			else {
				org.openxava.test.model.Family2Remote remote = (org.openxava.test.model.Family2Remote) newSubfamily1_family;
				setSubfamily1_familyKey((org.openxava.test.model.Family2Key) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily1_family", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Key getSubfamily1_familyKey() {				
		org.openxava.test.model.Family2Key key = new org.openxava.test.model.Family2Key(); 
		key.number = getSubfamily1_family_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_familyKey(org.openxava.test.model.Family2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Family2Key();
		} 
		setSubfamily1_family_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY1"
	 */
	public abstract int get_Subfamily1_family_number();
	public abstract void set_Subfamily1_family_number(int newSubfamily1_family_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getSubfamily1_family_number() { 
		return get_Subfamily1_family_number(); 
	}
	public void setSubfamily1_family_number(int newSubfamily1_family_number) { 
		set_Subfamily1_family_number(newSubfamily1_family_number); 	
	} 

	private org.openxava.test.model.Family2Home subfamily1_familyHome;	
	private org.openxava.test.model.Family2Home getSubfamily1_familyHome() throws Exception{
		if (subfamily1_familyHome == null) {
			subfamily1_familyHome = (org.openxava.test.model.Family2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Family2"),
			 		org.openxava.test.model.Family2Home.class);			 		
		}
		return subfamily1_familyHome;
	} 

	// Subfamily1_subfamily : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ISubfamily2 getSubfamily1_subfamily() {
		try {		
			return getSubfamily1_subfamilyHome().findByPrimaryKey(getSubfamily1_subfamilyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily1_subfamily", "SubfamilyInfo"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Subfamily2Remote getSubfamily1_subfamilyRemote() {
		return (org.openxava.test.model.Subfamily2Remote) getSubfamily1_subfamily();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_subfamily(org.openxava.test.model.ISubfamily2 newSubfamily1_subfamily) { 
		this.modified = true; 
		try {	
			if (newSubfamily1_subfamily == null) setSubfamily1_subfamilyKey(null);
			else {
				org.openxava.test.model.Subfamily2Remote remote = (org.openxava.test.model.Subfamily2Remote) newSubfamily1_subfamily;
				setSubfamily1_subfamilyKey((org.openxava.test.model.Subfamily2Key) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily1_subfamily", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Subfamily2Key getSubfamily1_subfamilyKey() {				
		org.openxava.test.model.Subfamily2Key key = new org.openxava.test.model.Subfamily2Key(); 
		key.number = getSubfamily1_subfamily_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_subfamilyKey(org.openxava.test.model.Subfamily2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Subfamily2Key();
		} 
		setSubfamily1_subfamily_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SUBFAMILY1"
	 */
	public abstract int get_Subfamily1_subfamily_number();
	public abstract void set_Subfamily1_subfamily_number(int newSubfamily1_subfamily_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getSubfamily1_subfamily_number() { 
		return get_Subfamily1_subfamily_number(); 
	}
	public void setSubfamily1_subfamily_number(int newSubfamily1_subfamily_number) { 
		set_Subfamily1_subfamily_number(newSubfamily1_subfamily_number); 	
	} 

	private org.openxava.test.model.Subfamily2Home subfamily1_subfamilyHome;	
	private org.openxava.test.model.Subfamily2Home getSubfamily1_subfamilyHome() throws Exception{
		if (subfamily1_subfamilyHome == null) {
			subfamily1_subfamilyHome = (org.openxava.test.model.Subfamily2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Subfamily2"),
			 		org.openxava.test.model.Subfamily2Home.class);			 		
		}
		return subfamily1_subfamilyHome;
	} 

	// Family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IFamily getFamily() {
		try {		
			return getFamilyHome().findByPrimaryKey(getFamilyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Family", "Product3"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.FamilyRemote getFamilyRemote() {
		return (org.openxava.test.model.FamilyRemote) getFamily();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setFamily(org.openxava.test.model.IFamily newFamily) { 
		this.modified = true; 
		try {	
			if (newFamily == null) setFamilyKey(null);
			else {
				org.openxava.test.model.FamilyRemote remote = (org.openxava.test.model.FamilyRemote) newFamily;
				setFamilyKey((org.openxava.test.model.FamilyKey) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Family", "Product3"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.FamilyKey getFamilyKey() {				
		org.openxava.test.model.FamilyKey key = new org.openxava.test.model.FamilyKey(); 
		key.oid = getFamily_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setFamilyKey(org.openxava.test.model.FamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.FamilyKey();
		} 
		setFamily_oid(key.oid);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY"
	 */
	public abstract String get_Family_oid();
	public abstract void set_Family_oid(String newFamily_oid);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getFamily_oid() { 
		return get_Family_oid(); 
	}
	public void setFamily_oid(String newFamily_oid) { 
		set_Family_oid(newFamily_oid); 	
	} 

	private org.openxava.test.model.FamilyHome familyHome;	
	private org.openxava.test.model.FamilyHome getFamilyHome() throws Exception{
		if (familyHome == null) {
			familyHome = (org.openxava.test.model.FamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/openxava.test/Family"),
			 		org.openxava.test.model.FamilyHome.class);			 		
		}
		return familyHome;
	}  	
	// Subfamily2 : Aggregate/Agregado 
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 
	public org.openxava.test.model.SubfamilyInfo getSubfamily2() {
		org.openxava.test.model.SubfamilyInfo r = new org.openxava.test.model.SubfamilyInfo(); 
		r.setFamily(getSubfamily2_family()); 
		r.setSubfamily(getSubfamily2_subfamily());		
		return r;
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 	 
	public void setSubfamily2(org.openxava.test.model.SubfamilyInfo newSubfamily2) { 
		this.modified = true; 	
		if (newSubfamily2 == null) newSubfamily2 = new org.openxava.test.model.SubfamilyInfo();		
		setSubfamily2_family(newSubfamily2.getFamily());		
		setSubfamily2_subfamily(newSubfamily2.getSubfamily());			
	} 

	// Subfamily2_family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IFamily2 getSubfamily2_family() {
		try {		
			return getSubfamily2_familyHome().findByPrimaryKey(getSubfamily2_familyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily2_family", "SubfamilyInfo"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Remote getSubfamily2_familyRemote() {
		return (org.openxava.test.model.Family2Remote) getSubfamily2_family();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_family(org.openxava.test.model.IFamily2 newSubfamily2_family) { 
		this.modified = true; 
		try {	
			if (newSubfamily2_family == null) setSubfamily2_familyKey(null);
			else {
				org.openxava.test.model.Family2Remote remote = (org.openxava.test.model.Family2Remote) newSubfamily2_family;
				setSubfamily2_familyKey((org.openxava.test.model.Family2Key) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily2_family", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Key getSubfamily2_familyKey() {				
		org.openxava.test.model.Family2Key key = new org.openxava.test.model.Family2Key(); 
		key.number = getSubfamily2_family_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_familyKey(org.openxava.test.model.Family2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Family2Key();
		} 
		setSubfamily2_family_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY2"
	 */
	public abstract int get_Subfamily2_family_number();
	public abstract void set_Subfamily2_family_number(int newSubfamily2_family_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getSubfamily2_family_number() { 
		return get_Subfamily2_family_number(); 
	}
	public void setSubfamily2_family_number(int newSubfamily2_family_number) { 
		set_Subfamily2_family_number(newSubfamily2_family_number); 	
	} 

	private org.openxava.test.model.Family2Home subfamily2_familyHome;	
	private org.openxava.test.model.Family2Home getSubfamily2_familyHome() throws Exception{
		if (subfamily2_familyHome == null) {
			subfamily2_familyHome = (org.openxava.test.model.Family2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Family2"),
			 		org.openxava.test.model.Family2Home.class);			 		
		}
		return subfamily2_familyHome;
	} 

	// Subfamily2_subfamily : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.ISubfamily2 getSubfamily2_subfamily() {
		try {		
			return getSubfamily2_subfamilyHome().findByPrimaryKey(getSubfamily2_subfamilyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Subfamily2_subfamily", "SubfamilyInfo"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Subfamily2Remote getSubfamily2_subfamilyRemote() {
		return (org.openxava.test.model.Subfamily2Remote) getSubfamily2_subfamily();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_subfamily(org.openxava.test.model.ISubfamily2 newSubfamily2_subfamily) { 
		this.modified = true; 
		try {	
			if (newSubfamily2_subfamily == null) setSubfamily2_subfamilyKey(null);
			else {
				org.openxava.test.model.Subfamily2Remote remote = (org.openxava.test.model.Subfamily2Remote) newSubfamily2_subfamily;
				setSubfamily2_subfamilyKey((org.openxava.test.model.Subfamily2Key) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily2_subfamily", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Subfamily2Key getSubfamily2_subfamilyKey() {				
		org.openxava.test.model.Subfamily2Key key = new org.openxava.test.model.Subfamily2Key(); 
		key.number = getSubfamily2_subfamily_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_subfamilyKey(org.openxava.test.model.Subfamily2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Subfamily2Key();
		} 
		setSubfamily2_subfamily_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SUBFAMILY2"
	 */
	public abstract int get_Subfamily2_subfamily_number();
	public abstract void set_Subfamily2_subfamily_number(int newSubfamily2_subfamily_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getSubfamily2_subfamily_number() { 
		return get_Subfamily2_subfamily_number(); 
	}
	public void setSubfamily2_subfamily_number(int newSubfamily2_subfamily_number) { 
		set_Subfamily2_subfamily_number(newSubfamily2_subfamily_number); 	
	} 

	private org.openxava.test.model.Subfamily2Home subfamily2_subfamilyHome;	
	private org.openxava.test.model.Subfamily2Home getSubfamily2_subfamilyHome() throws Exception{
		if (subfamily2_subfamilyHome == null) {
			subfamily2_subfamilyHome = (org.openxava.test.model.Subfamily2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Subfamily2"),
			 		org.openxava.test.model.Subfamily2Home.class);			 		
		}
		return subfamily2_subfamilyHome;
	} 

	// Methods/Metodos 

	private MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Product3").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.Product3Data getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.Product3Data data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.Product3Value getProduct3Value();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setProduct3Value(org.openxava.test.model.Product3Value value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setNumber(0); 
		setDescription(null); 
		setComments(null); 
		setFamilyKey(null); 	
	}
		
}