package org.openxava.util;

import java.rmi.*;


/**
 * Cualquier cosa que atrape recurso y tenga que liberarlos. <p>
 *
 * Es una aproximación para trabajar con objetos que tienen
 * que atrapar y liberar recursos (conexiones a objetos remotos,
 * bases de datos, etc). Los recursos se obtienen bajo demanda
 * (<i>lazy initialization</i>) y cuando se quieran liberar se llama
 * al método {@link #liberar}.<br>
 * Podemos usar otra aproximación con el interface {@link IIniciarParar}.<p>
 *
 * Se usan excepciones remotas por si se quiere
 * implementar por un objeto remoto, aunque esto
 * no es preceptivo.<br>
 *
 * @version 00.05.23
 * @author  Javier Paniza
 */

public interface ILiberate {

  /**
   * Libera todos los recursos atrapados. <br>
   * Es importante llamar a este método cuando ya no necesitemos más el objeto,
   * sin embargo puede ser llamado en cualquier momento de la vida del objeto,
   * y después de llamarse el objeto sigue siendo utilizable.<br>
   *
   * @exception RemoteException  Si hay algún problema al liberar los recursos.
   */
  void liberate() throws RemoteException;
}
