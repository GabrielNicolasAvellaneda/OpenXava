
// File generated by OpenXava: Tue Nov 08 13:16:28 CET 2005
// Archivo generado por OpenXava: Tue Nov 08 13:16:28 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Subfamily2		Entity/Entidad

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
 * @ejb:bean name="Subfamily2" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Subfamily2" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ISubfamily2"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Subfamily2" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Collection findByFamily(int number)" query="SELECT OBJECT(o) FROM Subfamily2 o WHERE o._Family_number = ?1 " view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Collection findByFamily(int number)" query="SELECT OBJECT(o) FROM Subfamily2 o WHERE o._Family_number = ?1 " 
 * 
 * @jboss:table-name "XAVATEST_SUBFAMILY2"
 *
 * @author Javier Paniza
 */
abstract public class Subfamily2Bean extends EJBReplicableBase 
			implements org.openxava.test.model.ISubfamily2, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.Subfamily2Key ejbCreate(Map values) 
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
	public org.openxava.test.model.Subfamily2Key ejbCreate(org.openxava.test.model.Subfamily2Data data) 
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
	public void ejbPostCreate(org.openxava.test.model.Subfamily2Data data) 
		throws
			CreateException,
			ValidationException {			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.Subfamily2Key ejbCreate(org.openxava.test.model.Subfamily2Value value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setSubfamily2Value(value); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.Subfamily2Value value) 
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
	private org.openxava.converters.NoConversionConverter remarksConverter;
	private org.openxava.converters.NoConversionConverter getRemarksConverter() {
		if (remarksConverter == null) {
			try {
				remarksConverter = (org.openxava.converters.NoConversionConverter) 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily2", "java.lang.String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Remarks", "Subfamily2", "java.lang.String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily2", "String"));
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "Subfamily2", "String"));
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

	// References/Referencias 

	// Family : Entity reference/Referencia a entidad
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.IFamily2 getFamily() {
		try {		
			return getFamilyHome().findByPrimaryKey(getFamilyKey());
		}
		catch (ObjectNotFoundException ex) {
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_reference_error", "Family", "Subfamily2"));
		}		
	}	
	
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Remote getFamilyRemote() {
		return (org.openxava.test.model.Family2Remote) getFamily();
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setFamily(org.openxava.test.model.IFamily2 newFamily) { 
		this.modified = true; 
		try {	
			if (newFamily == null) setFamilyKey(null);
			else {
				org.openxava.test.model.Family2Remote remote = (org.openxava.test.model.Family2Remote) newFamily;
				setFamilyKey((org.openxava.test.model.Family2Key) remote.getPrimaryKey());
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("set_reference_error", "Family", "Subfamily2"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public org.openxava.test.model.Family2Key getFamilyKey() {				
		org.openxava.test.model.Family2Key key = new org.openxava.test.model.Family2Key(); 
		key.number = getFamily_number();		
		return key;
	}	
	
	/**
	 * @ejb:interface-method
	 */
	public void setFamilyKey(org.openxava.test.model.Family2Key key) { 
		this.modified = true; 		
		if (key == null) {
			key = new org.openxava.test.model.Family2Key();
		} 
		setFamily_number(key.number);		
		
	}
	/**		
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "FAMILY"
	 */
	public abstract int get_Family_number();
	public abstract void set_Family_number(int newFamily_number);

	/**		
	 * @ejb:interface-method
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 */
	public int getFamily_number() { 
		return get_Family_number(); 
	}
	public void setFamily_number(int newFamily_number) { 
		set_Family_number(newFamily_number); 	
	} 

	private org.openxava.test.model.Family2Home familyHome;	
	private org.openxava.test.model.Family2Home getFamilyHome() throws Exception{
		if (familyHome == null) {
			familyHome = (org.openxava.test.model.Family2Home) PortableRemoteObject.narrow(
			 		BeansContext.get().lookup("ejb/org.openxava.test.model/Family2"),
			 		org.openxava.test.model.Family2Home.class);			 		
		}
		return familyHome;
	} 

	// Methods/Metodos 
	/**
	 * @ejb:interface-method
	 */
	public java.util.Collection getProductsValues()  {
		try { 		
			org.openxava.test.calculators.ProductsValuesOfSubfamilyCalculator getProductsValuesCalculator = (org.openxava.test.calculators.ProductsValuesOfSubfamilyCalculator)
				getMetaModel().getMetaMethod("getProductsValues").getMetaCalculator().getCalculator();  	
			getProductsValuesCalculator.setSubfamilyNumber(getNumber()); 
			return (java.util.Collection) getProductsValuesCalculator.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("method_execution_error", "getProductsValues", "Subfamily2"));
		}
	} 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Subfamily2").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.Subfamily2Data getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.Subfamily2Data data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.Subfamily2Value getSubfamily2Value();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setSubfamily2Value(org.openxava.test.model.Subfamily2Value value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setNumber(0); 
		setDescription(null); 
		setRemarks(null); 
		setFamilyKey(null); 	
	}
		
}