package org.openxava.util;

import java.rmi.*;
import java.util.*;

import org.openxava.validators.*;


/**
 * B�sicamente permite actualizar y obtener un cojunto de propiedades
 * de un solo golpe.<br>
 *
 * @author  Javier Paniza
 */

public interface IPropertiesContainer {

  /**
   * Permite obtener los valores de un conjunto de propiedades de una vez. <p>
   *
   * @param propiedadesAReplica  Nombres de las propiedades a replicar, separadas
   *                             por dos puntos (:). Las propiedades han de
   *                             existir en el objeto receptor.
   * @return Mapa con <tt>String nombrePropiedad:Object valor</tt>. Nunca ser� nulo.
   * @exception RemoteException  Alg�n problema de sistema u otro problema inesperado.
   */
  Map executeGets(String propiedadesAReplicar) throws RemoteException;
  /**
   * Actualiza las propiedades indicadas de un solo golpe. <p>
   *
   * @param propiedadesAActualizar Mapa con <tt>String nombrePropiedad:Object valor</tt>.
   *                               Nulo se toma como un mapa vac�o.
   * @exception ValidationException  Alg�n problema de validaci�n de los datos que se quieren asignar.
   * @exception RemoteException  Alg�n problema de sistema u otro problema inesperado.
   */
  void executeSets(Map propiedadesAActualizar) throws ValidationException, RemoteException;  
}
