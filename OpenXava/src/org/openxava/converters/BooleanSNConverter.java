package org.openxava.converters;


/**
 * En java un boolean y en la base de datos una cadena
 * que puede ser 'S' o 'N'.
 * 
 * @author Javier Paniza
 */
public class BooleanSNConverter implements IConverter {

	/**
	 * Constructor for ConversorBooleanSN.
	 */
	public BooleanSNConverter() {
		super();
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Boolean)) {		
			throw new ConversionException("conversion_db_boolean_expected");
		}
		return booleanToString(((Boolean) o).booleanValue());
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return Boolean.FALSE;
		if (!(o instanceof String)) {		
			throw new ConversionException("conversion_java_string_expected");
		}
		return new Boolean(stringToBoolean((String) o));
	}
	
	
	public static String booleanToString(boolean valor) {
		return valor?"S":"N";
	}
	
	public static boolean stringToBoolean(String valor) {
		if (valor == null) return false;
		return valor.equalsIgnoreCase("S");
	}


}
