package org.openxava.ejbx;

import javax.ejb.*;

/**
 * Añade la posibilidad de inicializar a un {@link IEJBContext}. <p>
 *
 * Es un interfaz de conveniencia para la implementación, si se
 * hace una clase que queramos usar como <code>IEJBContext</code>
 * debe implementar esté interfaz, aunque el usuario final no
 * conozca le existencia del mismo.<br>
 * También es importante que la clase implementadorá tenga un
 * constructor por defecto, en el que no ha de hacerse nada.<br>
 *
 * @version 00.02.09
 * @author  Javier Paniza
 */

public interface IEJBContextInit extends IEJBContext {

  /**
   * Establece un <code>EJBContext</code> necesario para la implantación. <br>
   * Será necesario llamar a este método para que el <code>IEJBContext</code> pueda
   * utilizarse.<br>
   */
  void setEJBContext(EJBContext ejbContext);
}
