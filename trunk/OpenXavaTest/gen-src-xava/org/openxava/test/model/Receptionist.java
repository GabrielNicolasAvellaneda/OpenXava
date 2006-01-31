
// File generated by OpenXava: Tue Jan 31 12:55:38 CET 2006
// Archivo generado por OpenXava: Tue Jan 31 12:55:38 CET 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Aggregate/Agregado: Receptionist

package org.openxava.test.model;

import java.util.*;
import java.math.*;
import java.rmi.RemoteException;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class Receptionist implements java.io.Serializable, org.openxava.test.model.IReceptionist {	
	
	// Properties/Propiedades 
	private int oid;
	public int getOid() {
		return oid;
	}
	public void setOid(int newOid) {
		this.oid = newOid;
	} 
	private org.openxava.converters.IConverter nameConverter;
	private org.openxava.converters.IConverter getNameConverter() {
		if (nameConverter == null) {
			try {
				nameConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("name");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "name"));
			}
			
		}	
		return nameConverter;
	} 
	private java.lang.String name;
	private java.lang.String get_Name() {
		return name;
	}
	private void set_Name(java.lang.String newName) {
		this.name = newName;
	} 	
	
	/**
	 * 
	 * 
	 */
	public String getName() {
		try {
			return (String) getNameConverter().toJava(get_Name());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Receptionist", "String"));
		}
	}
	
	/**
	 * 
	 */
	public void setName(String newName) {
		try { 
			set_Name((java.lang.String) getNameConverter().toDB(newName));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Receptionist", "String"));
		}		
	} 

	// References/Referencias 
	private org.openxava.test.model.IDeliveryPlace deliveryPlace;
	public org.openxava.test.model.IDeliveryPlace getDeliveryPlace() {
		return deliveryPlace;
	}
	public void setDeliveryPlace(org.openxava.test.model.IDeliveryPlace newDeliveryPlace) {
		this.deliveryPlace = newDeliveryPlace;
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 	
 	public static Collection findAll() {
 		org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from Receptionist as o"); 
 		org.hibernate.Query sizeQuery = org.openxava.hibernate.XHibernate.getSession().createQuery("select count(*) from Receptionist as o"); 
 		return new org.openxava.hibernate.impl.FastSizeList(query, sizeQuery);
 	} 


	private MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Customer").getMetaAggregate("Receptionist"); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		StringBuffer toStringValue = new StringBuffer("[.");
		java.lang.reflect.Field [] fields = getClass().getDeclaredFields();
		Arrays.sort(fields, FieldComparator.getInstance());
		for (int i=0; i < fields.length; i++) {
			try {
				if (getMetaModel().isKey(fields[i].getName())) {
					toStringValue.append(fields[i].get(this)).append('.');
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				toStringValue.append(" ").append('.');
			}
		}
		toStringValue.append(']');
		return toStringValue.toString();
	}

	public boolean equals(Object other) {		
		if (other == null) return false;
		return toString().equals(other.toString());
	}
	
	public int hashCode() {		
		return toString().hashCode();
	}
	
}