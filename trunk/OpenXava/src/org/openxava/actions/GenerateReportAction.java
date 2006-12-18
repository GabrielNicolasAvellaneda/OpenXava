package org.openxava.actions;

import javax.servlet.http.*;



import org.openxava.tab.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class GenerateReportAction extends BaseAction implements IRequestAction, IForwardAction {
	
	private HttpServletRequest request;
	private Tab tab;
	private String type;
	

	public void execute() throws Exception {
		if (!("pdf".equals(getType()) || "csv".equals(getType()))) {
			throw new XavaException("report_type_not_supported", getType(), "pdf, csv");
		}
		request.getSession().setAttribute("xava_reportTab", getTab());
	}
	
	public boolean inNewWindow() {
		return true;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getForwardURI() {		
		return "/xava/list." + getType() + 
			"?application=" + request.getParameter("application") +
			"&module=" + request.getParameter("module");
	}

	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

}
