package org.openxava.tab.impl;

import javax.ejb.*;
import javax.swing.table.*;


/**
 * Un <code>TableModel</code> con un objeto (normalmente de negocio)
 * asociado a cada fila. <p>
 *
 * @version 00.04.08
 * @author  Javier Paniza
 */

 /*
 00.01.27  Creación
 00.04.08  Se quita la RemoteException para hacer compatible con CORBA
 */

public interface IObjectTableModel extends TableModel {

  /**
   * Devuelve el objeto asociado a la fila indicada. <br>
   * @exception FinderException Si hay algún problema al intentar buscar
   *                            el objeto, p. ej. que el objeto no exista,
   *                            la fila indicada no tenga objeto asociado o
   *                            incluso un problema de sistema.
   */
  Object getObjectAt(int rowIndex) throws FinderException;
}
