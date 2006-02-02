
// File generated by OpenXava: Thu Feb 02 10:38:08 CET 2006
// Archivo generado por OpenXava: Thu Feb 02 10:38:08 CET 2006

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
	private org.openxava.converters.IConverter subfamilyConverter;
	private org.openxava.converters.IConverter getSubfamilyConverter() {
		if (subfamilyConverter == null) {
			try {
				subfamilyConverter = (org.openxava.converters.IConverter) 
					getMetaModel().getMapping().getConverter("subfamily");
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(XavaResources.getString("generator.create_converter_error", "subfamily"));
			}
			
		}	
		return subfamilyConverter;
	} 
	private java.lang.Integer subfamily;
	private java.lang.Integer get_Subfamily() {
		return subfamily;
	}
	private void set_Subfamily(java.lang.Integer newSubfamily) {
		this.subfamily = newSubfamily;
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

	// User defined finders/Buscadores definidos por el usuario 


	private MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Service").getMetaAggregate("AdditionalDetail"); 	
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