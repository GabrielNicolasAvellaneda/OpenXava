package org.openxava.converters;



/**
 * En java un boolean y en la base de datos una cadena
 * que puede ser 1 o 0.
 * 
 * @author Javier Paniza
 */
public class Boolean01Converter implements IConverter {

	/**
	 * Constructor for ConversorBooleanSN.
	 */
	public Boolean01Converter() {
		super();
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Boolean)) {		
			throw new ConversionException("conversion_db_boolean_expected");
		}
		return new Integer(booleanToInt(((Boolean) o).booleanValue()));
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return Boolean.FALSE;
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		return new Boolean(intToBoolean(((Number) o).intValue()));
	}
	
	
	public static int booleanToInt(boolean valor) {
		return valor?1:0;
	}
	
	public static boolean intToBoolean(int valor) {
		return valor != 0;
	}

}
