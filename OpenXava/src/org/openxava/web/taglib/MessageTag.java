package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class MessageTag extends TagSupport {
	
	private String key;
	private Object param;

	public int doStartTag() throws JspException {
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			String string = Is.empty(getParam())?XavaResources.getString(request, getKey()):XavaResources.getString(request, getKey(), getParam());
			pageContext.getOut().print(string);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(XavaResources.getString("message_tag_error", getKey()));				
		}
		return SKIP_BODY;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String string) {
		key = string;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}

}