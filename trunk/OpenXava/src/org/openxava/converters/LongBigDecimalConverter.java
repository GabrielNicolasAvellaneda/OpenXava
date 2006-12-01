package org.openxava.converters;

import java.math.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * In java a long and in database a BigDecimal.
 * 
 * @author Javier Paniza
 */
public class LongBigDecimalConverter implements IConverter {

	private Log log = LogFactory.getLog(LongBigDecimalConverter.class);
	
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Long)) {		
			throw new ConversionException("conversion_db_long_expected");
		}				
		return new BigDecimal(o.toString());
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (!(o instanceof BigDecimal)) {		
			throw new ConversionException("conversion_java_bigdecimal_expected");
		}
		return new Long(((BigDecimal) o).toString());
	}
			
}
