package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.commons.logging.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.web.style.*;

/**
 * @author Javier Paniza
 */

public class ButtonTag extends TagSupport implements IActionTag{ 
	
	private String argv; 
	 
	private static Log log = LogFactory.getLog(ButtonTag.class);
	
	private String action;
	
	public int doStartTag() throws JspException {
		try {				
			if (Is.emptyString(getAction())) {  
				return SKIP_BODY;
			}

			ModuleContext context = (ModuleContext) pageContext.getSession().getAttribute("context");
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			ModuleManager manager = (ModuleManager) context.get(request, "manager");
			MetaAction metaAction = MetaControllers.getMetaAction(getAction());
						
			pageContext.getOut().print("<input name='");
			pageContext.getOut().print("xava.action.");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("' type='hidden'/>");			
			pageContext.getOut().print("<button id='");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("'");
			pageContext.getOut().print(" title='");
			pageContext.getOut().print(metaAction.getKeystroke() + " - " + metaAction.getDescription(request));
			pageContext.getOut().print("'");
			pageContext.getOut().print(" class=");
			Style style = (Style) request.getAttribute("style");
			pageContext.getOut().print(style.getButton());
			pageContext.getOut().print("\tonclick='executeXavaAction(");
			pageContext.getOut().print('"');				
			pageContext.getOut().print(metaAction.getConfirmMessage(request));
			pageContext.getOut().print('"');
			pageContext.getOut().print(", ");
			pageContext.getOut().print(metaAction.isTakesLong());
			pageContext.getOut().print(", document.");
			pageContext.getOut().print(manager.getForm());
			pageContext.getOut().print(", \"");
			pageContext.getOut().print(getAction());
			pageContext.getOut().print('"');
			if (!Is.emptyString(getArgv())) { 
				pageContext.getOut().print(", \"");
				pageContext.getOut().print(getArgv());
				pageContext.getOut().print('"');
			}
			pageContext.getOut().println(")'>");
			pageContext.getOut().print("\t");
			pageContext.getOut().println(metaAction.getLabel(request));
			pageContext.getOut().println("</button>");
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new JspException(XavaResources.getString("button_tag_error"));				
		}
		return SKIP_BODY;
	}
	
	public String getArgv() { 
		return argv; 
	} 
		 
	public void setArgv(String string) { 
		argv = string; 
	} 
		
	public String getAction() {
		return action;
	}

	public void setAction(String string) {
		action = string;
	}

}