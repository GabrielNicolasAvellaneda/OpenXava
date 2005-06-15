package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class ImageTag extends TagSupport implements IActionTag {
	
	private String action;
	private String argv;

	public int doStartTag() throws JspException {
		try {									
			ModuleContext context = (ModuleContext) pageContext.getSession().getAttribute("context");
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			ModuleManager manager = (ModuleManager) context.get(request, "manager");
			MetaAction metaAction = MetaControllers.getMetaAction(getAction());
			
			pageContext.getOut().print("<input name='");
			pageContext.getOut().print("xava.action.");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("' type='hidden'/>");
			
			pageContext.getOut().print("<a id='");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("'");			
			pageContext.getOut().print(" title='");
			pageContext.getOut().print(metaAction.getDescription(request));
			pageContext.getOut().print("'");			
			pageContext.getOut().print(" href=\"javascript:executeXavaAction(");
			pageContext.getOut().print(metaAction.isTakesLong());
			pageContext.getOut().print(", document.");
			pageContext.getOut().print(manager.getForm());
			pageContext.getOut().print(", '");
			pageContext.getOut().print(getAction());
			if (!Is.emptyString(getArgv())) {
				pageContext.getOut().print("', '");
				pageContext.getOut().print(getArgv());				
			}
			pageContext.getOut().print("')\">");
			pageContext.getOut().print("<img src='");
			pageContext.getOut().print(request.getContextPath() + "/xava/" + metaAction.getImage());
			pageContext.getOut().println("'");
			pageContext.getOut().print("\talt='");
			pageContext.getOut().print(metaAction.getDescription(request));
			pageContext.getOut().println("'");
			pageContext.getOut().println("\tborder='0' align='middle'/></a>");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(
				"Error: Tag imagen no se puede ejecutar");					
		}
		return SKIP_BODY;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String string) {
		action = string;
	}

	public String getArgv() {
		return argv;
	}

	public void setArgv(String string) {
		argv = string;
	}

}