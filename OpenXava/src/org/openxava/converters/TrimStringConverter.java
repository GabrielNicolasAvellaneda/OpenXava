package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class TrimStringConverter implements IConverter {

	private Log log = LogFactory.getLog(TrimStringConverter.class);
	
	public Object toJava(Object o) throws ConversionException {
		return trim(o);
	}

	public Object toDB(Object o) throws ConversionException {		
		return trim(o);
	}

	private String trim(Object o) throws ConversionException {
		if (o == null) return "";
		if (!(o instanceof String)) {
			throw new ConversionException("conversion_trim_error");		
		}		
		return ((String) o).trim();
	}
	
}
