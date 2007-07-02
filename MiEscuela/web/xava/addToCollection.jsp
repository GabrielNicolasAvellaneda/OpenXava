<%@ include file="imports.jsp"%>

<jsp:useBean id="context" class="org.openxava.controller.ModuleContext" scope="session"/>
<jsp:useBean id="style" class="org.openxava.web.style.Style" scope="request"/>

<%
String collectionLabel = (String) context.get(request, "xava_currentCollectionLabel");
String rowAction = request.getParameter("rowAction");
%>

<table class="<%=style.getFrame()%>" style="float:left; margin-right:4px" <%=style.getFrameSpacing()%>>
<tr class="<%=style.getFrameTitle()%>"><th align='left' class="<%=style.getFrameTitleLabel()%>">
	<%=style.getFrameTitleStartDecoration()%>
	<xava:message key="add_to_collection_prompt" param="<%=collectionLabel%>"/>
	<%=style.getFrameTitleEndDecoration()%>
</th></tr>
<tr><td class="<%=style.getFrameContent()%>">

<jsp:include page="list.jsp">
	<jsp:param name="rowAction" value="<%=rowAction%>"/>
</jsp:include>

</td></tr>
</table>
