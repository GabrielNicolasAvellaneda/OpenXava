
// File generated by OpenXava: Tue Jun 09 11:32:14 CEST 2009
// Archivo generado por OpenXava: Tue Jun 09 11:32:14 CEST 2009

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Office		Entity/Entidad

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
public class Office implements java.io.Serializable, org.openxava.test.model.IOffice {	

	// Constructor
	public Office() {
		initMembers();
	} 

	private void initMembers() { 
		setNumber(0); 
		setZoneNumber(0); 
		setName(null); 
		setReceptionist(0); 	
	} 
	
	// Properties/Propiedades 
	private static org.openxava.converters.IConverter receptionistConverter;
	private org.openxava.converters.IConverter getReceptionistConverter() {
		if (receptionistConverter == null) {
			try {
				receptionistConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("receptionist");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "receptionist"));
			}
			
		}	
		return receptionistConverter;
	} 
	private java.lang.Integer receptionist;
	private java.lang.Integer get_Receptionist() {
		return receptionist;
	}
	private void set_Receptionist(java.lang.Integer newReceptionist) {
		this.receptionist = newReceptionist;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getReceptionist() {
		try {
			return ((Integer) getReceptionistConverter().toJava(get_Receptionist())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setReceptionist(int newReceptionist) {
		try { 
			set_Receptionist((java.lang.Integer) getReceptionistConverter().toDB(new Integer(newReceptionist)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office", "int"));
		}		
	} 
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Office", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Office", "String"));
		}		
	} 
	private static org.openxava.converters.IConverter zoneNumberConverter;
	private org.openxava.converters.IConverter getZoneNumberConverter() {
		if (zoneNumberConverter == null) {
			try {
				zoneNumberConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("zoneNumber");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "zoneNumber"));
			}
			
		}	
		return zoneNumberConverter;
	} 
	private java.lang.Integer zoneNumber;
	private java.lang.Integer get_ZoneNumber() {
		return zoneNumber;
	}
	private void set_ZoneNumber(java.lang.Integer newZoneNumber) {
		this.zoneNumber = newZoneNumber;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getZoneNumber() {
		try {
			return ((Integer) getZoneNumberConverter().toJava(get_ZoneNumber())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "ZoneNumber", "Office", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setZoneNumber(int newZoneNumber) {
		try { 
			set_ZoneNumber((java.lang.Integer) getZoneNumberConverter().toDB(new Integer(newZoneNumber)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "ZoneNumber", "Office", "int"));
		}		
	} 
	private int number;
	public int getNumber() {
		return number;
	}
	public void setNumber(int newNumber) {
		this.number = newNumber;
	} 

	// References/Referencias 

	private org.openxava.test.model.IClerk officeManager; 
	private java.lang.Integer officeManager_number; 	
	public org.openxava.test.model.IClerk getOfficeManager() {
		if (officeManager != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				officeManager.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return officeManager;
	}
	public void setOfficeManager(org.openxava.test.model.IClerk newClerk) throws RemoteException{
		if (newClerk != null && !(newClerk instanceof org.openxava.test.model.Clerk)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.officeManager = newClerk; 
		this.officeManager_number = newClerk == null?null:new Integer(newClerk.getNumber()); 
	} 

	private org.openxava.test.model.ICarrier defaultCarrier; 	
	public org.openxava.test.model.ICarrier getDefaultCarrier() {
		if (defaultCarrier != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				defaultCarrier.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return defaultCarrier;
	}
	public void setDefaultCarrier(org.openxava.test.model.ICarrier newCarrier) {
		if (newCarrier != null && !(newCarrier instanceof org.openxava.test.model.Carrier)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.defaultCarrier = newCarrier; 
	} 

	private org.openxava.test.model.IWarehouse mainWarehouse; 
	private java.lang.Integer mainWarehouse_number; 	
	public org.openxava.test.model.IWarehouse getMainWarehouse() {
		if (mainWarehouse != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				mainWarehouse.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return mainWarehouse;
	}
	public void setMainWarehouse(org.openxava.test.model.IWarehouse newWarehouse) throws RemoteException{
		if (newWarehouse != null && !(newWarehouse instanceof org.openxava.test.model.Warehouse)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.mainWarehouse = newWarehouse; 
		this.mainWarehouse_number = newWarehouse == null?null:new Integer(newWarehouse.getNumber()); 
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 	
 	public static Office findByNumber(int number) throws javax.ejb.ObjectNotFoundException {
 		if (XavaPreferences.getInstance().isJPAPersistence()) { 			
 			javax.persistence.Query query = org.openxava.jpa.XPersistence.getManager().createQuery("from Office as o where o.number = :arg0"); 
			query.setParameter("arg0", new Integer(number)); 
 			try {
				return (Office) query.getSingleResult();
			}
			catch (Exception ex) {
				// In this way in order to work with Java pre 5 
				if (ex.getClass().getName().equals("javax.persistence.NoResultException")) {
					throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "Office"));
				}
				else {
					ex.printStackTrace();
					throw new RuntimeException(ex.getMessage());
				}
			}  		
 		}
 		else {
 			org.hibernate.Query query = org.openxava.hibernate.XHibernate.getSession().createQuery("from Office as o where o.number = :arg0"); 
			query.setParameter("arg0", new Integer(number)); 
			Office r = (Office) query.uniqueResult();
			if (r == null) {
				throw new javax.ejb.ObjectNotFoundException(XavaResources.getString("object_not_found", "Office"));
			}
			return r; 
 		}
 	} 


	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Office").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		try {
			return getMetaModel().toString(this);
		}
		catch (XavaException ex) {
			System.err.println(XavaResources.getString("toString_warning", "Office"));
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