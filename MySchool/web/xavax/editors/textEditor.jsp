<%@ page import="org.xavax.model.meta.MetaProperty" %>

<%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String align = p.isNumber()?"right":"left";
boolean editable="true".equals(request.getParameter("editable"));
String disabled=editable?"":"disabled";
String script = request.getParameter("script");
boolean label = org.xavax.util.XavaxPreferences.getInstance().isReadOnlyAsLabel();
if (editable || !label) {
%>
<input name="<%=propertyKey%>" class=editor
	type="text" 
	title="<%=p.getDescription(request)%>"
	align='<%=align%>'
	maxlength="<%=p.getSize()%>" 
	size="<%=p.getSize()%>" 
	value="<%=fvalue%>"
	<%=disabled%>
	<%=script%>
	/>
<%
} else {
%>
<%=fvalue%>&nbsp;	
<%
}
%>
<% if (!editable) { %>
	<input type="hidden" name="<%=propertyKey%>" value="<%=fvalue%>">
<% } %>			
