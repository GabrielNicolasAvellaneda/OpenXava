package org.openxava.web.servlets;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.util.*;

import dori.jasper.engine.*;
import dori.jasper.engine.data.*;

/**
 * @author Javier Paniza
 */

public class GenerateReportServlet extends HttpServlet {
	
	private static Map changeInLabel;


	public static class TableModelDecorator implements TableModel {
							 
		private TableModel original;		
		private List metaProperties;
		private boolean withValidValues = false;
		private Locale locale;
		private boolean labelAsHeader = false;
		
		public TableModelDecorator(TableModel original, List metaProperties, Locale locale, boolean labelAsHeader) throws Exception {
			this.original = original;
			this.metaProperties = metaProperties;
			this.locale = locale;
			this.withValidValues = calculateWithValidValues();
			this.labelAsHeader = labelAsHeader;
		}

		private boolean calculateWithValidValues() {
			Iterator it = metaProperties.iterator();
			while (it.hasNext()) {
				MetaProperty m = (MetaProperty) it.next();
				if (m.hasValidValues()) return true;
			}
			return false;
		}
		
		private MetaProperty getMetaProperty(int i) {
			return (MetaProperty) metaProperties.get(i);
		}

		public int getRowCount() {			
			return original.getRowCount();
		}

		public int getColumnCount() {			
			return original.getColumnCount();
		}

		public String getColumnName(int c) {			
			return labelAsHeader?getMetaProperty(c).getLabel(locale):Strings.change(getMetaProperty(c).getQualifiedName(), ".", "_");
		}

		public Class getColumnClass(int c) {						
			return original.getColumnClass(c);
		}

		public boolean isCellEditable(int fila, int columna) {			
			return original.isCellEditable(fila, columna);
		}

		public Object getValueAt(int fila, int columna) {
			Object r = original.getValueAt(fila, columna);						
			if (r instanceof Boolean) {
				if (((Boolean) r).booleanValue()) return XavaResources.getString(locale, "yes");
				return XavaResources.getString(locale, "no");
			}
			if (withValidValues) {
				MetaProperty p = getMetaProperty(columna);
				if (p.hasValidValues()) {					
					return p.getValidValueLabel(locale, original.getValueAt(fila, columna));
				}
			}
			
			
			if (r instanceof java.util.Date) {
				return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(r);
			}

			if (r instanceof java.math.BigDecimal) {
				NumberFormat nf = NumberFormat.getNumberInstance(locale);
				nf.setMinimumFractionDigits(2);
				return nf.format(r);
			}
			
			return r;
		}

		public void setValueAt(Object valor, int fila, int columna) {
			original.setValueAt(valor, fila, columna);			
		}

		public void addTableModelListener(TableModelListener l) {
			original.addTableModelListener(l);			
		}

		public void removeTableModelListener(TableModelListener l) {
			original.removeTableModelListener(l);			
		}
	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Tab tab = (Tab) request.getSession().getAttribute("xava_reportTab");			
			String uri = request.getRequestURI(); 
			if (uri.endsWith(".pdf")) {
				Map parameters = new HashMap();
				tab.setRequest(request);				
				parameters.put("Title", tab.getTitle());				
				parameters.put("Organization", getOrganization(request, tab));
				InputStream is  = getReport(request, tab);															
				JRDataSource ds = getDataSource(tab, request);		
				JasperPrint jprint = JasperFillManager.fillReport(is, parameters, ds);					
				response.setContentType("application/pdf");				
				JasperExportManager.exportReportToPdfStream(jprint, response.getOutputStream());													 	
			}
			else if (uri.endsWith(".csv")) {
				response.setContentType("text/x-csv");
				response.getWriter().print(TableModels.toCSV(getTableModel(tab, request, true)));
			}
			else {
				throw new ServletException(XavaResources.getString("report_type_not_supported", "", ".pdf .csv"));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(XavaResources.getString("report_error"));
		}		
	}
	
	protected String getOrganization(HttpServletRequest request, Tab tab) throws MissingResourceException, XavaException {
		Locale locale = XavaResources.getLocale(request);
		if (Labels.exists("xava.organization", locale)) {
			return Labels.get("xava.organization", locale);
		}		
		else {
			return "";
		}
	}
			
	private InputStream getReport(HttpServletRequest request, Tab tab) throws IOException {
		StringBuffer surl = new StringBuffer("http://localhost:");
		surl.append(request.getServerPort());		
		surl.append(request.getContextPath());		
		surl.append("/xava/jasperReport");
		surl.append("?model=");
		surl.append(tab.getModelName());
		surl.append("&language=");		
		surl.append(request.getLocale().getLanguage());
		surl.append("&tab=");
		surl.append(tab.getTabName());
		surl.append("&properties=");
		surl.append(tab.getPropertiesNamesAsString());						
		URL url = new URL(surl.toString());		
		return url.openStream();
	}
	
	private JRDataSource getDataSource(Tab tab, ServletRequest request) throws Exception {
		return new JRTableModelDataSource(getTableModel(tab, request, false));		
	}		  	
	
	private TableModel getTableModel(Tab tab, ServletRequest request, boolean labelAsHeader) throws Exception {
		return new TableModelDecorator(tab.getAllDataTableModel(), tab.getMetaProperties(), request.getLocale(), labelAsHeader);
	}
	
}
