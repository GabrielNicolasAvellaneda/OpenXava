package org.openxava.tab.impl;

import java.util.*;

import javax.swing.table.*;

/**
 * <code>TableModel</code> en el que se pueden poner y coger todos
 * los datos en formato <code>Vector</code>. <p>
 *
 * El tipo de dato de cada posici�n del vector depende del <code>TableModel</code>
 * concreto, pero t�picamente ser� un array de objetos (uno por columna).<br>
 * La asignaci�n del vector es por referencia (no se hace copia), por lo
 * que si se obtiene el vector y se cambia se est� cambiando el modelo, por supuesto,
 * si se hace esto es necesario lanzar despu�s las notificaciones de cambio del
 * modelo que apliquen.<br>
 *
 * @version 00.04.17
 * @author  Javier Paniza
 */

public interface IVectorTableModel extends TableModel {

  /**
   * Vector con los datos contenidos en el modelo. <br>
   *
   * @return <code>[!= null]</code>
   */
  Vector getVector();
  /**
   * Establece un vector con los datos del modelo. <br>
   *
   * @param vector  Si es <code>null</code> se establece como si fuera
   *                un vector vac�o.
   */
  void setVector(Vector vector);
}
