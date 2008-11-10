package org.openxava.actions;

import javax.servlet.http.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class GenerateReportAction extends TabBaseAction implements IForwardAction {
	
	private HttpServletRequest request;
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
		super.setRequest(request);
		this.request = request;
	}

	public String getForwardURI() {		
		return "/xava/list." + getType() + 
			"?application=" + request.getParameter("application") +
			"&module=" + request.getParameter("module") +
			"&time=" + System.currentTimeMillis();
	}

	public String getType() {
		return type;
	}

	public void setType(String string) {
		type = string;
	}

}
