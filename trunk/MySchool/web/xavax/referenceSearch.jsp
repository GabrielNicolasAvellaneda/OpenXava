<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.xavax.controller.ModuleContext" scope="session"/>
<%
String referenceLabel = (String) context.get(request, "xavax_currentReferenceLabel");
%>

<table class=frame width='100%'>
	<tr class=frame><th align='left'> 
		<fmt:message key="choose_reference_prompt">
			<fmt:param><%=referenceLabel%></fmt:param> 
		</fmt:message>		
	</th></tr>
	<tr><td class=frame>

<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="ReferenceSearch.choose"/>
</jsp:include>

	</td></tr>
</table>
