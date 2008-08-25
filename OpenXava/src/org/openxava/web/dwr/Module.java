package org.openxava.web.dwr;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import org.openxava.controller.*;
import org.openxava.util.*;
import org.openxava.web.servlets.*;

/**
 * For accessing to module execution from DWR. <p> 
 * 
 * @author Javier Paniza
 */

public class Module extends DWRBase {

	final private static String MESSAGES_LAST_REQUEST ="xava_messagesLastRequest";
	final private static String ERRORS_LAST_REQUEST ="xava_errorsLastRequest";
	
	public static String request(HttpServletRequest request, HttpServletResponse response, String application, String module, Map values, Map multipleValues, String [] selected) throws Exception {
		checkSecurity(request, application, module);
		restoreLastMessages(request, application, module);
		InputStream is = Servlets.getURIAsStream(request, response, getQueryString(application, module, values, multipleValues, selected));
		String forwardURI = (String) request.getSession().getAttribute("xava_forward");		
		if (!Is.emptyString(forwardURI)) {
			String forwardInNewWindow = (String) request.getSession().getAttribute("xava_forward_inNewWindow");
			String name = ("true".equals(forwardInNewWindow))?"xava_forward_in_new_window":"xava_forward";
			request.getSession().removeAttribute("xava_forward");
			request.getSession().removeAttribute("xava_forward_inNewWindow");
			String result =  name + "=" + request.getScheme() + "://" + 
				request.getServerName() + ":" + request.getServerPort() + 
				request.getContextPath() + forwardURI;			
			return result;
		}		
		return InputStreams.toString(is);
	}	
	
	private static ModuleManager getManager(HttpServletRequest request, String application, String module) {
		return (ModuleManager) getContext(request).get(application, module, "manager");
	}

	public static void requestMultipart(HttpServletRequest request, HttpServletResponse response, String application, String module) throws Exception { 
		checkSecurity(request, application, module);
		Servlets.getURIAsStream(request, response, getQueryString(application, module, null, null, null));
		memorizeLastMessages(request, application, module);		
	}
	
	private static void memorizeLastMessages(HttpServletRequest request, String application, String module) {
		ModuleContext context = getContext(request);
		context.put(application, module, MESSAGES_LAST_REQUEST, request.getAttribute("messages"));
		context.put(application, module, ERRORS_LAST_REQUEST, request.getAttribute("errors"));
	}
	
	private static void restoreLastMessages(HttpServletRequest request, String application, String module) { 
		ModuleContext context = getContext(request);		
		if (context.exists(application, module, MESSAGES_LAST_REQUEST)) {
			Messages messages = (Messages) context.get(application, module, MESSAGES_LAST_REQUEST);
			request.setAttribute("messages", messages);
			context.remove(application, module, MESSAGES_LAST_REQUEST);			
		}
		if (context.exists(application, module, ERRORS_LAST_REQUEST)) {
			Messages errors = (Messages) context.get(application, module, ERRORS_LAST_REQUEST);
			request.setAttribute("errors", errors);
			context.remove(application, module, ERRORS_LAST_REQUEST);			
		}		
	}
		
	private static String getQueryString(String application, String module, Map values, Map multipleValues, String[] selected) {
		StringBuffer result = new StringBuffer("/xava/core.jsp?application=");
		result.append(application);
		result.append("&module=");
		result.append(module);
		addValuesQueryString(result, values, multipleValues, selected);		
		return result.toString();
	}

	private static void addValuesQueryString(StringBuffer sb, Map values, Map multipleValues, String [] selected) {
		if (values == null) return;
		if (multipleValues != null) {			
			for (Iterator it=multipleValues.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry en = (Map.Entry) it.next();			
				addMultipleValuesQueryString(sb, en.getKey(), en.getValue());
				values.remove(en.getKey());
			}					
		}
		for (Iterator it=values.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry en = (Map.Entry) it.next();
			if (!en.getKey().toString().equals("xava_selected")) { 
				sb.append('&');
				sb.append(en.getKey()); 
				sb.append('=');
				sb.append(filterValue(en.getValue()));
			}
		}
		for (int i=0; i<selected.length; i++) {
			String [] s = selected[i].split(":");
			sb.append('&');
			sb.append(s[0]);
			sb.append('=');
			sb.append(s[1]);
		}							
	}
	
	private static void addMultipleValuesQueryString(StringBuffer sb, Object key, Object value) {
		if (value == null) return;
		String [] tokens = value.toString().split("\n");
		for (int i=1; i< tokens.length - 1; i++) {
			sb.append('&');
			sb.append(key);
			sb.append('=');			
			sb.append(tokens[i].substring(tokens[i].indexOf('"') + 1, tokens[i].lastIndexOf('"')));
		}
	}

	private static Object filterValue(Object value) {
		if (value == null) return null;
		String s = value.toString()
			.replaceAll("%", "%25")
			.replaceAll("&", "%26")
			.replaceAll("=", "%3d");
		if (s.startsWith("[reference:")) return "true";
		return s;
	}

}
