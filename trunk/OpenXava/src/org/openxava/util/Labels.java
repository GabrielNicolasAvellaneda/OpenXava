package org.openxava.util;

import java.util.*;

import org.openxava.application.meta.*;


/**
 * Utility class for obtain the i18n of the labels. <p>
 * 
 * @author Javier Paniza
 */

public class Labels {
	
	public static String get(String id, Locale locale) throws MissingResourceException, XavaException {
		if (id == null) return "";
		try {			
			return getResource(id, locale);
		}
		catch (MissingResourceException ex) {			
			int idxPunto = id.indexOf(".");
			if (idxPunto < 0) throw ex;
			String idWitoutQualifier = removeViewOrTab(id);
			if (idWitoutQualifier != null) return get(idWitoutQualifier, locale);
			return get(id.substring(idxPunto + 1), locale);
		}
	} 
	
	/**
	 * @return null if not contains view or tab
	 */
	private static String removeViewOrTab(String id) {
		String r = removeQualifier(id, ".view.", ".views.");
		if (r != null) return r;
		return removeQualifier(id, ".tab.properties.", ".tabs.");		
	}
	
	private static String removeQualifier(String id, String singular, String plural)
	{
		try {
			int idx = id.indexOf(singular);
			if (idx >= 0) {
				return id.substring(0, idx) + id.substring(idx + singular.length() - 1);
			}
			idx = id.indexOf(plural);
			if (idx >= 0) {
				StringTokenizer st = new StringTokenizer(id.substring(idx + plural.length()), ".");
				if (st.countTokens() == 1) { // es la propia vista, no un miembro
					return id.substring(0, idx) + id.substring(idx + plural.length() - 1);
				}
				else { // it is a member
					String viewName = st.hasMoreTokens()?st.nextToken():"";
					int plus = 0;
					if (".tabs.".equals(plural)) {
						if (id.indexOf("properties") >= 0) plus="properties".length();
						if (id.indexOf("title") >= 0) plus= "title".length();					
					}				
					int c = idx + plural.length() + viewName.length() + plus;
					return id.substring(0, idx) + id.substring(idx + plural.length() + viewName.length() + plus);
				}
			}
			return null;
		}
		catch (Exception ex) {
			System.err.println(XavaResources.getString("label_i18n_warning", id));
			return null;
		}
	}	
	
	public static boolean exists(String id) throws XavaException {
		return exists(id, Locale.getDefault());
	}
	
	public static boolean exists(String id, Locale locale) throws XavaException {
		if (id == null) return false;
		try {
			getResource(id, locale);
			return true;
		}
		catch (MissingResourceException ex) {
			int idx = id.indexOf(".");
			if (idx < 0) return false;
			return exists(id.substring(idx + 1));
		}
	}
	
	
	private static String getResource(String id, Locale locale) throws MissingResourceException, XavaException {
		Iterator it = MetaApplications.getApplicationsNames().iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			try {
				ResourceBundle rb = ResourceBundle.getBundle(name + "-labels", locale);
				return rb.getString(id);
			}
			catch (MissingResourceException ex) {
			}						
			try {
				ResourceBundle rb = ResourceBundle.getBundle("Etiquetas" + name, locale);
				return rb.getString(id);
			}
			catch (MissingResourceException ex) {
			}			
		}		
		ResourceBundle rb = ResourceBundle.getBundle("Labels", locale);
		return rb.getString(id);
	}
	
	public static String removeUnderlined(String label) {				
		int idx =  label.indexOf('_');
		if (idx < 0) return label;
		String ini = idx > 0?label.substring(0, idx - 1):"";
		String end = idx == label.length() - 1?"":label.substring(idx + 1);
		return ini + end;
	}
		
}
