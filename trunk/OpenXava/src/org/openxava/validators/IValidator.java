package org.openxava.validators;


import org.openxava.util.*;


/**
 * Validador. <p>
 *  
 * El m�todo de esta interfaz no recibe el valor a validar,
 * este se ha de asignar como una propiedad. Las propiedades
 * que tenga el validador dependen de lo gen�rico que se quiera hacer.<br>
 * 
 * Por ejemplo, podemos usar un validador de la siguiente forma:
 * <pre>
 * IValidador v = new ValidadorLimite();
 * v.setLimite(1000);
 * v.setValor(factura.getImporte()); // por ejemplo
 * Mensajes errores = new Mensajes();
 * v.validar(errores);
 * // Y a errores se a�ade un  mensaje de validaci�n si procede 
 * </pre>
 *  
 * @author Javier Paniza
 */

public interface IValidator {
	
	/**
	 * Realiza la validaci�n. <p>
	 * 
	 * @param errores Lista de errores de validaci�n, una lista de id para 
	 * 								leer en un archivo de recursos.
	 * @throws Exception Cualquier problema que ocurra que impida realizar
	 *	la validaci�n
	 */
	void validate(Messages errores) throws Exception;

}
