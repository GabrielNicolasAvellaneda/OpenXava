package org.openxava.ejbx;

import javax.naming.*;


/**
 * Encuentra un recurso a partir de un nombre. <p>
 * Es de uso genérico, y más fácil de implementar que
 * el de JNDI.<br>
 * Usa una excepción remota por si se desea implementar por
 * un objeto remoto.<br>
 *
 * @version 00.02.09
 * @author  Javier Paniza
 */

public interface IContext {

  /**
   * Busca un recurso a partir de un nombre. <br>
   * El objeto devuelto puede ser de cualquier tipo.<br>
   *
   * @exception NamingException  Si no es posible encontrar el recurso
   *                             (incluido problemas de sistema).
   */
  Object lookup(String nombre) throws NamingException;
  
  void close() throws NamingException;
}
