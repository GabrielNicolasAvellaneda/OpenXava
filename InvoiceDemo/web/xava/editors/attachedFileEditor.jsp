<%@ include file="../imports.jsp"%>

<%@ page import="org.openxava.model.meta.MetaProperty" %>
<%@ page import="org.openxava.web.Ids" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.web.editors.FilePersistorFactory"%>
<%@ page import="org.openxava.web.editors.AttachedFile"%>

<%
String propertyKey = request.getParameter("propertyKey");
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
boolean editable="true".equals(request.getParameter("editable"));
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif=System.currentTimeMillis();
%>
<input id="<%=propertyKey%>" name="<%=propertyKey%>" type="hidden" value="<%=fvalue%>"/>
<%
AttachedFile file = null;
if (!Is.emptyString(fvalue)) file = FilePersistorFactory.getInstance().find(fvalue);
%>
<a href='<%=request.getContextPath()%>/xava/xfile?application=<%=applicationName%>&module=<%=module%>&fileId=<%=fvalue%>&dif=<%=dif%>' target="_blank" tabindex="1">
	<% if ( file != null ) { %>
		<%=file.getName()%>
	<% } %>
</a>
	
<% if (editable) { %>
	&nbsp;
	<span valign='middle'>
		<xava:image action='AttachedFiles.change' argv='<%="newFileProperty="+Ids.undecorate(propertyKey)%>'/>
		&nbsp;
		<xava:image action='AttachedFiles.delete' argv='<%="newFileProperty="+Ids.undecorate(propertyKey)%>'/>
	</span>
<% } %>	