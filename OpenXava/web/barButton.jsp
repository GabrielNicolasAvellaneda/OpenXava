<%@ include file="imports.jsp"%>

<%@page import="org.openxava.util.XavaPreferences"%>
<%@page import="org.openxava.util.Is"%>
<%@page import="org.openxava.controller.meta.MetaControllers"%>
<%@page import="org.openxava.controller.meta.MetaAction"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
boolean showLabels = XavaPreferences.getInstance().isShowLabelsForToolBarActions();
String actionName = request.getParameter("action");
if (!Is.emptyString(actionName)) {
	MetaAction action = MetaControllers.getMetaAction(request.getParameter("action"));
	String argv = request.getParameter("argv");
	String label = action.getLabel(request); 
%>

	
<span class="<%=style.getButtonBarButton()%>">
	<xava:link action="<%=action.getQualifiedName()%>" argv='<%=argv%>'>
		<% boolean showLabel = (showLabels || !action.hasImage()) && !Is.emptyString(label); %>
		<% if (action.hasImage()) { %>
		<span style="padding:4px; background: url(<%=request.getContextPath()%>/xava/<%=action.getImage()%>) no-repeat 5px 50%;">		
		<% if (!showLabel) { %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<% } else { %>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<% } %>
		</span>		
		<% } else {%>
		&nbsp;
		<% } %>
		<% if (showLabel) { %>			 				 			
		<%=label%>
		&nbsp;
		<% } %>		
	</xava:link>
</span>
<%
}
%>