package org.openxava.validators.meta;

import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.*;


/**
 * 
 * @author Javier Paniza
 */
public class MetaValidator extends MetaSetsContainer {
	private IRemoveValidator removeValidator;
	private IPropertyValidator propertyValidator;
	private IValidator validator;
	private java.lang.String name;
	public java.lang.String className;

	public MetaValidator() {
		super();
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getClassName() throws XavaException {
		if (Is.emptyString(className)) {
			className = MetaValidators.getMetaValidator(getName()).getClassName();
		}
		return className;
	}
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName() {
		return name;
	}
	/**
	 * 
	 * @param newClase java.lang.String
	 */
	public void setClassName(java.lang.String newClase) {
		className = newClase;
	}
	/**
	 * 
	 * @param newNombre java.lang.String
	 */
	public void setName(java.lang.String newNombre) {
		name = newNombre;
	}

	/**
	 * Crea un validador cada vez que se llama a este método,
	 * configurado con los valores asignados en xml.
	 * 
	 * @return IValidador
	 * @throws XavaException
	 */
	public IValidator createValidator() throws XavaException {
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof IValidator)) {
				throw new XavaException("validator_invalid_class", getClassName());
			}
			IValidator validador = (IValidator) o;
			if (containsMetaSets()) {
				assignPropertiesValues(validador);
			}						
			return validador;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("Imposible crear validador " + getClassName() + " por:\n" + ex.getLocalizedMessage());
		}
	}
	
	/**
	 * La primera vez crea, y las veces sucesivas devuelve el creado la primera
	 * vez. <p>
	 * 
	 * @return IValidador
	 */
	public IValidator getValidator() throws XavaException {
		if (validator == null) {
			validator = createValidator();
		}
		return validator;
	}
	
	/**
	 * Crea un validador cada vez que se llama a este método,
	 * configurado con los valores asignados en xml.
	 * 
	 * @return IValidadorPropiedad
	 * @throws XavaException
	 */
	public IPropertyValidator createPropertyValidator() throws XavaException {
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof IPropertyValidator)) {
				throw new XavaException("property_validator_invalid_class", getClassName());
			}
			IPropertyValidator validador = (IPropertyValidator) o;
			if (containsMetaSets()) {
				assignPropertiesValues(validador);
			}						
			return validador;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_validator_error", getClassName(), ex.getLocalizedMessage());
		}
	}
	
	/**
	 * La primera vez crea, y las veces sucesivas devuelve el creado la primera
	 * vez. <p>	 
	 */
	public IPropertyValidator getPropertyValidator() throws XavaException {
		if (propertyValidator == null) {
			propertyValidator = createPropertyValidator();
		}
		return propertyValidator;
	}
	
	/**
	 * Crea un validador cada vez que se llama a este método,
	 * configurado con los valores asignados en xml.
	 * 		 
	 */
	public IRemoveValidator createRemoveValidator() throws XavaException {
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof IRemoveValidator)) {
				throw new XavaException("remove_validator_invalid_class", getClassName());
			}
			IRemoveValidator validador = (IRemoveValidator) o;
			if (containsMetaSets()) {
				assignPropertiesValues(validador);
			}						
			return validador;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_validator_error", getClassName(), ex.getLocalizedMessage());
		}
	}
	
	/**
	 * La primera vez crea, y las veces sucesivas devuelve el creado la primera
	 * vez. <p>	 
	 */
	public IRemoveValidator getRemoveValidator() throws XavaException {
		if (removeValidator == null) {
			removeValidator = createRemoveValidator();
		}
		return removeValidator;
	}		
			
	
}