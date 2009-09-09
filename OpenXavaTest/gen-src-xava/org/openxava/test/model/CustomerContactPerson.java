
// File generated by OpenXava: Tue Sep 08 10:36:40 CEST 2009
// Archivo generado por OpenXava: Tue Sep 08 10:36:40 CEST 2009

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: CustomerContactPerson		Entity/Entidad

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
public class CustomerContactPerson implements java.io.Serializable, org.openxava.test.model.ICustomerContactPerson {	

	// Constructor
	public CustomerContactPerson() {
		initMembers();
	} 

	private void initMembers() { 
		setName(null); 	
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "CustomerContactPerson", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "CustomerContactPerson", "String"));
		}		
	} 

	// References/Referencias 

	private org.openxava.test.model.ICustomer customer; 	
	public org.openxava.test.model.ICustomer getCustomer() {
		if (customer != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				customer.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return customer;
	}
	public void setCustomer(org.openxava.test.model.ICustomer newCustomer) {
		if (newCustomer != null && !(newCustomer instanceof org.openxava.test.model.Customer)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.customer = newCustomer; 
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 	
 	public static CustomerContactPerson findFindByCustomer(int customerNumber) throws javax.ejb.ObjectNotFoundException {
 		if (XavaPreferences.getInstance().isJPAPersistence()) { 			
 			javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from CustomerContactPerson as o where o.customer.number = :arg0"); 
			query.setParameter("arg0", new Integer(customerNumber)); 
 			try {
				return (CustomerContactPerson) query.getSingleResult();
			}
			catch (Exception ex) {
				// In this way in order to work with Java pre 5 
				if (ex.getClass().getName().equals("javax.persistence.NoResultException")) {
					throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "CustomerContactPerson"));
				}
				else {
					ex.printStackTrace();
					throw new RuntimeException(ex.getMessage());
				}
			}  		
 		}
 		else {
 			org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from CustomerContactPerson as o where o.customer.number = :arg0"); 
			query.setParameter("arg0", new Integer(customerNumber)); 
			CustomerContactPerson r = (CustomerContactPerson) query.uniqueResult();
			if (r == null) {
				throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "CustomerContactPerson"));
			}
			return r; 
 		}
 	} 


	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("CustomerContactPerson").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		try {
			return getMetaModel().toString(this);
		}
		catch (XavaException ex) {
			System.err.println(XavaResources.getString("toString_warning", "CustomerContactPerson"));
			return super.toString();
		}		
	}

	public boolean equals(Object other) {		
		if (other == null) return false;
		return toString().equals(other.toString());
	}
	
	public int hashCode() {		
		return toString().hashCode();
	}
	
}