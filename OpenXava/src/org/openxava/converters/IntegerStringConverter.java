package org.openxava.converters;



/**
 * In java a int and in database a String.
 * 
 * @author Javier Paniza
 */
public class IntegerStringConverter implements IConverter {
	
	private final static Integer CERO = new Integer(0);

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
			ex.printStackTrace();			
			throw new ConversionException("conversion_error");
		}
	}
			
}
