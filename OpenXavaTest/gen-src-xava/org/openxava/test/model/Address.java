

// File generated by OpenXava: Tue Nov 09 12:31:08 CET 2010
// Archivo generado por OpenXava: Tue Nov 09 12:31:08 CET 2010

// WARNING: NO EDIT
// OJO: NO EDITAR

package org.openxava.test.model;

import org.openxava.component.MetaComponent;
import org.openxava.model.meta.MetaModel;
import org.openxava.util.XavaException;

public class Address implements java.io.Serializable, org.openxava.model.IModel, org.openxava.test.model.IWithCity {

	// Attributes/Atributos 
	private int zipCode; 
	private String street; 
	private String asString; 
	private String city; 
	private org.openxava.test.model.IState state;



	// Properties/Propiedades 
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int newZipCode) {
		this.zipCode = newZipCode;
	} 
	public String getStreet() {
		return street;
	}
	public void setStreet(String newStreet) {
		this.street = newStreet;
	} 
	public String getAsString() {
		return asString;
	}
	public void setAsString(String newAsString) {
		this.asString = newAsString;
	} 
	public String getCity() {
		return city;
	}
	public void setCity(String newCity) {
		this.city = newCity;
	}

	// References 
	public org.openxava.test.model.IState getState() {
		return state;
	}
	public void setState(org.openxava.test.model.IState newState) {
		this.state = newState;
	}
	
	private MetaModel metaModel;
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			metaModel = MetaComponent.get("Customer").getMetaAggregate("Address");
		}
		return metaModel;
	}
	
}