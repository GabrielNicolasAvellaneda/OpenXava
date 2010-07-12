package org.openxava.web.servlets;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;

import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.util.*;

/**
 * To generate custom Jasper Reports from that extends <code>JasperReportBaseAction</code>.
 * 
 * @author Javier Paniza
 * @author Daniel García Salas
 */

public class GenerateCustomReportServlet extends HttpServlet { 	

	private static Log log = LogFactory.getLog(GenerateCustomReportServlet.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String format = (String) request.getSession().getAttribute("xava.report.format");
		JasperPrint jprint = (JasperPrint) request.getSession().getAttribute("xava.report.jprint");
		request.getSession().removeAttribute("xava.report.format"); 
		request.getSession().removeAttribute("xava.report.jprint"); 
		try {
			if (format == null) {
				format = JasperReportBaseAction.PDF;
			}
				
			JRExporter exporter;
			if (format.equals(JasperReportBaseAction.EXCEL)) {
				response.setContentType("application/vnd.ms-excel");
				exporter = new JRXlsExporter();
			} 
			else if (format.equalsIgnoreCase(JasperReportBaseAction.RTF)) { 				
				response.setContentType("application/rtf"); 
				response.setHeader("Content-Disposition", "inline; filename=\"report.rtf\""); 
				exporter = new JRRtfExporter() ;//
			} 			
			else if (format.equalsIgnoreCase(JasperReportBaseAction.ODT)) {  				
				response.setContentType("application/vnd.oasis.opendocument.text");
				response.setHeader("Content-Disposition", "inline; filename=\"report.odt\""); 
				exporter = new JROdtExporter();
			}
			else {
				response.setContentType("application/pdf");
				exporter = new JRPdfExporter();				
			}
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
			exporter.exportReport();
		} 
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServletException(XavaResources.getString("report_error"));
		}		
	}

}