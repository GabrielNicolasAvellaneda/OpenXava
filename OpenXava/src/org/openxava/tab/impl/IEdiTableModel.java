package org.openxava.tab.impl;

import javax.swing.table.*;

/**
 * <code>TablaModel</code> editable. <p>
 * O dicho de otra forma un I<b>EdiTable</b>Model.<br>
 *
 * @version 00.03.16
 * @author  Javier Paniza
 */

public interface IEdiTableModel extends TableModel {

  /** A�ade una fila al final. */
  void addRow();
  /** Inserta una fila en la posici�n indicada. */
  void insertRow(int fila);
  /** Borra la fila de la posici�n indicada. */
  void removeRow(int fila);
  /** Borra las filas de las posiciones indicada. */
  void removeRows(int [] filas);
}
