package org.openxava.util;

import java.io.*;
import java.util.*;

/**
 * @author Javier Paniza
 */
public class XavaPreferences {
	
	private final static String FILE_PROPERTIES="xava.properties";
		
	private Properties properties;
	private static XavaPreferences instance; 

	public static XavaPreferences getInstance() {
		if (instance == null) {
			instance = new XavaPreferences();
		}
		return instance;
	}
	

	private Properties getProperties() {
		if (properties == null) {
			PropertiesReader reader = new PropertiesReader(XavaPreferences.class, FILE_PROPERTIES);
			try {
				properties = reader.get();
			} 
			catch (IOException e) {			
				System.err.println(XavaResources.getString("properties_file_error", FILE_PROPERTIES));
				properties = new Properties();
			}
		}
		return properties;
	}

	public boolean isReadOnlyAsLabel() {
		return "true".equalsIgnoreCase(getProperties().getProperty("readOnlyAsLabel", "false"));
	}
	
	public boolean isButtonBarOnTop() {
		return "true".equalsIgnoreCase(getProperties().getProperty("buttonBarOnTop", "true"));
	}
	
	public boolean isButtonBarOnBottom() {
		return "true".equalsIgnoreCase(getProperties().getProperty("buttonBarOnBottom", "true"));
	}
	
	public boolean isTabAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("tabAsEJB", "false"));
	}
	
	public boolean isDescriptionsCalculatorAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("descriptionsCalculatorAsEJB", "false"));
	}
	
	
	public String getFormLineSpacing() {
		return getProperties().getProperty("formLineSpacing", "1");
	}
	
}
