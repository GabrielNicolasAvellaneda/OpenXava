package org.openxava.ejbx;

import java.lang.reflect.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.util.*;
import org.openxava.validators.*;


/**
 * Clase de implementación base para un <i>EntityBean</i> que puede ser replicado. <p>
 *
 * Un ejemplo de <i>EntityBean</i> replicable:
 * <pre>
 * public class PersonaBean extends EJBReplicableBase {
 *
 *   public String nombre;
 *   public int edad;
 *
 *   public PersonaPK ejbCreate(Map propiedades) {
 *     ejecutarSets(propiedades);
 *     return null;
 *   }
 *
 *   public void ejbPostCreate(Map propiedades) {
 *   }
 *
 *   public String getNombre() {
 *     return nombre;
 *   }
 *
 *   public void setNombre(String nombre) {
 *     this.nombre = nombre;
 *   }
 *
 *   public String getNombre() {
 *     return nombre;
 *   }
 *
 *   public void setNombre(String nombre) {
 *     this.nombre = nombre;
 *   }
 *
 * }
 * </pre>
 *
 * @version 00.08.30
 * @author Javier Paniza
 */

public class EJBReplicableBase extends EntityBase {

  private PropertiesManager manejadorPropiedades = new PropertiesManager(this);


/**
  * Implementa {@link EJBReplicable#ejecutarGets}. <p>
  */
public Map executeGets(String propiedadesAReplicar) {
	try {
		return manejadorPropiedades.executeGets(propiedadesAReplicar);
	}
	catch (PropertiesManagerException ex) {
		throw new EJBException(ex.getMessage());
	}
	catch (InvocationTargetException ex) {
		ex.getTargetException().printStackTrace();
		throw new EJBException(XavaResources.getString("ejb_get_properties_error", ex.getTargetException().getMessage()));
	}
}    

/**
 * Implementa {@link EJBReplicable#ejecutarSets}. <p>
 */
public void executeSets(Map propiedadesAActualizar) throws ValidationException {
	try {				
		manejadorPropiedades.executeSets(propiedadesAActualizar);
	}
	catch (PropertiesManagerException ex) {
		throw new EJBException(ex.getMessage());
	}
	catch (InvocationTargetException ex) {
		Throwable causa = ex.getTargetException();
		if (causa instanceof ValidationException) {
			throw (ValidationException) causa;
		}
		else {
			causa.printStackTrace();
			throw new EJBException(XavaResources.getString("ejb_set_properties_error", ex.getTargetException().getMessage()));
		}
	}	  	
}    


}
