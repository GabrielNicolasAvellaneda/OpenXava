package org.openxava.converters;


/**
 * En java un long y en la base de datos un Number del tipo que sea.
 * 
 * @author Javier Paniza
 */
public class LongNumberConverter implements IConverter {
	
	private final static Long CERO = new Long(0);	

	public LongNumberConverter() {
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
		if (o == null) return new Long(0);
		if (!(o instanceof Number)) {		
			throw new ConversionException("conversion_java_number_expected");
		}
		return new Long(((Number) o).longValue());
	}
			
}
