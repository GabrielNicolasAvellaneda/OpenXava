package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * In java a long and in database a Number of any type.
 * 
 * @author Javier Paniza
 */
public class LongNumberConverter implements IConverter {
	
	private final static Long CERO = new Long(0);
	private static Log log = LogFactory.getLog(LongNumberConverter.class);

	public Object toDB(Object o) throws ConversionException {
		return o==null?CERO:o;
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Long(0);
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		return new Long(((Number) o).longValue());
	}
			
}
