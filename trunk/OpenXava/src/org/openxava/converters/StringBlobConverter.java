package org.openxava.converters;

import java.sql.Blob;



/**
 * @author Javier Paniza
 */
public class StringBlobConverter implements IConverter {
    public class StringValue implements java.io.Serializable {
    	
    	public String datos;
    	
    	StringValue(String datos) {
    		this.datos = datos;
    	}
    	
    }
	public Object toJava(Object o) throws ConversionException {
	    System.out.println("[StringBlobConverter.toJava:] o.getClass():" + o.getClass());
	    try{
	    return o==null?"":((StringValue)o).datos;
	    }catch (Exception ex){
	        ex.printStackTrace();
	        return "";
	    }

	}

	public Object toDB(Object o) throws ConversionException {
	    StringValue valor = new StringValue(o==null?"":o.toString());
	    return valor;
	}

	
	
}
