package org.openxava.converters;


/**
 * En java un valores-posibles y en la base de datos un número
 * entero cuyo primer valor es 0
 * 
 * @author Javier Paniza
 */
public class ValidValuesBase0Converter implements IConverter {

	public Object toDB(Object o) throws ConversionException {
		if (o == null) return new Integer(-1);
		if (!(o instanceof Integer)) {		
			throw new ConversionException("conversion_db_integer_excepted");
		}
		
		int valor = ((Integer) o).intValue();				
		return new Integer(valor - 1);		
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Integer(0);
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		
		int valor  = ((Number) o).intValue();		
		return new Integer(valor + 1);
	}
	
}
