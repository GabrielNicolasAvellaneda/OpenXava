
// File generated by OpenXava: Wed Oct 05 17:17:02 CEST 2005
// Archivo generado por OpenXava: Wed Oct 05 17:17:02 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Carrier		Entity/Entidad

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
public class Carrier implements java.io.Serializable, org.openxava.test.model.ICarrier {	
	
	// Properties/Propiedades 
	private org.openxava.converters.TrimStringConverter remarksConverter;
	private org.openxava.converters.TrimStringConverter getRemarksConverter() {
		if (remarksConverter == null) {
			try {
				remarksConverter = (org.openxava.converters.TrimStringConverter) 
					getMetaModel().getMapping().getConverter("remarks");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "remarks"));
			}
			
		}	
		return remarksConverter;
	} 
	private java.lang.String _remarks;
	private java.lang.String get_Remarks() {
		return _remarks;
	}
	private void set_Remarks(java.lang.String newRemarks) {
		this._remarks = newRemarks;
	} 	
	
	/**
	 * 
	 * 
	 */
	public java.lang.String getRemarks() {
		try {
			return (java.lang.String) getRemarksConverter().toJava(get_Remarks());
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Remarks", "Carrier", "java.lang.String"));
		}
	}
	
	/**
	 * 
	 */
	public void setRemarks(java.lang.String newRemarks) {
		try { 
			set_Remarks((java.lang.String) getRemarksConverter().toDB(newRemarks));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Remarks", "Carrier", "java.lang.String"));
		}		
	} 	
	/**
	 * 
	 * 
	 */
	public String getCalculated() {
		try { 		
			org.openxava.calculators.StringCalculator calculatedCalculator= (org.openxava.calculators.StringCalculator)
				getMetaModel().getMetaProperty("calculated").getMetaCalculator().getCalculator(); 
			return (String) calculatedCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.calculate_value_error", "Calculated", "Carrier", ex.getLocalizedMessage()));
		}
	}
	public void setCalculated(String newCalculated) {
		// for it is in value object
		// para que aparezca en los value objects
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Carrier", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "Carrier", "String"));
		}		
	} 
	private org.openxava.converters.IntegerNumberConverter numberConverter;
	private org.openxava.converters.IntegerNumberConverter getNumberConverter() {
		if (numberConverter == null) {
			try {
				numberConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("number");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "number"));
			}
			
		}	
		return numberConverter;
	} 
	private java.lang.Integer _number;
	private java.lang.Integer get_Number() {
		return _number;
	}
	private void set_Number(java.lang.Integer newNumber) {
		this._number = newNumber;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getNumber() {
		try {
			return ((Integer) getNumberConverter().toJava(get_Number())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Number", "Carrier", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setNumber(int newNumber) {
		try { 
			set_Number((java.lang.Integer) getNumberConverter().toDB(new Integer(newNumber)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Number", "Carrier", "int"));
		}		
	} 

	// References/Referencias 
	private org.openxava.test.model.IWarehouse warehouse;
	public org.openxava.test.model.IWarehouse getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(org.openxava.test.model.IWarehouse newWarehouse) {
		this.warehouse = newWarehouse;
	} 
	private org.openxava.test.model.IDrivingLicence drivingLicence;
	public org.openxava.test.model.IDrivingLicence getDrivingLicence() {
		return drivingLicence;
	}
	public void setDrivingLicence(org.openxava.test.model.IDrivingLicence newDrivingLicence) {
		this.drivingLicence = newDrivingLicence;
	} 

	// Colecciones/Collections 
	private java.util.Collection fellowCarriersCalculated;
	public java.util.Collection getFellowCarriersCalculated() {
		return fellowCarriersCalculated;
	}
	public void setFellowCarriersCalculated(java.util.Collection fellowCarriersCalculated) {
		this.fellowCarriersCalculated = fellowCarriersCalculated;
	} 
	private java.util.Collection fellowCarriers;
	public java.util.Collection getFellowCarriers() {
		return fellowCarriers;
	}
	public void setFellowCarriers(java.util.Collection fellowCarriers) {
		this.fellowCarriers = fellowCarriers;
	} 

	// Methods/Metodos 
	/**
	 * @ejb:interface-method
	 */
	public void translate()  {
		try { 		
			org.openxava.test.calculators.TranslateCarrierNameCalculator translateCalculator = (org.openxava.test.calculators.TranslateCarrierNameCalculator)
				getMetaModel().getMetaMethod("translate").getMetaCalculator().getCalculator(); 
				translateCalculator.setEntity(this); 
			translateCalculator.calculate(); 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("method_execution_error", "translate", "Carrier"));
		}
	} 	

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Carrier").getMetaEntity(); 	
		}
		return metaModel;
	}
	
	public String toString() { 
		return "Carrier::" + _number;
	}

	public boolean equals(Object other) {		
		if (other == null) return false;
		return toString().equals(other.toString());
	}
	
	public int hashCode() {		
		return toString().hashCode();
	}
	
}