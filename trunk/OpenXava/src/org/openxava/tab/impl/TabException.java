package org.openxava.tab.impl;

/**
 * Excepción lanzada por algunas clases de este paquete. <p>
 *
 * Será lanzada para indicar la imposibilidad de realizar una
 * operación y no haya una excepción más específica.<br>
 * También se puede usar para indicar un error de sistema en clases
 * o interfaces no remotos (si usaramos RemoteException hariamos
 * código no compatible con CORBA).
 *
 * @version 00.04.08
 * @author  Javier Paniza
 */

public class TabException extends Exception {

  /**
   * Se crea a partir del mensaje indicado. <br>
   */
  public TabException(String mensaje) {
	super(mensaje);
  }
  /**
   * Se crea a partir de una excepción. <br>
   * No la anida, solo usa su mensaje.<br>
   */
  public TabException(Throwable ex) {
	super(ex.getLocalizedMessage());
  }
}
