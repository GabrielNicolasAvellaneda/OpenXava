package org.openxava.ejbx;

import javax.ejb.*;

/**
 * A�ade la posibilidad de inicializar a un {@link IEJBContext}. <p>
 *
 * Es un interfaz de conveniencia para la implementaci�n, si se
 * hace una clase que queramos usar como <code>IEJBContext</code>
 * debe implementar est� interfaz, aunque el usuario final no
 * conozca le existencia del mismo.<br>
 * Tambi�n es importante que la clase implementador� tenga un
 * constructor por defecto, en el que no ha de hacerse nada.<br>
 *
 * @version 00.02.09
 * @author  Javier Paniza
 */

public interface IEJBContextInit extends IEJBContext {

  /**
   * Establece un <code>EJBContext</code> necesario para la implantaci�n. <br>
   * Ser� necesario llamar a este m�todo para que el <code>IEJBContext</code> pueda
   * utilizarse.<br>
   */
  void setEJBContext(EJBContext ejbContext);
}
