package org.openxava.converters;

import org.openxava.util.*;


/**
 * En java un valores-posibles y en la base de datos una letra
 * correspondiente a la posición de la cadena que hay
 * en la propiedad 'letters'.
 * 
 * @author Javier Paniza
 */
public class ValidValuesLetterConverter implements IConverter {

	private String letters;

	public Object toDB(Object o) throws ConversionException {
		if (!(o instanceof Integer)) {		
			throw new ConversionException("conversion_db_integer_excepted");
		}
		assertLetters();
		int valor = ((Integer) o).intValue();
		if (valor == 0) return "";
		try {
			return String.valueOf(getLetters().charAt (valor - 1));		
		}
		catch (IndexOutOfBoundsException ex) {
			throw new ConversionException("conversion_db_valid_values", new Integer(valor), getLetters());
		}
	}
	
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return new Integer(0);
		if (!(o instanceof String)) {		
			throw new ConversionException("conversion_java_string_expected");
		}
		assertLetters();
		String valor  = (String) o;
		if (Is.emptyString(valor)) return new Integer(0);
		int idx = getLetters().indexOf(valor);
		if (idx < 0) {
			throw new ConversionException("conversion_java_valid_values", valor,  getLetters());
		}
		return new Integer(idx + 1);
	}
	
	private void assertLetters() throws ConversionException {
		if (Is.emptyString(getLetters())) {
			throw new ConversionException("conversion_valid_values_letters_required", getClass().getName());
		}
	}

	public String getLetters() {
		return letters;
	}

	public void setLetters(String string) {
		letters = string;
	}

}
