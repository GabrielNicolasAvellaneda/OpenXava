<%@ include file="../imports.jsp"%>

<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<table>
<%
for (int i=0; i<10; i++) {
%>
<tr>
	<th align='left' class=<%=style.getLabel()%>>
		<fmt:message key="enter_new_image"/>
	</th>
	<td>
		<input name = "newImage" class=<%=style.getEditor()%> type="file" size='60'/>
	</td>
</tr>
<%
}
%>
</table>
 

