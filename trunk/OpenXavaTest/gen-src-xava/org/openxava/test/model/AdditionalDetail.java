
// File generated by OpenXava: Thu Nov 17 18:42:02 CET 2005
// Archivo generado por OpenXava: Thu Nov 17 18:42:02 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Service		Aggregate/Agregado: AdditionalDetail

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
public class AdditionalDetail implements java.io.Serializable, org.openxava.test.model.IAdditionalDetail {	
	
	// Properties/Propiedades 
	private int counter;
	public int getCounter() {
		return counter;
	}
	public void setCounter(int newCounter) {
		this.counter = newCounter;
	} 
	private org.openxava.converters.IntegerNumberConverter subfamilyConverter;
	private org.openxava.converters.IntegerNumberConverter getSubfamilyConverter() {
		if (subfamilyConverter == null) {
			try {
				subfamilyConverter = (org.openxava.converters.IntegerNumberConverter) 
					getMetaModel().getMapping().getConverter("subfamily");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "subfamily"));
			}
			
		}	
		return subfamilyConverter;
	} 
	private java.lang.Integer _subfamily;
	private java.lang.Integer get_Subfamily() {
		return _subfamily;
	}
	private void set_Subfamily(java.lang.Integer newSubfamily) {
		this._subfamily = newSubfamily;
	} 	
	
	/**
	 * 
	 * 
	 */
	public int getSubfamily() {
		try {
			return ((Integer) getSubfamilyConverter().toJava(get_Subfamily())).intValue();
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Subfamily", "AdditionalDetail", "int"));
		}
	}
	
	/**
	 * 
	 */
	public void setSubfamily(int newSubfamily) {
		try { 
			set_Subfamily((java.lang.Integer) getSubfamilyConverter().toDB(new Integer(newSubfamily)));
		}
		catch (org.openxava.converters.ConversionException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("generator.conversion_error", "Subfamily", "AdditionalDetail", "int"));
		}		
	} 

	// References/Referencias 
	private org.openxava.test.model.IService service;
	public org.openxava.test.model.IService getService() {
		return service;
	}
	public void setService(org.openxava.test.model.IService newService) {
		this.service = newService;
	} 
	private org.openxava.test.model.IServiceType type;
	public org.openxava.test.model.IServiceType getType() {
		return type;
	}
	public void setType(org.openxava.test.model.IServiceType newServiceType) {
		this.type = newServiceType;
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	private MetaModel metaModel;
	private MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Service").getMetaAggregate("AdditionalDetail"); 	
		}
		return metaModel;
	}
	
	public String toString() { 
		return "AdditionalDetail::" + counter + "::" + service;
	}

	public boolean equals(Object other) {		
		if (other == null) return false;
		return toString().equals(other.toString());
	}
	
	public int hashCode() {		
		return toString().hashCode();
	}
	
}