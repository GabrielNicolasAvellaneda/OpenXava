package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class StringNotNullConverter implements IConverter {

	private Log log = LogFactory.getLog(StringNotNullConverter.class);
	
	public Object toJava(Object o) throws ConversionException {
	    if (o == null) return "";
		return o;
	}

	public Object toDB(Object o) throws ConversionException {
	    if (o == null) return "";
		return o;
	}
		
}
