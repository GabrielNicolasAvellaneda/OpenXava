package org.openxava.tab.impl;

import java.rmi.*;



/**
 * Proveedor de datos para un <code>TableModel</code> o similar. <p>
 * Permite basicamente realizar consultas y después obtener los
 * datos de la consulta a trozos.<br>
 *
 * @version 0.1.27
 * @author  Javier Paniza
 */

public interface ITabProvider extends ISearch {
	
  /**
   * Obtiene el siguiente trozo de datos. <br>
   * Este método puede ser llamado por un <code>TableModel</code>
   * para obtener más datos a medida que los necesite.<br>
   */
  DataChunk nextChunk() throws RemoteException;
  
  /**
   * Cantidad de registro de la última consulta ejecutada.
   */
  int getResultSize() throws RemoteException; 
  
  /**
   * La siguientez ve que se llame a <tt>siguienteTrozo</tt>
   * devuelve el primert trozo y con datos recientes de la db. <p> 
   *  
   * @throws RemoteException
   */
  void reset() throws RemoteException;  
  
}
