package org.openxava.tab.impl;

import javax.swing.table.*;

/**
 * Un <code>TablaModel</code> que puede refrescar los datos. <p>
 *
 * @version 00.04.08
 * @author  Javier Paniza
 */

 /*
 00.01.27  Creación
 00.04.08  Se cambia RemoteException por TabException para hacerlo
		   compatible con CORBA
 */

public interface IRefreshTableModel extends TableModel {

  /**
   * Vuelve a recargar los datos de la fuente de datos. <br>
   * @exception TabException  Si hay algún error de sistema.
   */
  void refresh() throws TabException;
}
