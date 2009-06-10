<%@page import="java.util.Iterator"%>
<%@page import="org.openxava.test.model.Color"%><%

String propertyKey = request.getParameter("propertyKey");
Object value = request.getAttribute(propertyKey + ".value");
if (value == null) value = new Integer(0);
%>


<%
Iterator it = Color.findAll().iterator();
for (int c=0; it.hasNext() && c < 3; c++) {
	Color color = (Color) it.next();
	String checked = value.equals(color.getNumber())?"checked='checked'":"";
%>	

<span style="font-weight: bold; color: #<%=color.getHexValue()%>; vertical-align: bottom"> 
	<input name="<%=propertyKey%>" value="<%=color.getNumber()%>" type="radio" <%=checked%>/>
	<%=color.getName()%>
</span>
<%
}
%>