package org.openxava.tab.impl;

/**
 * Excepci�n lanzada por algunas clases de este paquete. <p>
 *
 * Ser� lanzada para indicar la imposibilidad de realizar una
 * operaci�n y no haya una excepci�n m�s espec�fica.<br>
 * Tambi�n se puede usar para indicar un error de sistema en clases
 * o interfaces no remotos (si usaramos RemoteException hariamos
 * c�digo no compatible con CORBA).
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
   * Se crea a partir de una excepci�n. <br>
   * No la anida, solo usa su mensaje.<br>
   */
  public TabException(Throwable ex) {
	super(ex.getLocalizedMessage());
  }
}
