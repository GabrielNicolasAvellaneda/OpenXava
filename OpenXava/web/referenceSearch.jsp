<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String referenceLabel = (String) context.get(request, "xava_currentReferenceLabel");
String rowAction = request.getParameter("rowAction");
%>

<%=style.getFrameHeaderStartDecoration()%>
<%=style.getFrameTitleStartDecoration()%>
<xava:message key="choose_reference_prompt" param="<%=referenceLabel%>"/>
<%=style.getFrameTitleEndDecoration()%>
<%=style.getFrameHeaderEndDecoration()%>
<%=style.getFrameContentStartDecoration()%>
<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="<%=rowAction%>"/>
	<jsp:param name="singleSelection" value="true"/>
</jsp:include>
<%=style.getFrameContentEndDecoration()%>
