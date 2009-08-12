package org.openxava.invoicing.util;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class InvoicingPreferences {
	
	private final static String FILE_PROPERTIES="invoicing.properties";
	
	private static Properties properties;	

	private static Properties getProperties() {
		if (properties == null) {
			PropertiesReader reader = new PropertiesReader(InvoicingPreferences.class, FILE_PROPERTIES);
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
	
	public boolean isEMailAsUserNameInPortal() {
		return "true".equalsIgnoreCase(getProperties().getProperty("emailAsUserNameInPortal", "false").trim());
	}
	
	public String getSMTPHost() {
        return getProperties().getProperty("smtpHost");
    }

    public int getSMTPPort() {
        return Integer.parseInt(getProperties().getProperty("smtpPort"));
    }

    public String getSMTPUserID() {        
        return getProperties().getProperty("smtpUserId");        
    }

    public String getSMTPUserPassword() {        
        return getProperties().getProperty("smtpUserPassword");        
    }

	public String getCSVSeparator() {
		return getProperties().getProperty("csvSeparator", ";");
	}	
	
	public String getPersistenceProviderClass() {
		return getProperties().getProperty("persistenceProviderClass", "org.openxava.model.impl.JPAPersistenceProvider").trim();
	}
	
	public String getStyleClass() {
		return getProperties().getProperty("styleClass", "org.openxava.web.style.Liferay51Style").trim();
	}
	
	public String getStyleCSS() {
		return getProperties().getProperty("styleCSS", "liferay51/css/everything_unpacked.css").trim();
	}
		
	public boolean isMapFacadeAsEJB() {
		return "true".equalsIgnoreCase(getProperties().getProperty("mapFacadeAsEJB", "false").trim());
	}
	
	/**
	 *  
	 * @return true if <code>isMapFacadeAsEJB() == true</code>, otherwise the value 
	 * 		of <code>mapFacadeAutoCommit</code> property. 
	 */
	public boolean isMapFacadeAutoCommit() {
		if (isMapFacadeAsEJB()) return true; 
		return "true".equalsIgnoreCase(getProperties().getProperty("mapFacadeAutoCommit", "false").trim());
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
	
	public boolean isHibernatePersistence() { 		
		if (!hibernatePersistenceLoaded) {
			hibernatePersistenceLoaded = true;
			hibernatePersistence = getPersistenceProviderClass().toUpperCase().indexOf("HIBERNATE") >= 0;			
		}			
		return hibernatePersistence;
	}	
	
	public boolean isDetailOnBottomInCollections() { 
		return "true".equalsIgnoreCase(getProperties().getProperty("detailOnBottomInCollections", "false" ).trim());
	}
	
	public boolean isJPACodeInPOJOs() {			
		return "true".equalsIgnoreCase(getProperties().getProperty("jpaCodeInPOJOs", Boolean.toString(isJPAPersistence()) ).trim());
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
	
	
	public boolean isFailOnAnnotationMisuse() {
 		return "true".equalsIgnoreCase(getProperties().getProperty("failOnAnnotationMisuse", "true").trim());
	}
	
	
	public int getMaxSizeForTextEditor() {
		if (maxSizeForTextEditor == 0) { 
			maxSizeForTextEditor = Integer.parseInt(getProperties().getProperty("maxSizeForTextEditor", "100"));
		}
		return maxSizeForTextEditor;
	}
	
	public int getPageRowCount() {
		if (pageRowCount == 0) { 
			pageRowCount = Integer.parseInt(getProperties().getProperty("pageRowCount", "10"));
		}
		return pageRowCount;
	}	
	
	public int getAddColumnsPageRowCount() {
		if (addColumnsPageRowCount == 0) { 
			addColumnsPageRowCount = Integer.parseInt(getProperties().getProperty("addColumnsPageRowCount", "100"));
		}
		return addColumnsPageRowCount;
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
				System.err.println("[XavaPreferences.getJavaLoggingLevel] " + XavaResources.getString("incorrect_log_level", log, JAVA_LOGGING_LEVEL_DEFAULT_VALUE));				
			}
		}
		return javaLoggingLevel;
	}
	
	public Level getHibernateJavaLoggingLevel() {
		if (hibernateJavaLoggingLevel == null) {
			String log = getProperties().getProperty("hibernateJavaLoggingLevel", JAVA_LOGGING_LEVEL_DEFAULT_VALUE).trim();
			try {
				hibernateJavaLoggingLevel = Level.parse(log);
			}
			catch (Exception ex) {
				// Because it's a log error, we don't use log, but direct System.err
				hibernateJavaLoggingLevel = Level.parse(JAVA_LOGGING_LEVEL_DEFAULT_VALUE);
				System.err.println("[XavaPreferences.getHibernateJavaLoggingLevel] " + XavaResources.getString("incorrect_log_level", log, JAVA_LOGGING_LEVEL_DEFAULT_VALUE));				
			}
		}
		return hibernateJavaLoggingLevel;
	}	

	/**
	 * If <code>true</code> when an action has no image it uses a button
	 * for display it, else it uses a link. <p>
	 * 
	 * The default value is <code>false</code>, that is, by default
	 * links for displaying no image actions.<br>
	 */
	public boolean isButtonsForNoImageActions() {  
		return "true".equalsIgnoreCase(getProperties().getProperty("buttonsForNoImageActions", "false" ).trim()); 
	}
	
	/** 
	 * If <code>true</code> a upper case conversions will applied
	 * to string arguments for conditions in list and collections. <p>
	 * 
	 * If <code>true</code> the searching using list or collections are
	 * more flexible (the user can use indistinctly upper or lower case)
	 * but can be slower in some databases (because they cannot use index).
	 * 
	 * The default value is <code>true</code>.<br>
	 */
	public boolean isToUpperForStringArgumentsInConditions() {  
		return "true".equalsIgnoreCase(getProperties().getProperty("toUpperForStringArgumentsInConditions", "true" ).trim()); 
	}
	
	/** 
	 * If <code>true</code> filter is show by default for list on init. <p>
	 * 
	 * The user always have the option to show or hide the filter.<br>
	 * The default value is <code>true</code>.<br>
	 */
	public boolean isShowFilterByDefaultInList() {  
		return "true".equalsIgnoreCase(getProperties().getProperty("showFilterByDefaultInList", "true" ).trim()); 
	}
	
	/** 
	 * If <code>true</code> filter is show by default for collections on init. <p>
	 * 
	 * The user always have the option to show or hide the filter.<br>
	 * The default value is <code>true</code>.<br>
	 */
	public boolean isShowFilterByDefaultInCollections() {  
		return "true".equalsIgnoreCase(getProperties().getProperty("showFilterByDefaultInCollections", "true" ).trim()); 
	}	
	
	public String getPortletLocales(){
		return getProperties().getProperty("portletLocales", 
				"bg, ca, de, en, es, fr, in, it, ja, ko, nl, pl, pt, ru, sv, zh");
	}
	
	public String getDefaultPersistenceUnit() {
		return getProperties().getProperty("defaultPersistenceUnit", "default");
	}

}
