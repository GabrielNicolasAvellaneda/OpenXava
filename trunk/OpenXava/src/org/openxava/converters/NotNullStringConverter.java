package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class NotNullStringConverter implements IConverter {

	private static Log log = LogFactory.getLog(NotNullStringConverter.class);
	
	public Object toJava(Object o) throws ConversionException {
		return notNull(o);
	}

	public Object toDB(Object o) throws ConversionException {		
		return notNull(o);
	}

	private String notNull(Object o) throws ConversionException {
		if (o == null) return "";
		return o.toString();
	}
	
}
