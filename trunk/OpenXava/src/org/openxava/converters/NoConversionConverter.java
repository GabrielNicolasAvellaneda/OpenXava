package org.openxava.converters;

/**
 * Created on 20/10/2004 (10:43:16 AM)
 * @author Ana Andres
 * 
 */
public class NoConversionConverter implements IConverter{

	public Object toJava(Object o) throws ConversionException {
		return o;
	}

	public Object toDB(Object o) throws ConversionException {
		return o;
	}

}
