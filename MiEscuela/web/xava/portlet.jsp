<%-- 
Calling directly to module.jsp does not work well in liferay (at least until 4.1.3)
because the parameter sent from the portlet to the jsp are frozen for all the next
JSP calls. 
Therefore we use this JSP.
--%>
<jsp:include page="module.jsp">
	<jsp:param name="application" value="<%=request.getParameter("xava.portlet.application")%>"/>
	<jsp:param name="module" value="<%=request.getParameter("xava.portlet.module")%>"/>
</jsp:include>