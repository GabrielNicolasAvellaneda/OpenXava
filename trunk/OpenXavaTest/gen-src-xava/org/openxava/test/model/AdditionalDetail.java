
// File generated by OpenXava: Fri Jun 22 18:13:50 CEST 2007
// Archivo generado por OpenXava: Fri Jun 22 18:13:50 CEST 2007

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

	// Constructor
	public AdditionalDetail() {
		initMembers();
	} 

	private void initMembers() { 
		setCounter(0); 
		setSubfamily(0); 	
	} 
	
	// Properties/Propiedades 
	private int counter;
	public int getCounter() {
		return counter;
	}
	public void setCounter(int newCounter) {
		this.counter = newCounter;
	} 
	private static org.openxava.converters.IConverter subfamilyConverter;
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
		if (service != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				service.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return service;
	}
	public void setService(org.openxava.test.model.IService newService) {
		if (newService != null && !(newService instanceof org.openxava.test.model.Service)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.service = newService; 
	} 

	private org.openxava.test.model.IServiceType type; 	
	public org.openxava.test.model.IServiceType getType() {
		if (type != null) {
			// Because not-found='ignore' annul lazy initialization, we simulate it
			try {
				type.toString();
			}
			catch (Exception ex) {
				return null;
			}
		}	
		return type;
	}
	public void setType(org.openxava.test.model.IServiceType newServiceType) {
		if (newServiceType != null && !(newServiceType instanceof org.openxava.test.model.ServiceType)) {
			throw new IllegalArgumentException(XavaResources.getString("ejb_to_pojo_illegal")); 
		}
		this.type = newServiceType; 
	} 

	// Colecciones/Collections 

	// Methods/Metodos 	

	// User defined finders/Buscadores definidos por el usuario 


	private static MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) { 
			metaModel = MetaComponent.get("Service").getMetaAggregate("AdditionalDetail"); 	
		}
		return metaModel;
	}
	
	public String toString() {		
		try {
			return getMetaModel().toString(this);
		}
		catch (XavaException ex) {
			System.err.println(XavaResources.getString("toString_warning", "AdditionalDetail"));
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