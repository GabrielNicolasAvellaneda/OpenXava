package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

/**
  Interface que permite buscar entre varias busquedas predefinidas
y tambi�n especificando una condici�n concreta. <p>

  Puede ser implementado por un controlador. Cuando se realice
una busqueda el controlador implementador se encargar� de actualizar
el modelo si procede.<br>

  Se usan excepciones t�picas de EJB para facilitar una
posible implementaci�n remota.<br>

  @author  Javier Paniza
*/

public interface ISearch {

  /**
   * Se �ndica el �ndice de la busqueda que desea realizar y la clave. <br>
   * Si no se encuentra ning�n objeto se generar� una colecci�n o secuencia
   * vac�a, pero no se lanza ninguna excepci�n. <br>
   * @exception FinderException  Si hay alg�n problema de l�gica.
   * @exception RemoteException  Si hay alg�n problema de sistema.
   * @exception IndexOutOfBoundsException  Si se especifica el �ndice de una
   *                                       consulta que no existe.
   */
  void search(int indice, Object clave) throws FinderException, RemoteException;

  /**
   * Se �ndica la condici�n de la busqueda que desea realizar y la clave. <br>
   * Si no se encuentra ning�n objeto se generar� una colecci�n o secuencia
   * vac�a, pero no se lanza ninguna excepci�n. <br>
   * @exception FinderException  Si hay alg�n problema de l�gica.
   * @exception RemoteException  Si hay alg�n problema de sistema.
   */  
  void search(String condicion, Object clave) throws FinderException, RemoteException;
}
