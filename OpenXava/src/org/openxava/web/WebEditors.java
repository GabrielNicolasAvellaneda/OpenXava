package org.openxava.web;

import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.meta.*;
import org.openxava.web.meta.*;

/**
 * @author Javier Paniza
 */

public class WebEditors { 	

	private static Log log = LogFactory.getLog(WebEditors.class);
	final private static String PREFIX = "editors/";
	
	public static boolean mustToFormat(MetaProperty p, String viewName) throws XavaException { 
		try {
			return getMetaEditorFor(p, viewName).isFormat(); 
		}
		catch (ElementNotFoundException ex) {
			return true; 
		}
	}
	
	public static boolean hasMultipleValuesFormatter(MetaProperty p, String viewName) throws XavaException { 
		try {
			return getMetaEditorFor(p, viewName).hasMultipleValuesFormatter(); 
		}
		catch (ElementNotFoundException ex) {
			return false; 
		}
	}	
	
	public static boolean hasFrame(MetaProperty p, String viewName) throws XavaException { 
		try {
			return getMetaEditorFor(p, viewName).isFrame(); 
		}
		catch (ElementNotFoundException ex) {
			return false; 
		}
	}

	public static Object parse(HttpServletRequest request, MetaProperty p, String [] strings, Messages errors, String viewName) throws XavaException { 
		try {
			String string = strings == null?null:strings[0];						
			if (!(p.isKey() && p.isHidden())) { 
				MetaEditor ed = getMetaEditorFor(p, viewName);
				if (ed.hasFormatter()) { 								
					return ed.getFormatter().parse(request, string);
				}
				else if (ed.hasMultipleValuesFormatter()) {								
					return ed.getMultipleValuesFormatter().parse(request, strings);
				}
				else if (ed.isFormatterFromType()){				
					MetaEditor edType = MetaWebEditors.getMetaEditorForTypeOfProperty(p); 
					if (edType != null && edType.hasFormatter()) {				
						return edType.getFormatter().parse(request, string);
					}
					else if (edType != null && edType.hasMultipleValuesFormatter()) {				
						return edType.getMultipleValuesFormatter().parse(request, strings);
					} 
				}
			}
			return p.parse(string, Locales.getCurrent());
		}
		catch (Exception ex) {
			log.warn(ex.getMessage(), ex);
			String messageId = p.isNumber()?"numeric":"no_expected_type";
			errors.add(messageId, p.getName(), p.getMetaModel().getName());
			return null;
		}		
	}
		
	public static Object parse(HttpServletRequest request, MetaProperty p, String string, Messages errors, String viewName) throws XavaException { 
		String [] strings = string == null?null:new String [] { string };
		return parse(request, p, strings, errors, viewName); 
	}
		
	public static String format(HttpServletRequest request, MetaProperty p, Object object, Messages errors, String viewName) throws XavaException {
		Object result = formatToStringOrArray(request, p, object, errors, viewName, false);
		if (result instanceof String []) return arrayToString((String []) result);		
		return (String) result;
	}
	
	public static String format(HttpServletRequest request, MetaProperty p, Object object, Messages errors, String viewName, boolean fromList) throws XavaException {
		Object result = formatToStringOrArray(request, p, object, errors, viewName, fromList);
		if (result instanceof String []) return arrayToString((String []) result);		
		return (String) result;
	}
	
	private static String arrayToString(String[] strings) {
		if (strings == null) return "";
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			result.append(strings[i]);
			if (i < strings.length - 1) result.append('/');
		}
		return result.toString();
	}
	

	/** 
	 * @return If has a multiple converter return a array of string else return a string
	 */
	public static Object formatToStringOrArray(HttpServletRequest request, MetaProperty p, Object object, Messages errors, String viewName, boolean fromList) throws XavaException { 
		Object result = formatToStringOrArrayImpl(request, p, object, errors, viewName, fromList);
		if (fromList && result != null && !hasMarkup(result)) {
			return result.toString().replaceAll(" ", "&nbsp;"); 
		}
		return result; 
	}
	
	public static Object formatTitle(HttpServletRequest request, MetaProperty p, Object object, Messages errors, String viewName, boolean fromList) throws XavaException { 
		Object result = formatToStringOrArrayImpl(request, p, object, errors, viewName, fromList);
		if (result != null && hasMarkup(result)) {
			return p.getLabel(); 
		}
		return result; 		
	}

	private static boolean hasMarkup(Object result) { 
		return result.toString().contains("<") && result.toString().contains(">");
	}

	public static Object formatToStringOrArrayImpl(HttpServletRequest request, MetaProperty p, Object object, Messages errors, String viewName, boolean fromList) throws XavaException {  
		try {
			MetaEditor ed = getMetaEditorFor(p, viewName); 			
			if (fromList && !Is.empty(ed.getListFormatterClassName())){
				return ed.getListFormatter().format(request, object);
			}
			else if (ed.hasFormatter()) {				
				return ed.getFormatter().format(request, object);
			}
			else if (ed.hasMultipleValuesFormatter()) { 
				return ed.getMultipleValuesFormatter().format(request, object);
			}
			else if (ed.isFormatterFromType()){
				MetaEditor edType = MetaWebEditors.getMetaEditorForType(p.getTypeName());
				if (edType != null && edType.hasFormatter()) {				
					return edType.getFormatter().format(request, object);
				}
				else if (edType != null && edType.hasMultipleValuesFormatter()) { 
					return edType.getMultipleValuesFormatter().format(request, object);
				}
			}			
			return p.format(object, Locales.getCurrent());									
		}
		catch (Exception ex) {
			log.warn(ex.getMessage(), ex);			
			errors.add("no_convert_to_string", p.getName(), p.getMetaModel().getName());
			return "";
		}
	}
	
	public static String getUrl(MetaProperty p, String viewName) throws XavaException {
		try {						
			return PREFIX + getMetaEditorFor(p, viewName).getUrl();
		}
		catch (Exception ex) {
			log.warn(ex.getMessage(), ex);
			return PREFIX + "notAvailableEditor.jsp";
		}		
	}	
	
	public static MetaEditor getMetaEditorFor(MetaMember m, String viewName) throws ElementNotFoundException, XavaException {
		if (m.getMetaModel() != null) {
			try {				
				MetaView metaView = m.getMetaModel().getMetaView(viewName);				
				String editorName = metaView.getEditorFor(m);				
				if (!Is.emptyString(editorName)) {
					MetaEditor metaEditor = MetaWebEditors.getMetaEditorByName(editorName);
					if (metaEditor != null) {
						return metaEditor;
					}
					else {
						log.warn(XavaResources.getString("editor_by_name_for_property_not_found", editorName, m.getName()));
					}
				}
			}
			catch (ElementNotFoundException ex) {
			}
		}
		return MetaWebEditors.getMetaEditorFor(m);
	}
				
	/** 
	 * If a depends on b
	 */
	public static boolean depends(MetaProperty a, MetaProperty b, String viewName) {		
		try {			
			if (a.depends(b)) return true;
			return getMetaEditorFor(a, viewName).depends(b);
		}
		catch (ElementNotFoundException ex) {
			return false;
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("a_depends_b_warning", a, b), ex); 
			return false;
		}
	}

	/**
	 * If the property depends of some other property displayed in the view. <p>
	 */
	public static boolean dependsOnSomeOther(MetaProperty metaProperty, String viewName) {
		try {			
			return getMetaEditorFor(metaProperty, viewName).dependsOnSomeOther();
		}
		catch (ElementNotFoundException ex) {
			return false;
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("a_depends_b_warning", metaProperty.getName(), "some other"), ex);
			return false;
		}
	}

}
