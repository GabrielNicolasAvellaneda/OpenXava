package org.openxava.converters;

import java.math.*;


/**
 * En java un int y en la base de datos un BigDecimal.
 * 
 * @author Javier Paniza
 */
public class IntegerBigDecimalConverter implements IConverter {

	/**
	 * Constructor for ConversorBooleanSN.
	 */
	public IntegerBigDecimalConverter() {
		super();
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Integer)) {		
			throw new ConversionException("conversion_db_integer_excepted");
		}				
		return new BigDecimal(o.toString());
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (!(o instanceof BigDecimal)) {		
			throw new ConversionException("conversion_java_bigdecimal_excepted");
		}
		return new Integer(((BigDecimal) o).intValue());
	}
			
}
