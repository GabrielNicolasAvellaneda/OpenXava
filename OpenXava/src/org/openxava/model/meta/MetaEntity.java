package org.openxava.model.meta;


import java.util.*;

import org.openxava.util.*;
import org.openxava.util.meta.*;



/**
 * 
 * 
 * @author Javier Paniza
 */
abstract public class MetaEntity extends MetaModel {
	

	
	/**
	 * Comentario de constructor Entidad.
	 */
	public MetaEntity() {
		super();
	}
	
	/**
	 * @return Los nombres de los los campos clave. String.
	 */
	abstract public Collection getKeyFields() throws XavaException;

	
	public boolean isKey(String nombrePropiedad) throws XavaException {	
		if (isGenerateXDocLet() && super.isKey(nombrePropiedad)) return true; 	
		return getKeyFields().contains(nombrePropiedad);		
	}

	/**
	 * Del componente
	 * @see MetaElement#getName()
	 */
	public String getName() {
		return getMetaComponent().getName();
	}
	
	/**
	 * Si tiene campos claves (<tt>getCamposClave</tt>) que no
	 * son propiedades (y por ende no coinciden con <tt>getNombresPropiedadesClave())</tt>
	 */
	public boolean hasHiddenKeys() throws XavaException {		
		return !getKeyPropertiesNames().containsAll(getKeyFields());
	}
	
	public String getId() {
		return getName();
	}



}