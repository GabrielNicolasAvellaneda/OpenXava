package org.openxava.web.servlets;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.event.*;
import javax.swing.table.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * To generate automatically reports from list mode. <p>
 * 
 * Uses JasperReports.
 * 
 * @author Javier Paniza
 */

public class GenerateReportServlet extends HttpServlet {
	
	private Log log = LogFactory.getLog(GenerateReportServlet.class);
	
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

		public boolean isCellEditable(int row, int column) {			
			return original.isCellEditable(row, column);
		}

		public Object getValueAt(int row, int column) {
			Object r = original.getValueAt(row, column);		

			if (r instanceof Boolean) {
				if (((Boolean) r).booleanValue()) return XavaResources.getString(locale, "yes");
				return XavaResources.getString(locale, "no");
			}
			if (withValidValues) {
				MetaProperty p = getMetaProperty(column);
				if (p.hasValidValues()) {					
					return p.getValidValueLabel(locale, original.getValueAt(row, column));
				}
			}
			
			if (r instanceof Time) {
				return DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(r);
			}
			
			if (r instanceof Timestamp){
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				return dateFormat.format( r );
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

		public void setValueAt(Object value, int row, int column) {
			original.setValueAt(value, row, column);			
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
			if (Users.getCurrent() == null) { // for a bug in websphere portal 5.1 with Domino LDAP
				Users.setCurrent((String)request.getSession().getAttribute("xava.user"));
			}						
			request.getParameter("application"); // for a bug in websphere 5.1 
			request.getParameter("module"); // for a bug in websphere 5.1		
			Tab tab = (Tab) request.getSession().getAttribute("xava_reportTab");
			tab.setRequest(request); 
			String uri = request.getRequestURI();
			if (uri.endsWith(".pdf")) {
				Map parameters = new HashMap();								
				parameters.put("Title", tab.getTitle());				
				parameters.put("Organization", getOrganization(request, tab));
				InputStream is  = getReport(request, response, tab);															
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
			log.error(ex.getMessage(), ex);
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
			
	private InputStream getReport(HttpServletRequest request, HttpServletResponse response, Tab tab) throws ServletException, IOException { 
		StringBuffer suri = new StringBuffer();
		suri.append("/xava/jasperReport");
		suri.append("?model=");
		suri.append(tab.getModelName());
		suri.append("&language=");		
		suri.append(Locales.getCurrent().getLanguage());
		suri.append("&tab=");
		suri.append(tab.getTabName());
		suri.append("&properties=");
		suri.append(tab.getPropertiesNamesAsString());
		return Servlets.getURIAsStream(request, response, suri.toString());		
	}
	
	private JRDataSource getDataSource(Tab tab, ServletRequest request) throws Exception {
		return new JRTableModelDataSource(getTableModel(tab, request, false));		
	}		  	
	
	private TableModel getTableModel(Tab tab, ServletRequest request, boolean labelAsHeader) throws Exception {
		return new TableModelDecorator(tab.getAllDataTableModel(), tab.getMetaProperties(), Locales.getCurrent(), labelAsHeader);
	}
	
}
