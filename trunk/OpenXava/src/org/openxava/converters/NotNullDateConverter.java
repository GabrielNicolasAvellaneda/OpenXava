package org.openxava.converters;

import java.util.*;

import org.openxava.util.*;


/**
 * 04:41:10 PM
 * @author Ana Andrés
 */
public class NotNullDateConverter implements IConverter{
	private static final Date FECHA_NULA = Dates.create(1,1,1);
	
	public Object toJava(Object o) throws ConversionException {
		if(o == null) return null;
		if (!(o instanceof Date)) {		
			throw new ConversionException(
				"Se esperaba un dato de tipo Date para hacer la conversión");
		}
		Date fecha = (Date)o;
		return (fecha.compareTo(FECHA_NULA) == 0) ? null : fecha;
	}

	public Object toDB(Object o) throws ConversionException {
		if (o == null) return FECHA_NULA;
		if (!(o instanceof Date)) {		
			throw new ConversionException(
				"Se esperaba un dato de tipo Date para hacer la conversión");
		}
		return o;
	}

}
