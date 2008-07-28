package org.openxava.web.servlets;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;

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
		String design = (String) request.getSession().getAttribute("xava.report.design");
		String format = (String) request.getSession().getAttribute("xava.report.format");
		try {
			ServletContext application = request.getSession().getServletContext();
			System.setProperty("jasper.reports.compile.class.path",					 
				application.getRealPath("/WEB-INF/lib/jasperreports.jar") +
				System.getProperty("path.separator") + 
				application.getRealPath("/WEB-INF/classes/")
			);
			
			Map parameters = (Map) request.getSession().getAttribute("xava.report.parameters");
			InputStream xmlDesign = null;
			if (isAbsolutePath(design)) {
				xmlDesign = new FileInputStream(design);
			}
			else {
				xmlDesign = GenerateCustomReportServlet.class.getResourceAsStream("/" + design);
			} 
			if (xmlDesign == null) throw new MalformedURLException("design_not_found"); 
			JasperReport report = JasperCompileManager.compileReport(xmlDesign);
			JRDataSource ds = (JRDataSource) request.getSession().getAttribute("xava.report.datasource");
			JasperPrint jprint = null;
			if (ds == null) {
				String modelName = (String) request.getSession().getAttribute("xava.report.modelName");
				Connection con = DataSourceConnectionProvider.getByComponent(modelName).getConnection();
				jprint = JasperFillManager.fillReport(report, parameters, con);
				con.close();
			}
			else {
				jprint = JasperFillManager.fillReport(report, parameters, ds);
			}

			if (format == null) {
				format = JasperReportBaseAction.PDF;
			}
				
			if (format.equals(JasperReportBaseAction.EXCEL)) {
				response.setContentType("application/vnd.ms-excel");
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
				exporter.exportReport();
			} 
			else {
				response.setContentType("application/pdf");
				JasperExportManager.exportReportToPdfStream(jprint, response.getOutputStream());
			}			
			
			request.getSession().removeAttribute("xava.report.parameters");
			request.getSession().removeAttribute("xava.report.design");
			request.getSession().removeAttribute("xava.report.datasource");
			request.getSession().removeAttribute("xava.report.modelName");
			request.getSession().removeAttribute("xava.report.format");
		} 
		catch (MalformedURLException ex) {
			if ("design_not_found".equals(ex.getMessage())) {
				throw new ServletException(XavaResources.getString("jasper_report_design_not_found", design));
			}
			else {
				log.error(ex.getMessage(), ex);
				throw new ServletException(XavaResources.getString("report_error"));				
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ServletException(XavaResources.getString("report_error"));
		}		
	}

	private boolean isAbsolutePath(String design) {
		return design.startsWith("/") || 
			(
				design.length() > 2 &&
				design.charAt(1) == ':' && 
				Character.isLetter(design.charAt(0))
			);
	}
			
}