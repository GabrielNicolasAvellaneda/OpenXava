package org.openxava.converters;

/**
 * In java a String a a Blog in database. <p>
 * 
 * @author Javier Paniza
 */
public class StringBlobConverter implements IConverter {
	
	public class StringValue implements java.io.Serializable {
    	
   	public String data;
    	
   	StringValue(String data) {
   		this.data = data;
   	}
    	
  }
	
	public Object toJava(Object o) throws ConversionException {	    
		try{
			return o==null?"":((StringValue)o).data;
		}
		catch (Exception ex){
			ex.printStackTrace();
			return "";
		}
	}

	public Object toDB(Object o) throws ConversionException {
	    StringValue value = new StringValue(o==null?"":o.toString());
	    return value;
	}
		
}
