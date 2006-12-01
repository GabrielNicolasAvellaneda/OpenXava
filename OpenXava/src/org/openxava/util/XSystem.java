package org.openxava.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Global utilities about the system. <p>
 * 
 * @author Javier Paniza
 */

public class XSystem {

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
	
}
