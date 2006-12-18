package org.openxava.converters;






/**
 * In java a int and in database a Number of any type. <p>
 * 
 * @author Javier Paniza
 */
public class IntegerNumberConverter implements IConverter {
	
	private final static Integer CERO = new Integer(0);
	

	public Object toDB(Object o) throws ConversionException {
		return o==null?CERO:o;
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return CERO;				
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		return new Integer(((Number) o).intValue());
	}
			
}
