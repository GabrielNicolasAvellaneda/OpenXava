package org.openxava.controller.meta;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class MetaUseObject {
	
	private String name;
	private String actionProperty;
	
	public String getName() {
		return name;
	}

	/**
	 * Si no está establecida asume el nombre del objeto, sin el prefijo. <p>
	 * 
	 * Por ej. si el nombre es 'vista' asume 'vista' y si es 'xava_vista"
	 * asume 'vista'.
	 * @return
	 */
	public String getActionProperty() {
		if (!Is.emptyString(actionProperty)) return actionProperty; 
		if (name==null) return "";
		int posPunto = name.indexOf('_'); 
		if (posPunto >= 0) {
			return name.substring(posPunto+1); 
		}
		else {
			return name;
		}
	}

	public void setName(String string) {
		name = string;
	}

	public void setActionProperty(String string) {
		actionProperty = string;
	}

}
