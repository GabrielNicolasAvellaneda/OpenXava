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
 * tendrán un constructor por defecto, y la inicialización
 * se hará en el método {@link #init}.
 *
 * @version 00.02.10
 * @author  Javier Paniza
 */

public interface IInit {

  // Puede ser null, en ese caso funcionamiento dependiente del objeto
  /**
   * Inicialización del objeto. <p>
   * La inicialización debería ponerse en este método, y
   * no en el constructor.<br>
   * Es posible especificar un nombre, así podemos tener
   * diferentes configuraciones de un mismo tipo de
   * de objeto identificadas con un nombre. Este nombre
   * puede usarse para leer de un archivo de propiedades,
   * con la tradicionales sentencias if u otra técnica.<br>
   *
   * @param nombre Nombre del contexto a inicializar.
   * @exception InitException  Si hay algún problema al iniciar.
   */
  void init(String nombre) throws InitException;
}
