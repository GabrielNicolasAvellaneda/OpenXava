package org.openxava.util.meta;

import java.io.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class MetaSet implements Serializable {
	
	private String propertyName;
	private String propertyNameFrom;
	private String value;

	/**
	 * Returns the nombrePropiedad.
	 * @return String
	 */
	public String getPropertyName() {
		return propertyName;
	}

	public String getPropertyNameFrom() {
		if (hasValue()) return ""; // value an property from are not compatibles
		if (Is.emptyString(propertyNameFrom)) return getPropertyName();		
		return propertyNameFrom;
	}

	public void setPropertyName(String nombrePropiedad) {
		this.propertyName = nombrePropiedad;
	}

	public void setPropertyNameFrom(String nombrePropiedadDesde) {
		this.propertyNameFrom = nombrePropiedadDesde;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String valor) {
		this.value = valor;
	}
	
	public boolean hasValue() {
		// No trim because spaces are valid values
		return !(value == null || value.length() == 0);
	}

}
