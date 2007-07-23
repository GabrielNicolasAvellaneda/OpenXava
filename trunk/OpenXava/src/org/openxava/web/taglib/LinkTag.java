package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.commons.logging.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class LinkTag extends TagSupport implements IActionTag {

	private static Log log = LogFactory.getLog(LinkTag.class);
	
	private String action;
	private String argv;
	private String cssClass;
	private boolean hasBody;
		
	public int doStartTag() throws JspException {		
		try {
			if (Is.emptyString(getAction())) {  
				return SKIP_BODY;
			}
			hasBody=false;
			ModuleContext context =
				(ModuleContext) pageContext.getSession().getAttribute(
					"context");
			ModuleManager manager =
				(ModuleManager) context.get(
					(HttpServletRequest) pageContext.getRequest(),
					"manager");
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			MetaAction metaAction = MetaControllers.getMetaAction(getAction());
						 		
			pageContext.getOut().print("<input name='");
			pageContext.getOut().print("xava.action.");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("' type='hidden'/>\n");
			
			pageContext.getOut().print("<a id='");
			pageContext.getOut().print(getAction());
			pageContext.getOut().println("'");	
			if (!Is.emptyString(getCssClass())) {
				pageContext.getOut().print(" class='");
				pageContext.getOut().print(getCssClass());
				pageContext.getOut().print("'");	
			}
			pageContext.getOut().print(" title='");
			pageContext.getOut().print(metaAction.getKeystroke() + " - " +  metaAction.getDescription(request));
			pageContext.getOut().print("'");
			
			pageContext.getOut().print(" href=\"javascript:executeXavaAction(");
			pageContext.getOut().print("'");
			pageContext.getOut().print(metaAction.getConfirmMessage(request));
			pageContext.getOut().print("'");
			pageContext.getOut().print(", ");			
			pageContext.getOut().print(metaAction.isTakesLong());
			pageContext.getOut().print(", document.");
			
			pageContext.getOut().print(manager.getForm());
			pageContext.getOut().print(", '");
			pageContext.getOut().print(getAction());
			pageContext.getOut().print("'");
			if (!Is.emptyString(getArgv())) {
				pageContext.getOut().print(", '");
				pageContext.getOut().print(getArgv());
				pageContext.getOut().print("'");
			}
			pageContext.getOut().print(")\">");

		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new JspException(XavaResources.getString("link_tag_error", getAction()));
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doAfterBody() throws JspException {					
		hasBody = true;
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		try {
			if (!hasBody) {
				pageContext.getOut().print(
					MetaControllers.getMetaAction(getAction()).getLabel(
						pageContext.getRequest()));								
			}			
			pageContext.getOut().print("</a>");
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new JspException(XavaResources.getString("link_tag_error", getAction()));
		}
		return super.doEndTag();
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

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

}