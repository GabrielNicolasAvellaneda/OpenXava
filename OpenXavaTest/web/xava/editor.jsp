<%@ include file="imports.jsp"%>

<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>


<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.view.meta.MetaPropertyView" %>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xava_view":viewObject;
org.openxava.view.View view = (org.openxava.view.View) context.get(request, viewObject);
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);

boolean editable = view.isEditable(p);
boolean lastSearchKey = view.isLastSearchKey(p); 
boolean throwPropertyChanged = view.throwsPropertyChanged(p);
if (lastSearchKey) {
	throwPropertyChanged = true; 
}
if (lastSearchKey || p.isSearchKey()) {
	editable = true;
}
	
String labelKey = propertyKey + "_LABEL_";

int labelFormat = view.getLabelFormatForProperty(p);
String label = view.getLabelFor(p);
%>
<%@ include file="htmlTagsEditor.jsp"%>
<%  
if (first && !view.isAlignedByColumns()) label = org.openxava.util.Strings.change(label, " ", "&nbsp;");
%>

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
<% } %> 
<span id="xava_error_image_<%=p.getQualifiedName()%>"> 
<% if (errors.memberHas(p)) { %>
<img src="<%=request.getContextPath()%>/xava/images/error.gif"/>
<% } %>
</span>
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
<span id="xava_editor_<%=view.getPropertyPrefix()%><%=p.getName()%>"> 
<xava:editor property="<%=p.getName()%>" editable="<%=editable%>" throwPropertyChanged="<%=throwPropertyChanged%>"/>
</span>
<% 
 if (lastSearchKey) {
	
	String referencedModel = p.getMetaModel().getName();
%>
	<% if (view.isSearch()) {%>
<xava:action action='<%=view.getSearchAction()%>' argv='<%="keyProperty="+propertyKey%>'/>
	<% } %>
	<% if (view.isCreateNew()) {%>
<xava:action action='Reference.createNew' argv='<%="model="+referencedModel + ",keyProperty=" + propertyKey%>'/>
	<% } %>
	<% if (view.isModify()) {%>
<xava:action action='Reference.modify' argv='<%="model="+referencedModel + ",keyProperty=" + propertyKey%>'/>	
	<% } %>
<% } %>
<%
for (java.util.Iterator itActions = view.getActionsNamesForReference(lastSearchKey).iterator(); itActions.hasNext();) {
	String action = (String) itActions.next();
%>
<xava:action action="<%=action%>"/> 
<%
}
%>	

<%
for (java.util.Iterator itActions = view.getActionsNamesForProperty(p, editable || p.isReadOnly()).iterator(); itActions.hasNext();) {
	String action = (String) itActions.next();
%>
<xava:action action="<%=action%>" argv='<%="xava.keyProperty="+propertyKey%>'/>
<%
}

%>

<%=postEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { %>
</td></tr>
</table>
<% } %>
