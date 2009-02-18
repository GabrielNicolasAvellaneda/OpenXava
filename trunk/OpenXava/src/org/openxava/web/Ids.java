package org.openxava.web;

import javax.servlet.*;

/**
 * To decorate ids used for HTML elements.
 * 
 * @author Javier Paniza
 */

public class Ids {

	public static String decorate(String application, String module, String name) {
		if (name == null) return null;
		if (name.startsWith("ox_")) return  name;
		return "ox_" + application + "_" + module + "__" + name;
	}
	
	public static String decorate(ServletRequest request, String name) {
		return decorate(
			request.getParameter("application"), 
			request.getParameter("module"), 
			name);
	}
	
	public static String undecorate(String name) {
		if (name == null) return null;
		if (!name.startsWith("ox_")) return name;
		return name.substring(name.indexOf("__") + 2);		
	}

}
