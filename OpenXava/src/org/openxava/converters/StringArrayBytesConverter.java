/*
 * Created on 19-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.openxava.converters;


import org.apache.commons.logging.*;
import org.openxava.util.XavaResources;

/**
 * @author Luis Miguel
 */
public class StringArrayBytesConverter implements IConverter {
	
	private static Log log = LogFactory.getLog(StringArrayBytesConverter.class);
    	
	public Object toJava(Object o) throws ConversionException {	    
    	if (o == null) return "";    	
	    if (!(o instanceof byte[])) {			    	
			throw new ConversionException("conversion_java_byte_array_expected");
		}
		byte[] b = (byte[]) o;
		try {
			return new String(b);
		}
		catch (Exception e){
			log.error(XavaResources.getString("byte_array_to_string_warning"), e);
			return "";
		}
		
	}
	
	public Object toDB(Object o) throws ConversionException {
	    return o==null?null:o.toString().getBytes();
	}
	
}
