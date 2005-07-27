package org.openxava.web.servlets;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.*;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class JasperReportServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		try {
			String model = request.getParameter("model");
			String language = request.getParameter("language");
			String tab = request.getParameter("tab");
			String properties = request.getParameter("properties");
			
			ServletContext application = request.getSession().getServletContext();		
									
			System.setProperty("jasper.reports.compile.class.path",					 
					application.getRealPath("/WEB-INF/lib/jasperreports.jar") +
					System.getProperty("path.separator") + 
					application.getRealPath("/WEB-INF/classes/")
					);											
			JasperCompileManager.compileReportToStream(getCorrienteListado(request, model, language, tab, properties), response.getOutputStream());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(XavaResources.getString("jasper_error"));
		}		
	}
	
	private InputStream getCorrienteListado(HttpServletRequest request, String modelo, String lenguaje, String tab, String properties) throws IOException {
		StringBuffer surl = new StringBuffer("http://localhost:");
		surl.append(request.getServerPort());		
		surl.append(request.getRequestURI());
		surl.append(".jsp?model=");
		surl.append(modelo);
		surl.append("&language=");
		surl.append(lenguaje);
		surl.append("&tab=");
		surl.append(tab);
		surl.append("&properties=");
		surl.append(properties);						
		URL url = new URL(surl.toString());		
		return url.openStream();
	}
	
}
