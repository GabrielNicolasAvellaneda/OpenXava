package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.commons.logging.*;
import org.openxava.controller.*;
import org.openxava.util.*;
import org.openxava.web.*;

/**
 * Logic used by OX for generated ids for the HTML parts. <p>
 * 
 * @author Javier Paniza
 */

public class IdTag extends TagSupport {
	
	private static Log log = LogFactory.getLog(IdTag.class);
	
	private String name;
	
	public int doStartTag() throws JspException {		
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");
			String module = (String) context.get(request, "xava_currentModule");
			if (Is.empty(module)) module = request.getParameter("module");
			pageContext.getOut().print(
				Ids.decorate(
					request.getParameter("application"),
					module,
					name
				)
			);		
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new JspException(XavaResources.getString("id_tag_error", name));				
		}

		return SKIP_BODY;
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}