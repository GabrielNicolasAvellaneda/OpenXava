<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String collectionLabel = (String) context.get(request, "xava_currentCollectionLabel");
String rowAction = request.getParameter("rowAction");
%>

<%=style.getFrameStartDecoration(org.openxava.util.XavaResources.getString(request, "add_to_collection_prompt", collectionLabel)) %>
<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="<%=rowAction%>"/>
</jsp:include>
<%=style.getFrameEndDecoration()%>
