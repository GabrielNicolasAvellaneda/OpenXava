
// File generated by OpenXava: Tue Jan 09 13:28:31 CET 2007
// Archivo generado por OpenXava: Tue Jan 09 13:28:31 CET 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: FilterBySubfamily		Entity/Entidad

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
public class FilterBySubfamily implements java.io.Serializable, org.openxava.test.model.IFilterBySubfamily {	

	// Constructor
	public FilterBySubfamily() {
		initMembers();
	} 

	private void initMembers() { 
		setOid(null); 	
	} 
	
	// Properties/Propiedades 
	private String oid;
	public String getOid() {
		return oid;
	}
	public void setOid(String newOid) {
		this.oid = newOid;
	} 

	// References/Referencias 

	private org.openxava.test.model.ISubfamily2 subfamilyTo; 	
	public org.openxava.test.model.ISubfamily2 getSubfamilyTo() {
		if (subfamilyTo != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				subfamilyTo.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return subfamilyTo;
	}
	public void setSubfamilyTo(org.openxava.test.model.ISubfamily2 newSubfamily2) {
		if (newSubfamily2 != null && !(newSubfamily2 instanceof org.openxava.test.model.Subfamily2)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.subfamilyTo = newSubfamily2; 
	} 

	private org.openxava.test.model.ISubfamily2 subfamily; 	
	public org.openxava.test.model.ISubfamily2 getSubfamily() {
		if (subfamily != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				subfamily.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return subfamily;
	}
	public void setSubfamily(org.openxava.test.model.ISubfamily2 newSubfamily2) {
		if (newSubfamily2 != null && !(newSubfamily2 instanceof org.openxava.test.model.Subfamily2)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.subfamily = newSubfamily2; 
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 	
 	public static FilterBySubfamily findByOid(java.lang.String oid) throws javax.ejb.ObjectNotFoundException {
 		if (XavaPreferences.getInstance().isJPAPersistence()) { 			
 			javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from FilterBySubfamily as o where o.oid = :arg0"); 
			query.setParameter("arg0", oid); 
 			try {
				return (FilterBySubfamily) query.getSingleResult();
			}
			catch (Exception ex) {
				// In this way in order to work with Java pre 5 
				if (ex.getClass().getName().equals("javax.persistence.NoResultException")) {
					throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "FilterBySubfamily"));
				}
				else {
					ex.printStackTrace();
					throw new RuntimeException(ex.getMessage());
				}
			}  		
 		}
 		else {
 			org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from FilterBySubfamily as o where o.oid = :arg0"); 
			query.setParameter("arg0", oid); 
			FilterBySubfamily r = (FilterBySubfamily) query.uniqueResult();
			if (r == null) {
				throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "FilterBySubfamily"));
			}
			return r; 
 		}
 	} 


	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("FilterBySubfamily").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		try {
			return getMetaModel().toString(this);
		}
		catch (XavaException ex) {
			System.err.println(XavaResources.getString("toString_warning", "FilterBySubfamily"));
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