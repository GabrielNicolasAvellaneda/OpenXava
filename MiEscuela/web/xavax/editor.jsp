<%@ include file="imports.jsp"%>

<jsp:useBean id="errors" class="org.xavax.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>


<%@ page import="org.xavax.model.meta.MetaProperty" %>
<%@ page import="org.xavax.view.meta.MetaPropertyView" %>
<%@ page import="org.xavax.web.WebEditors" %>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xavax_view":viewObject;
org.xavax.view.View view = (org.xavax.view.View) context.get(request, viewObject);
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);

boolean throwsChanged=view.throwsPropertyChanged(p);
String scriptFoco = "onfocus=focus_property.value='" + propertyKey + "'";
String script = throwsChanged?
	"onchange='throwPropertyChanged(\"" + propertyKey + "\")' ":"";
script = script + scriptFoco;
Object value = request.getAttribute(propertyKey + ".value");

if (WebEditors.mustToFormat(p)) {
	String fvalue = WebEditors.format(request, p, value, errors);
	request.setAttribute(propertyKey + ".fvalue", fvalue);
}
boolean editable = view.isEditable(p);
String editableKey = propertyKey + "_EDITABLE_";

int labelFormat = view.getLabelFormatForProperty(p);

%>

<%@ include file="htmlTagsEditor.jsp"%>

<input type="hidden" name="<%=editableKey%>" value="<%=editable%>">

<%=preLabel%>
<% if (labelFormat == MetaPropertyView.NORMAL_LABEL) { %>
<%=p.getLabel(request)%>
<% } %>
<%=postLabel%>
<%=preIcons%>
<% if (p.isKey()) { %>
<img src="images/key.gif"/>
<% } else if (p.isRequired()) { %>	
<img src="images/required.gif"/>
<% } if (errors.memberHas(p)) { %>
<img src="images/error.gif"/>
<% } %>
<%=postIcons%>
<%=preEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { 
	String label = labelFormat == MetaPropertyView.SMALL_LABEL?p.getLabel(request):"&nbsp;";
%>
<table border='0' cellpadding='0', cellspacing='0'><tr><td align='bottom'>
<span class='smallLabel'><%=label%></span>

</td></tr>
<tr><td valign='middle'>
<% } %>
<jsp:include page="<%=WebEditors.getUrl(p)%>">
	<jsp:param name="script" value="<%=script%>"/>
	<jsp:param name="editable" value="<%=editable%>"/>
</jsp:include>
<% if (editable && view.isRepresentsEntityReference() && view.isLastPropertyKey(p)) {
	String referencedModel = p.getMetaModel().getName();
%>
<xavax:image action="<%=view.getSearchAction()%>" argv="<%="keyProperty="+propertyKey%>"/>
	<% if (view.isCreateNew()) {%>
<xavax:link action='Reference.createNew' argv='<%="model="+referencedModel + ",keyProperty=" + propertyKey%>'/>
	<% } %>
<% } %>
<%
if (editable) {
	java.util.Iterator itActions = view.getActionsNamesForProperty(p).iterator();
	while (itActions.hasNext()) {
		String action = (String) itActions.next();
%>
<xavax:action action="<%=action%>"/>
<%
	}
}
%>

<%=postEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { %>
</td></tr>
</table>
<% } %>
