package org.openxava.actions;

import java.util.*;
import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.view.*;
import net.sf.jasperreports.engine.*;

/**
 * To generate your custom Jasper Report. <p>
 * 
 * You only need to overwrite the abstract methods.<br>
 * 
 * @author Javier Paniza
 */

abstract public class JasperReportBaseAction extends BaseAction implements IForwardAction, IModelAction {
	
	private HttpServletRequest request;
	private View view;
	private String modelName;
	private static Log log = LogFactory.getLog(JasperReportBaseAction.class);
	
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
	 * It has to be in classpath.
	 */
	abstract protected String getJRXML() throws Exception;
	
	/**
	 * Parameters to send to report.
	 */
	abstract protected Map getParameters() throws Exception;	

	public void execute() throws Exception {		
		request.getSession().setAttribute("xava.report.design", getJRXML());
		request.getSession().setAttribute("xava.report.parameters", getParameters());
		request.getSession().setAttribute("xava.report.datasource", getDataSource());
		request.getSession().setAttribute("xava.report.modelName", modelName);
	}

	public String getForwardURI() {		
		return "/xava/report.pdf";
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
