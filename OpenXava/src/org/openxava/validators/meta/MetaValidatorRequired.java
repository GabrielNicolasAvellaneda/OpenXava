package org.openxava.validators.meta;

import org.openxava.util.*;

/**
 * Inserte aquí la descripción del tipo.
 * 
 * @author Javier Paniza
 */
public class MetaValidatorRequired {
	
	private java.lang.String forType;
	private String forStereotype;
	private java.lang.String validatorName;
	public java.lang.String validatorClass;
	
	/**
	 * Comentario de constructor ValidadorRequerido.
	 */
	public MetaValidatorRequired() {
		super();
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getValidatorClass() throws XavaException {
		if (validatorClass == null) {
			validatorClass =
				MetaValidators
					.getMetaValidator(getValidatorName())
					.getClassName();
		}
		return validatorClass;
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getValidatorName() {
		return validatorName;
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getForType() {
		return forType;
	}
	/**
	 * 
	 * @param newClaseValidador java.lang.String
	 */
	public void setValidatorClass(java.lang.String newClaseValidador) {
		validatorClass = newClaseValidador;
	}
	/**
	 * 
	 * @param newNombreValidador java.lang.String
	 */
	public void setValidatorName(java.lang.String newNombreValidador) {
		validatorName = newNombreValidador;
	}
	/**
	 * 
	 * @param newParaClase java.lang.String
	 */
	public void setForType(java.lang.String newParaClase) {
		forType = newParaClase;
	}
	/**
	 * Returns the paraEstereotipo.
	 * @return String
	 */
	public String getForStereotype() {
		return forStereotype;
	}

	/**
	 * Sets the paraEstereotipo.
	 * @param paraEstereotipo The paraEstereotipo to set
	 */
	public void setForStereotype(String paraEstereotipo) {
		this.forStereotype = paraEstereotipo;
	}

}
