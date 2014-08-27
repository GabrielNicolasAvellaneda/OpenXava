<%@ include file="../imports.jsp"%>

<%@ page import="java.util.Collection" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.web.Ids" %>
<%@ page import="org.openxava.web.editors.AttachedFile" %>
<%@ page import="org.openxava.web.editors.FilePersistorFactory" %>

<%
String propertyKey = request.getParameter("propertyKey");
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
boolean editable = "true".equals(request.getParameter("editable"));
String applicationName = request.getParameter("application");
String module = request.getParameter("module");
long dif = System.currentTimeMillis();
%>
<input id="<%=propertyKey%>" name="<%=propertyKey%>" value="<%=fvalue%>" type="hidden"/>
<%
boolean isEmpty = true;
if (!Is.emptyString(fvalue)) {
	Collection<AttachedFile> files = FilePersistorFactory.getInstance().findLibrary(fvalue);
	isEmpty = files.isEmpty();
	for (AttachedFile file : files) {
%>
	  <a href='<%=request.getContextPath()%>/xava/xfile?application=<%=applicationName%>&module=<%=module%>&fileId=<%=file.getId()%>&dif=<%=dif%>' target="_blank" tabindex="1">		
			<%=file.getName()%>		
	  </a>
	  <%if(editable) {%>
	  	  <span valign='middle'>	
		    <xava:image action='Library.remove' argv='<%="fileId=" + file.getId()%>'/>
		  </span>
	  <%} %>
	  &nbsp;&nbsp;
<%	}
}
if(editable) {
%>
	<span valign='middle'>
	  <xava:image action='Library.add' argv='<%="newLibraryProperty=" + Ids.undecorate(propertyKey)%>'/>
	  <%if(isEmpty) { %>			
		<xava:link action='Library.add' argv='<%="newLibraryProperty=" + Ids.undecorate(propertyKey)%>'/>
	  <%} %>
	</span>   		
<%}%>
