package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Ana Andres 
 */
public class NoConversionConverter implements IConverter{

	private Log log = LogFactory.getLog(NoConversionConverter.class);
	
	public Object toJava(Object o) throws ConversionException {
		return o;
	}

	public Object toDB(Object o) throws ConversionException {
		return o;
	}

}
