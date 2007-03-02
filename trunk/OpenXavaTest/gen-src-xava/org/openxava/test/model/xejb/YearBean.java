
// File generated by OpenXava: Thu Mar 01 13:37:27 CET 2007
// Archivo generado por OpenXava: Thu Mar 01 13:37:27 CET 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Year		Entity/Entidad

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
 * @ejb:bean name="Year" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.model/Year" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IYear"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="Year" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 	
 * @ejb:finder signature="Year findById(int id)" query="SELECT OBJECT(o) FROM Year o WHERE o.id = ?1" view-type="remote" result-type-mapping="Remote"
 * @jboss:query signature="Year findById(int id)" query="SELECT OBJECT(o) FROM Year o WHERE o.id = ?1" 
 * 
 * @jboss:table-name "XAVATEST_YEAR"
 *
 * @author Javier Paniza
 */
abstract public class YearBean extends EJBReplicableBase 
			implements org.openxava.test.model.IYear, EntityBean {	
			
	private boolean creating = false;		
	private boolean modified = false;

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.YearKey ejbCreate(Map values) 
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
	public org.openxava.test.model.YearKey ejbCreate(org.openxava.test.model.YearData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setData(data); 
		setId(data.getId()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.YearData data) 
		throws
			CreateException,
			ValidationException { 			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.model.YearKey ejbCreate(org.openxava.test.model.YearValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		modified = false;
		setYearValue(value); 
		setId(value.getId()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.model.YearValue value) 
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
	private static org.openxava.converters.IConverter yearConverter;
	private org.openxava.converters.IConverter getYearConverter() {
		if (yearConverter == null) {
			try {
				yearConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("year");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("generator.create_converter_error", "year"));
			}
			
		}	
		return yearConverter;
	} 
	/**	 
	 * @ejb:persistent-field
	 * 
	 * @jboss:column-name "YEAR"
	 */
	public abstract java.lang.Integer get_Year();
	public abstract void set_Year(java.lang.Integer newYear); 	
	
	/**
	 * @ejb.value-object match="persistentCalculatedAndAggregate"
	 * @ejb:interface-method
	 */
	public int getYear() {
		try {
			return ((Integer) getYearConverter().toJava(get_Year())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Year", "Year", "int"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setYear(int newYear) {
		try { 
			this.modified = true; 
			set_Year((java.lang.Integer) getYearConverter().toDB(new Integer(newYear)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Year", "Year", "int"));
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
	public abstract int getId();
	/**
	  * 
	  */
	public abstract void setId(int newId); 

	// Colecciones/Collections		

	// References/Referencias 

	// Methods/Metodos 

	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Year").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.YearData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.model.YearData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.model.YearValue getYearValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setYearValue(org.openxava.test.model.YearValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	} 

	private void initMembers() { 
		setId(0); 
		setYear(0); 	
	} 		
}