package org.openxava.util;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Permite leer archivos de propiedades. <p>
 *
 * @author: Javier Paniza
 */
public class PropertiesReader {

	private Class clase;
	private String urlArhivoPropiedades;
	private Properties propiedades;
	
	
/**
 * Constructor. <p<
 *
 * @param urlArchivoPropiedades No puede ser nulo.
 * @param clase Clase de la que se obtiene el <tt>ClassLoader</tt> para leer el archivo. No puede ser nulo
 */
public PropertiesReader(Class clase, String urlArchivoPropiedades) {
	Assert.arg(clase, urlArchivoPropiedades);
	this.clase = clase;
	this.urlArhivoPropiedades = urlArchivoPropiedades;
}
  // Añade las Properties en url a p.
  // Solo añade las propiedades todavía no existentes en p
  private void add(Properties p, URL url) throws IOException {
	// assert(p, url);
	InputStream is = url.openStream();
	Properties propiedades = new Properties();
	propiedades.load(is);
	Enumeration keys = propiedades.keys();
	while (keys.hasMoreElements()) {
	  Object k = keys.nextElement();
	  if (!p.containsKey(k)) {
		p.put(k, propiedades.get(k));
	  }
	}
	try { is.close(); } catch (IOException ex) {}
  }      
  /**
   * Devuelve las propiedades asociadas al archivo indicado. <p>
   *
   * Lee todos los archivos en el classpath con el nombre del archivo de propiedades
   * usado al construir. El resultado es una mezcla de las propiedades de estos
   * archivos.<br>
   * La lectura solo la hace la primera vez.<br>
   *
   * @return Nunca será nulo.
   */
  public Properties get() throws IOException {
	  if (propiedades == null) {		
			Enumeration e = clase.getClassLoader().getResources(urlArhivoPropiedades);
			propiedades = new Properties();		
			while (e.hasMoreElements()) {
				URL url = (URL) e.nextElement();			
			  add(propiedades, url);
			}		
		}
		return propiedades;
  }          
}
