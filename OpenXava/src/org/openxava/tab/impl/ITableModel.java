package org.openxava.tab.impl;

import javax.swing.table.*;

/**
 * Añade al <code>TableModel</code> de la swing la
 * posiblidad de añadir, quitar y modificar líneas. <p>
 *
 * En el modelo no habrá filas con valor <code>null</code>,
 * aunque si puede haber celdas que valgan <code>null</code>.
 * Cuando se añadan, inserten o modifiquen celdas se puede enviar
 * <code>null</code> pero la implementación almacenará un <i>array</i>
 * vacío o equivalente en su lugar.<br>
 *
 * @version 00.04.14
 * @author  Javier Paniza
 */

public interface ITableModel extends TableModel {

  /**
   * Añade la fila enviada al final.
   *
   * @param rowData Datos a añadir, si vale <code>null</code> se añade
   *                una fila cuyos datos son <code>null</code>, pero la
   *                fila en sí no llega a ser <code>null</code>.
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
   *                fila en sí no llega a ser <code>null</code>.
   */
  void insertRow(int row, Object[] rowData);
  /**
   * Borra todas las filas.
   */
  void removeAllRows();
  /**
   * Borra la fila de la posición indicada.
   */
  void removeRow(int row);
  /**
   * Establece nuevos valores en la fila indicada a
   * partir de los datos enviados.
   *
   * @param rowData Datos a establecer, si vale <code>null</code> se establece
   *                una fila cuyos datos son <code>null</code>, pero la
   *                fila en sí no llega a ser <code>null</code>.
   */
  void setRow(int row, Object[] rowData);
}
