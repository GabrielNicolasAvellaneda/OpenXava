package org.openxava.converters;



/**
 * En java un int y en la base de datos un Number del tipo que sea.
 * 
 * @author Javier Paniza
 */
public class IntegerNumberConverter implements IConverter {
	
	private final static Integer CERO = new Integer(0);

	public IntegerNumberConverter() {
		super();		
	}

	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		return o==null?CERO:o;
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Integer(0);				
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		return new Integer(((Number) o).intValue());
	}
			
}
