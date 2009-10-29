package org.openxava.web;

import javax.servlet.*;
import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;

/**
 * To decorate ids used for HTML elements.
 * 
 * @author Javier Paniza
 */

public class Ids {

	public static String decorate(String application, String module, String name) {
		if (name == null) return null;
		name = name.replaceAll("\\.", "___"); 
		if (name.startsWith("ox_")) return name;
		return "ox_" + application + "_" + module + "__" + name;
	}
	
	public static String decorate(HttpServletRequest request, String name) { 
		ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");
		String module = (String) context.get(request, "xava_currentModule");
		if (Is.empty(module)) module = request.getParameter("module");
		return decorate(				
			request.getParameter("application"), 
			module, 
			name);
	}
	
	public static String undecorate(String name) {
		if (name == null) return null;
		name = name.replaceAll("___", ".");
		if (!name.startsWith("ox_")) return name;
		return name.substring(name.indexOf("__") + 2);		
	}

}
