package org.openxava.converters;

import org.openxava.util.*;


/**
 * Un array de bytes (<tt>byte []</tt>) que en la db se guardará
 * como un objeto de tipo <tt>org.openxava.util.Photo</tt>. <p>
 * 
 * Util para guardar fotos en hypersonic, que no admite <tt>byte</tt>,
 * ni <tt>Blob</tt>, pero sí <tt>Object</tt>.<br>
 * 
 * @author Javier Paniza
 */
public class ArrayBytesPhotoConverter implements IConverter {


	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof byte [])) {		
			throw new ConversionException("conversion_db_byte_array_expected");
		}
		byte [] f = (byte []) o;
		return new Photo(f);
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof Photo)) {		
			throw new ConversionException("conversion_java_photo_expected");
		}
		return ((Photo) o).data;
	}
		
}
