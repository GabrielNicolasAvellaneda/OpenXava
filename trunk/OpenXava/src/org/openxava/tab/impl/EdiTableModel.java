package org.openxava.tab.impl;

import java.util.*;

/**
 * Implementación de un {@link IEdiTableModel}. <p>
 *
 * Añade algunos métodos útiles para configurar el modelo.
 * Los datos se guardan internamente en un <code>Vector</code>.
 *
 * @version 00.03.17
 * @author  Javier Paniza
 */

public class EdiTableModel extends DefaultTableModel implements IEdiTableModel {

  private Vector tipos = new Vector();

  public EdiTableModel() {
  }
  //TODO: Override this javax.swing.table.DefaultTableModel method
  public void addColumn(Object titulo) {
	addColumn(titulo, (Class) null);
  }
  /**
   * Añade una nueva columna al modelo. <br>
   * @param titulo  Encabezado de la columna
   * @param tipo    Tipo de dato almacenado en la columna. Si es <code>null</code>
   *                se asume <code>java.lang.Object</code>
   */
  public void addColumn(Object titulo, Class tipo) {
	super.addColumn(titulo);
	if (tipo == null) tipo = java.lang.Object.class;
	tipos.add(tipo);
  }

  public void addRow() {
	super.addRow(new Object[getColumnCount()]);
  }

  public Class getColumnClass(int columna) {
	return (Class) tipos.elementAt(columna);
  }

  public void insertRow(int fila) {
	super.insertRow(fila, new Object[getColumnCount()]);
  }

  public void removeRows(int[] filas) {
	for (int i=0; i<filas.length; i++) {
	  removeRow(filas[i]);
	}
  }
}
