package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

/**
  Interface que permite buscar entre varias busquedas predefinidas
y también especificando una condición concreta. <p>

  Puede ser implementado por un controlador. Cuando se realice
una busqueda el controlador implementador se encargará de actualizar
el modelo si procede.<br>

  Se usan excepciones típicas de EJB para facilitar una
posible implementación remota.<br>

  @author  Javier Paniza
*/

public interface ISearch {

  /**
   * Se índica el índice de la busqueda que desea realizar y la clave. <br>
   * Si no se encuentra ningún objeto se generará una colección o secuencia
   * vacía, pero no se lanza ninguna excepción. <br>
   * @exception FinderException  Si hay algún problema de lógica.
   * @exception RemoteException  Si hay algún problema de sistema.
   * @exception IndexOutOfBoundsException  Si se especifica el índice de una
   *                                       consulta que no existe.
   */
  void search(int indice, Object clave) throws FinderException, RemoteException;

  /**
   * Se índica la condición de la busqueda que desea realizar y la clave. <br>
   * Si no se encuentra ningún objeto se generará una colección o secuencia
   * vacía, pero no se lanza ninguna excepción. <br>
   * @exception FinderException  Si hay algún problema de lógica.
   * @exception RemoteException  Si hay algún problema de sistema.
   */  
  void search(String condicion, Object clave) throws FinderException, RemoteException;
}
