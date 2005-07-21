<%@ page import="org.openxava.model.meta.MetaProperty" %>
  
 <%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String align = p.isNumber()?"right":"left";
boolean editable="true".equals(request.getParameter("editable"));
String disabled=editable?"":"disabled";
String script = request.getParameter("script");
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel();
System.out.println("[enclosing_type.enclosing_method:] " + propertyKey);
int c=0;
int indx=0;
while (indx>=0){
	indx=propertyKey.indexOf(".",indx + 1);
	c+=1;
}
if (editable || !label) {
%>
	<input type="text" name="<%=propertyKey%>" id="<%=propertyKey%>" class=editor title="<%=p.getDescription(request)%>"
	align='<%=align%>'
	maxlength="<%=p.getSize()%>" 
	size="<%=p.getSize()%>"  
	value="<%=fvalue%>" <%=disabled%>	<%=script%>><input type="reset" value=" ... "
onclick="return showCalendar('<%=propertyKey%>', '%d/%m/%Y');"><br />
	
	
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
