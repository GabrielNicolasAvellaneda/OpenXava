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
	
int labelFormat = view.getLabelFormatForProperty(p);
String label = view.getLabelFor(p);
%>
<%@ include file="htmlTagsEditor.jsp"%>
<%  
if (first && !view.isAlignedByColumns()) label = org.openxava.util.Strings.change(label, " ", "&nbsp;");
%>

<%=preLabel%>
<% if (labelFormat == MetaPropertyView.NORMAL_LABEL) { %>
<span id="<xava:id name='<%="label_" + view.getPropertyPrefix() + p.getName()%>'/>">
<%=label%>
</span>
<% } %>
<%=postLabel%>
<%=preIcons%>
<% if (p.isKey()) { %>
<img src="<%=request.getContextPath()%>/xava/images/key.gif"/>
<% } else if (p.isRequired()) { %>	
<img src="<%=request.getContextPath()%>/xava/images/required.gif"/>
<% } %> 
<span id="<xava:id name='<%="error_image_" + p.getQualifiedName()%>'/>"> 
<% if (errors.memberHas(p)) { %>
<img src="<%=request.getContextPath()%>/xava/images/error.gif"/>
<% } %>
</span>
<%=postIcons%>
<%=preEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { 	
%>
<table border='0' cellpadding='0', cellspacing='0'><tr><td align='bottom'>
<span id="<xava:id name='<%="label_" + view.getPropertyPrefix() + p.getName()%>'/>" class="<%=style.getSmallLabel()%>"><%=label%></span>

</td></tr>
<tr><td style='vertical-align: middle'>
<% } %>
<span id="<xava:id name='<%="editor_" + view.getPropertyPrefix() + p.getName()%>'/>"> 
<xava:editor property="<%=p.getName()%>" editable="<%=editable%>" throwPropertyChanged="<%=throwPropertyChanged%>"/>
</span>
<% if (view.propertyHasActions(p)) { %>
<span id="<xava:id name='<%="property_actions_" + view.getPropertyPrefix() + p.getName()%>'/>">
<jsp:include page="propertyActions.jsp">
	<jsp:param name="propertyName" value="<%=p.getName()%>"/>
	<jsp:param name="lastSearchKey" value="<%=lastSearchKey%>"/>
	<jsp:param name="editable" value="<%=editable%>"/>
</jsp:include>
</span>
<% } %>

<%=postEditor%>
<% if (labelFormat == MetaPropertyView.SMALL_LABEL) { %>
</td></tr>
</table>
<% } %>
