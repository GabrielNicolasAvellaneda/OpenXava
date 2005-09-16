<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.Style" scope="request"/>

<%
String referenceLabel = (String) context.get(request, "xava_currentReferenceLabel");
%>

<table class=<%=style.getFrame()%> width='100%'>
	<tr class=<%=style.getFrame()%>><th align='left'> 
		<fmt:message key="choose_reference_prompt">
			<fmt:param><%=referenceLabel%></fmt:param> 
		</fmt:message>		
	</th></tr>
	<tr><td class=<%=style.getFrame()%>>

<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="ReferenceSearch.choose"/>
</jsp:include>

	</td></tr>
</table>
