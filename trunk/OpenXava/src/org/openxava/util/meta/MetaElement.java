package org.openxava.util.meta;

import java.util.*;
import javax.servlet.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
abstract public class MetaElement implements java.io.Serializable {
	private String description;
	private java.lang.String name;
	private String label;

	protected boolean has18nLabel() {		
		String id = getId();
		if (id == null) return false;
		try {					
			Labels.get(id, Locale.getDefault()); 
			return true;
		}
		catch (MissingResourceException ex) {
			return false;
		}					
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("element_i18n_warning", id));			
			return false;
		}
	}
	
	protected boolean hasName() {
		String n = getName();
		return n != null && !n.trim().equals("");
	}
	
	public String getLabel() {
		return getLabel(Locale.getDefault());
	}
	
	public String getLabel(ServletRequest request) {
		return getLabel(getLocale(request));
	}
	
	protected Locale getLocale(ServletRequest request) {
		return XavaResources.getLocale(request);
	}
	
	
	/**
	 * For refine the label calculation
	 */
	public String getLabel(Locale locale) {
		return getLabel(locale, getId());
	}
	
	/**
	 * Implementation of label obtaining. <p>  
	 */
	protected String getLabel(Locale locale, String id) {
		if (id == null) return "";
		try {				
			return Labels.get(id, locale);
		}
		catch (Exception ex) {			
			System.err.println(XavaResources.getString("element_i18n_warning", id));			
			if (Is.emptyString(label))
				label = firstUpper(getName());
			return label;
		}		
	}
	
		
	/**
	 * Unique id of element, normally used to search the label in the resources files.	 
	 */
	public abstract String getId();
	
	
	/**	 
	 * @return java.lang.String Not null
	 */
	public java.lang.String getName() {
		return name == null ? "" : name;
	}
	
	public void setLabel(String newLabel) {
		label = newLabel;
	}
	
	public void setName(java.lang.String newName) {
		name = newName;
	}
	
	
	protected String firstUpper(String s) {
		return Strings.firstUpper(s);
	}
	
	protected String firstLower(String s) {
		return Strings.firstLower(s);
	}
	
	public String getDescription() {
		return getDescription(Locale.getDefault());
	}
	
	public String getDescription(Locale locale) {
		String id = getId();
		if (id == null) return "";
		try {			
			return Labels.get(id + "[description]", locale);
		}
		catch (Exception ex) {
			if (has18nLabel()) return getLabel(locale); 
			return Is.emptyString(description)?getLabel(locale):description;
		}		
	}
	
	public String getDescription(ServletRequest request) {
		return getDescription(getLocale(request));
	}	
	public void setDescription(String newDescription) {
		description = newDescription;
	}
		
	
}