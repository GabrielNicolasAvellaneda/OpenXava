package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

/**
 * TableModel eXtendido. <p>
 * Tiene la posibilidad de obtener un objeto por fila,
 * reiniciar la tabla y conocer el tamaño de la colección visualizada.<br>
 * Diseñado inicialmente para su uso en {@link IEntityTab}.<br>
 *
 * @version 00.01.27
 * @author  Javier Paniza
 */

public interface IXTableModel extends IObjectTableModel, IRefreshTableModel {

  // Incluido en IObjectTableModel, para no ser un interface abstracto
  Object getObjectAt(int rowIndex) throws FinderException;
  // Incluido en IRefrescarTableModel, para no ser un interface abstracto
  void refresh() throws TabException;
	void removeAllRows();
	/**
	 * Cantidad de objetos total representados por el table model. <p>
	 * 
	 * getRowCount() por su parte ofrece la cantidad de objetos cargados, no
	 * la total.
	 */
	int getTotalSize() throws RemoteException;

	
}
