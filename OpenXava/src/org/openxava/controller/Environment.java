package org.openxava.controller;

import java.util.*;

import org.openxava.controller.meta.*;
import org.openxava.util.*;

/**
 * Lee primero las variables enviadas al crear (normalmente extraidas del módulo),
 * y después todas las definidas en los controladores.
 * 
 * @author Javier Paniza
 */

public class Environment implements java.io.Serializable {
	
	private Map localVariables;
	
	/**
	 * 
	 * @param variablesLocales Puede ser nulo.
	 */
	public Environment(Map variablesLocales) {
		this.localVariables = variablesLocales;
	}

	/**
	 * @return Nulo si no existe.
	 */
	public String getValue(String nombre) throws XavaException {
		String valor = null;
		if (localVariables != null) {
			valor = (String) localVariables.get(nombre);
		}
		if (valor != null) return valor;
		return MetaControllers.getEnvironmentVariable(nombre);
	}

}
