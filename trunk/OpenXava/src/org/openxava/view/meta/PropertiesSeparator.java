package org.openxava.view.meta;

import org.openxava.model.meta.*;

/**
 * Se usa dentro de una colecci�n de propiedades para 
 * indicar una separaci�n. <p>
 * 
 * Desciende de <tt>MetaPropiedad</tt> para poderse procesar por
 * cualquier m�todo que reciba <tt>MetaPropiedad</tt>.<br>
 * 
 * @author Javier Paniza
 */

public class PropertiesSeparator extends MetaProperty {
	
	public static final PropertiesSeparator INSTANCE = new PropertiesSeparator(); 
	
	private PropertiesSeparator() {
	}
	
	/**
	 * @see org.openxava.util.meta.MetaElement#getName()
	 */
	public String getName() {
		return "[SEPARADOR]";
	}
	
	public String getLabel() {
		return "";
	}

}
