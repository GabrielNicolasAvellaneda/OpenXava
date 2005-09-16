<%@ include file="imports.jsp"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>


<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.view.meta.MetaPropertyView" %>
<%@ page import="org.openxava.web.WebEditors" %>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);

org.openxava.controller.ModuleManager manager = (org.openxava.controller.ModuleManager) context.get(request, "manager", "org.openxava.controller.ModuleManager");
String formName = manager.getForm();	
boolean throwsChanged=view.throwsPropertyChanged(p);
String scriptFoco = "onfocus=focus_property.value='" + propertyKey + "'";
String script = throwsChanged?
	"onchange='throwPropertyChanged(document." + formName + ", \"" + propertyKey + "\")' ":"";
script = script + scriptFoco;
Object value = request.getAttribute(propertyKey + ".value");

if (WebEditors.mustToFormat(p)) {
	String fvalue = WebEditors.format(request, p, value, errors);
	request.setAttribute(propertyKey + ".fvalue", fvalue);
}
boolean editable = view.isEditable(p);
String editableKey = propertyKey + "_EDITABLE_";
String labelKey = propertyKey + "_LABEL_";

int labelFormat = view.getLabelFormatForProperty(p);
String label = view.getLabelForProperty(p);
%>

<%@ include file="htmlTagsEditor.jsp"%>

<input type="hidden" name="<%=editableKey%>" value="<%=editable%>">

<%=preLabel%>
<% if (labelFormat == MetaPropertyView.NORMAL_LABEL) { %>
<%=label%>
<% } %>
<%=postLabel%>
<%=preIcons%>
<% if (p.isKey()) { %>
<img src="<%=request.getContextPath()%>/xava/images/key.gif"/>
<% } else if (p.isRequired()) { %>	
<img src="<%=request.getContextPath()%>/xava/images/required.gif"/>
<% } if (errors.memberHas(p)) { %>
<img src="<%=request.getContextPath()%>/xava/images/error.gif"/>
<% } %>
<%=postIcons%>
<%=preEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { 
	label = labelFormat == MetaPropertyView.SMALL_LABEL?label:"&nbsp;";
%>
<table border='0' cellpadding='0', cellspacing='0'><tr><td align='bottom' id='<%=labelKey%>'>
<span class=<%=style.getSmallLabel()%>><%=label%></span>

</td></tr>
<tr><td valign='middle'>
<% } %>
<%
String editorURL = WebEditors.getUrl(p);
char nexus = editorURL.indexOf('?') < 0?'?':'&';
editorURL = editorURL + nexus + "script="+script+"&editable="+editable;
%>
<jsp:include page="<%=editorURL%>"/>
<% 
if ((editable && view.isRepresentsEntityReference() && view.isLastKeyProperty(p)) || // with key visible
	(view.isRepresentsEntityReference() && view.isFirstPropertyAndViewHasNoKeys(p) && view.isKeyEditable())) // with key hidden
	{
	String referencedModel = p.getMetaModel().getName();
%>
	<% if (view.isSearch()) {%>
<xava:action action='<%=view.getSearchAction()%>' argv='<%="keyProperty="+propertyKey%>'/>
	<% } %>
	<% if (view.isCreateNew()) {%>
<xava:action action='Reference.createNew' argv='<%="model="+referencedModel + ",keyProperty=" + propertyKey%>'/>
	<% } %>
<% } %>
<%
if (editable || p.isReadOnly()) {
	java.util.Iterator itActions = view.getActionsNamesForProperty(p).iterator();
	while (itActions.hasNext()) {
		String action = (String) itActions.next();
%>
<xava:action action="<%=action%>"/>
<%
	}
}
%>

<%=postEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { %>
</td></tr>
</table>
<% } %>
