package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

/**
 * Interface que facilita una <b>impl</b>ementaci�n remota de
 * un {@link IEntityTab}. <p>
 *
 * A�ade m�todos que no ser�n usados nunca por el usuario final
 * del componente, pero que tienen que estar disponible de
 * forma p�blica y remota para poder implementar {@link IXTableModel}
 * de forma remota.<br>
 *
 * @version 0.2.17
 * @author  Javier Paniza
 */

/*
00.01.27  Creaci�n
00.02.17  Se divide en IEntityTab y IEntidadImpl
*/


public interface IEntityTabImpl extends IEntityTab, ITabProvider
{

  /**
   * Busca una entidad concreta a partir de una clave. <br>
   * Esta clave se suele obtener de las columnas de la
   * tabla.<br>
   * Este m�todo es un m�todo de utilidad normalmente llamado por
   * la tabla para implementar <code>getObjectAt</code>. No ser�
   * tipico que el programador de aplicaciones lo llame directamente.
   *
   * @exception FinderException  Si hay alg�n problema de l�gica.
   * @exception RemoteException  Si hay alg�n problema de sistema.
   */
  Object findEntity(Object [] clave) throws FinderException, RemoteException;
}
