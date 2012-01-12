
// File generated by OpenXava: Tue Jan 10 10:52:35 CET 2012
// Archivo generado por OpenXava: Tue Jan 10 10:52:35 CET 2012

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Issue		Entity/Entidad

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
public class Issue implements java.io.Serializable, org.openxava.test.model.IIssue {	

	// Constructor
	public Issue() {
		initMembers();
	} 

	private void initMembers() { 
		setId(null); 
		setDescription(null); 	
	} 
	
	// Properties/Propiedades 
	private static org.openxava.converters.IConverter descriptionConverter;
	private org.openxava.converters.IConverter getDescriptionConverter() {
		if (descriptionConverter == null) {
			try {
				descriptionConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("description");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "description"));
			}
			
		}	
		return descriptionConverter;
	} 
	private java.lang.String description;
	private java.lang.String get_Description() {
		return description;
	}
	private void set_Description(java.lang.String newDescription) {
		this.description = newDescription;
	} 	
	
	/**
	 * 
	 * 
	 */
	public String getDescription() {
		try {
			return (String) getDescriptionConverter().toJava(get_Description());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "Issue", "String"));
		}
	}
	
	/**
	 * 
	 */
	public void setDescription(String newDescription) {
		try { 
			set_Description((java.lang.String) getDescriptionConverter().toDB(newDescription));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "Issue", "String"));
		}		
	} 
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String newId) {
		this.id = newId;
	} 

	// References/Referencias 

	private org.openxava.test.model.IWorker worker; 	
	public org.openxava.test.model.IWorker getWorker() {
		if (worker != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				worker.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return worker;
	}
	public void setWorker(org.openxava.test.model.IWorker newWorker) {
		if (newWorker != null && !(newWorker instanceof org.openxava.test.model.Worker)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.worker = newWorker; 
	} 

	// Colecciones/Collections 
	private java.util.Collection comments;
	public java.util.Collection getComments() {
		return comments;
	}
	public void setComments(java.util.Collection comments) {
		this.comments = comments;
	} 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 	
 	public static Issue findById(java.lang.String id) throws javax.ejb.ObjectNotFoundException {
 		if (XavaPreferences.getInstance().isJPAPersistence()) { 			
 			javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from Issue as o where o.id = :arg0"); 
			query.setParameter("arg0", id); 
 			try {
				return (Issue) query.getSingleResult();
			}
			catch (Exception ex) {
				// In this way in order to work with Java pre 5 
				if (ex.getClass().getName().equals("javax.persistence.NoResultException")) {
					throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "Issue"));
				}
				else {
					ex.printStackTrace();
					throw new RuntimeException(ex.getMessage());
				}
			}  		
 		}
 		else {
 			org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from Issue as o where o.id = :arg0"); 
			query.setParameter("arg0", id); 
			Issue r = (Issue) query.uniqueResult();
			if (r == null) {
				throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "Issue"));
			}
			return r; 
 		}
 	} 


	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Issue").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		try {
			return getMetaModel().toString(this);
		}
		catch (XavaException ex) {
			System.err.println(XavaResources.getString("toString_warning", "Issue"));
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