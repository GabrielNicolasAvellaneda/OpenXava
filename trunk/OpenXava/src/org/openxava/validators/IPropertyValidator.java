package org.openxava.validators;


import org.openxava.util.*;

/**
 * Validador para una propiedad.
 * 
 * @author Javier Paniza
 */
public interface IPropertyValidator extends java.io.Serializable {

	/**
	 * Valida. <p>
	 *
	 * Los errores de validación se añaden a un objeto de tipo <tt>Errores</tt>. <br>
	 *
	 * @param errores Nunca nulo. Lista de ids para leer en el archivo de recursos.
	 * @param valor Valor a validar. Puede ser nulo.
	 * @param nombrePropiedad Id de la propiedad en el archivo de recursos 
	 * @param nombreModelo Id del objeto en archivo de recursos.	 
	 * @exception Exception  Algún problema de sistema.
	 */
	void validate(Messages errores,	Object valor,	String nombrePropiedad,	String nombreModelo) throws Exception;

}
