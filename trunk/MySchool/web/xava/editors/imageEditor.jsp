<%@ include file="../imports.jsp"%>

<%@ page import="org.openxava.model.meta.MetaProperty" %>

<%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
boolean editable="true".equals(request.getParameter("editable"));
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif=System.currentTimeMillis(); // to avoid browser caching
%>

<img name='<%=propertyKey%>' src='/<%=applicationName%>/xava/ximage?application=<%=applicationName%>&module=<%=module%>&property=<%=p.getName()%>&dif=<%=dif%>' title="<%=p.getDescription(request)%>"/>
	
<% if (editable) { %>	
<span valign='middle'>
	<xava:link action='ImageEditor.changeImage' argv='<%="newImageProperty="+p.getName()%>'/>
</span>
<% } %>	