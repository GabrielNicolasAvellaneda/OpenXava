<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>


<% 
if (messages.contains()) {
%>
<table id="messages">
<%
	java.util.Iterator it = messages.getStrings(request).iterator();	
	while (it.hasNext()) {		
%>
<tr><td class='messages'>
<%=it.next()%>
</td></tr>
<% } %>
</table>
<% } %>
