package org.openxava.converters;

import java.io.*;

/**
 * Estos conversores deben tener propiedades que se llenan
 * antes de llamar a toJava y después de llamar toDB. 
 * 
 * @author Javier Paniza
 */
public interface IMultipleConverter extends Serializable {
	
	/**
	 * Primero se estableceran las propiedades (con los datos en formato db), y
	 * después se llamará a toJava para obtener un objeto Java construido a
	 * partir de esas propiedades.
	 */
	Object toJava() throws ConversionException;
	/**
	 * Primero se llamara a este método enviando el objeto java que se quiere
	 * desglosar, y después se obtendra el objeto desglosado accediendo a
	 * las propiedades.
	 */	
	void toDB(Object objetoJava) throws ConversionException;

}
