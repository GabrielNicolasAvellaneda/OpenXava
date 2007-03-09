package org.openxava.converters;





/**
 * In java valid-values and in database a integer number 
 * whose first value is 0. <p>
 * 
 * @author Javier Paniza
 */
public class ValidValuesBase0Converter implements IConverter {

	
	
	public Object toDB(Object o) throws ConversionException {
		if (o == null) return new Integer(-1);
		if (!(o instanceof Integer)) {		
			throw new ConversionException("conversion_db_integer_expected");
		}
		
		int value = ((Integer) o).intValue();				
		return new Integer(value - 1);		
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Integer(0);
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		
		int value  = ((Number) o).intValue();		
		return new Integer(value + 1);
	}
	
}
