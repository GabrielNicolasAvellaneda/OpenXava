package org.openxava.util;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.logging.*;

/**
 * @author Javier Paniza
 */
public class XavaPreferences {
	
	private final static String FILE_PROPERTIES="xava.properties";
	private final static String JAVA_LOGGING_LEVEL_DEFAULT_VALUE="INFO";
	private static Log log = LogFactory.getLog(XavaPreferences.class);
		
	private static XavaPreferences instance;
	
	private Properties properties;
	
	private boolean ejb2PersistenceLoaded=false;
	private boolean ejb2Persistence=false;
	private boolean jpaPersistenceLoaded=false;
	private boolean jpaPersistence=false;	
	private boolean duplicateComponentWarningsLoaded=false;
	private boolean duplicateComponentWarnings=false;
	private int maxSizeForTextEditor;
	private boolean jpaCodeInPOJOs = false;
	private boolean jpaCodeInPOJOsLoaded = false;
	private Level javaLoggingLevel;

		
	
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
			catch (IOException ex) {			
				log.error(XavaResources.getString("properties_file_error", FILE_PROPERTIES),ex);
				properties = new Properties();
			}
		}		
		return properties;
	}

	public boolean isReadOnlyAsLabel() {
		return "true".equalsIgnoreCase(getProperties().getProperty("readOnlyAsLabel", "false").trim());
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
		return getProperties().getProperty("persistenceProviderClass", "org.openxava.model.impl.HibernatePersistenceProvider").trim();
	}
	
	public boolean isMapFacadeAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("mapFacadeAsEJB", "false").trim());
	}

	public boolean isEJB2Persistence() {
		if (!ejb2PersistenceLoaded) {
			ejb2PersistenceLoaded = true;
			ejb2Persistence = getPersistenceProviderClass().toUpperCase().indexOf("EJB") >= 0;
		}	
		return ejb2Persistence;
	}
	
	public boolean isJPAPersistence() {
		if (!jpaPersistenceLoaded) {
			jpaPersistenceLoaded = true;
			jpaPersistence = getPersistenceProviderClass().toUpperCase().indexOf("JPA") >= 0;
		}	
		return jpaPersistence;
	}
	
	public boolean isJPACodeInPOJOs() {
		if (!jpaCodeInPOJOsLoaded) {			
			jpaCodeInPOJOs = "true".equalsIgnoreCase(getProperties().getProperty("jpaCodeInPOJOs", Boolean.toString(isJPAPersistence()) ).trim());
			jpaCodeInPOJOsLoaded = true;
		}	
		return jpaCodeInPOJOs;		
	}
	
	/**
	 * Useful when you want to force some code (as finders) to not use JPA implementation. <p>
	 * 
	 * Warning! This does not change the value of persistenceProviderClass,
	 * only change the behaviour of code that call to isJPAPersistence.<br>
	 * Usually you use this method inside of test code to force to use hibernate
	 * or jpa implementation when work with POJOs.
	 * 
	 * For example, if you call <code>setJPAPersistence(false)</code> 
	 * then you force to the POJO finder to use hibernate always, instead of JPA.
	 */
	public void setJPAPersistence(boolean jpaPersistence) {
		this.jpaPersistence = jpaPersistence;
		this.jpaPersistenceLoaded = true;
	}
	

	public boolean isI18nWarnings() {
		return "true".equalsIgnoreCase(getProperties().getProperty("i18nWarnings", "true").trim());		
	}


	public boolean isDuplicateComponentWarnings() {
		if (!duplicateComponentWarningsLoaded) { 
			duplicateComponentWarnings = "true".equalsIgnoreCase(getProperties().getProperty("duplicateComponentWarnings", "true").trim());
			duplicateComponentWarningsLoaded = true;
		}
		return duplicateComponentWarnings;
	}
	
	public int getMaxSizeForTextEditor() {
		if (maxSizeForTextEditor == 0) { 
			maxSizeForTextEditor = Integer.parseInt(getProperties().getProperty("maxSizeForTextEditor", "100"));
		}
		return maxSizeForTextEditor;
	}	
	
	public void setDuplicateComponentWarnings(boolean duplicateComponentWarnings) {
		this.duplicateComponentWarnings = duplicateComponentWarnings;
		duplicateComponentWarningsLoaded = true;
	}
	
	public Level getJavaLoggingLevel() {
		if (javaLoggingLevel == null) {
			String log = getProperties().getProperty("javaLoggingLevel", JAVA_LOGGING_LEVEL_DEFAULT_VALUE).trim();
			try {
				javaLoggingLevel = Level.parse(log);
			}
			catch (Exception ex) {
				// Because it's a log error, we don't use log, but direct System.err
				javaLoggingLevel = Level.parse(JAVA_LOGGING_LEVEL_DEFAULT_VALUE);
				System.err.println("[XavaPreferences.getLogLevel] " + XavaResources.getString("incorrect_log_level", log, JAVA_LOGGING_LEVEL_DEFAULT_VALUE));				
			}
		}
		return javaLoggingLevel;
	}
		
}
