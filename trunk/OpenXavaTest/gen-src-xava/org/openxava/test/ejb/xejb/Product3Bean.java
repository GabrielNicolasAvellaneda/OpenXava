
// File generated by OpenXava: Tue Aug 23 12:25:46 CEST 2005
// Archivo generado por OpenXava: Tue Aug 23 12:25:46 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Product3		Entity/Entidad

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
 * @ejb:bean name="Product3" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/Product3" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IProduct3"
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
 * @jboss:table-name "XAVATEST@separator@PRODUCT3"
 *
 * @author Javier Paniza
 */
abstract public class Product3Bean extends EJBReplicableBase 
			implements org.openxava.test.ejb.IProduct3, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.Product3Key ejbCreate(Map values) 
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
	public org.openxava.test.ejb.Product3Key ejbCreate(org.openxava.test.ejb.Product3Data data) 
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
	public void ejbPostCreate(org.openxava.test.ejb.Product3Data data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.Product3Key ejbCreate(org.openxava.test.ejb.Product3Value value) 
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
	public void ejbPostCreate(org.openxava.test.ejb.Product3Value value) 
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
	public org.openxava.test.ejb.SubfamilyInfo getSubfamily1() {
		org.openxava.test.ejb.SubfamilyInfo r = new org.openxava.test.ejb.SubfamilyInfo(); 
		r.setFamily(getSubfamily1_family()); 
		r.setSubfamily(getSubfamily1_subfamily());		
		return r;
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 	 
	public void setSubfamily1(org.openxava.test.ejb.SubfamilyInfo newSubfamily1) { 
		this.modified = true; 	
		if (newSubfamily1 == null) newSubfamily1 = new org.openxava.test.ejb.SubfamilyInfo();		
		setSubfamily1_family(newSubfamily1.getFamily());		
		setSubfamily1_subfamily(newSubfamily1.getSubfamily());			
	} 

	// Subfamily1_family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Family getSubfamily1_family() {
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
	public void setSubfamily1_family(org.openxava.test.ejb.Family newSubfamily1_family) { 
		this.modified = true; 
		try {	
			if (newSubfamily1_family == null) setSubfamily1_familyKey(null);
			else setSubfamily1_familyKey((org.openxava.test.ejb.FamilyKey) newSubfamily1_family.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily1_family", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.FamilyKey getSubfamily1_familyKey() {				
		org.openxava.test.ejb.FamilyKey key = new org.openxava.test.ejb.FamilyKey(); 
		key.oid = getSubfamily1_family_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_familyKey(org.openxava.test.ejb.FamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.FamilyKey();
		} 
		setSubfamily1_family_oid(key.oid);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY1"
	 */
	public abstract String get_Subfamily1_family_oid();
	public abstract void set_Subfamily1_family_oid(String newSubfamily1_family_oid);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getSubfamily1_family_oid() { 
		return get_Subfamily1_family_oid(); 
	}
	public void setSubfamily1_family_oid(String newSubfamily1_family_oid) { 
		set_Subfamily1_family_oid(newSubfamily1_family_oid); 	
	} 

	private org.openxava.test.ejb.FamilyHome subfamily1_familyHome;	
	private org.openxava.test.ejb.FamilyHome getSubfamily1_familyHome() throws Exception{
		if (subfamily1_familyHome == null) {
			subfamily1_familyHome = (org.openxava.test.ejb.FamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/openxava.test/Family"),
			 		org.openxava.test.ejb.FamilyHome.class);			 		
		}
		return subfamily1_familyHome;
	} 

	// Subfamily1_subfamily : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Subfamily getSubfamily1_subfamily() {
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
	public void setSubfamily1_subfamily(org.openxava.test.ejb.Subfamily newSubfamily1_subfamily) { 
		this.modified = true; 
		try {	
			if (newSubfamily1_subfamily == null) setSubfamily1_subfamilyKey(null);
			else setSubfamily1_subfamilyKey((org.openxava.test.ejb.SubfamilyKey) newSubfamily1_subfamily.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily1_subfamily", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.SubfamilyKey getSubfamily1_subfamilyKey() {				
		org.openxava.test.ejb.SubfamilyKey key = new org.openxava.test.ejb.SubfamilyKey(); 
		key.oid = getSubfamily1_subfamily_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily1_subfamilyKey(org.openxava.test.ejb.SubfamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.SubfamilyKey();
		} 
		setSubfamily1_subfamily_oid(key.oid);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SUBFAMILY1"
	 */
	public abstract String get_Subfamily1_subfamily_oid();
	public abstract void set_Subfamily1_subfamily_oid(String newSubfamily1_subfamily_oid);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getSubfamily1_subfamily_oid() { 
		return get_Subfamily1_subfamily_oid(); 
	}
	public void setSubfamily1_subfamily_oid(String newSubfamily1_subfamily_oid) { 
		set_Subfamily1_subfamily_oid(newSubfamily1_subfamily_oid); 	
	} 

	private org.openxava.test.ejb.SubfamilyHome subfamily1_subfamilyHome;	
	private org.openxava.test.ejb.SubfamilyHome getSubfamily1_subfamilyHome() throws Exception{
		if (subfamily1_subfamilyHome == null) {
			subfamily1_subfamilyHome = (org.openxava.test.ejb.SubfamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.ejb/Subfamily"),
			 		org.openxava.test.ejb.SubfamilyHome.class);			 		
		}
		return subfamily1_subfamilyHome;
	} 

	// Family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Family getFamily() {
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
	public void setFamily(org.openxava.test.ejb.Family newFamily) { 
		this.modified = true; 
		try {	
			if (newFamily == null) setFamilyKey(null);
			else setFamilyKey((org.openxava.test.ejb.FamilyKey) newFamily.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Family", "Product3"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.FamilyKey getFamilyKey() {				
		org.openxava.test.ejb.FamilyKey key = new org.openxava.test.ejb.FamilyKey(); 
		key.oid = getFamily_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setFamilyKey(org.openxava.test.ejb.FamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.FamilyKey();
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

	private org.openxava.test.ejb.FamilyHome familyHome;	
	private org.openxava.test.ejb.FamilyHome getFamilyHome() throws Exception{
		if (familyHome == null) {
			familyHome = (org.openxava.test.ejb.FamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/openxava.test/Family"),
			 		org.openxava.test.ejb.FamilyHome.class);			 		
		}
		return familyHome;
	}  	
	// Subfamily2 : Aggregate/Agregado 
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 
	public org.openxava.test.ejb.SubfamilyInfo getSubfamily2() {
		org.openxava.test.ejb.SubfamilyInfo r = new org.openxava.test.ejb.SubfamilyInfo(); 
		r.setFamily(getSubfamily2_family()); 
		r.setSubfamily(getSubfamily2_subfamily());		
		return r;
	} 	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */ 	 
	public void setSubfamily2(org.openxava.test.ejb.SubfamilyInfo newSubfamily2) { 
		this.modified = true; 	
		if (newSubfamily2 == null) newSubfamily2 = new org.openxava.test.ejb.SubfamilyInfo();		
		setSubfamily2_family(newSubfamily2.getFamily());		
		setSubfamily2_subfamily(newSubfamily2.getSubfamily());			
	} 

	// Subfamily2_family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Family getSubfamily2_family() {
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
	public void setSubfamily2_family(org.openxava.test.ejb.Family newSubfamily2_family) { 
		this.modified = true; 
		try {	
			if (newSubfamily2_family == null) setSubfamily2_familyKey(null);
			else setSubfamily2_familyKey((org.openxava.test.ejb.FamilyKey) newSubfamily2_family.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily2_family", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.FamilyKey getSubfamily2_familyKey() {				
		org.openxava.test.ejb.FamilyKey key = new org.openxava.test.ejb.FamilyKey(); 
		key.oid = getSubfamily2_family_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_familyKey(org.openxava.test.ejb.FamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.FamilyKey();
		} 
		setSubfamily2_family_oid(key.oid);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY2"
	 */
	public abstract String get_Subfamily2_family_oid();
	public abstract void set_Subfamily2_family_oid(String newSubfamily2_family_oid);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getSubfamily2_family_oid() { 
		return get_Subfamily2_family_oid(); 
	}
	public void setSubfamily2_family_oid(String newSubfamily2_family_oid) { 
		set_Subfamily2_family_oid(newSubfamily2_family_oid); 	
	} 

	private org.openxava.test.ejb.FamilyHome subfamily2_familyHome;	
	private org.openxava.test.ejb.FamilyHome getSubfamily2_familyHome() throws Exception{
		if (subfamily2_familyHome == null) {
			subfamily2_familyHome = (org.openxava.test.ejb.FamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/openxava.test/Family"),
			 		org.openxava.test.ejb.FamilyHome.class);			 		
		}
		return subfamily2_familyHome;
	} 

	// Subfamily2_subfamily : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.Subfamily getSubfamily2_subfamily() {
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
	public void setSubfamily2_subfamily(org.openxava.test.ejb.Subfamily newSubfamily2_subfamily) { 
		this.modified = true; 
		try {	
			if (newSubfamily2_subfamily == null) setSubfamily2_subfamilyKey(null);
			else setSubfamily2_subfamilyKey((org.openxava.test.ejb.SubfamilyKey) newSubfamily2_subfamily.getPrimaryKey());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Subfamily2_subfamily", "SubfamilyInfo"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.ejb.SubfamilyKey getSubfamily2_subfamilyKey() {				
		org.openxava.test.ejb.SubfamilyKey key = new org.openxava.test.ejb.SubfamilyKey(); 
		key.oid = getSubfamily2_subfamily_oid();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setSubfamily2_subfamilyKey(org.openxava.test.ejb.SubfamilyKey key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.ejb.SubfamilyKey();
		} 
		setSubfamily2_subfamily_oid(key.oid);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "SUBFAMILY2"
	 */
	public abstract String get_Subfamily2_subfamily_oid();
	public abstract void set_Subfamily2_subfamily_oid(String newSubfamily2_subfamily_oid);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public String getSubfamily2_subfamily_oid() { 
		return get_Subfamily2_subfamily_oid(); 
	}
	public void setSubfamily2_subfamily_oid(String newSubfamily2_subfamily_oid) { 
		set_Subfamily2_subfamily_oid(newSubfamily2_subfamily_oid); 	
	} 

	private org.openxava.test.ejb.SubfamilyHome subfamily2_subfamilyHome;	
	private org.openxava.test.ejb.SubfamilyHome getSubfamily2_subfamilyHome() throws Exception{
		if (subfamily2_subfamilyHome == null) {
			subfamily2_subfamilyHome = (org.openxava.test.ejb.SubfamilyHome) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.ejb/Subfamily"),
			 		org.openxava.test.ejb.SubfamilyHome.class);			 		
		}
		return subfamily2_subfamilyHome;
	} 
	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Product3").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.Product3Data getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.ejb.Product3Data data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.Product3Value getProduct3Value();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setProduct3Value(org.openxava.test.ejb.Product3Value value);
	
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