<%@ include file="../imports.jsp"%>

<%@ page import="org.openxava.model.meta.MetaProperty" %>

<%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
boolean editable="true".equals(request.getParameter("editable"));
String disabled=editable?"":"disabled";
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif=System.currentTimeMillis(); // para evitar que el navegador cachee
%>

<img src='ximage?application=<%=applicationName%>&module=<%=module%>&property=<%=p.getName()%>&dif=<%=dif%>' title="<%=p.getDescription(request)%>"/>
	
<% if (editable) { %>	
<span valign='middle'>
	<xava:link action='ImageEditor.changeImage' argv='<%="newImageProperty="+p.getName()%>'/>
</span>
<% } %>	