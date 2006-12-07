package org.openxava.util;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads properties files. <p>
 *
 * @author: Javier Paniza
 */
public class PropertiesReader {

	private Class theClass;
	private String propertiesFileURL;
	private Properties properties;
	
	private static Log log = LogFactory.getLog(PropertiesReader.class);
	
	/**
	 * @param propertiesFileURL  Cannot be null
	 * @param theClass  Class from obtain the <code>ClassLoader</code> used to read the file. Cannot be nul
	 */
	public PropertiesReader(Class theClass, String propertiesFileURL) {
		Assert.arg(theClass, propertiesFileURL);
		this.theClass = theClass;
		this.propertiesFileURL = propertiesFileURL;
	}
	
  // Adds properties in url to p.
  // Only adds still not existing properties in p
  private void add(Properties p, URL url) throws IOException {
		// assert(p, url);
		InputStream is = url.openStream();
		Properties properties = new Properties();
		properties.load(is);
		Enumeration keys = properties.keys();
		while (keys.hasMoreElements()) {
		  Object k = keys.nextElement();
		  if (!p.containsKey(k)) {
			p.put(k, properties.get(k));
		  }
		}
		try { is.close(); } catch (IOException ex) {}
  }
  
  /**
   * Returns properties associated to indicated file. <p> 
   * 
   * Read all files in classpath with the property file name used in constructor.
   * The result is a mix of all properties of this files. <br>
   * Only read the first time. <br>
   *
   * @return Not null
   */
  public Properties get() throws IOException {
	  if (properties == null) {		
			Enumeration e = theClass.getClassLoader().getResources(propertiesFileURL);
			properties = new Properties();		
			while (e.hasMoreElements()) {
				URL url = (URL) e.nextElement();			
			  add(properties, url);
			}		
		}
		return properties;
  }          
  
}
