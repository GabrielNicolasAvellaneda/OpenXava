package org.openxava.tab.impl;

import java.rmi.*;

/**
 * Algo que tiene un {@link IXTableModel}. <p>
 *
 * @version 00.01.28
 * @author  Javier Paniza
 */

public interface IWithXTableModel {

  /**
   * La <code>TableModel</code> que se puede obtener. <br>
   */
  IXTableModel getTable() throws RemoteException;
}
