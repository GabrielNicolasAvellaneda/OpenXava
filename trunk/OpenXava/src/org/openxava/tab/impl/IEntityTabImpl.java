package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

/**
 * Interface que facilita una <b>impl</b>ementación remota de
 * un {@link IEntityTab}. <p>
 *
 * Añade métodos que no serán usados nunca por el usuario final
 * del componente, pero que tienen que estar disponible de
 * forma pública y remota para poder implementar {@link IXTableModel}
 * de forma remota.<br>
 *
 * @version 0.2.17
 * @author  Javier Paniza
 */

/*
00.01.27  Creación
00.02.17  Se divide en IEntityTab y IEntidadImpl
*/


public interface IEntityTabImpl extends IEntityTab, ITabProvider
{

  /**
   * Busca una entidad concreta a partir de una clave. <br>
   * Esta clave se suele obtener de las columnas de la
   * tabla.<br>
   * Este método es un método de utilidad normalmente llamado por
   * la tabla para implementar <code>getObjectAt</code>. No será
   * tipico que el programador de aplicaciones lo llame directamente.
   *
   * @exception FinderException  Si hay algún problema de lógica.
   * @exception RemoteException  Si hay algún problema de sistema.
   */
  Object findEntity(Object [] clave) throws FinderException, RemoteException;
}
