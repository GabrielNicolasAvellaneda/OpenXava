package org.openxava.validators;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class ValidationException extends Exception {
	
	private Messages errores;
	
	public ValidationException() {		
	}
	
	public ValidationException(String textoMensaje) {
		super(textoMensaje);
	}

	public ValidationException(ValidationException ex) {		
		errores = ((ValidationException) ex).getErrors();
	}

	public ValidationException(Messages errores) {
		this.errores = errores;		
	}
	
	public String getMessage() {
		return errores==null?super.getMessage():errores.toString();		
	}

	
	public Messages getErrors() {
		return errores;
	}
	
}
