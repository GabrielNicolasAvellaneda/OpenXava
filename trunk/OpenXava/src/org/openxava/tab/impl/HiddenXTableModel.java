package org.openxava.tab.impl;

import java.util.*;

/**
 * Decora un {@link IXTableModel} para ocultar columnas. <p>
 *
 * El uso es muy sencillo, un ejemplo:
 * <pre>
 * int [] ocultas = {0, 1};
 * IXTableModel decorada = new OcultaXTableModel(original, ocultas);
 * </pre>
 * Después de esto podemos usar <tt>decorada</tt> la cual presenta
 * los datos de <tt>original</tt> pero sin visualizar las columnas
 * 1 y 2.<br>
 *
 * @version 00.06.14
 * @author  Javier Paniza
 */
public class HiddenXTableModel extends XTableModelDecoratorBase {

  private int [] indices;
  private int columnCount;

  /**
   * @param aDecorar  <tt>TableModel</tt> a decorar ocultando columnas.
   * @param indiceOcultas  Indice de las columnas a ocultar, si es <tt>null</tt>
   *                       no se oculta ninguna.
   * @exception IllegalArgumentException  Si <tt>aDecorar == null</tt>.
   */
  public HiddenXTableModel(IXTableModel aDecorar, int [] indiceOcultas) {
	super(aDecorar);
	// assert(aDecorar);
	int nc = aDecorar.getColumnCount();
	Vector original = new Vector();
	int i;
	// ponemos los índices originales
	for (i=0; i<nc; i++) {
	  original.add(new Integer(i));
	}
	// quitamos los ocultos, si procede
	if (indiceOcultas != null) {
	  for (i=0; i<indiceOcultas.length; i++) {
		original.remove(new Integer(indiceOcultas[i]));
	  }
	}
	// Iniciamos columnCount e indices
	columnCount = original.size();
	indices = new int[columnCount];
	for (i=0; i<columnCount; i++) {
	  indices[i] = ((Integer) original.get(i)).intValue();
	}
  }

  public Class getColumnClass(int columnIndex) {
	return super.getColumnClass(toFilaOriginal(columnIndex));
  }

  public int getColumnCount() {
	return columnCount;
  }

  public String getColumnName(int columnIndex) {
	return super.getColumnName(toFilaOriginal(columnIndex));
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
	return super.getValueAt(rowIndex, toFilaOriginal(columnIndex));
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
	return super.isCellEditable(rowIndex, toFilaOriginal(columnIndex));
  }

  public void setValueAt(Object valor, int rowIndex, int columnIndex) {
	super.setValueAt(valor, rowIndex, toFilaOriginal(columnIndex));
  }
  // Convirte el número de la fila visualizada en el
  // número de la fila del tablaModel decorado
  private int toFilaOriginal(int filaVisible) {
	return indices[filaVisible];
  }
}
