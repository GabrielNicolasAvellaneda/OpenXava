package org.openxava.filters;

/**
 * Para filtrar el valor de los parametros enviados a las consultas. <p> 
 * 
 * @author Javier Paniza
 */
public interface IFilter {
	
	/**	  
	 * @param o Argumento a filtrar. Un objeto
	 * @return Argumento filtrado. Puede ser un array de objetos.
	 * @throws FilterException Cualquier otro error. Bien de sistema o de
	 * 			programación.
	 */
	Object filter(Object o) throws FilterException;
}