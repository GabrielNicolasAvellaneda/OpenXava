package org.openxava.util;


/**
 * Excepciones lanzadas por {@link PropertiesManager}. <p>
 *
 * @author  Javier Paniza
 */

public class PropertiesManagerException extends Exception {


  public PropertiesManagerException() {
	  super("Imposible manejar propiedades");
  }  
  public PropertiesManagerException(String mensaje) {
	super(mensaje);
  }
}
