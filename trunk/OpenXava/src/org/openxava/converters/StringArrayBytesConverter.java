/*
 * Created on 19-may-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.openxava.converters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.XavaResources;

/**
 * @author Luis Miguel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringArrayBytesConverter implements IConverter {
    
	private static Log log = LogFactory.getLog(StringArrayBytesConverter.class);
	
	public Object toJava(Object o) throws ConversionException {	    
    	if (o == null) return "";
    	log.info("toJava: o.getClass():" + o.getClass().getName());
	    if (!(o instanceof byte[])) {		
			throw new ConversionException("conversion_java_blob_expected");
		}
		byte[] b = (byte[]) o;
		try {
			return new String(b);
		}catch (Exception e){
			log.error(XavaResources.getString("blob_to_array_warning"), e);
			return "";
		}
		
	}
	


public Object toDB(Object o) throws ConversionException {
    return o==null?null:o.toString().getBytes();
    //StringValue value = new StringValue(o==null?"":o.toString());
    //return value;
}
}
