<%
String propertyKey = request.getParameter("propertyKey");
String fvalue = (String) request.getAttribute(propertyKey + ".fvalue");
if ("0".equals(fvalue)) fvalue = "";
%>

<input type="hidden" name="<%=propertyKey%>" value="<%=fvalue%>">
<b><%=fvalue%></b>
