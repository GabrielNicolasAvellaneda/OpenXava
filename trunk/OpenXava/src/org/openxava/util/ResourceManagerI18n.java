package org.openxava.util;

import java.text.*;
import java.util.*;

/**
 * @author Javier Paniza
 */

public class ResourceManagerI18n {
	
	private String resourcesFile;
		
	public ResourceManagerI18n(String resourcesFile) {
		Assert.assertNotNull("Resource file is required", resourcesFile); // this message cannot be i18n
		this.resourcesFile = resourcesFile;
	}
	
	public String getString(String key) {	
		try {			
			return ResourceBundle.getBundle(resourcesFile).getString(key);
		}
		catch (MissingResourceException e) {
			System.err.println(XavaResources.getString("element_i18n_warning", key));			
			return '[' + key + ']';
		}
	}
	
	public String getString(String key, Object argv0) {
		return getString(key, new Object [] { argv0 });
	}
	
	public String getString(String key, Object argv0, Object argv1) {
		return getString(key, new Object [] { argv0, argv1 });
	}
	
	public String getString(String key, Object argv0, Object argv1, Object argv2) {
		return getString(key, new Object [] { argv0, argv1, argv2 });
	}
	
	public String getString(String key, Object argv0, Object argv1, Object argv2, Object argv3) {
		return getString(key, new Object [] { argv0, argv1, argv2, argv3 });
	}	
	
	public String getString(String key, Object [] argv) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(Locale.getDefault());
		formatter.applyPattern(getString(key));
		return formatter.format(argv);		
	}
	
	public String getString(Locale locale, String key) {	
		try {			
			return ResourceBundle.getBundle(resourcesFile, locale).getString(key);
		}
		catch (MissingResourceException e) {
			System.err.println(XavaResources.getString("element_i18n_warning", key));
			return '[' + key + ']';
		}
	}
	
	public String getString(Locale locale, String key, Object argv0) {		
		return getString(locale, key, new Object [] { argv0 });
	}
	
	public String getString(Locale locale, String key, Object [] argv) {		
		MessageFormat formateador = new MessageFormat("");
		formateador.setLocale(locale);
		formateador.applyPattern(getString(locale, key));
		return formateador.format(argv);		
	}
	

	public int getChar(String key) {
		try {
			String s = ResourceBundle.getBundle(resourcesFile).getString(key);
			if (s == null) return ' ';
			if (s.length() == 0) return ' ';
			return s.charAt(0);			
		}
		catch (MissingResourceException e) {
			System.err.println(XavaResources.getString("char_i18n_warning", key));
			return ' ';
		}
	}	
	
}
