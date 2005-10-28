
// File generated by OpenXava: Fri Oct 28 13:33:20 CEST 2005
// Archivo generado por OpenXava: Fri Oct 28 13:33:20 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Office2		Entity/Entidad

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
public class Office2 implements java.io.Serializable, org.openxava.test.model.IOffice2 {	
	
	// Properties/Propiedades 
	private org.openxava.converters.IntegerNumberConverter receptionistConverter;
	private org.openxava.converters.IntegerNumberConverter getReceptionistConverter() {
		if (receptionistConverter == null) {
			try {
				receptionistConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("receptionist");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "receptionist"));
			}
			
		}	
		return receptionistConverter;
	} 
	private java.lang.Integer _receptionist;
	private java.lang.Integer get_Receptionist() {
		return _receptionist;
	}
	private void set_Receptionist(java.lang.Integer newReceptionist) {
		this._receptionist = newReceptionist;
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office2", "int"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Receptionist", "Office2", "int"));
		}		
	} 
	private org.openxava.converters.TrimStringConverter nameConverter;
	private org.openxava.converters.TrimStringConverter getNameConverter() {
		if (nameConverter == null) {
			try {
				nameConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("name");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "name"));
			}
			
		}	
		return nameConverter;
	} 
	private java.lang.String _name;
	private java.lang.String get_Name() {
		return _name;
	}
	private void set_Name(java.lang.String newName) {
		this._name = newName;
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Office2", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Office2", "String"));
		}		
	} 
	private int zoneNumber;
	public int getZoneNumber() {
		return zoneNumber;
	}
	public void setZoneNumber(int newZoneNumber) {
		this.zoneNumber = newZoneNumber;
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
	public org.openxava.test.model.IClerk getOfficeManager() {
		return officeManager;
	}
	public void setOfficeManager(org.openxava.test.model.IClerk newClerk) {
		this.officeManager = newClerk;
	} 
	private org.openxava.test.model.ICarrier defaultCarrier;
	public org.openxava.test.model.ICarrier getDefaultCarrier() {
		return defaultCarrier;
	}
	public void setDefaultCarrier(org.openxava.test.model.ICarrier newCarrier) {
		this.defaultCarrier = newCarrier;
	} 
	private org.openxava.test.model.IWarehouse mainWarehouse;
	public org.openxava.test.model.IWarehouse getMainWarehouse() {
		return mainWarehouse;
	}
	public void setMainWarehouse(org.openxava.test.model.IWarehouse newWarehouse) {
		this.mainWarehouse = newWarehouse;
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Office2").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() { 
		return "Office2::" + number + "::" + zoneNumber + "::" + mainWarehouse;
	}

	public boolean equals(Object other) {		
		if (other == null) return false;
		return toString().equals(other.toString());
	}
	
	public int hashCode() {		
		return toString().hashCode();
	}
	
}