package org.openxava.util;

/**
 * Envuelve una array de bytes para poder grabar fotos
 * en bases de datos que no soporten <tt>byte []</tt> pero sí
 * <tt>java.lang.Object</tt>. <p>
 * 
 * @author Javier Paniza
 */
public class Photo implements java.io.Serializable {

	public byte[] data;

	public Photo(byte[] datos) {
		this.data = datos;
	}

}