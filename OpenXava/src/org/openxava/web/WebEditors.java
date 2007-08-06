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
	
	public static boolean mustToFormat(MetaProperty p) throws XavaException {
		try {
			return MetaWebEditors.getMetaEditorFor(p).isFormat();
		}
		catch (ElementNotFoundException ex) {
			return true; 
		}
	}
	
	public static boolean hasFrame(MetaProperty p) throws XavaException {
		try {
			return MetaWebEditors.getMetaEditorFor(p).isFrame();
		}
		catch (ElementNotFoundException ex) {
			return false; 
		}
	}	
	
	public static Object parse(HttpServletRequest request, MetaProperty p, String [] strings, Messages errors) throws XavaException { 
		try {
			String string = strings == null?null:strings[0];			
			MetaEditor ed = MetaWebEditors.getMetaEditorFor(p);			
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
			return p.parse(string, Locales.getCurrent());
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			String messageId = p.isNumber()?"numeric":"no_expected_type";
			errors.add(messageId, p.getName(), p.getMetaModel().getName());
			return null;
		}		
	}
		
	public static Object parse(HttpServletRequest request, MetaProperty p, String string, Messages errors) throws XavaException {
		String [] strings = string == null?null:new String [] { string };
		return parse(request, p, strings, errors);
	}
		
	public static String format(HttpServletRequest request, MetaProperty p, Object object, Messages errors) throws XavaException {
		Object result = formatToStringOrArray(request, p, object, errors);
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
	public static Object formatToStringOrArray(HttpServletRequest request, MetaProperty p, Object object, Messages errors) throws XavaException {
		try {
			MetaEditor ed = MetaWebEditors.getMetaEditorFor(p);
			if (ed.hasFormatter()) {				
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
			log.error(ex.getMessage(), ex);			
			errors.add("no_convert_to_string", p.getName(), p.getMetaModel().getName());
			return "";
		}
	}
	
	
	public static String getUrl(MetaProperty p) {
		try {						
			return PREFIX + MetaWebEditors.getMetaEditorFor(p).getUrl();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return PREFIX + "notAvailableEditor.jsp";
		}
	}
	
	public static String getUrl(MetaProperty p, String viewName) throws XavaException { 	
		if (p.getMetaModel() != null) {
			try {				
				MetaView metaView = p.getMetaModel().getMetaView(viewName);
				String editorName = metaView.getEditorForProperty(p);
				if (!Is.emptyString(editorName)) {
					MetaEditor metaEditor = MetaWebEditors.getMetaEditorByName(editorName);
					if (metaEditor != null) {
						return PREFIX + metaEditor.getUrl();
					}
					else {
						log.warn(XavaResources.getString("editor_by_name_for_property_not_found", editorName, p.getName()));
					}
				}
			}
			catch (ElementNotFoundException ex) {
				log.warn(XavaResources.getString("editor_for_property_problem", p.getName()));				
			}
		}
		return getUrl(p);		
	}	
		
	/** 
	 * If a depends on b
	 */
	public static boolean depends(MetaProperty a, MetaProperty b) {		
		try {			
			if (a.depends(b)) return true;
			return MetaWebEditors.getMetaEditorFor(a).depends(b);
		}
		catch (ElementNotFoundException ex) {
			return false;
		}
		catch (Exception ex) {
			log.error(XavaResources.getString("a_depends_b_warning", a, b), ex);
			return false;
		}
	}

}
