package org.openxava.util;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;



/**
 * @author Javier Paniza
 */

public class XavaResources {
	
	private static final ResourceManagerI18n impl = new ResourceManagerI18n("XavaResources"); 
		
	public static String getString(String key) {	
		return impl.getString(key);
	}
	
	public static String getString(String key, Object argv0) {
		return impl.getString(key, argv0);
	}
	
	public static String getString(String key, Object argv0, Object argv1) {
		return impl.getString(key, argv0, argv1);
	}
	
	public static String getString(String key, Object argv0, Object argv1, Object argv2) {
		return impl.getString(key, argv0, argv1, argv2);
	}
	
	public static String getString(String key, Object argv0, Object argv1, Object argv2, Object argv3) {
		return impl.getString(key, argv0, argv1, argv2, argv3);
	}	
			
	public static String getString(String key, Object [] argv) {
		return impl.getString(key, argv);
	}

	public static String getString(Locale locale, String key) {	
		return impl.getString(locale, key);
	}

	public static String getString(HttpServletRequest request, String key) {	
		return impl.getString(getLocale(request), key);
	}
	
	public static String getString(HttpServletRequest request, String key, Object argv1) {
		return impl.getString(getLocale(request), key, argv1);
	}
	
	public static String getString(HttpServletRequest request, String key, Object [] argv) {
		return impl.getString(getLocale(request), key, argv);
	}
	

	public static int getChar(String key) {
		return impl.getChar(key);
	}	
	
	/**
	 * Locale usado para obtener los recursos en una aplicación web. <p>
	 */
	public static Locale getLocale(ServletRequest request) {
		Object o = request.getAttribute("xava.locale");		
		if (o == null) {
			return request.getLocale();	
		}
		else {
			if (o instanceof Locale) {
				return (Locale) o;
			}
			else {
				System.err.println("¡ADVERTENCIA! Atributo xava.locale debería ser de tipo java.util.Locale. Se usa Locale por defecto");
				return request.getLocale();
			}
		}		
	}
	
	

}
