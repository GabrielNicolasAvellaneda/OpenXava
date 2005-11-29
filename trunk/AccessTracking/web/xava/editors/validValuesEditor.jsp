<%@ page import="org.openxava.util.KeyAndDescription" %>
<%@ page import="org.openxava.util.Is" %>
<%@ page import="org.openxava.model.meta.MetaProperty" %>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String propertyKey = request.getParameter("propertyKey");
String script = request.getParameter("script");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
Integer ivalue = (Integer) request.getAttribute(propertyKey + ".value");
int value = ivalue==null?0:ivalue.intValue();
boolean editable = "true".equals(request.getParameter("editable"));
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel();
if (editable) { 
%>
<select name="<%=propertyKey%>" class=<%=style.getEditor()%> <%=script%> title="<%=p.getDescription(request)%>">
	<option value="0"></option>
<%
	java.util.Iterator it = p.validValuesLabels(request);
	int i = 0;
	while (it.hasNext()) {
		i++;
		String selected = value == i ?"selected":"";
		
%>
	<option value="<%=i%>" <%=selected%>><%=it.next()%></option>
<%
	} // while
%>
</select>	
<% 
} else { 
	Object description = p.getValidValueLabel(request, value);	
	if (label) {
%>
	<%=description%>
<%
	}
	else {
%>
	<input name = "<%=propertyKey%>_DESCRIPTION_" class=<%=style.getEditor()%>
	type="text" 
	title="<%=p.getDescription(request)%>"	
	maxlength="<%=p.getSize()%>" 	
	size="<%=p.getSize()%>" 
	value="<%=description%>"
	disabled
	/>
<%  } %>
	<input type="hidden" name="<%=propertyKey%>" value="<%=value%>">	
<% } %>			
