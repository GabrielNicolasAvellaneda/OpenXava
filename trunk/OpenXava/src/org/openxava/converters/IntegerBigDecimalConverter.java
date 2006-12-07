package org.openxava.converters;

import java.math.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * In java a int and in database a BigDecimal. <p>
 * 
 * @author Javier Paniza
 */
public class IntegerBigDecimalConverter implements IConverter {

	private static Log log = LogFactory.getLog(IntegerBigDecimalConverter.class);
	
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Integer)) {		
			throw new ConversionException("conversion_db_integer_excepted");
		}				
		return new BigDecimal(o.toString());
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (!(o instanceof BigDecimal)) {		
			throw new ConversionException("conversion_java_bigdecimal_excepted");
		}
		return new Integer(((BigDecimal) o).intValue());
	}
			
}
