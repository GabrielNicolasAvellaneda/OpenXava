package org.openxava.actions;

import java.util.*;
import javax.servlet.http.*;



import org.openxava.util.*;
import org.openxava.view.*;
import net.sf.jasperreports.engine.*;

/**
 * To generate your custom Jasper Report. <p>
 * 
 * You only need to overwrite the abstract methods.<br>
 * 
 * @author Javier Paniza
 * @author Daniel García Salas  
 */

abstract public class JasperReportBaseAction extends BaseAction implements IForwardAction, IModelAction {
	
	public static String PDF = "pdf";
	public static String EXCEL = "excel"; 
	public static String RTF = "rtf"; 
	
	private HttpServletRequest request;
	private View view;
	private String modelName;
	private String format = PDF;
	
	/**
	 * Data to print. <p>
	 * 
	 * If return null then a JDBC connection is sent to JasperReport,
	 * this is for the case of a SQL inside JasperReport design.
	 */
	abstract protected JRDataSource getDataSource() throws Exception;
	
	/**
	 * The name of the XML with the JasperReports design. <p>
	 * 
	 * If it is a relative path (as <code>reports/myreport.jrxml</code> has 
	 * to be in classpath. If it is a absolute path (as 
	 * <code>/home/java/reports/myreport.xml</code> or 
	 * <code>C:\\JAVA\\REPORTS\MYREPORT.JRXML</code> then it look at the 
	 * file system.
	 */
	abstract protected String getJRXML() throws Exception;
	
	/**
	 * Parameters to send to report.
	 */
	abstract protected Map getParameters() throws Exception;
	
	/**
	 * Output report format, it can be 'pdf' or 'excel'. <p>
	 */
	public String getFormat() throws Exception {
		return format;
	}

	/**
	 * Output report format, it can be 'pdf', 'excel' or 'rtf'. <p>
	 */	
	public void setFormat(String format) throws Exception {
		if (!EXCEL.equalsIgnoreCase(format) && 
			!PDF.equalsIgnoreCase(format) &&
			!RTF.equalsIgnoreCase(format)) {
			throw new XavaException("invalid_report_format", "'excel', 'pdf', 'rtf'");
		} 
		this.format = format;		
	}

	public void execute() throws Exception {		
		request.getSession().setAttribute("xava.report.design", getJRXML());
		request.getSession().setAttribute("xava.report.parameters", getParameters());
		request.getSession().setAttribute("xava.report.datasource", getDataSource());
		request.getSession().setAttribute("xava.report.modelName", modelName);
		request.getSession().setAttribute("xava.report.format", getFormat());
	}

	public String getForwardURI() {		
		return "/xava/report.pdf?time="+System.currentTimeMillis();
	}

	public boolean inNewWindow() {
		return true;
	}
	
	public void setRequest(HttpServletRequest request) {
		super.setRequest(request);
		this.request = request;
	}
		
	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	
	public void setModel(String modelName) { //  to obtain a JDCB connection, if required
		this.modelName = modelName;
	}
	
}