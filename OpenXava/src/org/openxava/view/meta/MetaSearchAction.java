package org.openxava.view.meta;

import java.io.*;
import java.util.*;

/**
 * @author Javier Paniza
 */
public class MetaSearchAction implements Serializable, Cloneable {
	
	private String className = "puntocom.xava.xvista.swing.AccionBuscar"; // only for spanish/swing version

	private Map propertiesValues;
	private String actionName;
		

	/**
	 * Only for spanish/swing version.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * Only for spanish/swing version.
	 */	
	public void setClassName(String nombreClase) {
		this.className = nombreClase;
	}
	
	public Map getPropertiesValues() {		
		return propertiesValues==null?new HashMap():propertiesValues;				
	}

	public void addPropertyValue(String nombre, String valor) {
		if (propertiesValues == null) {
			propertiesValues = new HashMap();
		}
		propertiesValues.put(nombre, valor);
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String string) {
		actionName = string;
	}
	
	public Object clone() throws CloneNotSupportedException { 
		MetaSearchAction clon = (MetaSearchAction) super.clone();
		if (propertiesValues != null) clon.propertiesValues = new HashMap(propertiesValues);
		return clon;
	}

}
