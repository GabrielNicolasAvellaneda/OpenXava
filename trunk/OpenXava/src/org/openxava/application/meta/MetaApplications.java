package org.openxava.application.meta;


import java.util.*;

import org.openxava.application.meta.xmlparse.*;
import org.openxava.util.*;




/**
 * 
 * @author Javier Paniza
 */
public class MetaApplications {
	
	private static Collection nombresAplicaciones;

	private static Map metaAplicaciones;
	
	/**
	 * Solo se ha de llamar desde dentro del parser.
	 * @throws XavaException
	 */
	public static void _addMetaApplication(MetaApplication aplicacion) throws XavaException {
		if (metaAplicaciones == null) {
			throw new XavaException("only_from_parse", "MetaApplications._addMetaApplication");
		}
		metaAplicaciones.put(aplicacion.getName(), aplicacion);
	}
	
	/**
	 * @return Colección de <tt>Aplicacion</tt>. Nunca nulo.
	 */
	public static Collection getMetaApplications() throws XavaException {
		if (metaAplicaciones == null) {
			configurar();
		}
		return metaAplicaciones.values();
	}
	
	private static void configurar() throws XavaException {
		metaAplicaciones = new HashMap();
		ApplicationParser.configurarAplicaciones();
	}
	
	/**
	 * 
	 * @param nombre java.lang.String
	 * @exception XavaException  Cualquier problema.
	 * @exception ElementNotFoundException
	 */
	public static MetaApplication getMetaApplication(String nombre) throws ElementNotFoundException, XavaException {
		if (metaAplicaciones == null) {
			configurar();
		}
		MetaApplication result = (MetaApplication) metaAplicaciones.get(nombre);
		if (result == null) {
			throw new ElementNotFoundException(
				"La aplicación " + nombre + " no está definida");
		}
		return result;
	}

	public static Collection getApplicationsNames() throws XavaException {
		if (nombresAplicaciones == null) {
			nombresAplicaciones = new ArrayList();
			Iterator it = getMetaApplications().iterator();
			while (it.hasNext()) {
				MetaApplication ap = (MetaApplication) it.next();
				nombresAplicaciones.add(ap.getName());
			}
		}
		return nombresAplicaciones;
	}
	
}
