package org.openxava.converters;

import java.sql.*;




/**
 * Un array de bytes (<tt>byte []</tt>) que en la db se guardará
 * como un objeto de tipo <tt>Blob</tt>. <p>
 * 
 * Util para guardar fotos en as400 donde cualquier objeto que
 * dejemos en un campo BLOB se recupera como un <tt>java.sql.Blob</tt>.<br>
 * 
 * @author Javier Paniza
 */
public class ArrayBytesBlobConverter implements IConverter {

	
	/**
	 * @see org.openxava.converters.IConversorTipo#toDB(Object)
	 */
	public Object toDB(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof byte [])) {		
			throw new ConversionException("conversion_db_byte_array_expected");
		}		
		return o;
	}
	
	/**
	 * @see org.openxava.converters.IConversorTipo#toJava(Object)
	 */
	public Object toJava(Object o) throws ConversionException {
		if (o == null) return null;
		if (!(o instanceof Blob)) {		
			throw new ConversionException("conversion_java_blob_expected");
		}
		Blob b = (Blob) o;
		try {
			return b.getBytes(1l, (int) b.length());
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.err.println("¡ADVERTENCIA! Imposible convertir un Blob en un array de bytes. Asumimos nulo.");
			return null;
		}
	}
		
}
