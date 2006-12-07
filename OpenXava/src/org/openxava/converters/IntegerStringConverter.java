package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.XavaResources;

/**
 * In java an int and in database a String.
 * 
 * @author Javier Paniza
 */
public class IntegerStringConverter implements IConverter {
	
	private final static Integer CERO = new Integer(0);
	private static Log log = LogFactory.getLog(IntegerStringConverter.class);

	public Object toDB(Object o) throws ConversionException {
		return o==null?"0":o.toString();
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Integer(0);				
		if (!(o instanceof String)) {		
			throw new ConversionException("conversion_java_string_expected");
		}
		try {
			return new Integer((String) o);
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);			
			throw new ConversionException("conversion_error");
		}
	}
			
}
