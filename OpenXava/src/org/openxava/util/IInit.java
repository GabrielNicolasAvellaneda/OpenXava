package org.openxava.util;


/**
 * Con posibilidad de ser iniciado. <p>
 *
 * Este interfaz se penso inicialmente para usar
 * con {@link Factory}. Aunque puede ser usado
 * en otras situaciones, en donde creemos un objeto
 * de una clase desconocida y necesitemos iniciarlo.
 * Un ejemplo de posible uso:
 * <pre>
 * IInit obj = (IInit) miClase.newInstance();
 * obj.init("obj");
 * </pre>
 * Los objetos que implementen este interfaz generalmente
 * tendr�n un constructor por defecto, y la inicializaci�n
 * se har� en el m�todo {@link #init}.
 *
 * @version 00.02.10
 * @author  Javier Paniza
 */

public interface IInit {

  // Puede ser null, en ese caso funcionamiento dependiente del objeto
  /**
   * Inicializaci�n del objeto. <p>
   * La inicializaci�n deber�a ponerse en este m�todo, y
   * no en el constructor.<br>
   * Es posible especificar un nombre, as� podemos tener
   * diferentes configuraciones de un mismo tipo de
   * de objeto identificadas con un nombre. Este nombre
   * puede usarse para leer de un archivo de propiedades,
   * con la tradicionales sentencias if u otra t�cnica.<br>
   *
   * @param nombre Nombre del contexto a inicializar.
   * @exception InitException  Si hay alg�n problema al iniciar.
   */
  void init(String nombre) throws InitException;
}
