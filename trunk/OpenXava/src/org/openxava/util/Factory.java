package org.openxava.util;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Clase genérica para implementar factorias de objetos. <br>
 *
 * Las clases de los objetos a construir estarán en un
 * archivo de propiedades con la forma <i>nombre=clase</i>. Donde
 * <i>nombre</i> es un identificador y <i>clase</i> la clase
 * del objeto a crear, la cual ha de tener constructor por defecto.<br>
 * Si la clase implementa {@link IInit} se llamará el método {@link IInit#init}
 * después de la creación.<br>
 * Existe una entrada que será tomada como entrada por defecto, es
 * decir la usada al llamar a <code>create()</code> sin argumentos.
 * La entrada por defecto se puede definir al construir la factoría
 * o si no se define ninguna se tomará la entrada indicada en <i>_default/_defecto</i>. <br>
 *
 * Un ejemplo de archivo de propiedades sería:
 * <pre>
 * _defecto=rapido
 * rapido=miempresa.miapp.paquete1.MiClaseRapida
 * lento=miempresa.miapp.paquete1.MiClaseLenta
 * </pre>
 * O en inglés
 * _default=fast
 * fast=mycorp.myapp.package1.MyFastClass
 * slow=mycorp.myapp.package1.MySlowClass
 * 
 * @author  Javier Paniza
 */

public class Factory {

  // Si cambian las variables final cambiar doc de cabecera y los constructores
  private final static String DEFAULT_ENTRY_ES = "_defecto";
  private final static String DEFAULT_ENTRY_EN = "_default";

  private URL archivoPropiedades;
  private String entradaDefecto = null; // entrada que índica que entrada es la por defecto
  private String defecto; // el nombre de la entrada por defecto, se obtiene de entradaDefecto

  private Hashtable clases = new Hashtable(); // de Class
  private Properties propiedades;

  /**
   * Crea una factoría de objetos a partir un archivo de propiedades. <br>
   * La entrada por que índica la entrada por defecto sera <i>_defecto</i>.<br>
   * <b>Precondiciones:</b>
   * <ul>
   * <li> archivoPropiedades != null
   * </ul>
   * @param archivoPropiedades  URL del archivo con la lista de nombres/clases.
   */
  public Factory(URL archivoPropiedades) {
	this(archivoPropiedades, null);
  }
  /**
   * Crea una factoría de objetos a partir un archivo de propiedades, indicando
   * la entrada que índica la entrada por defecto. <br>
   * <b>Precondiciones:</b>
   * <ul>
   * <li> archivoPropiedades != null
   * </ul>
   * @param archivoPropiedades  URL del archivo con la lista de nombres/clases.
   * @param defecto  Indica que entrada será usada como opción por defecto, si
   *                 es <code>null</code> se asume el valor <i>_defecto</i>.
   */
  public Factory(URL archivoPropiedades, String defecto) {
	if (archivoPropiedades == null) {
	  throw new IllegalArgumentException(XavaResources.getString("properties_file_not_valid"));
	}
	this.archivoPropiedades = archivoPropiedades;
	if (defecto != null) this.entradaDefecto = defecto;
  }
  /**
   * Crea un objeto por defecto. <br>
   * <b>Postcondición:</b>
   * <ul>
   * <li> return != null
   * </ul>
   * Lo hace según lo indicado en la entrada por defecto
   * del archivo de propiedades.<br>
   * @exception InitException  Si hay algún problema el iniciar.
   */
  public Object create() throws InitException {
	return create(getDefecto());
  }
  /**
   * Crea el objeto indicado. <br>
   * <b>Postcondición:</b>
   * <ul>
   * <li> return != null
   * </ul>
   * El argumento es el nombre identificativo del contexto, el cual está
   * registrado en el archivo de propiedades.<br>
   * @exception InitException  Si hay algún problema el iniciar.
   */
  public Object create(String nombre) throws InitException {
	try {
	  Object rs = getClase(nombre).newInstance();
	  if (rs instanceof IInit) {
		IInit ini = (IInit) rs;
		ini.init(nombre);
	  }
	  return rs;
	}
	catch (InitException ex) {
	  throw ex;
	}
	catch (Exception ex) {
	  ex.printStackTrace();
	  throw new InitException("factory_create_error", nombre);
	}
  }
  /**
   * Devuelve la clase asociada al nombre indicado. <br>
   * Las clases se almacenan para su reuso si hace falta.<br>
   */
  private Class getClase(String nombre) throws Exception {
	Class clase = (Class) clases.get(nombre);
	if (clase == null) {
	  String nombreClase = getPropiedades().getProperty(nombre);
	  if (nombreClase == null) {
		throw new InitException("factory_class_not_found", nombre, archivoPropiedades);
	  }
	  clase = Class.forName(nombreClase);
	  clases.put(nombre, clase);
	}
	return clase;
  }
  /**
   * Devuelve la entrada por defecto, obtenida de la entrada <tt>entradaDefecto</tt>.
   */
  private String getDefecto() throws InitException {
	if (defecto == null) {
	  try {
		if (entradaDefecto == null) {
			defecto = getPropiedades().getProperty(DEFAULT_ENTRY_EN);
			if (defecto == null) defecto = getPropiedades().getProperty(DEFAULT_ENTRY_ES); 
		}
		else {
			defecto = getPropiedades().getProperty(entradaDefecto);
		}
		if (defecto == null) {
		  throw new InitException("factory_entry_not_found", entradaDefecto, archivoPropiedades);
		}
	  }
	  catch (IOException ex) {
	   ex.printStackTrace();
	   throw new InitException("properties_file_read_error");
	  }
	}
	return defecto;
  }
  /**
   * Lee el archivo de propiedades y los convierte en un <code>Properties</code>.<br>
   */
  private Properties getPropiedades() throws IOException {
	if (propiedades == null) {
	  InputStream is = archivoPropiedades.openStream();
	  propiedades = new Properties();
	  propiedades.load(is);
	  try { is.close(); } catch (IOException ex) {}
	}
	return propiedades;
  }
}
