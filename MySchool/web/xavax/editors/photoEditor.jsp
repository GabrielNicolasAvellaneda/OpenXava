<%@ include file="../imports.jsp"%>

<%@ page import="org.xavax.model.meta.MetaProperty" %>
<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>

<%
String viewObject = request.getParameter("viewObject");
viewObject = (viewObject == null || viewObject.equals(""))?"xavax_view":viewObject;
org.xavax.view.View view = (org.xavax.view.View) context.get(request, viewObject);
MetaProperty p = (MetaProperty) view.getMetaPropertyFoto();
boolean editable=view.isEditable(p);
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif=System.currentTimeMillis(); // to avoid the navigator cache
%>
	
<% if (editable) { %>	
<xavax:link action='ImageEditor.changeImage' argv='<%="newImageProperty="+p.getName()%>'>
	<img src='ximage?applicacion=<%=applicationName%>&module=<%=module%>&property=<%=p.getName()%>&dif=<%=dif%>' 
		alt='<fmt:message key="change_image"/>'
		longdesc='<fmt:message key="change_image"/>'		
		height=100
	/>	
</xavax:link>
<% } else { %>	
<img src='ximage?application=<%=applicationName%>&module=<%=module%>&property=<%=p.getName()%>&dif=<%=dif%>'/>
<% } %>