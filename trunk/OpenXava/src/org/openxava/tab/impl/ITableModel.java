package org.openxava.tab.impl;

import javax.swing.table.*;

/**
 * A�ade al <code>TableModel</code> de la swing la
 * posiblidad de a�adir, quitar y modificar l�neas. <p>
 *
 * En el modelo no habr� filas con valor <code>null</code>,
 * aunque si puede haber celdas que valgan <code>null</code>.
 * Cuando se a�adan, inserten o modifiquen celdas se puede enviar
 * <code>null</code> pero la implementaci�n almacenar� un <i>array</i>
 * vac�o o equivalente en su lugar.<br>
 *
 * @version 00.04.14
 * @author  Javier Paniza
 */

public interface ITableModel extends TableModel {

  /**
   * A�ade la fila enviada al final.
   *
   * @param rowData Datos a a�adir, si vale <code>null</code> se a�ade
   *                una fila cuyos datos son <code>null</code>, pero la
   *                fila en s� no llega a ser <code>null</code>.
   */
  void addRow(Object[] rowData);
  /**
   * Devuelve un array con los datos de la fila indicada.
   *
   * @return <code>[!= null]</code>
   */
  Object [] getRow(int row);
  /**
   * Inserta la fila enviada en el lugar indicado.
   *
   * @param rowData Datos a insertar, si vale <code>null</code> se inserta
   *                una fila cuyos datos son <code>null</code>, pero la
   *                fila en s� no llega a ser <code>null</code>.
   */
  void insertRow(int row, Object[] rowData);
  /**
   * Borra todas las filas.
   */
  void removeAllRows();
  /**
   * Borra la fila de la posici�n indicada.
   */
  void removeRow(int row);
  /**
   * Establece nuevos valores en la fila indicada a
   * partir de los datos enviados.
   *
   * @param rowData Datos a establecer, si vale <code>null</code> se establece
   *                una fila cuyos datos son <code>null</code>, pero la
   *                fila en s� no llega a ser <code>null</code>.
   */
  void setRow(int row, Object[] rowData);
}
