<jsp:useBean id="messages" class="org.openxava.util.Messages" scope="request"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<% 
if (messages.contains()) {
%>
<table id="messages">
<%
	java.util.Iterator it = messages.getStrings(request).iterator();	
	while (it.hasNext()) {		
%>
<tr><td class=<%=style.getMessages()%>>
<%=it.next()%>
</td></tr>
<% } %>
</table>
<% } %>
