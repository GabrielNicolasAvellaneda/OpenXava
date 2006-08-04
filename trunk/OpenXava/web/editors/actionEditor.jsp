<%@ include file="../imports.jsp"%>

<%@ page import="org.openxava.view.meta.MetaViewAction" %>

<%
String propertyKey = request.getParameter("propertyKey");
MetaViewAction p = (MetaViewAction) request.getAttribute(propertyKey);
boolean editable="true".equals(request.getParameter("editable"));

if (editable) {
%>
<xava:action action="<%=p.getAction()%>"/>
<%
}
%>
