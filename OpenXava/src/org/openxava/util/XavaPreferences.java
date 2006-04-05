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
	private boolean ejb2PersistenceLoaded=false;
  private boolean ejb2Persistence=false;
  
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
		return "true".equalsIgnoreCase(getProperties().getProperty("readOnlyAsLabel", "false").trim());
	}
	
	public boolean isButtonBarOnTop() {
		return "true".equalsIgnoreCase(getProperties().getProperty("buttonBarOnTop", "true").trim());
	}
	
	public boolean isButtonBarOnBottom() {
		return "true".equalsIgnoreCase(getProperties().getProperty("buttonBarOnBottom", "true").trim());
	}
	
	public boolean isTabAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("tabAsEJB", "false").trim());
	}
	
	public boolean isShowCountInList() {
		return "true".equalsIgnoreCase(getProperties().getProperty("showCountInList", "true").trim());
	}
		
	public String getFormLineSpacing() {
		return getProperties().getProperty("formLineSpacing", "1");
	}
	
	public String getPersistenceProviderClass() {
		return getProperties().getProperty("persistenceProviderClass", "org.openxava.model.impl.EJBPersistenceProvider");
	}
	
	public boolean isMapFacadeAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("mapFacadeAsEJB", "true").trim());
	}

	public boolean isEJB2Persistence() {
		if (!ejb2PersistenceLoaded) {
			ejb2PersistenceLoaded = true;
			ejb2Persistence = getPersistenceProviderClass().toUpperCase().indexOf("EJB") >= 0;
		}	
		return ejb2Persistence;
	}


	public boolean isI18nWarnings() {
		return "true".equalsIgnoreCase(getProperties().getProperty("i18nWarnings", "true").trim());		
	}
}
