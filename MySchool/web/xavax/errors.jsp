<jsp:useBean id="errors" class="org.xavax.util.Messages" scope="request"/>


<%
if (errors.contains()) {
%>
<table id="errors">
<%
	java.util.Iterator it = errors.getStrings(request).iterator();
	while (it.hasNext()) {		
%>
<tr><td class='errors'>
<%=it.next()%>
</td></tr>
<% } %>
</table>
<% } %>
