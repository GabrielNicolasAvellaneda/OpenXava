<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String referenceLabel = (String) context.get(request, "xava_currentReferenceLabel");
String rowAction = request.getParameter("rowAction");
%>

<%=style.getFrameStartDecoration(org.openxava.util.XavaResources.getString(request, "choose_reference_prompt", referenceLabel)) %>
<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="<%=rowAction%>"/>
	<jsp:param name="singleSelection" value="true"/>
</jsp:include>
<%=style.getFrameEndDecoration()%>
