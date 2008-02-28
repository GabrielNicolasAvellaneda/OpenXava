package org.openxava.util;

import java.util.*;
import java.util.logging.*;

import org.apache.commons.logging.*;
import org.openxava.component.*;

/**
 * Global utilities about the system. <p>
 * 
 * @author Javier Paniza
 */

public class XSystem {

	private static Log log = LogFactory.getLog(XSystem.class);
	
	private static boolean onServer = false;
	private static boolean java5OrBetter;
	private static boolean java5calculated = false;	
	
	/**
	 * Does that {@link #onServer} returns <tt>true</tt>.
	 *
	 * Must to be called from a static block in a common base class of EJB, or
	 * in all EJB is there aren't base class.<br>
	 */
	public static void _setOnServer() {
		onServer = true;
	}
	
	/**
	 * If we are in a client: java application, applet, servlet, jsp, etc. <p> 
	 */
	public static boolean onClient() {
		return !onServer();
	}
	
	/**
	 * If we are in a EJB server. <p>
	 */
	public static boolean onServer() {
		return onServer;
	}
	
	public static boolean isJava5OrBetter() {
		if (!java5calculated) {
			String version = System.getProperty("java.specification.version");		
			java5OrBetter = !(version.equals("1.4") || version.equals("1.3") || version.equals("1.2") || version.equals("1.1") || version.equals("1.0"));
			java5calculated = true;
		}
		return java5OrBetter;
	}

	
	public static void _setLogLevelFromJavaLoggingLevelOfXavaPreferences() {		
		Logger rootLogger = Logger.getLogger("");
		Handler [] rootHandler = rootLogger.getHandlers();		
		for (int i=0; i<rootHandler.length; i++) {
			if (rootHandler[i] instanceof ConsoleHandler)
				rootHandler[i].setLevel(Level.ALL);
		}		
		Logger.getLogger("org.openxava").setLevel(XavaPreferences.getInstance().getJavaLoggingLevel());
		try {
			for (Iterator it = MetaComponent.getAllPackageNames().iterator(); it.hasNext(); ) {
				String packageName = (String) it.next();
				Logger.getLogger(packageName).setLevel(XavaPreferences.getInstance().getJavaLoggingLevel());
			}			
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("logging_level_not_set"));
		}
		Logger.getLogger("org.hibernate").setLevel(XavaPreferences.getInstance().getHibernateJavaLoggingLevel());
	}
	
	/**
	 * Suitable to use inside a XML as encoding. <p>
	 */
	public static String getEncoding() {
		String encoding = System.getProperty("file.encoding");
		if (encoding ==null) return "ISO-8859-1";
		if ("Cp1252".equalsIgnoreCase(encoding)) return "ISO-8859-1"; 
		if ("utf8".equalsIgnoreCase(encoding)) return "UTF-8";
		return encoding;	
	}
	
}
