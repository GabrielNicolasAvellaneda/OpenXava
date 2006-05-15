<%@ include file="imports.jsp"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<%-- ¿Context y estilo son necesarios?--%>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>


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
// tmp String formName = manager.getForm();	
// tmp boolean throwsChanged=view.throwsPropertyChanged(p);
// tmp String scriptFoco = "onfocus=focus_property.value='" + propertyKey + "'";
// tmp String script = throwsChanged?
// tmp 	"onchange='throwPropertyChanged(document." + formName + ", \"" + propertyKey + "\")' ":"";
// tmp script = script + scriptFoco;

// tmp Object value = request.getAttribute(propertyKey + ".value");
// tmp if (WebEditors.mustToFormat(p)) {
// tmp 	Object fvalue = WebEditors.formatToStringOrArray(request, p, value, errors);
// tmp 	request.setAttribute(propertyKey + ".fvalue", fvalue);
// tmp }
boolean editable = view.isEditable(p);
// tmp String editableKey = propertyKey + "_EDITABLE_";
String labelKey = propertyKey + "_LABEL_";

int labelFormat = view.getLabelFormatForProperty(p);
String label = view.getLabelFor(p);
%>

<%@ include file="htmlTagsEditor.jsp"%>

<%-- tmp 
<input type="hidden" name="<%=editableKey%>" value="<%=editable%>">
--%>

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
<tr><td style='vertical-align: middle'>
<% } %>
<%
// tmp String editorURL = WebEditors.getUrl(p);
// tmp char nexus = editorURL.indexOf('?') < 0?'?':'&';
// tmp editorURL = editorURL + nexus + "script="+script+"&editable="+editable;
%>
<%--tmp 
<xava:include page="<%=editorURL%>"/>
--%>
<xava:editor property="<%=p.getName()%>"/>
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
