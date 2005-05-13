
// File generated by OpenXava: Fri May 13 17:18:01 CEST 2005
// Archivo generado por OpenXava: Fri May 13 17:18:01 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: State		Entity/Entidad

package org.openxava.test.model;

import java.util.*;
import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.*;

/**
 * 
 * @author MCarmen Gimeno
 */
public class State implements java.io.Serializable {	
	
	// Properties/Propiedades 	
	/**
	 * 
	 * 
	 */
	public String getFullName() {
		try { 		
			org.openxava.calculators.ConcatCalculator fullNameCalculator= (org.openxava.calculators.ConcatCalculator)
				getMetaModel().getMetaProperty("fullName").getMetaCalculator().getCalculator();  	
			fullNameCalculator.setString1(getId());  	
			fullNameCalculator.setString2(getName()); 
			return (String) fullNameCalculator.calculate();
		}
		catch (NullPointerException ex) {
			// Usually for multilevel property access with null references 
			return null; 			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.calculate_value_error", "FullName", "State", ex.getLocalizedMessage()));
		}
	}
	public void setFullName(String newFullName) {
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "State", "String"));
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
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Name", "State", "String"));
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

	// Colecciones/Collections 
	
	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("State").getMetaEntity(); 	
		}
		return metaModel;
	}
}