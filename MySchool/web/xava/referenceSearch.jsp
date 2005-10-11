<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String referenceLabel = (String) context.get(request, "xava_currentReferenceLabel");
%>

<table class=<%=style.getFrame()%> width='100%' <%=style.getFrameSpacing()%>>
	<tr class=<%=style.getFrameTitle()%>><th class=<%=style.getFrameTitleLabel()%> align='left'> 
		<fmt:message key="choose_reference_prompt">
			<fmt:param><%=referenceLabel%></fmt:param> 
		</fmt:message>		
	</th></tr>
	<tr><td class=<%=style.getFrameContent()%>>

<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="ReferenceSearch.choose"/>
</jsp:include>

	</td></tr>
</table>
