<jsp:useBean id="errors" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>


<%
if (errors.contains()) {
%>
<table id="errors">
<%
	java.util.Iterator it = errors.getStrings(request).iterator();
	while (it.hasNext()) {		
%>
<tr><td class='<%=style.getErrors()%>'>
<%=it.next()%>
</td></tr>
<% } %>
</table>
<% } %>
