package org.openxava.web.taglib;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.controller.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;


/**
 * @author Javier Paniza
 */

public class EditorTag extends TagSupport {
	
	private String property;		
	private boolean editable; 
	private boolean explicitEditable = false; 
	private boolean throwPropertyChanged; 
	private boolean explicitThrowPropertyChanged; 
	private static Log log = LogFactory.getLog(EditorTag.class);
	
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
			String propertyKey= propertyPrefix + property; 
			String valueKey = propertyKey + ".value";
			request.setAttribute(propertyKey, metaProperty);
			Object value = view.getValue(property);
			request.setAttribute(valueKey, value);
						
			Messages errors = (Messages) request.getAttribute("errors"); 									
			String formName = manager.getForm();	
			boolean throwsChanged=explicitThrowPropertyChanged?this.throwPropertyChanged:view.throwsPropertyChanged(property); 
			String scriptFoco = "onfocus=focus_property.value='" + propertyKey + "'";
			String script = throwsChanged?
				"onchange='throwPropertyChanged(document." + formName + ", \"" + propertyKey + "\")' ":"";
			script = script + scriptFoco;

			boolean editable = explicitEditable?this.editable:view.isEditable(property);  

			String editorURL = org.openxava.web.WebEditors.getUrl(metaProperty);
			char nexus = editorURL.indexOf('?') < 0?'?':'&';
			editorURL = editorURL + nexus + "script="+script+"&editable="+editable+"&propertyKey="+propertyKey;
			
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
			try {
				// If the JSP that uses this tag is in a subfolder
				pageContext.include("../xava/" + editorURL);
			}
			catch (Exception ex) {
				// If the JSP that uses this tag is in root folder
				pageContext.include("xava/" + editorURL);
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		this.explicitEditable = true;
	}

	public boolean isThrowPropertyChanged() {
		return throwPropertyChanged;
	}

	public void setThrowPropertyChanged(boolean throwPropertyChanged) {
		this.throwPropertyChanged = throwPropertyChanged;
		this.explicitThrowPropertyChanged = true;
	}

	
}