package org.openxava.web;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.web.meta.*;



/**
 * @author Javier Paniza
 */

public class WebEditors { 

	private static Properties properties;

	final private static String PREFIX = "editors/";
	
	public static boolean mustToFormat(MetaProperty p) throws XavaException {
		return MetaWebEditors.getMetaEditorFor(p).isFormat();		
	}
	
	public static boolean hasFrame(MetaProperty p) throws XavaException {
		return MetaWebEditors.getMetaEditorFor(p).isFrame();		
	}	

	public static Object parse(HttpServletRequest request, MetaProperty p, String string, Messages errores) throws XavaException {
		try {
			MetaEditor ed = MetaWebEditors.getMetaEditorFor(p);			
			if (ed.hasFormatter()) {								
				return ed.getFormatter().parse(request, string);
			}
			else if (ed.isFormatterFromType()){
				MetaEditor edType = MetaWebEditors.getMetaEditorForType(p.getTypeName());
				if (edType != null && edType.hasFormatter()) {				
					return edType.getFormatter().parse(request, string);
				}
			}
			return p.parse(string, request.getLocale());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			String idMensaje = p.isNumber()?"numeric":"no_expected_type";
			errores.add(idMensaje, p.getName(), p.getMetaModel().getName());
			return null;
		}
	}
	
	public static String format(HttpServletRequest request, MetaProperty p, Object object, Messages errores) throws XavaException {
		try {
			MetaEditor ed = MetaWebEditors.getMetaEditorFor(p);
			if (ed.hasFormatter()) {				
				return ed.getFormatter().format(request, object);
			}
			else if (ed.isFormatterFromType()){
				MetaEditor edType = MetaWebEditors.getMetaEditorForType(p.getTypeName());
				if (edType != null && edType.hasFormatter()) {				
					return edType.getFormatter().format(request, object);
				}
			}			
			return p.format(object, request.getLocale());									
		}
		catch (Exception ex) {
			ex.printStackTrace();			
			errores.add("no_convert_to_string", p.getName(), p.getMetaModel().getName());
			return "";
		}
	}
	
	
	public static String getUrl(MetaProperty p) {
		try {						
			if (p.hasValidValues()) return PREFIX + "validValuesEditor.jsp";
			return PREFIX + MetaWebEditors.getMetaEditorFor(p).getUrl();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return PREFIX + "notAvailableEditor.jsp";
		}
	}
		
	/** 
	 * Si a depende de b;
	 * @param a
	 * @param b
	 * @return
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
			ex.printStackTrace();
			System.err.println("¡ADVERTENCIA! Imposible determinar si " + a + " depende de " + b +". Asumimos falso");
			return false;
		}
	}

}
