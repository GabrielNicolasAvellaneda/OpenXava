
// File generated by OpenXava: Mon May 09 09:30:45 CEST 2005
// Archivo generado por OpenXava: Mon May 09 09:30:45 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Service		Entity/Entidad

package org.openxava.test.model;

import java.util.*;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class Service implements java.io.Serializable {	
	
	// Properties/Propiedades 
	private org.openxava.converters.IntegerNumberConverter familyConverter;
	private org.openxava.converters.IntegerNumberConverter getFamilyConverter() {
		if (familyConverter == null) {
			try {
				familyConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("family");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "family"));
			}
			
		}	
		return familyConverter;
	} 
	private java.lang.Integer _family;
	private java.lang.Integer get_Family() {
		return _family;
	}
	private void set_Family(java.lang.Integer newFamily) {
		this._family = newFamily;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getFamily() {
		try {
			return ((Integer) getFamilyConverter().toJava(get_Family())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Family", "Service", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setFamily(int newFamily) {
		try { 
			set_Family((java.lang.Integer) getFamilyConverter().toDB(new Integer(newFamily)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Family", "Service", "int"));
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
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "description"));
			}
			
		}	
		return descriptionConverter;
	} 
	private java.lang.String _description;
	private java.lang.String get_Description() {
		return _description;
	}
	private void set_Description(java.lang.String newDescription) {
		this._description = newDescription;
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "Service", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Description", "Service", "String"));
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

	// Colecciones/Collections 
	private java.util.Collection additionalDetails;
	public java.util.Collection getAdditionalDetails() {
		return additionalDetails;
	} 
	
	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Service").getMetaEntity(); 	
		}
		return metaModel;
	}
}