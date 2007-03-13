<%@ page import="org.openxava.model.meta.MetaProperty" %>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String propertyKey = request.getParameter("propertyKey");
String script = request.getParameter("script");
MetaProperty p = (MetaProperty) request.getAttribute(propertyKey);
Object ovalue = request.getAttribute(propertyKey + ".value");
int baseIndex = 1;
int value = 0;
if (p.isNumber()) value = ovalue==null?0:((Integer) ovalue).intValue();
else {
	// We assume that if it isn't Number then it's an Enum of Java 5, we use instropection
	// to allow this code run in a Java 1.4 servlet container.
	baseIndex = 0;
	if (ovalue == null) {
		value = -1;	
	}
	else {
		value = ((Integer) org.openxava.util.Objects.execute(ovalue, "ordinal")).intValue();
	}
}

boolean editable = "true".equals(request.getParameter("editable"));
boolean label = org.openxava.util.XavaPreferences.getInstance().isReadOnlyAsLabel();
if (editable) { 
%>
<select name="<%=propertyKey%>" class=<%=style.getEditor()%> <%=script%> title="<%=p.getDescription(request)%>">
	<option value="<%=baseIndex==0?"":"0"%>"></option>
<%
	java.util.Iterator it = p.validValuesLabels(request);
	for (int i = baseIndex; it.hasNext(); i++) {
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
