package org.openxava.converters;

import java.math.*;


/**
 * En java un int y en la base de datos un BigDecimal.
 * 
 * @author Javier Paniza
 */
public class LongBigDecimalConverter implements IConverter {

	/**
	 * Constructor for ConversorBooleanSN.
	 */
	public LongBigDecimalConverter() {
		super();
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Long)) {		
			throw new ConversionException("conversion_db_long_expected");
		}				
		return new BigDecimal(o.toString());
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (!(o instanceof BigDecimal)) {		
			throw new ConversionException("conversion_java_bigdecimal_expected");
		}
		return new Long(((BigDecimal) o).toString());
	}
			
}
