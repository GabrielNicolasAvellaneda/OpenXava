
// File generated by OpenXava: Fri Apr 01 19:41:26 CEST 2005
// Archivo generado por OpenXava: Fri Apr 01 19:41:26 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: DeliveryType		Entity/Entidad

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
 * @ejb:bean name="DeliveryType" type="CMP" jndi-name="@subcontext@/ejb/org.openxava.test.ejb/DeliveryType" reentrant="false" view-type="remote"
 * @ejb:interface extends="org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IDeliveryType"
 * @ejb:data-object extends="java.lang.Object"
 * @ejb:home extends="javax.ejb.EJBHome"
 * @ejb:pk extends="java.lang.Object"
 *
 * @ejb.value-object name="DeliveryType" match="persistentCalculatedAndAggregate"
 *   
 * @ejb:env-entry name="DATA_SOURCE" type="java.lang.String" value="jdbc/DataSource"
 * @ejb:resource-ref  res-name="jdbc/DataSource" res-type="javax.sql.DataSource"  res-auth="Container" jndi-name="jdbc/@datasource@"
 * @jboss:resource-ref  res-ref-name="jdbc/DataSource" resource-name="jdbc/DataSource"
 * 
 * 
 * @jboss:table-name "XAVATEST_DELIVERYTYPE"
 *
 * @author Javier Paniza
 */
abstract public class DeliveryTypeBean extends EJBReplicableBase 
			implements org.openxava.test.ejb.IDeliveryType, EntityBean {	
			
	private boolean creating = false;		

	// Create 

	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.DeliveryTypeKey ejbCreate(Map values) 
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
		try { 		
			org.openxava.test.calculators.DeliveryTypePostcreateCalculator calculator0= (org.openxava.test.calculators.DeliveryTypePostcreateCalculator)
				getMetaModel().getMetaCalculatorPostCreate(0).getCalculator(); 
			calculator0.setNumber(getNumber()); 
			calculator0.setEntity(this); 
			calculator0.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "DeliveryType", ex.getLocalizedMessage()));
		} 
	} 
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.DeliveryTypeKey ejbCreate(org.openxava.test.ejb.DeliveryTypeData data) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		setData(data); 
		setNumber(data.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryTypeData data) 
		throws
			CreateException,
			ValidationException { 
		try { 		
			org.openxava.test.calculators.DeliveryTypePostcreateCalculator calculator0= (org.openxava.test.calculators.DeliveryTypePostcreateCalculator)
				getMetaModel().getMetaCalculatorPostCreate(0).getCalculator(); 
			calculator0.setNumber(getNumber()); 
			calculator0.setEntity(this); 
			calculator0.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "DeliveryType", ex.getLocalizedMessage()));
		}			
	}
	
	
	/**
	 * @ejb:create-method
	 */	 
	public org.openxava.test.ejb.DeliveryTypeKey ejbCreate(org.openxava.test.ejb.DeliveryTypeValue value) 
		throws
			CreateException,
			ValidationException {
		initMembers();	
		creating = true;	
		setDeliveryTypeValue(value); 
		setNumber(value.getNumber()); 
			
		return null;
	} 
	public void ejbPostCreate(org.openxava.test.ejb.DeliveryTypeValue value) 
		throws
			CreateException,
			ValidationException { 
		try { 		
			org.openxava.test.calculators.DeliveryTypePostcreateCalculator calculator0= (org.openxava.test.calculators.DeliveryTypePostcreateCalculator)
				getMetaModel().getMetaCalculatorPostCreate(0).getCalculator(); 
			calculator0.setNumber(getNumber()); 
			calculator0.setEntity(this); 
			calculator0.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_create_error", "DeliveryType", ex.getLocalizedMessage()));
		}			
	}
	
	public void ejbLoad() {
		creating = false;
	}
	
	public void ejbStore() {
		if (creating) {
			creating = false;
			return;
		} 
		try { 		
			org.openxava.test.calculators.DeliveryTypePostmodifyCalculator calculator0= (org.openxava.test.calculators.DeliveryTypePostmodifyCalculator)
				getMetaModel().getMetaCalculatorPostModify(0).getCalculator(); 
			calculator0.setEntity(this); 
			calculator0.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("entity_modify_error", "DeliveryType", ex.getLocalizedMessage()));
		}			
	} 	
	
	// Properties/Propiedades 
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
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryType", "String"));
		}
	}
	
	/**
	 * @ejb:interface-method
	 */
	public void setDescription(String newDescription) {
		try {
			set_Description((java.lang.String) getDescriptionConverter().toDB(newDescription));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("generator.conversion_error", "Description", "DeliveryType", "String"));
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
	// Methods/Metodos 

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("DeliveryType").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.DeliveryTypeData getData();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setData(org.openxava.test.ejb.DeliveryTypeData data);
	
	/**
	 * @ejb:interface-method
	 */	
	public abstract org.openxava.test.ejb.DeliveryTypeValue getDeliveryTypeValue();		
	
	/**
	 * @ejb:interface-method
	 */		
	public abstract void setDeliveryTypeValue(org.openxava.test.ejb.DeliveryTypeValue value);
	
	public void setEntityContext(javax.ejb.EntityContext ctx) {
		super.setEntityContext(ctx);
	}
	public void unsetEntityContext() {
		super.unsetEntityContext();
	}
	
	private void initMembers() { 
		setNumber(0); 
		setDescription(null); 
	}
		
}