package org.openxava.converters;

import java.util.*;


/**
 * En java un boolean y en la base de datos una cadena
 * que puede ser 1 o 0.
 * 
 * @author Javier Paniza
 */
public class DateUtilSQLConverter implements IConverter {

	/**
	 * Constructor for ConversorBooleanSN.
	 */
	public DateUtilSQLConverter() {
		super();
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof java.util.Date)) {		
			throw new ConversionException("conversion_db_utildate_expected");
		}
		return new java.sql.Date(((Date)o).getTime());
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof java.sql.Date)) {		
			throw new ConversionException("conversion_java_sqldate_expected");
		}
		return new Date(((java.sql.Date) o).getTime());
	}
	
	
	

}
