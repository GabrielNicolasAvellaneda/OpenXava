package org.openxava.converters;

import java.io.*;

/**
 * Estos conversores deben tener propiedades que se llenan
 * antes de llamar a toJava y despu�s de llamar toDB. 
 * 
 * @author Javier Paniza
 */
public interface IMultipleConverter extends Serializable {
	
	/**
	 * Primero se estableceran las propiedades (con los datos en formato db), y
	 * despu�s se llamar� a toJava para obtener un objeto Java construido a
	 * partir de esas propiedades.
	 */
	Object toJava() throws ConversionException;
	/**
	 * Primero se llamara a este m�todo enviando el objeto java que se quiere
	 * desglosar, y despu�s se obtendra el objeto desglosado accediendo a
	 * las propiedades.
	 */	
	void toDB(Object objetoJava) throws ConversionException;

}
