package org.openxava.util;

import java.io.*;



/**
 * Una clase que engloba una clave y una descripcion. <p>
 * 
 * @author Javier Paniza
 */
public class KeyAndDescription implements Serializable {
	boolean showCode=false;
	private Object key;
	private Object description;
	
	public KeyAndDescription() {
	}
	
	public KeyAndDescription(Object clave, Object descripcion) {
		this.key = clave;
		this.description = descripcion;
	}

	/**
	 * Returns the codigo.
	 * @return Object
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * Returns the descripcion.
	 * @return Object
	 */
	public Object getDescription() {
		return description;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setKey(Object codigo) {
		this.key = codigo;
	}

	/**
	 * Sets the descripcion.
	 * @param descripcion The descripcion to set
	 */
	public void setDescription(Object descripcion) {
		this.description = descripcion;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object otro) {
		if (!(otro instanceof KeyAndDescription)) return false;
		KeyAndDescription o = (KeyAndDescription) otro;	
		return Is.equal(this.key, o.key);	
	}

	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (showCode)
			return description==null?"":description.toString().trim() + " [" +key+"]";
		else 
			return description==null?"":description.toString().trim();
	}

	/**
	 * Returns the mostrarCodigo.
	 * @return boolean
	 */
	public boolean isShowCode() {
		return showCode;
	}

	/**
	 * Sets the mostrarCodigo.
	 * @param mostrarCodigo The mostrarCodigo to set
	 */
	public void setShowCode(boolean mostrarCodigo) {
		this.showCode = mostrarCodigo;
	}

}
