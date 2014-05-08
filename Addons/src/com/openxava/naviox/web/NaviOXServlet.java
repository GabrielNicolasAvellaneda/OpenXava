package com.openxava.naviox.web;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.openxava.util.*;
import org.openxava.web.style.*;

/**
 * 
 * @author Javier Paniza
 */
public class NaviOXServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String [] uri = request.getRequestURI().split("/");
		if (uri.length < 4) {
			response.getWriter().print(XavaResources.getString(request, "module_name_missing"));
			return;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(
			"/naviox/index.jsp?application=" + uri[1] + "&module=" + uri[3]);
		
		Style.setPotalInstance(NaviOXStyle.getInstance()); // We manage style in NaviOX as in the portal case, to override the style defined in xava.properties and by device 
		dispatcher.forward(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	

}
