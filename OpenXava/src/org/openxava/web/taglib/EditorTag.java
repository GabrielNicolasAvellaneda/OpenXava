package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.openxava.controller.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;


/**
 * @author Javier Paniza
 */

public class EditorTag extends TagSupport {
	
	private String property;		
	
	public int doStartTag() throws JspException {
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");
			org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
									
			String viewObject = request.getParameter("viewObject");
			viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
			View view = (View) context.get(request, viewObject);

			MetaProperty metaProperty = view.getMetaProperty(property); 

			String propertyPrefix = request.getParameter("propertyPrefix");
			propertyPrefix = (propertyPrefix == null || propertyPrefix.equals(""))?"xava." + view.getModelName() + ".":propertyPrefix;
			String propertyKey= propertyPrefix + metaProperty.getName();
			String valueKey = propertyKey + ".value";
			request.setAttribute(propertyKey, metaProperty);
			request.setAttribute(valueKey, view.getValue(metaProperty.getName()));
						
			Messages errors = (Messages) request.getAttribute("errors"); 									
			String formName = manager.getForm();	
			boolean throwsChanged=view.throwsPropertyChanged(metaProperty);
			String scriptFoco = "onfocus=focus_property.value='" + propertyKey + "'";
			String script = throwsChanged?
				"onchange='throwPropertyChanged(document." + formName + ", \"" + propertyKey + "\")' ":"";
			script = script + scriptFoco;

			boolean editable = view.isEditable(metaProperty);

			String editorURL = "../xava/" + org.openxava.web.WebEditors.getUrl(metaProperty);
			char nexus = editorURL.indexOf('?') < 0?'?':'&';
			editorURL = editorURL + nexus + "script="+script+"&editable="+editable+"&propertyKey="+propertyKey;

			Object value = request.getAttribute(propertyKey + ".value");
			if (org.openxava.web.WebEditors.mustToFormat(metaProperty)) {
				Object fvalue = org.openxava.web.WebEditors.formatToStringOrArray(request, metaProperty, value, errors);
				request.setAttribute(propertyKey + ".fvalue", fvalue);
			}
			
			String editableKey = propertyKey + "_EDITABLE_";
			pageContext.getOut().print("<input type='hidden' name='");
			pageContext.getOut().print(editableKey);
			pageContext.getOut().print("' value='");
			pageContext.getOut().print(editable);
			pageContext.getOut().println("'>");
			pageContext.include(editorURL);			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new JspException(XavaResources.getString("editor_tag_error", property));
		}	
		return SKIP_BODY;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;		
	}
	
}