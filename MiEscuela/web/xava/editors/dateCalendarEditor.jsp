<%@ page import="org.openxava.model.meta.MetaProperty" %>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>
  
 <%
String propertyKey = request.getParameter("propertyKey");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
String align = p.isNumber()?"right":"left";
boolean editable="true".equals(request.getParameter("editable"));
String disabled=editable?"":"disabled";
String script = request.getParameter("script");
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel();
int c=0;
int indx=0;
while (indx>=0){
	indx=propertyKey.indexOf(".",indx + 1);
	c+=1;
}
if (editable || !label) {
%>
<input type="text" name="<%=propertyKey%>" id="<%=propertyKey%>" class=<%=style.getEditor()%> title="<%=p.getDescription(request)%>"
	align='<%=align%>'
	maxlength="<%=p.getSize()%>" 
	size="<%=p.getSize()%>"  
	value="<%=fvalue%>" <%=disabled%>	<%=script%>><%if (editable) {%><input type="reset" value=" ... "
	name="<%=propertyKey%>_CALENDAR_BUTTON_"
	onclick="return showCalendar('<%=propertyKey%>', '%d/%m/%Y');"><%} %>

	
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
