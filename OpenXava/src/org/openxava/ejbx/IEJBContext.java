package org.openxava.ejbx;

import java.security.*;

import org.openxava.util.*;


/**
 * Encuentra los recursos t�picos para implantar un EJB. <br>
 * Aisla diferencias entre EJB 1.0 y EJB 1.1, diferencias entre
 * diferentes EJB 1.0, y suaviza el API de EJB 1.1.<br>
 * Se aconseja usar un <code>IEJBContext</code> frente al acceso directo de
 * recursos, esto aumentar� la portabilidad del bean.<br>
 * Los objetos de este tipo se obtendr�n mediante {@link EJBContextFactory}.<br>
 *
 * El m�todo {@link IConnectionProvider#getConnection()}
 * sin argumentos, obtendra la conexi�n de una propiedad del
 * bean llamada DATA_SOURCE que apunte a una fuente de datos v�lida.
 * Es importante seguir esta norma para asegurar la compatibilidad del c�digo
 * del bean entre diferentes versiones.<br>
 * Tambi�n se puede establecer la conexi�n por defecto mediante c�digo usando
 * el m�todo {@link IConnectionProvider#setDefaultDataSource}
 * <h4>Notas de dise�o</h4>
 * Otra posibilidad hubiera sido utilizar un <i>lookup</i> que funcionara
 * al estilo EJB 1.1, es decir buscando todos los recursos mediante
 * JNDI. Esto es un poco m�s aparatoso para el programador
 * final, ya que hay que usar subcontextos y m�s cantidad de moldes.
 * Tambi�n es m�s aparatoso para implementar.
 *
 *
 * @version 00.10.27
 * @author  Javier Paniza
 */

 /*
 00.02.09  Creaci�n
 00.05.24  Se quita RemoteException de getContext()
 00.10.27  Se elimina getContext
 */

public interface IEJBContext extends IConnectionProvider {

  /** Como <code>EJBContext.getCallerPrincipal</code> en EJB 1.1. */
  Principal getCallerPrincipal();
  /**
   * Valor de propiedad a partir del nombre.<br>
   * Devuelve <code>null</code> si la propiedad no
   * existe o no puede conseguirla por alguna cosa.<br>
   * Si la propiedad existe pero no se le ha especificado
   * un valor devuelve cadena vac�a.<br>
   */
  String getProperty(String nombre);
  /** Como <code>EJBContext.isCallerInRole</code> en EJB 1.1. */
  boolean isCallerInRole(String roleName);
}
