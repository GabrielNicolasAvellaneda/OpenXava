package org.openxava.application.meta;


import java.util.*;

import org.openxava.util.*;
import org.openxava.util.meta.*;





/**
 * @author Javier Paniza
 */
public class MetaApplication extends MetaElement implements java.io.Serializable {


	private Map metaModulos = new HashMap();
	
	
	/**
	 * 
	 * @param nuevo Nunca nulo
	 */
	public void addMetaModule(MetaModule nuevo) {
		metaModulos.put(nuevo.getName(), nuevo);
		nuevo.setMetaApplication(this);
	}
	
	
	/**
	 * 
	 * @exception XavaException  Cualquier problema. 
	 * @return de <tt>Modulo</tt>. Nunca nulo.
	 */
	public Collection getMetaModules() throws XavaException {
		return metaModulos.values();
	}
	
	/**
     * @exception ElementNotFoundException
	 */
	public MetaModule getMetaModule(String nombre) throws ElementNotFoundException, XavaException {
		MetaModule result = (MetaModule) metaModulos.get(nombre);
		if (result == null) {
			throw new ElementNotFoundException(
				"El modulo " + nombre + " no está definido");
		}
		return result;
	}
	
	public String toString() {
		return "Aplicacion: " + getName(); 
	}

	public String getId() {
		return getName();
	}
	
}


